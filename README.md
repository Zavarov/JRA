# JDA (Java Reddit API)

JDA provides a simple way to exchange information between a Java application and the Reddit endpoints. 

### Getting started

By creating a new instance of the `Client` class and calling the `login` method, the user 
establishes the initial authentication with the Reddit API.


While not enforced, it is highly recommended to always call the `logout` once the program
is about to terminate. This not only minimizes the risk of a third party misusing a token
that has been leaked accidentally, but also makes the life easier for Reddit, since they
don't have to store the granted tokens longer than necessary.

### Installing

In order to install this project, simply execute the maven command:

```
mvn clean install
```

## Running the tests

In order to run the tests that directly connect to the Reddit API, you need to create the json file containing the 
credentials for the respective module in /src/test/resources/config.json.

The template for the file is
```
{
  "platform" : "PLATFORM",
  "name"     : "NAME",
  "id"       : "ID",
  "secret"   : "SECRET"
}
```

The tests are done over [r/redditdev](https://www.reddit.com/r/redditdev/) and the submissions over the past day.
Since those are live values, the results of the tests after every execution may differ.

## Built With

* [MontiCore](https://github.com/MontiCore/monticore) - For generating the source code for the architecture.
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Zavarov**

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments
A big shoutout to all the people involved with [pushshift.io](https://pushshift.io/). They do an awesome job
and their site was fundamental for circumventing the 1000 submissions restriction.