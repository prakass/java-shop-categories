package org.neo4j.examples.shopcategories;

public interface AttributeDefinition
{
    String getName();

    void setName( String string );

    boolean isRequired();

    void setRequired( boolean required );

    Object getDefaultValue();

    void setDefaultValue( Object value );

    String getUnit();
}
