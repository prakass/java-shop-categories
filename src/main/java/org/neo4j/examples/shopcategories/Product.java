package org.neo4j.examples.shopcategories;

public interface Product
{
    Iterable<AttributeValue> getAttributeValues();

    Category getCategory();
}
