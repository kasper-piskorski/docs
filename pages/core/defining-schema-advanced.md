---
title: Defining an Advanced Schema
keywords: core, schema
last_updated: August 15, 2016
tags: [core]
summary: "Demonstrates how to create an Advanced schema for a Mindmaps knowledge graph"
sidebar: home_sidebar
permalink: core/defining-schema-advanced.html
folder: core
---

# Advanced Schema 

It is recommend that you read [this](core/defining-schema-simple.html) section before continuing. 
The following section will introduce more adavanced concepts which you can use to create more expressive graphs.
This section will also discuss creating a graph which supports Mindmaps reasoning and inference features.

## Sub Classing Types

Let's say we wanted to model a graph which encapsulates movies and tv shows, as well as the actors who can play in them. 
The Naive way to do this would be as follows:

1. Create the Entity types:
```java
EntityType person = mindmapsTransaction.putEntityType("Person");
EntityType movie = mindmapsTransaction.putEntityType("Movie");
EntityType tvShow = mindmapsTransaction.putEntityType("TV Show");
```

2. Create the roles and relation types:

```java
RoleType actorInMovie = mindmapsTrancation.putRoleType("Actor In Movie");
RoleType movieCastIn = mindmapsTrancation.putRoleType("Movie Cast In");
RelationType starsInMovie = mindmapsTransaction.putRelationType("Stars In Movie").hasRole(actorInMovie).hasRole(movieCastIn);
```


```java
RoleType actorInTvShow = mindmapsTrancation.putRoleType("Actor In Tv Show");
RoleType tvShowCastIn = mindmapsTrancation.putRoleType("Tv Show Cast In");
RelationType starsInTvShow = mindmapsTransaction.putRelationType("Stars In Tv Show").hasRole(actorInTvShow).hasRole(tvShowCastIn);
```

3. Allow the roles to be played

```java
person.playsRole(actorInMovie).playsRole(actorInTvShow);
movie.playsRole(movieCastIn;
tvShow.playsRole(tvShowCastIn);
```

The above schema is valid and will allow us to model our data. However, there are a lot of duplicate definitions.
For example, an actor should be able to star in anything (ideally) a movie, a tv show, a theater production etc . . . 
Furthermore, there is a lot of contextual overlap between movies and TV shows. 
Ideally we should be able to do so and with Mindmaps we can.

Subclassing allows us to define things which fall into categories and sub categories. 
This allows us to create a deep object orientated schema where the rules of the super class provide additional functionality to the sub classes.
 
Lets convert our above schema into a more expressive.

1. Movies and Tv Show are similar so we should have a super category to represent both:

```java
EntityType person = mindmapsTransaction.putEntityType("Person");
EntityType production = mindmapsTransaction.putEntityType("Production");
EntityType movie = mindmapsTransaction.putEntityType("Movie").superType();
EntityType tvShow = mindmapsTransaction.putEntityType("TV Show").superType(production);
```

2. Actors should be able to star in any type of production, be it a Movie or a TV Show:

```java
RoleType actor = mindmapsTrancation.putRoleType("Actor");
RoleType productionCastIn = mindmapsTrancation.putRoleType("Production Cast In");
RelationType starsIn = mindmapsTransaction.putRelationType("Stars In").hasRole(actor).hasRole(productionCastIn);
```

3. Allow the role to be played:

```java
person.playsRole(actor);
production.playsRole(productionCastIn);
```

This schema is more expressive but is also shorted because common role type (e.g. **Actor*) are inherited through a super class.
This also makes defining the schema shorter and gives more opportunities for graph. 
For example, we can start to differentiate between male and female actors withough largely impacting the schema: 

```java
EntityType person = mindmapsTransaction.putEntityType("Person");
EntityType man = mindmapsTransaction.putEntityType("Man").superType(person);
EntityType woman = mindmapsTransaction.putEntityType("Woman").superType();
```

## Rule Types 

TODO