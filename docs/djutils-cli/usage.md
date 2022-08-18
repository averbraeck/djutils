## DJUTILS-CLI Usage

### Introduction

Most code needs an executable program for which parameters can be set, e.g.:

```text
java -jar FederateStarter.jar --port=5000 --server=local
```

Parsing these command line parameters is quite some work. Several packages have been developed to parse the Command Line Interface (CLI)  in an easy manner. In DJUTILS we use and extend picocli (see [https://picocli.info](https://picocli.info)). Picocli is an open-source one-file framework that can parse all sorts of command line parameters with very little code. The package has an Apache license and can be freely used. It is embedded into DJUTILS-CLI through Maven.


### Usage in DJUTILS

The class `CliIUtil` offers a helper method to display `--help` and `--version` without starting the program. The method is used as follows: 

```java
 public static void main(final String[] args) throws Exception
 {
     Program program = new Program(); // initialize the Checkable class with the @Option information
     CliUtil.execute(program, args); // register Unit converters, parse the command line, catch --help, --version and error
     // do rest of what the main method should do
 }
```
 
When the program is `Checkable`, the `check()` method is called after the arguments have been parsed. Here, further checks on the arguments (i.e., range checks) can be carried out. Potentially, `check()` can also provide other initialization of the program to be executed, but this can better be provided by other methods in `main()` . Make sure that expensive initialization is not carried out in the constructor of the program class that is given to the execute method. Alternatively, move the command line options to a separate class, e.g. called `Options` and initialize that class rather than the real program class. The real program can then take the values of the program from the `Options` class. An example:  

```java
public class Program
 {
     @Command(description = "Test program for CLI", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
     public static class Options implements Checkable
     {
         @Option(names = {"-p", "--port"}, description = "Internet port to use", defaultValue = "80")
         private int port;
 
         public int getPort()
         {
             return this.port;
         }
 
         @Override
         public void check() throws Exception
         {
             if (this.port <= 0 || this.port > 65535)
                 throw new Exception("Port should be between 0 and 65535");
         }
     }
 
     public Program()
     {
         // initialization for the program; avoid really starting things
     }
 
     public static void main(final String[] args)
     {
         Options options = new Options();
         CliUtil.execute(options, args);
         System.out.println("port = " + options.getPort());
         // you can now call methods on the program, e.g. for real initialization using the CLI parameters in options
     }
 }
```


### DJUNITS Type Converters

The class `CliUnitConverters` offers conversion methods for DJUNITS scalars so these can be used on the command line, e.g.: 

```text
    java -jar ProgramApp.jar --timeout=5min
```

The option that uses the `Duration` converter is specified as follows:

```java
    /** how much time max before timeout kicks in? */
    @Option(names = {"-t", "--timeout"}, description = "Maximum duration before timeout stops the process",
            defaultValue = "10min")
    private Duration timeout;
```

Converters have to be registered before they can be used. When the `CliUtil.execute(program, args);` method is used, all converters for DJUNITS scalars are registered. In case you want to register an individual converter, call the folllowing code on the `CommandLine cmd`:

```java
    cmd.registerConverter(Duration.class, new DURATION());
```

Building a converter works as follows: make a method in the converter class that implements a convert() method from String to the intended data type. See below for an example.

```java
    /**
     * Convert a duration String with unit on the command line to a Duration scalar.
     */
    public static class DURATION implements ITypeConverter<Duration>
    {
        /** {@inheritDoc} */
        @Override
        public Duration convert(final String value) throws Exception
        {
            return Duration.valueOf(value);
        }
    }
```

All converters for Scalar double DJUTILS classes have already been coded in the class `CliUnitConverters`.


### Changing default options

When a subclass inherits CLI options from a superclass, sometimes default values of options (and version numbers, program description, or program name) need to be changed. Several methods help to make these changes of the values in the annotations. The code below uses the Options settings in the Program and creates overrides for them:

```java
public static void main(final String[] args)
{
    Options options = new Options();
    // change the default port value from 80 to 8080
    CliUtil.changeOptionDefault(options, "port", "8080"); 
    // change the version of the file to the last changed date of the Options class file
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    CliUtil.changeCommandVersion(options, formatter.format(
         new Date(ClassUtil.classFileDescriptor(options.getClass()).getLastChangedDate())));
    CliUtil.changeCommandName(options, "NewProgram");
    CliUtil.changeCommandDescription(options, "New version of the program");
    CliUtil.execute(options, args);
    System.out.println("port = " + options.getPort());
    // you can now call methods on the program, e.g. for real initialization using the CLI parameters in options
}
```

An example of the output when running the program without options is:

```text
port = 8080
```

An example of the output of this file with options "--version" is:

```text
2019-08-14 00:25:42
```

An example of the output of this file with options "--help" is:

```text
Usage: NewProgram [-hV] [-p=&lt;port&gt;]
New version of the program
  -h, --help          Show this help message and exit.
  -p, --port=&lt;port&gt;   Internet port to use
  -V, --version       Print version information and exit.
```

It is clear that the default values have been changed.

!!! Note
    Note that the overrides are static and stored in a static map in `CliUtil`. This means that future instances of the  `Options` in the same run will have changed values as well (although, they can of course be changed back for the next run). As we often use the CLI options only once, and at the start of the `public static void main`, this is usually not a problem. When it is, it might be better to create a new Program / Options class that inherits from the previous one and has different values for the `@Command` properties. When a default value of an `@Option` property needs to be changed, calling `changeOptionDefault` is the only way to reflect this in the options for the user.

