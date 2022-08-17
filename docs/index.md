# DJUTILS - Delft Java Utilities projects

DJUTILS is a set of Java classes that are used by other TU Delft projects such as [DSOL](https://simulation.tudelft.nl/dsol/manual/), [OpenTrafficSim](https://opentrafficsim.org/docs/current/), [Sim0MQ](https://sim0mq.org/), [GSCG](https://gscg.org/), and others. The DJUTILS projects in this main group import no, or very few external packages.

DJUTILS is a highly diverse software collection. All of this software has in common that it is written in Java and that the writers/designers believe that it can (and should) be reused in other projects. The software is organized in packages

* [djutils](https://djutils.org/manual/djutils-project): several small projects that require few or no external packages.
* [djutils-cli](https://djutils.org/manual/cli-package): command line arguments processing with defaults taken from environment variables and other sources, usage summary, etc.
* [djutils-serialization](https://djutils.org/manual/djutils-serialization): utilities to serialize and deserialize data.
* [djutils-data](https://djutils.org/manual/djutils-data-project): utilities to collect, store, write and read structured data, e.g., measurements from sensors, or results from a simulation.
* [djutils-draw](https://djutils.org/manual/djutils-draw-project): utilities that manipulate points, poly-lines, line-segments, rays, in 2D and 3D.
* [djutils-stats](https://djutils.org/manual/djutils-stats-project): utilities that ingest a series of values, e.g., measurements from a sensor, or results from a simulation, and compute statistical properties (mean, standard deviation, skewness, kurtosis, estimated cumulative probabilities).

There are two more packages that are not important to re-users of the DJUTILS project. These packages contain software and maintenance information that is used to maintain the DJUTILS project.

* djutils-generator: code generators used to compute lookup-tables needed within the DJUTILS project, etc.
* djutils-parent: master pom file for the DJUTILS project.


# Origin

DJUTILS was developed at the Delft University of Technology as part of the [Open Traffic Simulator](https://opentrafficsim.org/docs/current/) project (started in 2014) and the [Delft Simulation Object Library](https://simulation.tudelft.nl/dsol/manual/) (DSOL) project (started in 2002).

The main authors/contributors of the DJUTILS project are Alexander Verbraeck, Peter Knoppers, and Wouter Schakel.