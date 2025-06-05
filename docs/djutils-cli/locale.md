# DJUTILS-CLI Use of locale

## Introduction

Depending on the computer's locale, one of the two examples below works, and one does not work:

```text
java -jar Program.jar --duration=1.25s
java -jar Program.jar --duration=1,25s
```

The locale determines how a value, in this case a duration that is specified in seconds, will be parsed. The US English locale uses symbols `0-9` for the numbers, a decimal point, and a comma for the thousands separator. In several European settings, the thousands separator is a point, and the decimal separator is a comma. Some locales have different symbols for thousands separators, such as a space or half space. Some locales have different symbols for the numbers. This can lead to confusion. 

The options that are offered through the djutils-cli project and the `CliUtil` class are:

- specify nothing, resulting in accepting values and default values in arguments to be in `en-US`.
- specifying a `--locale` option, allowing the setting of a locale for the parameters; the default values as specified in the program are still `en-US`.
- specifying a `@DefaultValueLocale` annotation to a parameter, indicating that its default value is in another locale than `en_US`; the value is also expected to use this locale. An override for the values can take place with the `--locale` option.


## Specifying the locale

The class `CliIUtil` has the option to allow the user to use the `--locale=XX` option to specify the locale. In the example from the introduction, the following examples would work, without any change in the code:

```java
@Command(description = "Test", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
public static class Options
{
  @Option(names = {"--duration"}, description = "duration", defaultValue = "0.0s")
  private Duration duration;
```

```text
java -jar Program.jar --duration=1.25s
java -jar Program.jar --duration=1.25s --locale=en-US
java -jar Program.jar --duration=1.25s --locale=en
java -jar Program.jar --duration=1,25s --locale=nl_NL
java -jar Program.jar --duration=1,25s --locale=nl
```

## Specifying a locale for the defaults

When a program is made for a set of users who all use a certain locale, e.g., Dutch, the defaults can also be specified in the program using that locale.

```java
@Command(description = "Test", name = "Test", mixinStandardHelpOptions = true, version = "1.0")
public static class Options
{
    @DefaultValueLocale("nl")
    @Option(names = {"-d", "--duration"}, description = "Duration", defaultValue = "0,5s")
    private Duration duration;

    @DefaultValueLocale("nl")
    @Option(names = {"-l", "--length"}, description = "Length", defaultValue = "17,2m")
    private Length length;
    
    @DefaultValueLocale("nl")
    @Option(names = {"-f", "--fraction"}, description = "Fraction", defaultValue = "0,4")
    private double fraction;
```

The user can now specify the parameters in the chosen locale **without** a `--locale` option:

```text
java -jar Program.jar --duration=1,25uur --length=200,67m --fraction=0,5
```
