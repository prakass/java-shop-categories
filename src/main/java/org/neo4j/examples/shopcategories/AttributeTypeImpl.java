package org.neo4j.examples.shopcategories;

import org.neo4j.graphdb.Node;

public class AttributeTypeImpl extends ContainerWrapperWithName<Node> implements
        AttributeType
{
    private static final String UNIT = "Unit";

    AttributeTypeImpl( final Node node )
    {
        super( node );
    }

    public AttributeTypeImpl( final Node node, final String name,
            final String unitName )
    {
        super( node, name );
        setUnit( unitName );
    }

    public String getUnit()
    {
        return (String) getUnderlyingContainer().getProperty( UNIT );
    }

    public void setUnit( final String unitName )
    {
        getUnderlyingContainer().setProperty( UNIT, unitName );
    }
}
