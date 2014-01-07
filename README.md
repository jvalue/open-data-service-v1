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
* GET /api -> prints the API
* GET /pegelonline/stations -> json representation of all stations /
* GET /pegelonline/stations/{stationname} -> a specific station /
* GET /pegelonline/stations/{stationname}/currentMeasurement -> the current measurement of a specific station