package org.neo4j.examples.shopcategories;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class ShopCategoriesAppTest
{
    private static ShopCategoriesApp shopCat;

    @BeforeClass
    public static void setup()
    {
        shopCat = new ShopCategoriesApp();
        shopCat.setup();
    }

    @Test
    public void dummy()
    {
        assertEquals( true, true );
    }
}
