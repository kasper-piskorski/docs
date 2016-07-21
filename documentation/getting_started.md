# Getting Started

## Downloading and Installing Mindmaps

> Mindmaps requires Java 8 (Standard Edition) with the `$JAVA_HOME` set
> accordingly. Mindmaps also requires Maven 3.

Mindmaps can be downloaded from
[here](http://mindmaps.io/download/mindmaps-0.2.1.zip). Running the following
in the terminal will get you up and running:

```bash
unzip mindmaps-0.2.1.zip
cd mindmaps-build
bin/mindmaps.sh start
```

> To start Mindmaps graph, run `mindmaps.sh start`. To stop Mindmaps graph, run
> `mindmaps.sh stop`. To delete all data in Mindamps graph, stop the Mindmaps
> backend, then run `mindmaps.sh clean`

This will start an instance of Cassandra which serves as the supported backend
for Mindmaps EAP.

## Setting up Graql

The Graql query language allows you to interface with a Mindmaps graph. We will
use Graql to load an example data set and run some simple queries against this
example. The following will make Graql load an example data set and exit:

```bash
bin/graql.sh -f examples/pokemon.gql
=================================================================================================
||||||||||||||||||||      1 TitanGraph(s) are instantiated for Mindmaps      ||||||||||||||||||||
=================================================================================================
```

This will create and persist an example data set. Now we can run the following
query using the Graql shell:

```sql
>>> match $x isa pokemon
$x id "Bulbasaur" isa pokemon;
$x id "Charmander" isa pokemon;
$x id "Pikachu" isa pokemon;
...
```

If you see the above output then congrats! You have set up Mindmaps.

## Setting up Java API

Inside the `lib` directory of mindmaps.zip are the jars you will need to
develop with mindmaps. 

If you would like to start developing with minimal effort you can integrate
`graql-shell-0.2.1-jar-with-dependencies.jar` into your project. This however
may be a bit inflexible as all dependencies are compiled in. 

If you would like more flexibility, in the `lib` directory all the mindmaps
jars are provided with no dependencies. Using these would require you to use
maven to acquire the other dependencies.

Guides to adding external jars in different IDEs:

- [IntelliJ](https://www.jetbrains.com/help/idea/2016.1/configuring-module-dependencies-and-libraries.html)
- [Eclipse](http://www.tutorialspoint.com/eclipse/eclipse_java_build_path.htm)
- [Netbeans](http://oopbook.com/java-classpath-2/classpath-in-netbeans/)
