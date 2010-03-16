package org.neo4j.examples.shopcategories;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class ProductImpl extends ContainerWrapper<Node> implements Product
{
    public ProductImpl( final Node node )
    {
        super( node );
    }

    public void setAttribute( final AttributeDefinition attributeDefinition,
            final Object value )
    {
        getUnderlyingContainer().setProperty( attributeDefinition.getName(),
                value );
    }

    public Map<AttributeDefinition, Object> getAttributeValues()
    {
        Iterable<AttributeDefinition> defs = this.getCategory().getAllAttributeDefinitions();
        Map<AttributeDefinition, Object> values = new HashMap<AttributeDefinition, Object>();
        for ( AttributeDefinition def : defs )
        {
            Object value = getUnderlyingContainer().getProperty( def.getName(),
                    null );
            if ( value == null )
            {
                value = def.getDefaultValue();
            }
            if ( value != null )
            {
                values.put( def, value );
            }
        }
        return values;
    }

    public Category getCategory()
    {
        Relationship categoryRel = getUnderlyingContainer().getSingleRelationship(
                RelationshipTypes.PRODUCT, Direction.INCOMING );
        return new CategoryImpl( categoryRel.getStartNode() );
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder( "Product:\n" );
        for ( Entry<AttributeDefinition, Object> entry : getAttributeValues().entrySet() )
        {
            str.append( " " ).append( entry.getKey().getName() ).append( " (" ).append(
                    entry.getKey().getTypeName() ).append( ") -> " ).append(
                    entry.getValue() ).append( '\n' );
        }
        return str.toString();
    }
}
