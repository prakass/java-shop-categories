package org.neo4j.examples.shopcategories;

import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class ShopCategoriesServiceImpl implements ShopCategoriesService
{
    private final GraphDatabaseService graphDb;
    private Transaction tx;

    public ShopCategoriesServiceImpl()
    {
        graphDb = new EmbeddedGraphDatabase( "target/neo4j-db" );
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
        tx.failure();
        tx.finish();
    }

    public Category createCategory( final String name, final Category parent )
    {
        Node categoryNode = graphDb.createNode();
        Category category = new CategoryImpl( categoryNode, name );
        if ( parent == null )
        {
            Relationship categoryRel = graphDb.getReferenceNode().getSingleRelationship(
                    RelationshipTypes.ROOTCATEGORY, Direction.OUTGOING );
            if ( categoryRel != null )
            {
                categoryRel.delete();
            }
            graphDb.getReferenceNode().createRelationshipTo( categoryNode,
                    RelationshipTypes.ROOTCATEGORY );
        }
        else
        {
            parent.addSubcategory( category );
        }
        return category;
    }

    public Category getRootCategory()
    {
        Relationship categoryRel = graphDb.getReferenceNode().getSingleRelationship(
                RelationshipTypes.ROOTCATEGORY, Direction.OUTGOING );
        if ( categoryRel == null )
        {
            throw new IllegalStateException( "No root category defined." );
        }
        Node categoryNode = categoryRel.getEndNode();
        Category category = new CategoryImpl( categoryNode );
        return category;
    }

    public AttributeType createAttributeType( final String name )
    {
        return new AttributeTypeImpl( graphDb.createNode(), name );
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
