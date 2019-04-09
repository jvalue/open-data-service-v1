# PegelOnline Configuration Example

## Description

This is an example on how to configure the ODS. Further documentation on the REST API can be found here:
- implementation of the [REST api](https://github.com/jvalue/open-data-service/tree/master/server/src/main/java/org/jvalue/ods/rest)
- [Postman collection](https://www.getpostman.com/collections/25d694d4ba21348c5530)


## Overview


The placeholder `{{ods_base_url}}` is represented as `http://localhost:8080/ods/api/v1` in the default configuration.

As an example, the ODS is configured and used for PegelOnline as data source:

#### Create a source

In this step the data source is configured. This contains the meta data of the source, like the origin, author, terms of use, etc.
Additionally, the schema of the data source is provided, including which data is mandatory and which are optional.

#### Configure a filter chain

By configuring a filter chain, simple steps towards the final data are executed. This includes specifying which adapter is used (protocol + format to retrieve the data).
The DbInsertionFilter is responsible for persisting the data in the final format, the NotificationFilter triggers notifications. Additionally, some configuration of the filter chain can be given, like when it should be executed.

#### Create a view on the retrieved data

This represents a querying mechanism that operates on the stored data. It relies on the capabilities of the underlying CouchDB and uses an map-reduce-like syntax.

#### Get the data

Fetching the data from the configured data view is a simple HTTP call. The format of the data depends on the prior steps.


## Create data source

`HTTP PUT` on destination `{{ods_base_url}}/datasources/pegelonline`

<details><summary>Headers</summary>

Header | Value
|---|---|
Content-Type | application/json
Authorization | Basic YWRtaW46N2tweWd2YXF0M3FwM25lMnY4YnpxYzJkcmdq

</details>


<details><summary>Body</summary>

```$json
{
  "domainIdKey": "/gaugeId",
  "schema":
  {
    "$schema": "http://json-schema.org/draft-04/schema#",
    "type": "object",
    "properties": {
        "uuid": {
            "type": "string"
        },
        "number": {
            "type": "string"
        },
        "shortname": {
            "type": "string"
        },
        "longname": {
            "type": "string"
        },
        "km": {
            "type": "number"
        },
        "agency": {
            "type": "string"
        },
        "longitude": {
            "type": "number"
        },
        "latitude": {
            "type": "number"
        },
        "water": {
            "type": "object",
            "properties": {
                "shortname": {
                    "type": "string"
                },
                "longname": {
                    "type": "string"
                }
            }
        },
        "timeseries": {
            "type": "array",
            "items": {
                "type": "object",
                "properties": {
                    "shortname": {
                        "type": "string"
                    },
                    "longname": {
                        "type": "string"
                    },
                    "unit": {
                        "type": "string"
                    },
                    "equidistance": {
                        "type": "integer"
                    },
                    "currentMeasurement": {
                        "type": "object",
                        "properties": {
                            "timestamp": {
                                "type": "string"
                            },
                            "value": {
                                "type": "number"
                            },
                            "trend": {
                                "type": "integer"
                            },
                            "stateMnwMhw": {
                                "type": "string"
                            },
                            "stateNswHsw": {
                                "type": "string"
                            }
                        }
                    },
                    "gaugeZero": {
                        "type": "object",
                        "properties": {
                            "unit": {
                                "type": "string"
                            },
                            "value": {
                                "type": "integer"
                            },
                            "validFrom": {
                                "type": "string"
                            }
                        }
                    },
                    "characteristicValues": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "properties": {
                                "shortname": {
                                    "type": "string"
                                },
                                "longname": {
                                    "type": "string"
                                },
                                "unit": {
                                    "type": "string"
                                },
                                "value": {
                                    "type": "integer"
                                },
                                "validFrom": {
                                    "type": "string"
                                },
                                "timespanStart": {
                                    "type": "string"
                                },
                                "timespanEnd": {
                                    "type": "string"
                                },
                                "occurrences": {
                                    "type": "array",
                                    "items": {
                                        "type": "string"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
  },
  "metaData": {
    "name": "de-pegelonline",
    "title": "pegelonline",
    "author": "Wasser- und Schifffahrtsverwaltung des Bundes (WSV)",
    "authorEmail": "https://www.pegelonline.wsv.de/adminmail",
    "notes": "PEGELONLINE stellt kostenfrei tagesaktuelle Rohwerte verschiedener gewÃƒÂ¤sserkundlicher Parameter (z.B. Wasserstand) der Binnen- und KÃƒÂ¼stenpegel der WasserstraÃƒÅ¸en des Bundes bis maximal 30 Tage rÃƒÂ¼ckwirkend zur Ansicht und zum Download bereit.",
    "url": "https://www.pegelonline.wsv.de",
    "termsOfUse": "http://www.pegelonline.wsv.de/gast/nutzungsbedingungen"
  }
}
```
</details>




## Configure filter chain
`HTTP PUT` on destination `{{ods_base_url}}/datasources/pegelonline/filterChains/mainFilter`

<details><summary>Headers</summary>

Header | Value
|---|---|
Content-Type | application/json
Authorization | Basic YWRtaW46N2tweWd2YXF0M3FwM25lMnY4YnpxYzJkcmdq

</details>


<details><summary>Body</summary>

```$json
{
  "processors" : [
    {
      "name" : "JsonSourceAdapter",
      "arguments" : {
        "sourceUrl" : "http://pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true&includeCharacteristicValues=true"
      }
    },
    {
      "name" : "PegelOnlineMerger",
      "arguments" : { }
    },
    {
      "name" : "DbInsertionFilter",
      "arguments" : {
        "updateData" : true
      }
    },
    {
      "name" : "NotificationFilter",
      "arguments" : { }
    }
  ],
  "executionInterval" : {
    "period" : 60,
    "unit" : "MINUTES"
  }
}
```
</details>


## Configure data view
`HTTP PUT` on destination `{{ods_base_url}}/datasources/pegelonline/views/someView`

<details><summary>Headers</summary>

Header | Value
|---|---|
Content-Type | application/json
Authorization | Basic YWRtaW46N2tweWd2YXF0M3FwM25lMnY4YnpxYzJkcmdq

</details>


<details><summary>Body</summary>

```$json
{
  "mapFunction": "function(doc) { emit(doc.viewId, doc); }"
}
```
</details>


## Get the data
`HTTP GET` on destination `{{ods_base_url}}/datasources/pegelonline/views/someView?execute=true`

<details><summary>Headers</summary>

Header | Value
|---|---|
Content-Type | application/json
Authorization | Basic YWRtaW46N2tweWd2YXF0M3FwM25lMnY4YnpxYzJkcmdq

</details>
