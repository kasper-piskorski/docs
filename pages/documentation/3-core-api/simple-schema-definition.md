---
title: Defining a Schema
keywords: core, schema
last_updated: August 23, 2016
tags: [java, core-api]
summary: "Demonstrates how to create a schema for a Mindmaps knowledge graph."
sidebar: documentation_sidebar
permalink: /documentation/core-api/simple-schema-definition.html
folder: documentation
---

This article assumes that you have already looked at page that covers [Mindmaps Basics](../the-basics/mindmaps-basics.html).


Defining a schema for your graph allows you to:

* control how your data relates to each other
* ensure your graph is consistent
* design domain specific knowledge graphs.

In Mindmaps, a schema is defined by adding **types** to your graph.
These **types** allow you to divide your data into different categories but also control how data instances may relate to each other.

In this section we will review each of these types and explain how you use them when defining a Mindmaps graph.

## Basic Type Definitions

This section describes basic types which are **essential** to a Mindmaps schema.
When creating a Mindmaps knowledge graph you should always begin by defining these schema elements.

### Entity Type

Entity types essentially define a broad category into which data can fall. For example Al Pacino, Patrick Stewart,
and Charlize Theron could all be categorised as people.
Similarly we could say that Godfather, Star Trek Nemesis and Monster can all be categorised as movies.

We would define such **Entity Types** as follows:

```java
EntityType person = mindmapsGraph.putEntityType("Person");
EntityType movie = mindmapsGraph.putEntityType("Movie");
```

With this we can arrange our data into categories:

```java
alPacino = mindmapsGraph.addEntity(person).setValue("Al Pacino");
patrickStewart = mindmapsGraph.addEntity(person).setValue("Patrick Stewart");
charliseTheron = mindmapsGraph.addEntity(person).setValue("Charlise Theron");

godfather = mindmapsGraph.addEntity(movie).setValue("Godfather");
starTrek = mindmapsGraph.addEntity(movie).setValue("Star Trek Nemesis");
monster = mindmapsGraph.addEntity(movie).setValue("Monster");
```

### Relation and Role Types

The greatest advantage of a graph database is the expressiveness of the relationships between data.
With Mindmaps we can control how our data relates to each other.

For example how can we say that Patrick Stewart starred in Star Trek Nemesis?
To be able to do so we must first allow such behaviour between our types.
We begin doing this by defining a **Relation Type** which categorises the relationships of a specific type.
For example:

```java
RelationType starsIn = mindmapsGraph.putRelationType("Stars In");
```

In addition to this we have to specify the roles of relationship:

```java
RoleType actor = mindmapsGraph.putRoleType("Actor");
RoleType movieCastIn = mindmapsGraph.putRoleType("Movie Cast In");
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

Now finally we can specify that Patrick Stewart was in Star Trek Nemesis.
First we create an instance of the Relation Type:


```java
Relation relation = mindmapsGraph.addRelation(starsIn);
```

Then we specify who is playing which role:

```java
relation.putRolePlayer(actor, patrickStewart).putRolePlayer(movieCastIn, startrek);
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