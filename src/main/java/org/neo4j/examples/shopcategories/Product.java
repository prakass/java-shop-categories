package org.neo4j.examples.shopcategories;

import java.util.Map;

public interface Product
{
    Map<AttributeDefinition, Object> getAttributeValues();

    void setAttribute( AttributeDefinition attributeDefinition, Object value );

    Category getCategory();
}
