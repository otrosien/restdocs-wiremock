# Spring REST Docs WireMock Integration

[![Build Status](https://travis-ci.org/ePages-de/restdocs-wiremock.png)](https://travis-ci.org/ePages-de/restdocs-wiremock)
[ ![Download](https://api.bintray.com/packages/epages/maven/restdocs-wiremock/images/download.svg) ](https://bintray.com/epages/maven/restdocs-wiremock/_latestVersion)

This is a REST Docs plugin for auto-generating [WireMock](http://wiremock.org/) stubs
as part of documenting your REST API with [Spring REST Docs](http://projects.spring.io/spring-restdocs/).

The basic idea is to use the requests and responses from the test cases as mock templates for re-use 
in a client setup. The mock templates are packaged as jar files and can be published into your company's
artifact repository.

## Contents

This repository contains three projects

* `restdocs-wiremock`: The library to extend Spring REST Docs with wiremock stub generation.
* `restdocs-server`: A sample server documenting its REST API (the Spring REST Docs "notes" example)
* `restdocs-client`: The client using the server API, with integration testing against a wiremock implementation.


## How to include `restdocs-wiremock` into your project

The project is published on `jcenter` from bintray, so firstly, you need to add `jcenter` as package repository for your project.

Then, when using gradle, add a testCompile dependency.

```
dependencies {
  testCompile('com.epages:restdocs-wiremock:0.5.2')
}
```

When using maven, add a dependency in test scope.

```
<dependency>
	<groupId>com.epages</groupId>
	<artifactId>restdocs-wiremock</artifactId>
	<version>0.5.2</version>
	<scope>test</scope>
</dependency>
```

## How does it look like?

During REST Docs run, snippets like the one below are generated and put into a dedicated jar file, which you can
publish into your artifact repository. 

Integration into your test code is as simple as replacing the `andDo(document())` calls with
`andDo(documentWithWireMock())` from `com.epages.restdocs.WireMockDocumentation`. For example:

```java
class ApiDocumentation {
    void testIndex() {
        this.mockMvc.perform(get("/notes/1").accept(MediaType.APPLICATION_JSON)) 
        .andExpect(status().isOk()) 
        .andDo(documentWithWireMock("get-note"));
    }
}
```

The snippet below is the resulting snippet of a `200 OK` response to `/notes/1`, with
the response body as provided by the integration test.

```json
{
  "request" : {
    "method" : "GET",
    "urlPath" : "/notes/1"
  },
  "response" : {
    "status" : 200,
    "headers" : {
      "Content-Type" : [ "application/hal+json" ],
      "Content-Length" : [ "344" ]
    },
    "body" : "{\n  \"title\" : \"REST maturity model\",\n  \"body\" : \"http://martinfowler.com/articles/richardsonMaturityModel.html\",\n  \"_links\" : {\n    \"self\" : {\n      \"href\" : \"http://localhost:8080/notes/1\"\n    },\n    \"note\" : {\n      \"href\" : \"http://localhost:8080/notes/1\"\n    },\n    \"tags\" : {\n      \"href\" : \"http://localhost:8080/notes/1/tags\"\n    }\n  }\n}"
  }
}
```


## Building from source

### Publish the current restdocs-wiremock library code into your `mavenLocal`.

```shell
./gradlew restdocs-wiremock:publishToMavenLocal
```

You should have `restdocs-wiremock-0.5.3-SNAPSHOT` in your maven repository:

```shell
 ls ~/.m2/repository/com/epages/restdocs-wiremock/0.5.3-SNAPSHOT/
restdocs-wiremock-0.5.3-SNAPSHOT.jar  restdocs-wiremock-0.5.3-SNAPSHOT.pom
```

###  Run the server tests, which uses the WireMock integration into Spring REST Docs.

```shell
./gradlew restdocs-server:build restdocs-server:publishToMavenLocal
```

As a result, there is a `restdocs-server-wiremock` jar file in your maven repository:

```shell
ls ~/.m2/repository/com/epages/restdocs-server/0.5.3-SNAPSHOT/
restdocs-server-0.5.3-SNAPSHOT-wiremock.jar
```

Mind that this jar only contains a set of json files without explicit dependency on WireMock itself. 

###  Run the client tests, that expect a specific API from the server. 

By mocking a server via WireMock the client can be tested in isolation, but would notice a breaking change.

```shell
./gradlew restdocs-client:build
```

	
