package org.neo4j.examples.shopcategories;

import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.util.GraphDatabaseUtil;

public class ShopCategoriesServiceImpl implements ShopCategoriesService
{
    private final GraphDatabaseService graphDb;
    private Transaction tx;
    private final GraphDatabaseUtil util;

    public ShopCategoriesServiceImpl()
    {
        graphDb = new EmbeddedGraphDatabase( "target/neo4j-db" );
        util = new GraphDatabaseUtil( graphDb );
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }

    public void beginTx()
    {
        tx = graphDb.beginTx();
    }

    public void commitTx()
    {
        tx.success();
        tx.finish();
    }

    public void rollbackTx()
    {
        tx.finish();
    }

    public Category createCategory( final String name, final Category parent )
    {
        Node categoryNode = graphDb.createNode();
        Category category = new CategoryImpl( categoryNode, name );
        parent.addSubcategory( category );
        return category;
    }

    public Category getRootCategory()
    {
        Node categoryNode = util.getOrCreateSubReferenceNode( DynamicRelationshipType.withName( "ROOTCATEGORY" ) );
        Category category = new CategoryImpl( categoryNode );
        category.setName( "Products" );
        return category;
    }

    public AttributeType createAttributeType( final String name,
            final String unitName )
    {
        return new AttributeTypeImpl( graphDb.createNode(), name, unitName );
    }

    public Product createProduct( final Category category,
            final Map<AttributeDefinition, Object> values )
    {
        Iterable<AttributeDefinition> attributes = category.getAllAttributeDefinitions();
        for ( AttributeDefinition def : attributes )
        {
            if ( def.isRequired() && values.get( def ) == null )
            {
                throw new IllegalArgumentException(
                        "Required attribute not set: " + def );
            }
        }
        Product product = new ProductImpl( graphDb.createNode() );
        for ( Entry<AttributeDefinition, Object> entry : values.entrySet() )
        {
            product.setAttribute( entry.getKey(), entry.getValue() );
        }
        category.addProduct( product );
        return product;
    }
}
