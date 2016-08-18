---
title: Core Overview
keywords: core, overview
last_updated: August 10, 2016
tags: []
summary: "MindmapsDB Core Tutorial. Runs through a basic example of how to use Mindmaps Core API."
sidebar: documentation_sidebar
permalink: /documentation/core-api/overview.html
folder: documentation
---

# Overview

> You should checkout the [Quickstart
> Tutorial](documentation/basic-tutorial.md) before looking through here. It
> is also recommended looking at the example code provided.

Mindmaps Core API is the implementation of the object model discussed in
[Mindmaps Basics](documentation/mindmaps-basics.md). It allows you to create
Mindmaps Graphs using Java 8. It supports any
[Tinkerpop](http://tinkerpop.incubator.apache.org/docs/3.0.2-incubating/) 3.0.z
version.

Mindmaps Core API is catered towards constructing a Mindmaps Graphs. Graql is
catered towards querying in more complex manners.

## Getting a Graph and a Transaction

Once you have started MindmapsDB with `mindmaps.sh start` you will be able to create graphs as follows:

```java
    MindmapsGraph graph = MindmapsClient.getGraph("MyGraph");
```

Now that we have a graph we can create thread bound transactions from the graph

```java
    MindmapsTransaction transaction = graph.newTransaction();
```

## Modelling A The Philosophers

With a transaction in place lets go through and repeat the [Quickstart Tutorial](documentation/basic-tutorial.md) using the core Java API.

### Entity Types

First we create some philosophers, do to so we define the category of `people`

```java
    EntityType person = transaction.putEntityType("person");
```

Next we specify who our people are:

```java
    Entity socrates = transaction.putEntity("Socrates", person);
    Entity plato = transaction.putEntity("Plato", person);
    Entity aristotle = transaction.putEntity("Aristotle", person);
    Entity alexander = transaction.putEntity("Alexander", person);
```

We can query for all the people by:

```java
    people.instances().forEach(System.out::println);
```

Now lets create some schools of philosophy

```java
    EntityType school = transaction.putEntityType("school");
    Entity peripateticism = transaction.putEntity("Peripateticism", school);
    Entity platonism = transaction.putEntity("Platonism", school);
    Entity idealism = transaction.putEntity("Idealism", school);
    Entity cynicism = transaction.putEntity("Cynicism", school);
```

### Relation Types

Now that we have some data lets specify how these philosophers relate to these schools. First we create the Relation Type and its Role Types:

```java
    RoleType philosopher = mindmapsGraph.putRoleType("philosopher");
    RoleType philosophy = mindmapsGraph.putRoleType("philosophy");
    RelationType practice = mindmapsGraph.putRelationType("practice").hasRole(philosopher).hasRole(philosophy);
```

Next we can relate our philosophers and schools to east other:

```java
    transaction.addRelation(practice).putRolePlayer(philosopher, socrates).putRolePlayer(philosophy, platonisim);
    transaction.addRelation(practice).putRolePlayer(philosopher, plato).putRolePlayer(philosophy, idealism);
    transaction.addRelation(practice).putRolePlayer(philosopher, plato).putRolePlayer(philosophy, platonisim);
    transaction.addRelation(practice).putRolePlayer(philosopher, aristotle).putRolePlayer(philosophy, peripateticism);
```

We can even define how our philosophers relate to each other:

```java
    RoleType teacher = transaction.putRoleType("teacher");
    RoleType student = transaction.putRoleType("student");
    RelationType education = transaction.putRelationType("education").hasRole(teacher).hasRole(student);

    transaction.putRelation(education).putRolePlayer(teacher, socrates).putRolePlayer(student, plato);
    transaction.putRelation(education).putRolePlayer(teacher, plato).putRolePlayer(student, aristotle);
    transaction.putRelation(education).putRolePlayer(teacher, aristotle).putRolePlayer(student, alexander);
```
