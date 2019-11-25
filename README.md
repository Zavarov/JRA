# reddit

This project implements a Reddit adapter using [JRAW](https://github.com/mattbdean/JRAW)
and via a custom implementation backed by [pushshift.io](https://pushshift.io/), in order to request more than 1000 submissions.
It then uses the interface for analyzing both comments and submissions.
Overall, the first implementation is intended to be used in realtime, while the second one is intended for analysis over a long period of time.

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

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Zavarov**

## Dependencies:

This project requires at least **Java 11**.  
 * **Apache Commons Text**
   * Version: **1.8**
   * [Github](https://github.com/apache/commons-text)
 * **Apache Http Client**
   * Version: **4.5.10**
   * [Github](https://github.com/apache/httpcomponents-client)
 * **Guava**
   * Version: **28.1-jre**
   * [Github](https://github.com/google/guava)
 * **JRAW**
   * Version: **1.1.0**
   * [Github](https://github.com/mattbdean/JRAW)
 * **JSON**
   * Version: **20190722**
   * [Github](https://github.com/stleary/JSON-java)

## Plugins:
 * **Apache Maven JavaDoc Plugin**
   * Version: **3.1.1**
   * [Github](https://github.com/apache/maven-javadoc-plugin)

## Test Dependencies:
 * **AssertJ**
   * Version: **3.12.2**
   * [Github](https://github.com/joel-costigliola/assertj-core)
 * **JUnit**
   * Version: **4.12**
   * [Github](https://github.com/junit-team/junit4)

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments
A big shoutout to all the people involved with [pushshift.io](https://pushshift.io/). They do an awesome job
and their site was fundamental for my project of analyzing and evaluating the activity in individual subreddits.
