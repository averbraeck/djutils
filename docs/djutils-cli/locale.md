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


## Rules for applying the locale

The priorities for the locale of a **value** in the command line are:

1. The `--locale=xx` option. This has the highest priority, and overrides any other Locale.
2. The `@DefaultValueLocale` for the value. If the default value already has a locale, this locale is also used as a starting point for the value itself. (absent a `--locale` option).
3. US English.

We did **not** choose to use Java's default locale for option 3 above, since if it is not set, it is highly dependent on the locale of the operating system of the computer. This can lead to results that differ per computer. If another Locale than US English is to be used for fields where it matters (fields with a unit, float, double), specify it explicitly with a `@DefaultValueLocale`.  

The priorities for the locale of a **default value** in the specification for the command line are:

1. The `@DefaultValueLocale` for the value. If the default value already has a locale, this locale is also used as a starting point for the value itself. (absent a `--locale` option).
2. US English.

The choice for US English for option 1 is again to not leave it open for circumstances that can differ per computer. The above priorities guarantee reproducibility across all computers, independent of settings of the locality in a program.


## Expected behavior

The following table shows the expected behavior of the interplay between different settings for the locales:

```text
==================================================================
| Java     | @Default    | --locale | default | actual | outcome |
| Locale   | ValueLocale | setting  | value   | value  | (en-US) |
==================================================================
| any      | -           | -        | -       | 0.5h   | 0.5h    |
------------------------------------------------------------------
| any      | -           | -        | -       | 0,5u   | VVVV    |
==================================================================
| any      | nl_NL       | -        | -       | 0.5h   | VVVV    |
------------------------------------------------------------------
| any      | nl_NL       | -        | -       | 0,5u   | 0.5h    |
------------------------------------------------------------------
| any      | en_US       | -        | -       | 0.5h   | 0.5h    |
------------------------------------------------------------------
| any      | en_US       | -        | -       | 0,5u   | VVVV    |
==================================================================
| any      | -           | nl_NL    | -       | 0.5h   | VVVV    |
------------------------------------------------------------------
| any      | -           | nl_NL    | -       | 0,5u   | 0.5h    |
------------------------------------------------------------------
| any      | -           | en-US    | -       | 0.5h   | 0.5h    |
------------------------------------------------------------------
| any      | -           | en-US    | -       | 0,5u   | VVVV    |
==================================================================
| any      | -           | any      | 0.2h    | -      | 0.2h    |
------------------------------------------------------------------
| any      | -           | any      | 0,2u    | -      | DDDD    |
==================================================================
| any      | en-US       | en-US    | 0.2h    | 0.5h   | 0.5h    |
------------------------------------------------------------------
| any      | en-US       | en-US    | 0.2h    | 0,5u   | VVVV    |
------------------------------------------------------------------
| any      | en-US       | en-US    | 0,2u    | 0.5h   | DDDD    |
------------------------------------------------------------------
| any      | en-US       | en-US    | 0,2u    | 0,5u   | DDDD    |
==================================================================
| any      | en-US       | nl_NL    | 0.2h    | 0.5h   | VVVV    |
------------------------------------------------------------------
| any      | en-US       | nl_NL    | 0.2h    | 0,5u   | 0.5h    |
------------------------------------------------------------------
| any      | en-US       | nl_NL    | 0,2u    | 0.5h   | DDDD    |
------------------------------------------------------------------
| any      | en-US       | nl_NL    | 0,2u    | 0,5u   | DDDD    |
==================================================================
| any      | nl_NL       | en-US    | 0.2h    | 0.5h   | DDDD    |
------------------------------------------------------------------
| any      | nl_NL       | en-US    | 0.2h    | 0,5u   | DDDD    |
------------------------------------------------------------------
| any      | nl_NL       | en-US    | 0,2u    | 0.5h   | 0.5h    |
------------------------------------------------------------------
| any      | nl_NL       | en-US    | 0,2u    | 0,5u   | VVVV    |
==================================================================
| any      | nl_NL       | nl_NL    | 0.2h    | 0.5h   | DDDD    |
------------------------------------------------------------------
| any      | nl_NL       | nl_NL    | 0.2h    | 0,5u   | DDDD    |
------------------------------------------------------------------
| any      | nl_NL       | nl_NL    | 0,2u    | 0.5h   | VVVV    |
------------------------------------------------------------------
| any      | nl_NL       | nl_NL    | 0,2u    | 0,5u   | 0.5h    |
==================================================================

VVVV = value error
DDDD = default value error
```
