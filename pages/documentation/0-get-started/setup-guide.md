---
title: MindmapsDB Setup Guide
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql]
summary: "This document will teach you how to set up a MindmapsDB environment, start it up and load an example dataset to make a query using our query language, Graql."
sidebar: documentation_sidebar
permalink: /documentation/get-started/setup-guide.html
folder: documentation
---


## Download and Install Mindmaps

{% include note.html content="MindmapsDB requires Java 8 (Standard Edition) with the `$JAVA_HOME` set accordingly.   
MindmapsDB also requires Maven 3." %}

The latest version of MindmapsDB can be downloaded from the
[downloads page](../resources/downloads.html). Unzip it and run the following in the terminal:

```bash
cd [your MindmapsDB install directory]
bin/mindmaps.sh start
```

This will start an instance of Cassandra which serves as the supported backend for Mindmaps.

{% include note.html content="**Useful commands**  <br />
To start MindmapsDB graph, run `mindmaps.sh start`.   
To stop MindmapsDB graph, run `mindmaps.sh stop`. " %}


## Set up Graql

Graql is our query language, which allows you to interface with a MindmapsDB graph. We will use Graql to load an example data set and run some simple queries against it.  

The following will make Graql load an example data set and exit:

```bash
bin/graql.sh -f examples/pokemon.gql
```

This will create and persist an example data set. Now we can run the following query using the Graql shell:

```bash
bin/graql.sh
match $x isa pokemon
$x id "Bulbasaur" isa pokemon;
$x id "Charmander" isa pokemon;
$x id "Pikachu" isa pokemon;
...
```

If you see the above output then congratulations! You have set up MindmapsDB.



### Troubleshooting  
If you do not see the above message, please check our [troubleshooting page](../troubleshooting/known-issues.html). If you have any questions, please ask a question on our [discussion forum](http://discuss.mindmaps.io), or on [Stack Overflow](http://stackoverflow.com).



## Where next?
Our [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) will show you how to load populate and query a MindmapsDB Graph using Graql or Java.

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
        <td>v0.1.1.1</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>
