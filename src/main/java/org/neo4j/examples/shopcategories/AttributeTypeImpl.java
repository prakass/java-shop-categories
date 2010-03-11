package org.neo4j.examples.shopcategories;

import org.neo4j.graphdb.Node;

public class AttributeTypeImpl extends ContainerWrapperWithName<Node> implements
        AttributeType
{
    AttributeTypeImpl( final Node node )
    {
        super( node );
    }

    public AttributeTypeImpl( final Node node, final String name )
    {
        super( node, name );
    }
}
