# Tenuki

A [SPARQL 1.1 Uniform HTTP Protocol for Managing RDF Graphs](http://www.w3.org/TR/sparql11-http-rdf-update/) server.

## Building Tenuki

Tenuki requires [Apache Maven 2.x](http://maven.apache.org/) to build.

### Getting SDB 1.4.0-SNAPSHOT

SDB has not yet had a snapshot release that contains bug fixes needed for Tenuki to run. You will need to checkout [sdb-trunk](http://jena.svn.sourceforge.net/svnroot/jena/SDB/trunk), apply a [patch](https://gist.github.com/675719) and build (`ant jar`) SDB.

### Build complete jar with dependencies (w/o tests)

	mvn package -DskipTests=true

Currently tests have not been updated to work without a configured PostgreSQL SDB Datasource present on localhost.

## Running Tenuki

### Startup Tenuki with default configuration

	java -jar tenuki-0.4.jar

Will start a Tenuki server running at http://localhost:6060/ using a PostgreSQL database on localhost named `sdb`.
