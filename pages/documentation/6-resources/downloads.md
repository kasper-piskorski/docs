---
title: Downloads
keywords: setup, getting started, download
last_updated: October 1st, 2016
tags: [getting-started]
summary: "
This is a list of MindmapsDB releases. It's the place to come to download the most recent versions of MindmapsDB."
sidebar: documentation_sidebar
permalink: /documentation/resources/downloads.html
folder: documentation
comment_issue_id: 24
---


## Download MindmapsDB Latest Version

<span class="glyphicon glyphicon-download gi-2x">[Download](https://mindmaps.io/download/latest)</span>

### Past Versions
A list of previously released versions of MindmapsDB can be found on [Github](https://mindmaps.io/download).


## Prerequisites

{% include note.html content="We currently support Mac OS X and Linux. MindmapsDB requires Java 8 (Standard Edition) with the `$JAVA_HOME` set accordingly. MindmapsDB also requires Maven 3." %}

## Code
We are an open source project. If you want to look at our code, we are on Github at [https://github.com/mindmapsdb/mindmapsdb](https://github.com/mindmapsdb/mindmapsdb).

### Building the Code

To build MindmapsDB, you need Maven, node.js and npm. Make sure you have the most recent version of node.js and npm.

Using git, clone the [mindmapsdb repository](https://github.com/mindmapsdb/mindmapsdb) to a local directory.  In that directory:

```bash
mvn package
```

The code should build and the tests will then run (the tests will take approximately 10 minutes to finish). When the build has completed, you will find it in the `mindmaps-dist` directory under `target`. The zip file built into that directory is the same as that distributed as a release on [Github](https://mindmaps.io/download).

### Example Code
You can find an additional repo on Github containing our [example code](https://github.com/mindmapsdb/sample-projects), while further information about the examples is [here](../examples/examples.html).


## MindmapsDB Java API

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

## Questions?
Please see our [FAQ](../resources/faq.html) page if you encounter any problems when installing and running MindmapsDB. If our guide doesn't cover the issue, please do get in touch on our [discussion forums](http://discuss.mindmaps.io) or on [Stack Overflow](http://www.stackoverflow.com).

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/mindmapsdb/docs/issues/24" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
