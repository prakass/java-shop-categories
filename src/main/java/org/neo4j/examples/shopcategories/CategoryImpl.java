package org.neo4j.examples.shopcategories;

import java.util.Iterator;

import org.neo4j.commons.iterator.IterableWrapper;
import org.neo4j.commons.iterator.NestingIterable;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.TraversalPosition;
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

    public void addProduct( final Product product )
    {
        Node productNode = ( (ProductImpl) product ).getUnderlyingContainer();
        getUnderlyingContainer().createRelationshipTo( productNode,
                RelationshipTypes.PRODUCT );
    }

    public void addSubcategory( final Category category )
    {
        Node categoryNode = ( (CategoryImpl) category ).getUnderlyingContainer();
        if ( categoryNode.hasRelationship( RelationshipTypes.SUBCATEGORY,
                Direction.INCOMING ) )
        {
            throw new IllegalArgumentException(
                    "A category can only be added as a subcategory to one "
                            + "parent category." );
        }
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
                    final Relationship relationship )
            {
                return new AttributeDefinitionImpl( relationship );
            }
        };
    }

    public Iterable<AttributeDefinition> getAllAttributeDefinitions()
    {
        Traverser allCategories = getUnderlyingContainer().traverse(
                Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH,
                ReturnableEvaluator.ALL, RelationshipTypes.SUBCATEGORY,
                Direction.INCOMING );
        return new NestingIterable<AttributeDefinition, Node>( allCategories )
        {
            @Override
            protected Iterator<AttributeDefinition> createNestedIterator(
                    final Node categoryNode )
            {
                return ( new CategoryImpl( categoryNode ) ).getAttributeDefinitions().iterator();
            }
        };
    }

    public AttributeDefinition createAttributeDefinition(
            final AttributeType type, final String name )
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
                Order.DEPTH_FIRST, StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                RelationshipTypes.PRODUCT, Direction.OUTGOING );
        return new IterableWrapper<Product, Node>( traverser )
        {
            @Override
            protected Product underlyingObjectToObject( final Node node )
            {
                return new ProductImpl( node );
            }
        };
    }

    public Iterable<Product> getAllProducts()
    {
        Traverser traverser = getUnderlyingContainer().traverse(
                Order.DEPTH_FIRST,
                StopEvaluator.END_OF_GRAPH,
                new ReturnableEvaluator()
                {
                    public boolean isReturnableNode(
                            final TraversalPosition currentPos )
                    {
                        return !currentPos.isStartNode()
                               && currentPos.lastRelationshipTraversed().isType(
                                       RelationshipTypes.PRODUCT );
                    }
                }, RelationshipTypes.SUBCATEGORY, Direction.OUTGOING,
                RelationshipTypes.PRODUCT, Direction.OUTGOING );
        return new IterableWrapper<Product, Node>( traverser )
        {
            @Override
            protected Product underlyingObjectToObject( final Node node )
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
            protected Category underlyingObjectToObject( final Node node )
            {
                return new CategoryImpl( node );
            }
        };
    }
}
