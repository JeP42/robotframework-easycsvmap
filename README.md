[![Build Status](https://travis-ci.org/JeP42/robotframework-easycsvmap.svg?branch=master)](https://travis-ci.org/JeP42/robotframework-easycsvmap)
[![codecov](https://codecov.io/gh/JeP42/robotframework-easycsvmap/branch/master/graph/badge.svg)](https://codecov.io/gh/JeP42/robotframework-easycsvmap)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.github.jep42:easycsvmap-reactor)](https://sonarcloud.io/projects?search=com.github.jep42:easycsvmap-reactor)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jep42/robotframework-easycsvmap/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jep42/robotframework-easycsvmap)
![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/jep42/robotremoteserver-easycsvmap.svg?style=plastic)

## robotframework-easycsvmap

### Description
The library provides keywords for Robot Framework to handle CSV files. It enables test case developers to select the cells of a CSV via flexible selector expressions e.g. by specifying the column name instead of column index. In most cases this is more intuitive than selecting cells via column index and it simplifies the work with CSVs especially for CSVs with a large number of columns. Moreover, the selector expressions support usage of regular expressions to select particular rows via a custom search string.
The library supports sessions to be able to work with multiple CSV files in parallel.

### Keywords

See the [keyword documentation](https://jep42.github.io/robotframework-easycsvmap/RobotEasyCsv.html) for information about the provided keywords.

### Usage

Since the library is implemented in Java it can be used together with Jython just by importing the library within the test file. To be able to
use it with Python the library has to be imported as remote library (refer to the [RobotFrameworkUserGuide](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html#remote-library-interface) for more information about Remote Libraries). Be aware that the
Java remote server implementation jrobotremoteserver does not support different library scopes (ROBOT\_LIBRARY\_SCOPE), hence the scope of the library is always GLOBAL.

#### Jython

- download latest robotframework-easycsvmap-jar ([robotframework-easycsvmap-x.x.x.jar](https://repo.maven.apache.org/maven2/com/github/jep42/robotframework-easycsvmap))
- add the jar to classpath (as well as its dependencies)
- import the library

```
*** Settings ***
Library    com.github.jep42.roboteasycsv.RobotEasyCsv
```

#### Remote Library Server

- download the remote-server-jar ([robotframework-easycsvmap-remoteserver-x.x.x-jar-with-dependencies.jar](https://repo.maven.apache.org/maven2/com/github/jep42/robotframework-easycsvmap-remoteserver))
- start it via command line ``java -jar <remote-server-jar> --port 8270``
- import the library

```
*** Settings ***
Library    Remote    http://localhost:8270/RobotEasyCsv
```


#### Robot Testcase

Assume a CSV file ``${TEMPDIR}/userdata.csv`` with the following content:

```
firstname;lastname;email;sex
Peter;Pan;peterpan@neverland.com;male
Tinker;Bell;tb@neverland.net;female
James;Hook;captainhook@pirates.com;male
```

##### Reading CSV values

A common scenario is to read values from a CSV (e.g. to perform checks on it).


```
*** Test Cases ***
Check CSV Values
    ...
    Parse Csv From File    ${csvSessionId}    ${TEMPDIR}/userdata.csv    0
    {name}=    Get First Csv Value    ${csvSessionId}    {1}.lastname
    Should Be Equal 	${name} 	Pan
    {name}=    Get First Csv Value    ${csvSessionId}    {2}.lastname
    Should Be Equal 	${name} 	Bell
    ...
```

##### Changing CSV values

Another common scenario is to change values of a CSV (e.g. to test upload/download scenarios) or to add rows.

```
*** Test Cases ***
Change CSV Values
    ...
    Parse Csv From File    ${csvSessionId}    ${TEMPDIR}/userdata.csv    0
    Set Csv Values    ${csvSessionId}    {1}.email    peterp@neverland.net
    Set Csv Values    ${csvSessionId}    [email=^captain.*$].firstname    LittleJames
    Add Row    ${csvSessionId}    Wendy    Darling    wendy@home.com    female
    Save Csv To File    ${csvSessionId}    ${TEMPDIR}/userdata_updated.csv
    ...
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




