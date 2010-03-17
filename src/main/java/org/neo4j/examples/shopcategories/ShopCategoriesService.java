package org.neo4j.examples.shopcategories;

import java.util.Map;

public interface ShopCategoriesService
{

    void beginTx();

    void commitTx();

    void rollbackTx();

    Category createCategory( final String name, final Category parent );

    Category getRootCategory();

    AttributeType createAttributeType( final String name );

    Product createProduct( final Category category,
            final Map<AttributeDefinition, Object> values );

}
