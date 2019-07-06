# Project Title

This project provides a wrapper for the [JRAW](https://github.com/mattbdean/JRAW) library and multiple methods for analyzing both comments and submissions.

#### api

This module implements the wrapper for the [JRAW](https://github.com/mattbdean/JRAW) library.
It allows to request users, subreddits, comments and submissions.
Additionally, it is also possible to combine JRAW with the Pushshift API, to circumvent the 1000 submissions limit imposed by Reddit.

#### io

This module module implements the ability to store comments and submissions on the hard disk, to repeat analyses on data sets without being bound by the slow communication with Reddit.

#### charts

This module focuses on generating charts over the given datasets.
As an example, it is possible to create line charts over the number of submissions per day, or the distribution of submission flairs.

#### stats

This module focuses on a more complex analysis of the data sets.
As an example, it provides methods to compute the top submitters and top comments.

### Installing

In order to install this project, simply execute the maven command:

```
mvn clean install
```

## Running the tests

In order to run the tests of the api module, you need to set the Reddit id and secret of your Reddit bot to the [config](api/src/test/resources/config.json) file.

## Built With

* [MontiCore](https://github.com/MontiCore/monticore) - The language workbench for the comment and submission grammar in the io module.
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Zavarov**

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details
