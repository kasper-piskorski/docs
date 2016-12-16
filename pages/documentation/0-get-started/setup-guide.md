---
title: Grakn Setup Guide
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql]
summary: "This document will teach you how to set up a Grakn environment, start it up and load a simple example."
sidebar: documentation_sidebar
permalink: /documentation/get-started/setup-guide.html
folder: documentation
comment_issue_id: 16
---


## First: Download Grakn

[![download](/images/download.png)](https://grakn.ai/download/latest)

For more information on how to download older versions of GRAKN.AI, compile from source code, or import the Grakn Java API library as a development dependency, please visit our [Downloads page](../resources/downloads.html).

## Install GRAKN.AI
{% include note.html content="**Prerequisites**   <br />
GRAKN.AI requires Java 8 (Standard Edition) with the `$JAVA_HOME` set accordingly. If you don't already have this installed, you can find it [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).  
If you intend to build Grakn, you will also need Maven 3." %}

Unzip the download into your preferred location and run the following in the terminal to start Grakn:

```bash
cd [your Grakn install directory]
./bin/grakn.sh start
```

This will start an instance of Cassandra, which serves as the supported backend for Grakn. It starts also Grakn Engine, which is an HTTP server providing batch loading, monitoring and the browser dashboard.

{% include note.html content="**Useful commands**  <br />
To start Grakn, run `grakn.sh start`.   
To stop Grakn, run `grakn.sh stop`. 
To remove all graphs from Grakn, run `grakn.sh clean`" %}


## Test the Graql Shell

Graql is our knowledge-oriented query language, which allows you to interface with Grakn. To test that the installation is working correctly, here we will start the Graql shell and enter a few lines of code.

```bash
./bin/graql.sh
```

The Graql shell starts and you see a `>>>` prompt. Type in the following, but don't worry too much about what it means right now. We will go through it further in the [Quickstart Tutorial](../the-basics/quickstart-tutorial.html).  

```graql   
insert person sub entity;
insert name sub resource, datatype string;
insert person has-resource name;
insert isa person, has name "Topsy";
insert isa person, has name "Tim";
commit
match $x isa person, has name $n; select $x, $n;
```

Then, you should see the following (although the long strings of numbers will be different):

```
$x id "ENTITY-person-89b823a0-606d-434d-a533-1e172b90c7bc" isa person; $n value "Topsy" isa name;
$x id "ENTITY-person-afd29319-ff33-4b0c-b7e3-63d04ac81dcf" isa person; $n value "Tim" isa name;
```

If you do see the above output then congratulations! You have set up Grakn.

You can now open [localhost:4567](http://localhost:4567) in your browser to start the visualiser, which provides a graphical dashboard on a graph.

![Visualising a graph](/images/topsyandtim.png)

Further information about the visualiser can be found in [Visualising a Graph](../the-basics/visualiser.html).


### Troubleshooting  
If you do not see the above message, please check our [FAQ page](../resources/faq.html). If you have any questions, please ask a question on our [discussion forum](http://discuss.grakn.ai), or on [Stack Overflow](http://stackoverflow.com).


## Where next?
Our [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) will show you how to load populate and query Grakn using Graql.

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/16" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
