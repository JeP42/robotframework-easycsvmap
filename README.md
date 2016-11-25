## robotframework-easycsvmap

### Description

### Keywords

See the [keyword documentation](https://github.com/JeP42/robotframework-easycsvmap/RobotEasyCsv.html) for detailed information about the provided keywords.

### Usage

Since the library is implemented in Java it can be used together with Jython just by importing the library within the test file. To be able to
use it with Python the library has to be imported as remote library (refer to the [RobotFrameworkUserGuide](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html#remote-library-interface) for more information about Remote Libraries)

#### Jython

- download latest robotframework-easycsvmap-jar (robotframework-easycsvmap-0.0.1.jar)
- add to classpath
- import the library

```
*** Settings ***
Library    com.github.jep42.roboteasycsv.RobotEasyCsv
```

#### Remote Library Server

- download the remote-server-jar (robotframework-easycsvmap-remoteserver-0.0.1-jar-with-dependencies.jar)
- start it via command line ``java -jar <remote-server-jar> --port 8270``
- import the library

``
*** Settings ***
Library    Remote    http://localhost:8270/EasyCSVLibrary
``
