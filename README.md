# Spring REST Docs WireMock Integration

[![Build Status](https://travis-ci.org/ePages-de/restdocs-wiremock.png)](https://travis-ci.org/ePages-de/restdocs-wiremock)
[ ![Download](https://api.bintray.com/packages/epages/maven/restdocs-wiremock/images/download.svg) ](https://bintray.com/epages/maven/restdocs-wiremock/_latestVersion)

This is a sample of how to auto-generate [WireMock](http://wiremock.org/) stubs
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

When using gradle, add a testCompile dependency.

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


## How to run the provided examples

1. Publish the restdocs-wiremock artifact into your `mavenLocal`.


```shell
./gradlew restdocs-wiremock:publishToMavenLocal
```

You should have `restdocs-wiremock-0.5.2` in your maven repository:

```shell
 ls ~/.m2/repository/com/epages/restdocs-wiremock/0.5.2/
restdocs-wiremock-0.5.2.jar  restdocs-wiremock-0.1.pom
```

2. Run the server tests, which uses the WireMock integration into Spring REST Docs.

```shell
./gradlew restdocs-server:build restdocs-server:publishToMavenLocal
```

As a result, there is a `restdocs-server-wiremock` jar file in your maven repository:

```shell
ls ~/.m2/repository/com/epages/restdocs-server/0.5.2/
restdocs-server-0.5.2-wiremock.jar
```

Mind that this jar only contains a set of json files without explicit dependency on WireMock itself. 

3. Run the client tests, that expect a specific API from the server. By mocking a server
via WireMock the client can be tested in isolation, but would notice a breaking change.


```shell
./gradlew restdocs-client:build
```

	
