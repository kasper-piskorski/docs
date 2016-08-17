---
title: Delete Queries
keywords: graql, query, delete
last_updated: August 11, 2016
tags: [graql]
summary: "Graql Delete Queries"
sidebar: home_sidebar
permalink: graql/delete-queries.html
folder: graql
---

A delete query will delete the specified [variable
patterns](#variable-patterns) for every result of the [match
query](graql_match.html). If a variable pattern indicates just a variable, then
the whole concept will be deleted. If it is more specific (such as indicating
the `id` or `isa`) it will only delete the specified properties.

```sql
match $x isa pokemon delete $x
```
```java
qb.match(var("x").isa("pokemon")).delete("x").execute();
```


## Variable Patterns

A variable pattern in a delete query describes [properties](#properties) to
delete on a particular concept. The variable pattern is always bound to a
variable name.

If a variable pattern has no properties, then the concept itself is deleted.
Otherwise, only the specified properties are deleted.

## Properties

### has-role
Removes the given role from the relation type.

```sql
match $x id "evolution" delete $x has-role descendant;
```
```java
qb.match(var("x").id("evolution")).delete(var("x").hasRole("descendant"));
```


### plays-role
Disallows the concept type from playing the given role.

```sql
match $x id "type" delete $x plays-role attacking-type;
```
```java
qb.match(var("x").id("type")).delete(var("x").playsRole("attacking-type"));
```


### has
Deletes the resources of the given type on the concept. If a value is given,
only delete resources matching that value.

```sql
match $x id "Bulbasaur" delete $x has weight;
```
```java
qb.match(var("x").id("Bulbasaur")).delete(var("x").has("weight"));
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
        <td>v1</td>
        <td>11/08/2016</td>
        <td>New page for developer portal.</td>        
    </tr>

</table>
