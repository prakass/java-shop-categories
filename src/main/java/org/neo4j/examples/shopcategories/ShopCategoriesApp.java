package org.neo4j.examples.shopcategories;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class ShopCategoriesApp
{
    private final GraphDatabaseService graphDb;

    public ShopCategoriesApp()
    {
        graphDb = new EmbeddedGraphDatabase( "target/neo4j-db" );
        setUp();
    }

    public void setUp()
    {
        Category rootCategory = new CategoryImpl( graphDb.getReferenceNode(),
                "Electronics" );
        Category camera = new CategoryImpl( graphDb.createNode(), "Camera" );
        rootCategory.addSubcategory( camera );
        Category tv = new CategoryImpl( graphDb.createNode(),
                "Television & Video" );
        rootCategory.addSubcategory( tv );

    }
}
