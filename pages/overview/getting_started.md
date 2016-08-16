---
title: Setup Guide
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting_started, graql]
summary: "This document will teach you how to set up a Mindmaps environment, start it up and load an example dataset to make a query using our query language, Graql."
sidebar: home_sidebar
permalink: overview/getting_started.html
folder: overview
---


## Download and Install Mindmaps

{% include note.html content="Mindmaps requires Java 8 (Standard Edition) with the `$JAVA_HOME` set accordingly.   
Mindmaps also requires Maven 3." %}

The latest version of mindmaps can be downloaded from the
[downloads page](downloads.html). Unzip it and run the following in the terminal:

```bash
cd mindmaps-build
bin/mindmaps.sh start
```

{% include note.html content="**Useful commands**  <br />
To start Mindmaps graph, run `mindmaps.sh start`.   
To stop Mindmaps graph, run `mindmaps.sh stop`.   
To delete all data in Mindamps graph, stop the Mindmaps backend, then run `mindmaps.sh clean`." %}



This will start an instance of Cassandra which serves as the supported backend for Mindmaps.


![Starting Mindmaps successfully](/images/terminal_mindmaps_start.png)

## Set up Graql

Graql is our query language, which allows you to interface with a Mindmaps graph. We will use Graql to load an example data set and run some simple queries against it.  
The following will make Graql load an example data set and exit:

```bash
bin/graql.sh -f examples/pokemon.gql
```

This will create and persist an example data set. Now we can run the following query using the Graql shell:

```bash
bin/graql.sh
select $x where $x isa pokemon
$x id "Bulbasaur" isa pokemon;
$x id "Charmander" isa pokemon;
$x id "Pikachu" isa pokemon;
...
```

If you see the above output then congratulations! You have set up Mindmaps.



### Troubleshooting  
If you do not see the above message, please check our [troubleshooting page](troubleshooting.html). If you have any questions, please ask a question on our [discussion forum](http://discuss.mindmaps.io), or on [Stack Overflow](http://stackoverflow.com).



## Where next?
Our [Quick Start Tutorial](quickstart_tutorial.html) will show you how to load an ontology and data into a Mindmaps Graph using [Graql](quickstart_tutorial.html) or [Java](quickstart_tutorial_java.html).

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

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
