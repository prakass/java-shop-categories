package org.neo4j.examples.shopcategories;

public class AttributeValueImpl implements AttributeValue
{
    private final AttributeDefinition attributeDefinition;
    private final Object value;

    public AttributeValueImpl( final AttributeDefinition attributeDefinition,
            final Object value )
    {
        this.attributeDefinition = attributeDefinition;
        this.value = value;
    }

    public AttributeDefinition getAttribute()
    {
        return attributeDefinition;
    }

    public Object getValue()
    {
        return value;
    }
}
