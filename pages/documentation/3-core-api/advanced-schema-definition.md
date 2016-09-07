---
title: Defining an Advanced Schema
keywords: core, schema
last_updated: August 23, 2016
tags: [core-api, java]
summary: "Demonstrates how to create an Advanced schema for a Mindmaps knowledge graph."
sidebar: documentation_sidebar
permalink: /documentation/core-api/advanced-schema-definition.html
folder: documentation
---


This document introduces some advanced schema concepts, which you can use to create more expressive Mindmaps graphs. It is recommend that you read our document about [defining a simple schema](simple-schema-definition.html) before continuing.

Here, we also discuss creating a graph which supports Mindmaps reasoning and inference features.

## Sub Classing Types

Let's say we wanted to model a graph which encapsulates films and tv shows, as well as the actors who can play in them.
The naive way to do this would be as follows:

Create the Entity types:

```java
EntityType person = mindmapsTransaction.putEntityType("Person");
EntityType movie = mindmapsTransaction.putEntityType("Movie");
EntityType tvShow = mindmapsTransaction.putEntityType("TV Show");
```

Create the roles and relation types:

```java
RoleType actorInMovie = mindmapsTrancation.putRoleType("Actor In Movie");
RoleType movieCastIn = mindmapsTrancation.putRoleType("Movie Cast In");
RelationType starsInMovie = mindmapsTransaction.putRelationType("Stars In Movie").hasRole(actorInMovie).hasRole(movieCastIn);

RoleType actorInTvShow = mindmapsTrancation.putRoleType("Actor In Tv Show");
RoleType tvShowCastIn = mindmapsTrancation.putRoleType("Tv Show Cast In");
RelationType starsInTvShow = mindmapsTransaction.putRelationType("Stars In Tv Show").hasRole(actorInTvShow).hasRole(tvShowCastIn);
```

Allow the roles to be played

```java
person.playsRole(actorInMovie).playsRole(actorInTvShow);
movie.playsRole(movieCastIn;
tvShow.playsRole(tvShowCastIn);
```

The above schema is valid and will allow us to model our data. However, there are a lot of duplicate definitions.
For example, an actor should be able to star in anything (ideally) a film, a tv show, a theatre production etc.  Furthermore, there is a lot of contextual overlap between films and TV shows.

A more sophisticated approach uses subclassing, which allows us to define things that fall into categories and sub categories.
We can create a deep object-orientated schema where the rules of the super class provide additional functionality to the sub classes.

Lets convert our schema into something that is more expressive.

Films and TV shows are similar so we should have a super category to represent both:

```java
EntityType person = mindmapsTransaction.putEntityType("Person");
EntityType production = mindmapsTransaction.putEntityType("Production");
EntityType movie = mindmapsTransaction.putEntityType("Movie").superType();
EntityType tvShow = mindmapsTransaction.putEntityType("TV Show").superType(production);
```

Actors should be able to star in any type of production, be it a film or a TV Show:

```java
RoleType actor = mindmapsTrancation.putRoleType("Actor");
RoleType productionCastIn = mindmapsTrancation.putRoleType("Production Cast In");
RelationType starsIn = mindmapsTransaction.putRelationType("Stars In").hasRole(actor).hasRole(productionCastIn);
```

Allow the role to be played:

```java
person.playsRole(actor);
production.playsRole(productionCastIn);
```

This schema is more expressive but is also shorter because the common role type (e.g. *Actor*) are inherited through a super class.
This also makes defining the schema shorter and gives more opportunities for graph enhancements.
For example, we can start to differentiate between male and female actors without impacting the schema significantly:

```java
EntityType person = mindmapsTransaction.putEntityType("Person");
EntityType man = mindmapsTransaction.putEntityType("Man").superType(person);
EntityType woman = mindmapsTransaction.putEntityType("Woman").superType();
```

## Rule Types

MindmapsDB supports rule-based backward-chained (BC) reasoning to allow automated capturing and evolution of patterns within the graph. The BC approach is a goal-driven approach to reasoning where the subset of applicable rules is controlled by a particular query. In the BC mode, all the inferrable information is available at query time.

The inference rules are expressed in the following form:

#### if [rule-body] then [rule-head]

or in Prolog/Datalog terms:

#### [rule-head] :- [rule-body]

Both the head and the body of the rule are graql statements. In logical terms, we restrict the rules to be definite Horn clauses (i.e. disjunctions of atoms with at most one unnegated atom).

All rule instances are of type inference-rule which can be retrieved by:

```java
RuleType inferenceRule = mindmapsTransaction.getMetaRuleInference();
```

Rule instances can be added to the graph both through the Core API as well as through graql. Considering a sample rule reflecting the transitivity of a located-in relation, with the use of the Core API we can add it in the following way:

```java
String ruleBody = "match " +
                "(geo-entity $x, entity-location $y) isa is-located-in;" +
                "(geo-entity $y, entity-location $z) isa is-located-in; select $x, $z";

String ruleHead = "match (geo-entity $x, entity-location $z) isa is-located-in select $x, $z";

Rule rule = mindmapsTransaction.putRule("transitivity",ruleBody, ruleHead, inferenceRule);
```

The addition of the same rule instance can be expressed via an insert graql statement where the body and the head of the rule are separated with curly braces, the statement then reads:

```java
"transitivity" isa inference-rule,
lhs {match
(geo-entity $x, entity-location $y) isa is-located-in;
(geo-entity $y, entity-location $z) isa is-located-in;
select $x, $z},
(geo-entity $x, entity-location $z) isa is-located-in};
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
