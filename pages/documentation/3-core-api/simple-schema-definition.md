---
title: Defining a Schema
keywords: core, schema
last_updated: August 15, 2016
tags: [core]
summary: "Demonstrates how to create a schema for a Mindmaps knowledge graph"
sidebar: documentation_sidebar
permalink: /documentation/core-api/simple-schema-definition.html
folder: documentation
---

# Mindmaps Schema

Defining a schema for your graph allows you to:

* Control how your data relates to each other
* Ensure your graph is consistent
* Design domain specific knowledge graphs

In Mindmaps, a schema is defined by adding **types** to your graph.
These **types** allow you to divide your  data into different categories but also control how data instances may relate to each other.

In this section we will review each of these types and explain how you use them when defining a Mindmaps graph.

## Basic Type Definitions

This section describes basic types which are **essential** to a Mindmaps schema.
When creating a Mindmaps knowledge graph you should always begin with defining these schema elements.

### Entity Type

Entity types essentially define a broad category which data can fall with in. For example Al Pacino, Patrick Stewart,
and Charlize Theron could all be categorised as people.
Similarly we could say that Godfather, Star Treck Nemisis, and Monster can all be categorised as movies.

We would define such **Entity Types** as follows:

```java
EntityType person = mindmapsTransaction.putEntityType("Person");
EntityType movie = mindmapsTransaction.putEntityType("Movie");
```

With this we can arrange our data into categories:

```java
alPacino = mindmapsTransaction.addEntity(person).setValue("Al Pacino");
patrickStewart = mindmapsTransaction.addEntity(person).setValue("Patrick Stewart");
charliseTheron = mindmapsTransaction.addEntity(person).setValue("Charlise Theron");

godfather = mindmapsTransaction.addEntity(movie).setValue("Godfather");
starTreck = mindmapsTransaction.addEntity(movie).setValue("Star Treck Nemisis");
monster = mindmapsTransaction.addEntity(movie).setValue("Monster");
```

### Relation and Role Types

The greatest advantage of a graph database is the expressiveness of the relationships between data.
With Mindmaps we can control how our data relates to each other.

For example how can we say that Patrick Stewart starred in Star Treck Nemisis ?
To be able to do so we must first allow such behaviour between our types.
We begin doing this by defining a **Relation Type** which categorises the relationships of a specific type.
For example:

```java
RelationType starsIn = mindmapsTransaction.putRelationType("Stars In");
```

In addition to this we have to specify the roles of relationship:

```java
RoleType actor = mindmapsTrancation.putRoleType("Actor");
RoleType movieCastIn = mindmapsTrancation.putRoleType("Movie Cast In");
```

We also have to link these roles to the correct **Relaiton Type** (NB Roles are unique and cannot be in more than one
Relation Type):


```java
starsIn.hasRole(actor).hasRole(movieCastIn);
```

Finally we have to explicitly define which **Entity Typpes** are allowed to play which ***Role Types**:

```java
person.playsRole(actor);
movie.playsRolw(movieCastIn);
```

The above statements allow people to be actors and allow movies to have actors cast in them.

**Note:** Explicitly specifying which **Role Types** an **Entity Type** can play allows us to control our data more easily.
Essentially it would prevent us from accidentally saying that a movie starred in another movie.

Now finally we can specify that Patrick Stewart was in Star Treck Nemisis.
First we create an instance of the Relation Type:


```java
Relation relation = mindmapsTransaction.addRelation(starsIn);
```

Then we specify who is playing which role:

```java
relation.putRolePlayer(actor, patrickStewart).putRolePlayer(movieCastIn, startreck);
```
