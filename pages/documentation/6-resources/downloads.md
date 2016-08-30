---
title: Downloads
keywords: setup, getting started, download
last_updated: August 10, 2016
tags: [getting_started]
summary: "
This is a list of Mindmaps releases. It's the place to come to download the most recent versions of MindmapsDB."
sidebar: documentation_sidebar
permalink: /documentation/resources/downloads.html
folder: documentation
---


## Download Mindmaps

The latest version of mindmaps is v0.1.0.
It can be downloaded from [here](https://github.com/mindmapsdb/mindmapsdb/releases/download/v0.1.0-SNAPSHOT/mindmaps-dist-0.1.0-SNAPSHOT-dist.zip).


### Past Versions
A list of previously released versions of MindmapsDB can be found on [Github](https://github.com/mindmapsdb/mindmapsdb/releases).


## Prerequisites

{% include note.html content="Mindmaps requires Java 8 (Standard Edition) with the `$JAVA_HOME` set accordingly.   
Mindmaps also requires Maven 3." %}

## Code
We are an open source project. If you want to look at our code, we are on Github at [https://github.com/mindmapsdb/mindmapsdb](https://github.com/mindmapsdb/mindmapsdb).

### Building the Code

To build MindmapsDB, you need git and Maven.

Clone the [mindmapsdb repository](https://github.com/mindmapsdb/mindmapsdb) to a local directory.  In that directory:

```bash
mvn package
```

The code should build and tests will then run (the tests will take some time to complete - approximately 10 minutes). When the build has completed, you will find it in the `mindmaps-dist` directory under `target`.


## Mindmaps Java API

Inside the `lib` directory of mindmaps.zip are the jars you will need to
develop with mindmaps.

If you would like to start developing with minimal effort you can integrate the jar file, e.g.
`graql-shell-0.2.1-jar-with-dependencies.jar` into your project. This however
may be a bit inflexible as all dependencies are compiled in.

If you would like more flexibility, in the `lib` directory all the mindmaps
jars are provided with no dependencies. Using these would require you to use
Maven to acquire the other dependencies.

Here are some links to guides for adding external jars using different IDEs:

- [IntelliJ](https://www.jetbrains.com/help/idea/2016.1/configuring-module-dependencies-and-libraries.html)
- [Eclipse](http://www.tutorialspoint.com/eclipse/eclipse_java_build_path.htm)
- [Netbeans](http://oopbook.com/java-classpath-2/classpath-in-netbeans/)

## Troubleshooting
Please see our [troubleshooting](../troubleshooting/known-issues.html) page if you encounter any problems when installing and running MindmapsDB. If our guide doesn't cover the issue, please do get in touch on our [discussion forums](http://discuss.mindmaps.io) or on [Stack Overflow](http://www.stackoverflow.com).

{% include links.html %}

## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
    <tr>
        <td>v1</td>
        <td>09/08/2016</td>
        <td>New page for developer portal.</td>        
    </tr>
        <tr>
        <td>v1</td>
        <td>30/08/2016</td>
        <td>Update for first release.</td>        
    </tr>

</table>
