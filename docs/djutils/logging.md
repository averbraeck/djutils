# Logging helper classes

## Introduction

The 'logback' package combined with 'slf4j' (see [https://www.slf4j.org/](https://www.slf4j.org/) and [https://logback.qos.ch/](https://logback.qos.ch/), is a very fast and small set of logger classes. slf4j/logback is extended with several helper classes to make logging more fine-grained and easier to control. The most important extension provided in the DJUTILS package is the so-called `CategoryLogger`. This is a logger that has categories that can be treated in different ways. An example of the use of a logger with a category, where we have categories `Cat.BASE` and `Cat.ADVANCED`:

```java
CategoryLogger.filter(Cat.BASE).debug("Parameter {} initialized correctly", param1.toString());
CategoryLogger.filter(Cat.ADVANCED).error(exception, "Illegal argument: {}", arg);
```

In case a message should always be displayed, independent of the category or a category's settings, we can use the always() clause instead of the filter(category) clause:

```java
CategoryLogger.always().info("Program started");
CategoryLogger.always().error(exception, "Program failed to initialize. Command was: {}", command);
```


## LogCategory

Categories should be of the type `LogCategory`. An example of the definition of LogCategories for the DSOL project is given below. The comments have been left out.

```java
public final class Cat
{
  public static final LogCategory DSOL = new LogCategory("DSOL");
  public static final LogCategory NAMING = new LogCategory("NAMING");
  public static final LogCategory EVENT = new LogCategory("ROAD");
  public static final LogCategory SWING = new LogCategory("SWING");
  public static final LogCategory WEB = new LogCategory("WEB");
}
```


## Settings of the loggers

The `CategoryLogger` has several methods to add or remove LogCategories for which logging needs to be done. Only LogCategories that have been registered will be logged.

```java
CategoryLogger.addLogCategory(NAMING);
CategoryLogger.removeLogCategory(NAMING);
```

The log level for all category loggers can be set with the `CategoryLogger.setLogLevelAll(level)` method. The following log levels exist:

* TRACE: Output all log entries. The trace(...) method outputs on the TRACE level.
* DEBUG: Output all log entries but trace log entries. The debug(...) method outputs on the DEBUG level.
* INFO: Output all log entries but trace and debug log entries (default). The info(...) method outputs on the INFO level.
* WARN: Output error and warning log entries. The warn(...) method outputs on the WARN level.
* ERROR: Output only error log entries. The error(...) method outputs on the ERROR level.
* OFF: Disable logging (no log entries will be output)

The formatting of the log message for all category loggers can be set with the `CategoryLogger.setPattern(pattern)` method. An example of a pattern is:

```java
CategoryLogger.setPatternAll("%date{HH:mm:ss} %-5level %-6logger{0} %class.%method:%line - %msg%n");
```

which means that the date, log level, log category, class name, method and line number are logged, followed by the message, and a newline at the end. The most important formatting components are:

```
* %date{HH:mm:ss.SSS}   Timestamp (default format shown; many options like ISO8601)
* %level / %-5level     Log level (pad to fixed width with %-5level)
* %logger / %logger{0}  Logger name (full or last component only; {n} = # of segments)
* %thread               Thread name
* %msg / %message       The actual log message
* %n                    Platform-specific newline
* %class / %class{1}    Calling class (full or just last segment with {1})
* %method               Calling method
* %line                 Source line number
* %file                 Source file name
* %caller               Shortcut for class, method, file, and line in one
* %marker               SLF4J marker (if present)
* %X{key}               MDC value for given key
* %replace(p){r,e}      Apply regex replacement to pattern part p
* %highlight(%msg)      ANSI colored message (useful on console)
```

For a more complete list, see also [https://logback.qos.ch/manual/layouts.html](https://logback.qos.ch/manual/layouts.html).


## Logging

Each of the log methods trace(...), debug(...), info(...), warn(...), and error(...) has several configurations that can be used. Below, the definitions for info(...) are given, but these are the same for each of the other methods. The same holds for the always() clause, which can of course be replaced by filter(logCategory) clause.

| Method call | Explanation |
| ---------- | ---------- |
| `CategoryLogger.always().info(String);` | message = string |
| `CategoryLogger.always().info(Object);` | message = `object.toString()` |
| `CategoryLogger.always().info(String, Object...);` | message = string; each of the {} clauses in the string is filled <br/>with the corresponding object.toString() in the array |
| `CategoryLogger.always().info(Supplier);` | the supplier functional interface (see java.util.function.Supplier <br/>for more information) generates the message |
| `CategoryLogger.always().info(String, Supplier...);` | message = string; each of the {} clauses in the string is filled with <br/>a calculated value from the Supplier functional interfaces. |
| `CategoryLogger.always().info(Throwable);` | message = `throwable.getMessage()`. Stack trace can be shown if <br/>that is part of the message. |
| `CategoryLogger.always().info(Throwable, String);` | message = string. Stack trace can be shown if that is part of the message. |
| `CategoryLogger.always().info(Throwable, Object);` | message = `object.toString()`. Stack trace can be shown if that is <br/>part of the message. |
| `CategoryLogger.always().info(Throwable, String, Object...);` | message = string; each of the {} clauses in the string is filled with the <br/>corresponding `object.toString()` in the array. Stack trace can be shown <br/>if that is part of the message. |
| `CategoryLogger.always().info(Throwable, Supplier)`; | The message is generated by the supplier functional interface. <br/>Stack trace can be shown if that is part of the message. |
| `CategoryLogger.always().info(Throwable, String, Supplier...);` | message = string; each of the {} clauses in the string is filled with a <br/>calculated value from the Supplier functional interfaces. Stack trace <br/>can be shown if that is part of the message. |
