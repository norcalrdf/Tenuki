# Tenuki

A [SPARQL 1.1 Uniform HTTP Protocol for Managing RDF Graphs](http://www.w3.org/TR/sparql11-http-rdf-update/) server.

## Running Tenuki

1.	Create PostgreSQL user named `sdb` with password `changemeplease`
2.	Create PostgreSQL database with name `sdb_example`
3.	Run:
	java -jar tenuki-0.4.jar --create example.ini
4.	Visit [http://localhost:6060/] with your browser

## Usage

### Graph listing

	> GET /graphs HTTP/1.1
	> Host: localhost:6060
	> Accept: text/uri-list
	> 
	< HTTP/1.1 200 OK
	< Content-Type: text/uri-list
	example

### Graph Manipulation 

#### GET

	> GET /graphs/example HTTP/1.1
	> Host: localhost:6060
	> Accept: text/turtle, text/plain, text/rdf+n3, application/rdf+xml
	> 
	< HTTP/1.1 200 OK
	< Content-Type: text/turtle
	< 
	<http://example/s>
	      <http://example/p> <http://example/o2> , <http://example/o> .

Or using indirect (query param)

	> GET /graphs?graph=example HTTP/1.1
	> Host: localhost:6060
	> Accept: text/turtle, text/plain, text/rdf+n3, application/rdf+xml
	> 
	< HTTP/1.1 200 OK
	< Content-Type: text/turtle
	< 
	<http://example/s>
	      <http://example/p> <http://example/o2> , <http://example/o> .


## Building Tenuki

Tenuki requires [Apache Maven 2.x](http://maven.apache.org/) to build.

### Getting SDB 1.3.3-patch-011411

If SDB has not yet had a snapshot release that contains bug fixes needed for Tenuki to run. You will need to checkout [sdb-trunk](http://jena.svn.sourceforge.net/svnroot/jena/SDB/trunk), and apply the patches [patch](https://issues.apache.org/jira/browse/JENA-28) [patch](https://issues.apache.org/jira/browse/JENA-27) and build (`ant jar`) SDB.

### Build complete jar with dependencies

	mvn package



