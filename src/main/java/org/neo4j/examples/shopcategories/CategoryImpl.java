package org.neo4j.examples.shopcategories;

import org.neo4j.commons.iterator.IterableWrapper;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

public class CategoryImpl extends ContainerWrapperWithName<Node> implements
        Category
{
    CategoryImpl( final Node node )
    {
        super( node );
    }

    CategoryImpl( final Node node, final String name )
    {
        super( node, name );
    }

    public void addProduct( Product product )
    {
        Node productNode = ( (ProductImpl) product ).getUnderlyingContainer();
        getUnderlyingContainer().createRelationshipTo( productNode,
                RelationshipTypes.PRODUCT );
    }

    public void addSubcategory( Category category )
    {
        Node categoryNode = ( (CategoryImpl) category ).getUnderlyingContainer();
        getUnderlyingContainer().createRelationshipTo( categoryNode,
                RelationshipTypes.SUBCATEGORY );
    }

    public Iterable<AttributeDefinition> getAttributeDefinitions()
    {
        Iterable<Relationship> attributes = getUnderlyingContainer().getRelationships(
                RelationshipTypes.ATTRIBUTE, Direction.OUTGOING );
        return new IterableWrapper<AttributeDefinition, Relationship>(
                attributes )
        {
            @Override
            protected AttributeDefinition underlyingObjectToObject(
                    Relationship relationship )
            {
                return new AttributeDefinitionImpl( relationship );
            }
        };
    }

    public AttributeDefinition createAttributeDefinition( AttributeType type,
            String name )
    {
        Node typeNode = ( (AttributeTypeImpl) type ).getUnderlyingContainer();
        Relationship attributeRel = getUnderlyingContainer().createRelationshipTo(
                typeNode, RelationshipTypes.ATTRIBUTE );
        AttributeDefinitionImpl attributeDefinition = new AttributeDefinitionImpl(
                attributeRel );
        attributeDefinition.setName( name );
        return attributeDefinition;
    }

    public Iterable<Product> getProducts()
    {
        Traverser traverser = getUnderlyingContainer().traverse(
                Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                RelationshipTypes.SUBCATEGORY, Direction.OUTGOING,
                RelationshipTypes.PRODUCT, Direction.OUTGOING );
        return new IterableWrapper<Product, Node>( traverser )
        {
            @Override
            protected Product underlyingObjectToObject( Node node )
            {
                return new ProductImpl( node );
            }
        };
    }

    public Iterable<Category> getSubcategories()
    {
        Traverser traverser = getUnderlyingContainer().traverse(
                Order.DEPTH_FIRST, StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                RelationshipTypes.SUBCATEGORY, Direction.OUTGOING );
        return new IterableWrapper<Category, Node>( traverser )
        {
            @Override
            protected Category underlyingObjectToObject( Node node )
            {
                return new CategoryImpl( node );
            }
        };
    }
}
