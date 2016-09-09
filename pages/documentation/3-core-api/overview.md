---
title: Core Overview
keywords: core, overview
last_updated: August 10, 2016
tags: [java, core-api]
summary: "A basic example of how to use Mindmaps Core API."
sidebar: documentation_sidebar
permalink: /documentation/core-api/overview.html
folder: documentation
---


This article assumes that you have already looked at the [Quickstart
Tutorial](../the-basics/quickstart-tutorial.html). You can find the code below as part in the example code directory of the MindmapsDB zip.

## Introduction

Mindmaps Core API is the implementation of the object model discussed in
[Mindmaps Basics](../the-basics/mindmaps-basics.html). It allows you to create
Mindmaps Graphs using Java 8. It supports any
[Tinkerpop](http://tinkerpop.incubator.apache.org/docs/3.0.2-incubating/) 3.0.z version.


## Getting a Graph and a Transaction

Once you have started MindmapsDB with `mindmaps.sh start` you will be able to create a thread bound graphs as follows:

```java
    MindmapsGraph mindmapsGraph = MindmapsClient.getGraph("MyGraph");
```

## Modelling The Philosophers

With a graph in place lets go through and repeat the [Quickstart
Tutorial](../the-basics/quickstart-tutorial) using the core Java API.

### Entity Types

First we create some philosophers, so we define the category of `people`:

```java
    EntityType person = mindmapsGraph.putEntityType("person");
```

Next we specify who our people are:

```java
    Entity socrates = mindmapsGraph.putEntity("Socrates", person);
    Entity plato = mindmapsGraph.putEntity("Plato", person);
    Entity aristotle = mindmapsGraph.putEntity("Aristotle", person);
    Entity alexander = mindmapsGraph.putEntity("Alexander", person);
```

We can query for all the people:

```java
    people.instances().forEach(System.out::println);
```

Now lets create some schools of philosophy:

```java
    EntityType school = mindmapsGraph.putEntityType("school");
    Entity peripateticism = mindmapsGraph.putEntity("Peripateticism", school);
    Entity platonism = mindmapsGraph.putEntity("Platonism", school);
    Entity idealism = mindmapsGraph.putEntity("Idealism", school);
    Entity cynicism = mindmapsGraph.putEntity("Cynicism", school);
```

### Relation Types

Now that we have some data lets specify how these philosophers relate to these schools. First we create the `Relation Type` and its `Role Types`:

```java
    RoleType philosopher = mindmapsGraph.putRoleType("philosopher");
    RoleType philosophy = mindmapsGraph.putRoleType("philosophy");
    RelationType practice = mindmapsGraph.putRelationType("practice").hasRole(philosopher).hasRole(philosophy);
```

Next we can relate our philosophers and schools to each other:

```java
    mindmapsGraph.addRelation(practice).putRolePlayer(philosopher, socrates).putRolePlayer(philosophy, platonisim);
    mindmapsGraph.addRelation(practice).putRolePlayer(philosopher, plato).putRolePlayer(philosophy, idealism);
    mindmapsGraph.addRelation(practice).putRolePlayer(philosopher, plato).putRolePlayer(philosophy, platonisim);
    mindmapsGraph.addRelation(practice).putRolePlayer(philosopher, aristotle).putRolePlayer(philosophy, peripateticism);
```

Now we can define how our philosophers relate to each other:

```java
    RoleType teacher = mindmapsGraph.putRoleType("teacher");
    RoleType student = mindmapsGraph.putRoleType("student");
    RelationType education = mindmapsGraph.putRelationType("education").hasRole(teacher).hasRole(student);

    mindmapsGraph.putRelation(education).putRolePlayer(teacher, socrates).putRolePlayer(student, plato);
    mindmapsGraph.putRelation(education).putRolePlayer(teacher, plato).putRolePlayer(student, aristotle);
    mindmapsGraph.putRelation(education).putRolePlayer(teacher, aristotle).putRolePlayer(student, alexander);
```
{% include links.html %}

## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
        <tr>
        <td>v0.1.0.1</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>