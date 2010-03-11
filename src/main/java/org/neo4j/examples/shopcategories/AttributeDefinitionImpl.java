package org.neo4j.examples.shopcategories;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class AttributeDefinitionImpl extends
        ContainerWrapperWithName<Relationship> implements AttributeDefinition
{
    private static final String REQUIRED = "Required";
    private static final String DEFAULT_VALUE = "DefaultValue";

    public AttributeDefinitionImpl( final Relationship relationship )
    {
        super( relationship );
    }

    public Object getDefaultValue()
    {
        return getUnderlyingContainer().getProperty( DEFAULT_VALUE, null );
    }

    public void setDefaultValue( final Object value )
    {
        getUnderlyingContainer().setProperty( DEFAULT_VALUE, value );
    }

    public String getTypeName()
    {
        Node node = getUnderlyingContainer().getEndNode();
        AttributeType type = new AttributeTypeImpl( node );
        return type.getName();
    }

    public boolean isRequired()
    {
        return (Boolean) getUnderlyingContainer().getProperty( REQUIRED,
                Boolean.FALSE );
    }

    public void setRequired( final boolean required )
    {
        getUnderlyingContainer().setProperty( REQUIRED, required );
    }
}
