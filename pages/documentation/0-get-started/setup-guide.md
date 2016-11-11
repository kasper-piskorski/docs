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
<span class="glyphicon glyphicon-download gi-3x"></span>Visit the [downloads page](../resources/downloads.html) for the latest version of Grakn.    

## Install Grakn
{% include note.html content="**Prerequisites**   <br />
Grakn requires Java 8 (Standard Edition) with the `$JAVA_HOME` set accordingly. If you don't already have this installed, you can find it [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).  
If you intend to build Grakn, you will also need Maven 3." %}

Unzip the download into your preferred location and run the following in the terminal:

```bash
cd [your Grakn install directory]
bin/grakn.sh start
```

This will start an instance of Cassandra, which serves as the supported backend for Grakn. It starts also Grakn Engine, which is an HTTP server providing batch loading, monitoring and the browser dashboard.

{% include note.html content="**Useful commands**  <br />
To start Grakn, run `grakn.sh start`.   
To stop Grakn, run `grakn.sh stop`. " %}


## Set up Graql

Graql is our knowledge-oriented query language, which allows you to interface with Grakn. We will start Graql and enter a few lines to check that all is working as it should.

```bash
bin/graql.sh
```

The Graql shell starts and you see a `>>>` prompt. Type in the following:   

```graql   
>>> insert twin isa entity-type;
>>> insert topsy isa twin;
>>> insert tim isa twin;
>>> commit
>>> match $x isa twin;
$x id "tim" isa twin;
$x id "topsy" isa twin;
```

If you see the above output then congratulations! You have set up Grakn.

You can now open [localhost:4567](http://localhost:4567) in your browser to start the visualiser, which provides a graphical dashboard on a graph.

![Visualising a graph](/images/topsyandtim.jpg)

Further information about the visualiser can be found in [Visualising a Graph](../the-basics/visualiser.html).


### Troubleshooting  
If you do not see the above message, please check our [FAQ page](../resources/faq.html). If you have any questions, please ask a question on our [discussion forum](http://discuss.grakn.ai), or on [Stack Overflow](http://stackoverflow.com).


## Where next?
Our [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) will show you how to load populate and query Grakn using Graql or Java.

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/16" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
