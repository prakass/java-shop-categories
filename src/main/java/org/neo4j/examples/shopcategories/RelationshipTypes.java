package org.neo4j.examples.shopcategories;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType
{
    SUBCATEGORY,
    ROOTCATEGORY,
    PRODUCT,
    ATTRIBUTE
}
