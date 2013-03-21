Stuff - a REST, CQRS, EventSourcing, DCI, functional example
============================================================

Doing all the things mentioned in the title is hard, requires tons of libraries, and is too complicated for most devs
to get right. Or is it? This sample app is an attempt at exploring if it's possible to do all of that while still
keeping the code reasonably simple, and with minimal amount of libraries to help out.

Design
------
The app is packaged as a WAR and implemented with Restlet for the REST support. REST resources will call DCI functions
that encapsulate usecases, which in turn calls domain. Domain generates events which are distributed to all view models,
which then can be viewed through REST calls. And that's about it.

Installation notes
------------------
* Install Java 8 Lambda edition (that's the functional part)
* Add the following (with your correct path) to your .m2/settings.xml file for Maven to find Java 8:
```xml
<settings>
  <profiles>
    <profile>
      <id>compiler</id>
        <properties>
          <JAVA_1_8_HOME>/Library/Java/JavaVirtualMachines/jdk1.8.0</JAVA_1_8_HOME>
        </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>compiler</activeProfile>
  </activeProfiles>
</settings>
```
* Clone from GitHub
* Run mvn install
* Run the WAR in your favourite Container, or do mvn jetty:run
* View http://localhost:8080/stuff/
