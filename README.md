# JDA (Java Reddit API)

JDA provides a simple way to exchange information between a Java application and the Reddit endpoints. 

### Getting started

By creating a new instance of the `Client` class and calling the `login` method, the user 
establishes the initial authentication with the Reddit API.

While not enforced, it is highly recommended to always call the `logout` once the program
is about to terminate. This not only minimizes the risk of a third party misusing a token
that has been leaked accidentally, but also makes the life easier for Reddit, since they
don't have to store the granted tokens longer than necessary.

In order to simply this process, two Client implementation are already provided. ScriptClient
can be used to create an instance, acting on behalf of a user. The class UserlessClient creates
an anonymous instance.

### Installing

In order to install this project, simply execute the maven command:

```
mvn clean install
```

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