# widgets

## Challenge instructions
See [Challenge description](Take_Home_Test_(Java)_v2.pdf)

## Dependencies ##
* Maven
* Java 11

## Testing ##
```bash
$ mvn install
$ mvn test
```

## Caveats ##

* The third optional H2/JDBC challenge is unfinished and it is included in the *h2db* branch.
* Both the unit tests and the integration tests are running at the same *test* phase, as I didn't 
have enough time to separate them, but the objective is to have them in different commands/executions
* The code is not using different POJOs for the different layers (at least Controller vs Model layers) just to 
optimise my available time, but it is something desired in case I had more time to write the converters and test them properly
* The main class is being ignored from the coverage as I didn't have enough time to refactor it in a way it is testable from unit tests
* There is no exception handling nor input validation just to optimize the time, but if I had enough time, this is something I would implement
* the WidgetModel data object has a property called *zindex* while I wanted it to be zIndex, but the ObjectMapper was handling it in case insensitive. 
I know it is quite simple to override the property name via annotations, but I have just decided to focus at the main problem and when it's done I look at it.
