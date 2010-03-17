package org.neo4j.examples.shopcategories;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

public class ShopCategoriesTest
{
    private static ShopCategoriesService service;

    @BeforeClass
    public static void setup()
    {
        service = new ShopCategoriesServiceImpl();
        setupDb();
    }

    @AfterClass
    public static void teardown()
    {
        // cleanDb();
    }

    private static void setupDb()
    {
        service.beginTx();

        Category electronics = service.createCategory( "Electronics",
                service.getRootCategory() );
        Category cameras = service.createCategory( "Cameras", electronics );
        Category computers = service.createCategory( "Computers", electronics );

        Category desktops = service.createCategory( "Desktops", computers );
        Category laptops = service.createCategory( "Laptops", computers );

        AttributeType weight = service.createAttributeType( "Weight", "Kg" );
        AttributeType count = service.createAttributeType( "Count", "pcs." );
        AttributeType length = service.createAttributeType( "Length", "\"" );
        AttributeType frequency = service.createAttributeType( "Frequency",
                "MHz" );
        AttributeType name = service.createAttributeType( "Name", "" );
        AttributeType currency = service.createAttributeType( "Currency", "USD" );

        final AttributeDefinition aName = electronics.createAttributeDefinition(
                name, "Name" );
        aName.setRequired( true );
        final AttributeDefinition aPrice = electronics.createAttributeDefinition(
                currency, "Price" );
        aPrice.setRequired( true );
        final AttributeDefinition aWeight = electronics.createAttributeDefinition(
                weight, "Weight" );
        aWeight.setRequired( true );
        final AttributeDefinition aShippingWeight = computers.createAttributeDefinition(
                weight, "Shipping weight" );
        aShippingWeight.setRequired( true );
        final AttributeDefinition aCpuFreq = computers.createAttributeDefinition(
                frequency, "CPU frequency" );
        aCpuFreq.setRequired( true );
        AttributeDefinition aExpansionSlots = desktops.createAttributeDefinition(
                count, "Expansion slots" );
        aExpansionSlots.setDefaultValue( 4 );
        AttributeDefinition aDisplaySize = laptops.createAttributeDefinition(
                length, "Display size" );
        aDisplaySize.setDefaultValue( 15.0 );

        service.createProduct( desktops,
                new HashMap<AttributeDefinition, Object>()
                {
                    {
                        put( aName, "Dell Desktop" );
                        put( aWeight, 17.1 );
                        put( aShippingWeight, 22.3 );
                        put( aCpuFreq, 3000 );
                        put( aPrice, 890.0 );
                    }
                } );
        service.createProduct( laptops,
                new HashMap<AttributeDefinition, Object>()
                {
                    {
                        put( aName, "HP Laptop" );
                        put( aWeight, 3.5 );
                        put( aShippingWeight, 6.3 );
                        put( aCpuFreq, 2000 );
                        put( aPrice, 1200.0 );
                    }
                } );
        service.commitTx();
        service.beginTx();
        for ( Product product : computers.getAllProducts() )
        {
            System.out.println( product );
        }

        service.commitTx();
    }

    private static void cleanDb()
    {
        service.beginTx();
        Traverser traverser = ( (CategoryImpl) service.getRootCategory() ).getUnderlyingContainer().traverse(
                Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH,
                ReturnableEvaluator.ALL, RelationshipTypes.ATTRIBUTE,
                Direction.BOTH, RelationshipTypes.PRODUCT, Direction.BOTH,
                RelationshipTypes.SUBCATEGORY, Direction.BOTH );
        for ( Node node : traverser )
        {
            for ( Relationship rel : node.getRelationships() )
            {
                rel.delete();
            }
            node.delete();
        }
        service.commitTx();
    }

    @Before
    public void start()
    {
        service.beginTx();
    }

    @After
    public void end()
    {
        service.rollbackTx();
    }

    @Test( expected = IllegalArgumentException.class )
    public void productWithMissingRequiredAttributes()
    {
        service.createProduct(
                service.getRootCategory().getSubcategories().iterator().next(),
                new HashMap<AttributeDefinition, Object>() );
    }

    @Test
    public void testNumberOfProducts()
    {
        int count = 0;
        for ( Product product : service.getRootCategory().getAllProducts() )
        {
            count++;
        }
        assertEquals( 2, count );
    }
}
