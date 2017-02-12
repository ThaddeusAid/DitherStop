# DitherStop
================================

Based on a Google sample project.

## Goal
DitherStop.com is an application made in order to help Steam Gamers make an informed choice on the next game that is already in their account to play. This will be based on several things.  

* User ratings scraped off Steam
* A history of os user play times by tags or genre
* other stuff

## Backgroung
I buy a lot of bundles and only have a vague idea of most of the games in my library so I wanted a tool to help me choose an order to play them in. Thus the original Dither Stop was made in a simple AWS LAMP instance. I want to move DitherStop off AWS onto Google App Engine, hense this project. This is also a project to allow some of my students at San Jose State University to help learn Angular app coding.

Author: Thaddeus Aid <thaddeus.aid@gmail.com>

## Project Setup
Install [Apache Maven][1] and [Karma][2] if you haven't. See the links
for install instructions. Update the `angular-seed` submodule with the
following command:

> $ git submodule update --init

To run the app locally, run the following command:

> $ mvn appengine:devserver

## Testing

* Java Unit Tests
  > $ mvn test

* Javascript Unit Tests
  > $ scripts/test.[sh|bat]

* Integration Tests

  After launching the devserver, run the following:
  > $ scripts/e2e-test.[sh|bat]

Note: If you want to debug the Java Unit Tests, add `-DforkMode=never`
VM Option to your IDE's debug configuration.

### How to deploy
To deploy the app, change the value of the `application` element in
your `src/main/webapp/WEB-INF/appengine-web.xml` and run the following
command:

> $ mvn appengine:update

## TODO

* Integrate Karma javascript tests with Maven.  
* Write the end-to-end tests.  
* Write scraping program for harvesting information off [Steam](http://store.steampowered.com/)  

## Contributing changes

* See CONTRIB.md

## Licensing

* See LICENSE

[1]: http://maven.apache.org/
[2]: http://karma-runner.github.io/0.8/index.html
