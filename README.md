open-data-service
=================

Open Data Service


Build and run the Open Data Service via Gradle:
* Prerequisite: Get CouchDB running on http://127.0.0.1:5984 (standard settings)
* Build the project: gradle build
* Update the database (needed before first run): gradle grabData
* Run the REST service on 127.0.0.1:8182: gradle run

or

Build and run with Eclipse:
* Add the repository "http://dist.springsource.com/release/TOOLS/gradle" to Eclipse and install the addon option Gradle IDE.
* Then the code can be imported as a new Gradle project.
* respective main-classes are DataGrabberMain and DataServerMain


current REST-API:
* GET http://127.0.0.1:8182/api -> prints the API
