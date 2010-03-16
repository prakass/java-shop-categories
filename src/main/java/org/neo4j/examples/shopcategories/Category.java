package org.neo4j.examples.shopcategories;

public interface Category
{
    String getName();

    void setName( String name );

    Iterable<Category> getSubcategories();

    void addSubcategory( Category category );

    Iterable<Product> getAllProducts();

    Iterable<Product> getProducts();

    void addProduct( Product product );

    Iterable<AttributeDefinition> getAttributeDefinitions();

    Iterable<AttributeDefinition> getAllAttributeDefinitions();

    AttributeDefinition createAttributeDefinition( AttributeType type,
            String name );
}
