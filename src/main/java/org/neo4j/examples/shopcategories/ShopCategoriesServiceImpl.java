package org.neo4j.examples.shopcategories;

import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.util.GraphDatabaseLifecycle;
import org.neo4j.util.GraphDatabaseUtil;

public class ShopCategoriesServiceImpl implements ShopCategoriesService
{
    private enum RootRelationshipTypes implements RelationshipType
    {
        CATEGORY_ROOT,
        ATTRIBUTE_ROOT,
        ATTRIBUTE_TYPE
    }

    private final GraphDatabaseService graphDb;
    private Transaction tx;
    private final GraphDatabaseUtil util;
    private final GraphDatabaseLifecycle lifecyle;

    public ShopCategoriesServiceImpl()
    {
        graphDb = new EmbeddedGraphDatabase( "target/neo4j-db" );
        util = new GraphDatabaseUtil( graphDb );
        lifecyle = new GraphDatabaseLifecycle( graphDb );
    }

    public void shutdown()
    {
        lifecyle.manualShutdown();
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
        Node categoryNode = util.getOrCreateSubReferenceNode( RootRelationshipTypes.CATEGORY_ROOT );
        Category category = new CategoryImpl( categoryNode );
        category.setName( "Products" );
        return category;
    }

    public AttributeType createAttributeType( final String name,
            final String unitName )
    {
        Node typeRootNode = util.getOrCreateSubReferenceNode( RootRelationshipTypes.ATTRIBUTE_ROOT );
        Node typeNode = graphDb.createNode();
        typeRootNode.createRelationshipTo( typeNode,
                RootRelationshipTypes.ATTRIBUTE_TYPE );
        return new AttributeTypeImpl( typeNode, name, unitName );
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
