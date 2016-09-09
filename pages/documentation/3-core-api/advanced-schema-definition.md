---
title: Defining an Advanced Schema
keywords: core, schema
last_updated: August 23, 2016
tags: [core-api, java]
summary: "Demonstrates how to create an advanced schema for a Mindmaps knowledge graph."
sidebar: documentation_sidebar
permalink: /documentation/core-api/advanced-schema-definition.html
folder: documentation
---


This document introduces some advanced schema concepts, which you can use to create more expressive Mindmaps graphs. We recommend that you read our document about [defining a simple schema](simple-schema-definition.html) before continuing.

Here, we also discuss creating a graph which supports Mindmaps reasoning and inference features.

## Sub-Classing Types

Let's say we wanted to model a graph that encapsulates films and tv shows, as well as the actors who can play in them. The naive way to do this would be as follows:

Create the Entity types:

```java
EntityType person = mindmapsGraph.putEntityType("Person");
EntityType movie = mindmapsGraph.putEntityType("Movie");
EntityType tvShow = mindmapsGraph.putEntityType("TV Show");
```

Create the roles and relation types:

```java
RoleType actorInMovie = mindmapsGraph.putRoleType("Actor In Movie");
RoleType movieCastIn = mindmapsGraph.putRoleType("Movie Cast In");
RelationType starsInMovie = mindmapsGraph.putRelationType("Stars In Movie").hasRole(actorInMovie).hasRole(movieCastIn);

RoleType actorInTvShow = mindmapsGraph.putRoleType("Actor In Tv Show");
RoleType tvShowCastIn = mindmapsGraph.putRoleType("Tv Show Cast In");
RelationType starsInTvShow = mindmapsGraph.putRelationType("Stars In Tv Show").hasRole(actorInTvShow).hasRole(tvShowCastIn);
```

Allow the roles to be played:

```java
person.playsRole(actorInMovie).playsRole(actorInTvShow);
movie.playsRole(movieCastIn;
tvShow.playsRole(tvShowCastIn);
```

The above schema is valid and will allow us to model our data. However, there are a lot of duplicate definitions. For example, an actor should be able to star in anything (ideally): a film, a tv show, a theatre production etc.  Furthermore, there is a lot of contextual overlap between films and TV shows.

A more sophisticated approach uses sub-classing, which allows us to define things that fall into categories and sub-categories.
We can create a deep object-orientated schema where the rules of the super-class provide additional functionality to its sub-classes.

Let's convert our schema into something that is more expressive.   

Films and TV shows are similar so we should have a super-class `Production` to represent both:

```java
EntityType person = mindmapsGraph.putEntityType("Person");
EntityType production = mindmapsGraph.putEntityType("Production");
EntityType movie = mindmapsGraph.putEntityType("Movie").superType();
EntityType tvShow = mindmapsGraph.putEntityType("TV Show").superType(production);
```

Actors should be able to star in any type of `Production`, be it a film or a TV Show:

```java
RoleType actor = mindmapsGraph.putRoleType("Actor");
RoleType productionCastIn = mindmapsGraph.putRoleType("Production Cast In");
RelationType starsIn = mindmapsGraph.putRelationType("Stars In").hasRole(actor).hasRole(productionCastIn);
```

Allow the role to be played:

```java
person.playsRole(actor);
production.playsRole(productionCastIn);
```

This schema is more expressive but is also shorter because the common role type (e.g. *Actor*) are inherited through a super-class.
This also makes defining the schema shorter and gives more opportunities for graph enhancements.
For example, we can start to differentiate between male and female actors without impacting the schema significantly:

```java
EntityType person = mindmapsGraph.putEntityType("Person");
EntityType man = mindmapsGraph.putEntityType("Man").superType(person);
EntityType woman = mindmapsGraph.putEntityType("Woman").superType();
```

## Rule Types

MindmapsDB supports rule-based backward-chained (BC) reasoning to allow automated capturing and evolution of patterns within the graph. The BC approach is a goal-driven approach to reasoning where the subset of applicable rules is controlled by a particular query. In the BC mode, all the inferrable information is available at query time.

The inference rules are expressed in the following general form:

```
if [rule-body] then [rule-head]
```

or in Prolog/Datalog terms:

```
[rule-head] :- [rule-body]
```

In logical terms, we restrict the rules to be definite Horn clauses (i.e. disjunctions of atoms with at most one unnegated atom). In our system we define both the head and the body of rules as graql queries. Consequently the rules are statements of the form:

```
p :- q1, q2, ..., qn
```
where p and qi's are atoms that each correspond to a single graql statements.


A classic reasoning example is the ancestor example: the two rules stated below define the ancestor (anc) relationship, given a parenthood relationship (parent) between individuals:

```
R1: anc(X, Y):-parent(X, Y)
R2: anc(X, Y):-parent(X, Z), anc(Z, Y)
```
### Rule Java API
All rule instances are of type inference-rule which can be retrieved by:

```java
RuleType inferenceRule = mindmapsGraph.getMetaRuleInference();
```

Rule instances can be added to the graph both through the Core API as well as through graql. Considering the ancestor example, with the use of the Core API we can add the rules in the following way:

```java
String r1Body = "match (parent $x, child $y) isa Parent";
String r1Head = "match (ancestor $x, descendant $y) isa Ancestor";

String r2Body = "match (parent $x, child $z) isa Parent;(ancestor $z, descendant $y) isa Ancestor; select $x, $y";
String r2Head = "match (ancestor $x, descendant $y) isa Ancestor";

Rule rule1 = mindmapsGraph.putRule("R1", r1Body, r1Head, inferenceRule);
Rule rule2 = mindmapsGraph.putRule("R2", r2Body, r2Head, inferenceRule);
```

### Rule Graql Syntax
The addition of the rules specified above can be expressed via an insert graql statement where the body and the head of the rule are separated with curly braces, the corresponding statement then reads:

```java
insert
"R1" isa inference-rule,
lhs {match
(parent $x, child $y) isa Parent},
rhs {match
(ancestor $x, descendant $y) isa Ancestor};

"R2" isa inference-rule,
lhs {match
(parent $x, child $z) isa Parent;
(ancestor $z, descendant $y) isa Ancestor;
select $x, $y},
rhs {match
(ancestor $x, descendant $y) isa Ancestor};
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
