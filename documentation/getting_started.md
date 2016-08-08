---
layout: documentation
title: Getting Started
---

*Last edited: 3rd August 2016*  

<table>
    <tr>
        <td>Graql</td>
        <td>Beginner</td>
    </tr>
</table>



# Getting Started

This document will teach you how to set up a Mindmaps environment, start it up and load an example dataset to make a query using our query language, Graql.

**TO DO: Insert table of contents**

## Downloading and Installing Mindmaps

> **Prerequisites**  
> Mindmaps requires Java 8 (Standard Edition) with the `$JAVA_HOME` set
> accordingly. Mindmaps also requires Maven 3.

Mindmaps can be downloaded from
[here](http://mindmaps.io/download/mindmaps-0.2.1.zip). Unzip it and run the following in the terminal:

```
cd mindmaps-build
bin/mindmaps.sh start
```

> **Useful commands**  
> To start Mindmaps graph, run `mindmaps.sh start`.   
> To stop Mindmaps graph, run `mindmaps.sh stop`.   
> To delete all data in Mindamps graph, stop the Mindmaps backend, then run `mindmaps.sh clean`

This will start an instance of Cassandra which serves as the supported backend for Mindmaps.

![Starting Mindmaps successfully](/docs/images/terminal_mindmaps_start.png)

## Setting up Graql

Graql is our query language, which allows you to interface with a Mindmaps graph. We will use Graql to load an example data set and run some simple queries against it.  
The following will make Graql load an example data set and exit:

```
bin/graql.sh -f examples/pokemon.gql
```

This will create and persist an example data set. Now we can run the following query using the Graql shell:

```
bin/graql.sh
select $x where $x isa pokemon
$x id "Bulbasaur" isa pokemon;
$x id "Charmander" isa pokemon;
$x id "Pikachu" isa pokemon;
...
```

If you see the above output then congratulations! You have set up Mindmaps. 

> **Troubleshooting**  
> If you are not seeing the above message, please check out our troubleshooting documentation or ask a question on our discussion forum.
> 
> TO DO - LINKS!!!

## Setting up Java API

Inside the `lib` directory of mindmaps.zip are the jars you will need to
develop with mindmaps. 

If you would like to start developing with minimal effort you can integrate the jar file, e.g.
`graql-shell-0.2.1-jar-with-dependencies.jar` into your project. This however
may be a bit inflexible as all dependencies are compiled in. 

If you would like more flexibility, in the `lib` directory all the mindmaps
jars are provided with no dependencies. Using these would require you to use
maven to acquire the other dependencies.

Here are some links to guides for adding external jars using different IDEs:

- [IntelliJ](https://www.jetbrains.com/help/idea/2016.1/configuring-module-dependencies-and-libraries.html)
- [Eclipse](http://www.tutorialspoint.com/eclipse/eclipse_java_build_path.htm)
- [Netbeans](http://oopbook.com/java-classpath-2/classpath-in-netbeans/)

## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
        <tr>
        <td>v1.01</td>
        <td>03/08/2016</td>
        <td>Updated content and formatting.</td>        
    </tr>
    
</table>