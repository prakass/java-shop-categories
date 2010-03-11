package org.neo4j.examples.shopcategories;

import org.neo4j.graphdb.PropertyContainer;

public class ContainerWrapperWithName<T extends PropertyContainer> extends
        ContainerWrapper<T>
{
    private static final String NAME = "Name";

    ContainerWrapperWithName( final T container )
    {
        super( container );
    }

    ContainerWrapperWithName( final T container, final String name )
    {
        this( container );
        setName( name );
    }

    public String getName()
    {
        return (String) getUnderlyingContainer().getProperty( NAME );
    }

    public void setName( final String name )
    {
        getUnderlyingContainer().setProperty( NAME, name );
    }
}
