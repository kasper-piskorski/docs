---
title: Defining a Schema
keywords: schema
last_updated: August 23, 2016
tags: [java, graph-api]
summary: "Demonstrates how to create a schema for Grakn."
sidebar: documentation_sidebar
permalink: /documentation/building-an-ontology/simple-schema-definition.html
folder: documentation
comment_issue_id: 21
---

{% include warning.html content="This page is deprecated and will soon be removed." %}

This article assumes that you have already looked at page that covers [Grakn Basics](../the-basics/grakn-basics.html).


Defining a schema for your graph allows you to:

* control how your data relates to each other
* ensure your graph is consistent
* design domain-specific knowledge graphs.

In Grakn, a schema is defined by adding **types** to your graph.
These **types** allow you to divide your data into different categories but also control how data instances may relate to each other.

In this section we will review each of these types and explain how you use them when defining a graph in Grakn.

## Basic Type Definitions

This section describes basic types which are **essential** to a Grakn schema.
When creating a Grakn knowledge graph you should always begin by defining these schema elements.

### Entity Type

Entity types essentially define a broad category into which data can fall. For example Al Pacino, Patrick Stewart,
and Charlize Theron could all be categorised as people.
Similarly we could say that Godfather, Star Trek Nemesis and Monster can all be categorised as movies.

We would define such **Entity Types** as follows:

```java
EntityType person = graknGraph.putEntityType("Person");
EntityType movie = graknGraph.putEntityType("Movie");
```

With this we can arrange our data into categories:

```java
alPacino = person.addEntity();
patrickStewart = person.addEntity();
charliseTheron = person.addEntity();

godfather = movie.addEntity();
starTrek = movie.addEntity();
monster = movie.addEntity();
```

### Relation and Role Types

The greatest advantage of a graph database is the expressiveness of the relationships between data.
With Grakn we can control how our data relates to each other.

For example how can we say that Patrick Stewart starred in Star Trek Nemesis?
To be able to do so we must first allow such behaviour between our types.
We begin doing this by defining a **Relation Type** which categorises the relationships of a specific type.
For example:

```java
RelationType starsIn = graknGraph.putRelationType("Stars In");
```

In addition to this we have to specify the roles of relationship:

```java
RoleType actor = graknGraph.putRoleType("Actor");
RoleType movieCastIn = graknGraph.putRoleType("Movie Cast In");
```

We also have to link these roles to the correct **Relation Type** (NB Roles are unique and cannot be in more than one
Relation Type):


```java
starsIn.hasRole(actor).hasRole(movieCastIn);
```

Finally we have to explicitly define which **Entity Types** are allowed to play which **Role Types**:

```java
person.playsRole(actor);
movie.playsRole(movieCastIn);
```

The above statements allow people to be actors and allow movies to have actors cast in them.

{% include note.html content="Explicitly specifying which **Role Types** an **Entity Type** can play allows us to control our data more easily.
Essentially it would prevent us from accidentally saying that a movie starred in another movie." %}

Now, finally, we can specify that Patrick Stewart was in Star Trek Nemesis.
First we create an instance of the Relation Type:


```java
Relation relation = starsIn.addRelation();
```

Then we specify who is playing which role:

```java
relation.putRolePlayer(actor, patrickStewart).putRolePlayer(movieCastIn, starTrek);
```

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/21" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.