---
title: Graph API
keywords: java
last_updated: Development 2016
tags: [java]
summary: "The Graph API."
sidebar: documentation_sidebar
permalink: /documentation/developing-with-java/graph-api.html
folder: documentation
---

{% include warning.html content="Please note that this page is in progress and subject to revision." %}



<!--
What is the difference between the Graph API and Java Graql API?
Use the Graph API for:

* Manipulating and inserting
* A more efficient but advanced construction API for building API

Java Graql API?
Used for Querying - for traversals
-->

<!-- From Jo: @Filipe - please include something about rules and the graph API. This from earlier documentation - feel free to re-use or update... -->

### Rule Java API
All rule instances are of type inference-rule which can be retrieved by:

```java
RuleType inferenceRule = graknGraph.getMetaRuleInference();
```

Rule instances can be added to the graph both through the Graph API as well as through Graql. Considering the ancestor example, with the use of the Graph API we can add the rules in the following way:

```java
Pattern r1Body = var().rel("parent", "x").rel("child", "y").isa("Parent");
Pattern r1Head = var().rel("ancestor", "x").rel("descendant", "y").isa("Ancestor");

Pattern r2Body = and(
        var().rel("parent", "x").rel("child", "y").isa("Parent')"),
        var().rel("ancestor", "z").rel("descendant", "y").isa("Ancestor")
);
Pattern r2Head = var().rel("ancestor", "x").rel("descendant", "y").isa("Ancestor");

Rule rule1 = inferenceRule.addRule(r1Body, r1Head);
Rule rule2 = inferenceRule.addRule(r2Body, r2Head);
```


## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/23" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.


{% include links.html %}

