package org.neo4j.examples.shopcategories;

public interface Category
{
    String getName();

    void setName( String name );

    Iterable<Category> getSubcategories();

    void addSubcategory( Category category );

    /**
     * 
     * @return all products of this category and any subcategories
     */
    Iterable<Product> getProducts();

    void addProduct( Product product );

    Iterable<AttributeDefinition> getAttributeDefinitions();

    AttributeDefinition createAttributeDefinition( AttributeType type,
            String name );
}
