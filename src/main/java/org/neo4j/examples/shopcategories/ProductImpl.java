package org.neo4j.examples.shopcategories;

import java.util.Iterator;

import org.neo4j.commons.iterator.FilteringIterable;
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

public class ProductImpl extends ContainerWrapper<Node> implements Product
{
    public ProductImpl( Node node )
    {
        super( node );
    }

    public Iterable<AttributeValue> getAttributeValues()
    {
        Traverser traverser = getUnderlyingContainer().traverse(
                Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH,
                new ReturnableEvaluator()
                {
                    public boolean isReturnableNode( TraversalPosition pos )
                    {
                        return pos.lastRelationshipTraversed().isType(
                                RelationshipTypes.ATTRIBUTE );
                    }
                }, RelationshipTypes.SUBCATEGORY, Direction.INCOMING );
        NestingIterable<Relationship, Node> categories = new NestingIterable<Relationship, Node>(
                traverser )
        {
            @Override
            protected Iterator<Relationship> createNestedIterator( Node node )
            {
                return node.getRelationships( RelationshipTypes.ATTRIBUTE,
                        Direction.OUTGOING ).iterator();
            }
        };
        IterableWrapper<AttributeValue, Relationship> values = new IterableWrapper<AttributeValue, Relationship>(
                categories )
        {
            @Override
            protected AttributeValue underlyingObjectToObject(
                    Relationship attribute )
            {
                AttributeDefinition def = new AttributeDefinitionImpl(
                        attribute );
                String key = def.getName();
                Object value = getUnderlyingContainer().getProperty( key, null );
                if ( value == null )
                {
                    value = def.getDefaultValue();
                }
                if ( value != null )
                {
                    return new AttributeValueImpl( def, value );
                }
                return null;
            }
        };
        return new FilteringIterable<AttributeValue>( values )
        {
            @Override
            protected boolean passes( AttributeValue attrVal )
            {
                return attrVal != null;
            }
        };
    }

    public Category getCategory()
    {
        Relationship categoryRel = getUnderlyingContainer().getSingleRelationship(
                RelationshipTypes.PRODUCT, Direction.INCOMING );
        return new CategoryImpl( categoryRel.getStartNode() );
    }
}
