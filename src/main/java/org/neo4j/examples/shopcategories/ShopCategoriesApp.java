package org.neo4j.examples.shopcategories;

import java.util.HashMap;

public class ShopCategoriesApp
{
    private final ShopCategoriesServiceImpl service;

    public ShopCategoriesApp()
    {
        service = new ShopCategoriesServiceImpl();
    }

    public void setup()
    {
        service.beginTx();

        Category electronics = service.createCategory( "Electronics", null );
        Category cameras = service.createCategory( "Cameras", electronics );
        Category computers = service.createCategory( "Computers", electronics );

        Category desktops = service.createCategory( "Desktops", computers );
        Category laptops = service.createCategory( "Laptops", computers );

        AttributeType weight = service.createAttributeType( "weight" );
        AttributeType count = service.createAttributeType( "count" );
        AttributeType length = service.createAttributeType( "length" );
        AttributeType frequency = service.createAttributeType( "frequency" );
        AttributeType name = service.createAttributeType( "name" );

        final AttributeDefinition aName = electronics.createAttributeDefinition(
                name, "name" );
        aName.setRequired( true );
        final AttributeDefinition aWeight = electronics.createAttributeDefinition(
                weight, "weight" );
        aWeight.setRequired( true );
        final AttributeDefinition aShippingWeight = electronics.createAttributeDefinition(
                weight, "shipping weight" );
        aShippingWeight.setRequired( true );
        final AttributeDefinition aCpuFreq = computers.createAttributeDefinition(
                frequency, "cpu frequency" );
        aCpuFreq.setRequired( true );
        AttributeDefinition aExpansionSlots = desktops.createAttributeDefinition(
                count, "expansion slots" );
        aExpansionSlots.setDefaultValue( 4 );
        AttributeDefinition aDisplaySize = laptops.createAttributeDefinition(
                length, "display size" );
        aDisplaySize.setDefaultValue( 15.0 );

        service.createProduct( desktops,
                new HashMap<AttributeDefinition, Object>()
                {
                    {
                        put( aName, "Dell Desktop" );
                        put( aWeight, 17.1 );
                        put( aShippingWeight, 22.3 );
                        put( aCpuFreq, 3000 );
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
}
