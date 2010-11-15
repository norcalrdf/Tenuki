# Tenuki

A [SPARQL 1.1 Uniform HTTP Protocol for Managing RDF Graphs](http://www.w3.org/TR/sparql11-http-rdf-update/) server.

## Building Tenuki

Tenuki requires [Apache Maven 2.x](http://maven.apache.org/) to build.

### Getting SDB 1.4.0-SNAPSHOT

SDB has not yet had a snapshot release that contains bug fixes needed for Tenuki to run. You will need to checkout [sdb-trunk](http://jena.svn.sourceforge.net/svnroot/jena/SDB/trunk), apply a [patch](https://gist.github.com/675719) and build (`ant jar`) SDB.

### Build complete jar with dependencies

	mvn package

## Running Tenuki

1.	Create PostgreSQL user named `sdb` with password `changemeplease`
2.	Create PostgreSQL database with name `sdb_example`
3.	Run:
	java -jar tenuki-0.4.jar --create example.ini
4.	Visit [http://localhost:6060/] with your browser


