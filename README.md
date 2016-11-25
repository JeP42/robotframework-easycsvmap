[![Build Status](https://travis-ci.org/JeP42/robotframework-easycsvmap.svg?branch=master)](https://travis-ci.org/JeP42/robotframework-easycsvmap) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.github.jep42:robotframework-easycsvmap-reactor)](https://sonarqube.com/dashboard/index/com.github.jep42:robotframework-easycsvmap-reactor)

## robotframework-easycsvmap

### Description
The library provides keywords for Robot Framework to handle CSV files. It enables test case developers to select the cells of a CSV via flexible selector expressions e.g. by specifying the column name instead of column index. In most cases this is more intuitive than selecting cells via column index and it simplifies the work with CSVs especially for CSVs with a large number of columns. Moreover, the selector expressions support usage of regular expressions to select particular rows via a custom search string.

### Keywords

See the [keyword documentation](https://jep42.github.io/robotframework-easycsvmap/RobotEasyCsv.html) for information about the provided keywords.

### Usage

Since the library is implemented in Java it can be used together with Jython just by importing the library within the test file. To be able to
use it with Python the library has to be imported as remote library (refer to the [RobotFrameworkUserGuide](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html#remote-library-interface) for more information about Remote Libraries)

#### Jython

- download latest robotframework-easycsvmap-jar ([robotframework-easycsvmap-0.0.2.jar](https://repo.maven.apache.org/maven2/com/github/jep42/robotframework-easycsvmap/0.0.2/robotframework-easycsvmap-0.0.2.jar))
- add the jar to classpath (as well as its dependencies)
- import the library

```
*** Settings ***
Library    com.github.jep42.roboteasycsv.RobotEasyCsv
```

#### Remote Library Server

- download the remote-server-jar ([robotframework-easycsvmap-remoteserver-0.0.2-jar-with-dependencies.jar](https://repo.maven.apache.org/maven2/com/github/jep42/robotframework-easycsvmap-remoteserver/0.0.2/robotframework-easycsvmap-remoteserver-0.0.2-jar-with-dependencies.jar))
- start it via command line ``java -jar <remote-server-jar> --port 8270``
- import the library

```
*** Settings ***
Library    Remote    http://localhost:8270/RobotEasyCsv
```

### Dependencies
- org.robotframework:javalib-core:1.2.1 ([Download](https://mvnrepository.com/artifact/org.robotframework/javalib-core/1.2.1) from Maven Central)
- net.sf.opencsv:opencsv:2.3 ([Download](https://mvnrepository.com/artifact/net.sf.opencsv/opencsv/2.3) from Maven Central)
- com.google.code.findbugs:jsr305:2.0.1 ([Download](https://mvnrepository.com/artifact/com.google.code.findbugs/jsr305/2.0.1) from Maven Central)
- org.slf4j:slf4j-api:1.7.21 ([Download](https://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.7.21) from Maven Central)
- [only for sub-module remoteserver] com.github.ombre42:jrobotremoteserver:3.0 ([Download](https://mvnrepository.com/artifact/com.github.ombre42/jrobotremoteserver/3.0) from Maven Central)


### Build from source

- Clone the repository
- Execute ``mvn install`` on the root folder of the project to build the reactor which consist of two modules
- Sub-module "easycsvmap": The keyword library
- Sub-module "remoteserver": The remote server which already contains the keyword library




