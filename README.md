# reddit

This project implements the Reddit interface specified in [monticore-commons](https://github.com/Zavarov/monticore-commons) using [JRAW](https://github.com/mattbdean/JRAW)
and via a custom implementation backed by [pushshift.io](https://pushshift.io/), in order to request more than 1000 submissions.
It then uses the interface for analyzing both comments and submissions.
Overall, the first implementation is intended to be used in realtime, while the second one is intended for analysis over a long period of time.

#### api

This module implements the different interfaces for the client, submission and comments.

#### stats

This module focuses on a more complex analysis of the data sets.
As an example, it provides methods to compute the top submitters and top comments.
In future updates this module will be separated and become an independent project.

### Installing

In order to install this project, simply execute the maven command:

```
mvn clean install
```

## Running the tests

In order to run the tests of the api module, you need to create the json file containing the credentials for your Reddit bot to in api/src/test/resources/config.json.

The template for the file is
```
{
  "name"     : "NAME",
  "version"  : "VERSION",
  "clientId" : "CLIENTID",
  "secret"   : "SECRET"
}
```

## Built With

* [MontiCore](https://github.com/MontiCore/monticore) - The language workbench for the comment and submission grammar in the io module.
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Zavarov**

## Dependencies:

This project requires at least **Java 8**.
 * **Apache Http Client**
   * Version: **4.5.10**
   * [Github](https://github.com/apache/httpcomponents-client)
 * **Apache Maven JavaDoc Plugin**
   * Version: **3.1.1**
   * [Github](https://github.com/apache/maven-javadoc-plugin/)
 * **AssertJ**
   * Version: **3.12.2**
   * [Github](https://github.com/joel-costigliola/assertj-core)
 * **JRAW**
   * Version: **1.1.0**
   * [Github](https://github.com/mattbdean/JRAW)
 * **JSON**
   * Version: **20190722**
   * [Github](https://github.com/stleary/JSON-java)
 * **JUnit**
   * Version: **4.12**
   * [Github](https://github.com/junit-team/junit4)
 * **monticore-commons**
   * Version: **1.2**
   * [Github](https://github.com/Zavarov/monticore-commons)

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments
A big shoutout to all the people involved with [pushshift.io](https://pushshift.io/). They do an awesome job
and their site was fundamental for my project of analyzing and evaluating the activity in individual subreddits.
