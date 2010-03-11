package org.neo4j.examples.shopcategories;

import org.neo4j.graphdb.PropertyContainer;

/**
 * This is one way of abstracting the wrapping of a Node/Relationship,
 * {@link PropertyContainer} that is, in an abstract class which other
 * domain implementations can extend to gain functionality from.
 */
public abstract class ContainerWrapper<T extends PropertyContainer>
{
    private final T underlyingContainer;
    
    ContainerWrapper( final T container )
    {
        this.underlyingContainer = container;
    }
    
    public T getUnderlyingContainer()
    {
        return this.underlyingContainer;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public boolean equals( final Object otherObject )
    {
        if ( !getClass().isAssignableFrom( otherObject.getClass() ) )
        {
            return false;
        }
        return this.getUnderlyingContainer().equals(
                ( (ContainerWrapper<T>) otherObject ).getUnderlyingContainer() );
    }

    @Override
    public int hashCode()
    {
        return this.underlyingContainer.hashCode();
    }
}
