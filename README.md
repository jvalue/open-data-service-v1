open-data-service
=================

Open Data Service


Build and run the (unfinished) demo application via Gradle:
* gradle build
* gradle run

or

Build and run with Eclipse:
* Add the repository "http://dist.springsource.com/release/TOOLS/gradle" to Eclipse and install the addon option Gradle IDE.
* Then the code can be imported as a new Gradle project.


current REST-API:
* GET /pegelonline/stations
* GET /pegelonline/stations/{stationname}
* GET /pegelonline/stations/{stationname}/currentMeasurement

* commands return json representation of all stations / a specific station / the current measurement of a specific station