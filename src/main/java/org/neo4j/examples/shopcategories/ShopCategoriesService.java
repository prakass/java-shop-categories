package org.neo4j.examples.shopcategories;

import java.util.Map;

public interface ShopCategoriesService
{

    void beginTx();

    void commitTx();

    void rollbackTx();

    Category createCategory( String name, Category parent );

    Category getRootCategory();

    AttributeType createAttributeType( String name, String unitName );

    Product createProduct( final Category category,
            final Map<AttributeDefinition, Object> values );

}
