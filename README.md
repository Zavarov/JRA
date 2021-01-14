# reddit

This project implements a Reddit wrapper, allowing an user to access subreddits, submissions and comments.
Currently, it is only possible to read those instances.
The backend implementation is done via [JRAW](https://github.com/mattbdean/JRAW), a custom implementation using 
[pushshift.io](https://pushshift.io/) and via serialization using [JSON](https://en.wikipedia.org/wiki/JSON).

Class diagrams describe the architecture of the program, which are used in combination with [MontiCore](http://www.monticore.de/),
to generate the corresponding source code.

### Getting started

The vartas.reddit.Client class provides the interface the Reddit API and allows access both subreddits and user accounts.
Each subreddit consists of a collection of submission. It is possible to either request individual submissions by their
id or several via a time interval. 

Each submission has a list of comments. We preserve the tree structure of the comments, which can be
accessed using the visitor pattern. However, for simplicity, we also provide access to all comments
at once via an unordered list.

For code examples of the different functions, please visit the [Wiki](https://github.com/Zavarov/reddit/wiki).

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