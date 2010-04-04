A simple shop/categories example handling nested categories
and an attribute definition hierarchy.


Requirements

To execute the project, Java 5+ is needed. To run from source,
JDK 1.5+ is needed, and optionally Maven2 for dependency management.


Download packages

There's download packages located here:
http://m2.neo4j.org/org/neo4j/examples/java-shop-categories/1.0.0-SNAPSHOT/
Look for files ending in .zip or .tar.gz
Only Java 5+ is required to execute the example using the download.


Contents of the download package

bin/   start scripts for the application in Unix and Windows format
docs/  javadoc of all Neo4j component dependencies of the project
lib/   all dependency jars of the project
src/   source code of the project


Run from source

Using JDK:
   Use the download package and add the lib/ directory
   to the project classpath ("build path" in Eclipse).
   In this case, only JDK 1.5+ is required to run the code
   from source.
Using JDK + Maven:
   Checkout the code from GitHub and use the Maven2
   setup originally used for the project. This option requires
   JDK 1.5+ and Maven2 to be installed, see:
   http://wiki.neo4j.org/content/Java_Setup_HowTo
   With Maven2 installed, simply issue the following command
   at the command prompt:
   $ mvn clean compile exec:java -Dexec.mainClass="org.neo4j.examples.shopcategories.ShopCategoriesApp"
   or simply run the JUnit test (which contains essentially the same code):
   $ mvn test
   This command will download all dependencies that are needed on the first run.


Sample output

Product:
 CPU frequency -> 2000 MHz
 Shipping weight -> 6.3 Kg
 Display size -> 15.0 "
 Name -> HP Laptop 
 Price -> 1200.0 USD
 Weight -> 3.5 Kg

Product:
 CPU frequency -> 3000 MHz
 Shipping weight -> 22.3 Kg
 Expansion slots -> 4 pcs.
 Name -> Dell Desktop 
 Price -> 890.0 USD
 Weight -> 17.1 Kg
