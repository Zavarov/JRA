# reddit

This project implements a Reddit adapter using [JRAW](https://github.com/mattbdean/JRAW)
and via a custom implementation backed by [pushshift.io](https://pushshift.io/), in order to request more than 1000 submissions.
It then uses the interface for analyzing both comments and submissions.
Overall, we suggest using the first implementation in realtime, and the second one for analysis over a long time frame.

We use Class diagrams to describe the underlying architecture of the program, which are then,
in combination with [MontiCore](http://www.monticore.de/), to generate the corresponding source code.

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

* [MontiCore](https://github.com/MontiCore/monticore) - For generating the source code for the architecture.
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Zavarov**

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments
A big shoutout to all the people involved with [pushshift.io](https://pushshift.io/). They do an awesome job
and their site was fundamental for circumventing the 1000 submissions restriction.