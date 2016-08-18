---
title: Insert Queries
keywords: graql, query, insert
last_updated: August 10, 2016
tags: [graql]
summary: "Graql Insert Queries"
sidebar: documentation_sidebar
permalink: /documentation/graql/insert-queries.html
folder: documentation
---


An insert query will insert the specified [variable
patterns](#variable-patterns) into the graph. If a [match
query](match-queries.md) is provided, the query will insert the given variable
patterns for every result of the match query.

A variable pattern in an insert query describes [properties](#properties) to
set on a particular concept. The variable pattern can optionally be bound to a
variable or an ID.

```sql
insert
$p isa pokemon, id "Pichu", has pokedex-no 172;
(descendant Pikachu, ancestor $p) isa evolution;
```
```java
qb.insert(
    var("p").isa("pokemon").id("Pichu").has("pokedex-no", 172),
    var().isa("evolution")
        .rel("descendant", var().id("Pikachu"))
        .rel("ancestor", "p")
).execute();",
```


## Properties

### isa

```sql
insert "Totodile" isa pokemon;
```
```java
qb.insert(id("Totodile").isa("pokemon"));
```

Set the type of this concept.

### id

Create a concept with the given id, or retrieve it if one with that id exists.
The created or retrieved concept can then be modified with further properties.

```sql
insert "Pikachu" isa pokemon
```
```java
qb.insert(id("Pikachu").isa("pokemon"));
```


### value

Set the value of the concept.

```sql
insert trained-by isa relation-type, value "Trained By";
```
```java
qb.insert(id("trained-by").isa("relation-type").value("Trained By"));
```

### has

Add a resource of the given type to the concept.

```sql
insert "Pichu" isa pokemon has height 30;
```
```java
qb.insert(id("Pichu").isa("pokemon").has("height", 30));
```

The above example is equivalent to:

```sql
insert
$owner id "Pichu" isa pokemon;
$value isa height, value 30;

(height-owner $owner, height-value $value) isa has-height;
```
```java
qb.insert(
  var("owner").id("Pichu").isa("pokemon"),
  var("value").isa("height").value(30),
  var().rel("height-owner", "owner").rel("height-value", "value").isa("has-height")
);
```

### relation

Make the concept a relation that relates the given role players, playing the
given roles.

```sql
insert (pokemon-with-type "Pichu", type-of-pokemon "electric") isa has-type;
```
```java
qb.insert(
  var()
    .rel("pokemon-with-type", id("Pichu"))
    .rel("type-of-pokemon", id("electric"))
    .isa("has-type")
);",
```


## Type Properties

The following properties only apply to types.

### ako

Set the supertype of this concept type.

```sql
insert gen2-pokemon ako pokemon;
```
```java
qb.insert(id("gen2-pokemon").ako("pokemon"));
```


### has-role
Add a role to this relation type.

```sql
insert
trained-by isa relation-type, has-role trainer, has-role pokemon-trained;
```
```java
qb.insert(
  id("trained-by").isa("relation-type")
    .hasRole("trainer").hasRole("pokemon-trained")
);
```


### plays-role
Allow the concept type to play the given role.

```sql
insert pokemon plays-role pokemon-trained;
```
```java
qb.insert(id("pokemon").playsRole("pokemon-trained"));
```


### has-resource

Allow the concept type to have the given resource.

This is done by creating a specific relation type relating the concept type
and resource.

```sql
insert pokemon has-resource pokedex-no;
```
```java
qb.insert(id("pokemon").hasResource("pokedex-no"));
```

The above example is equivalent to:

```sql
insert

has-pokedex-no isa relation-type,
  has-role pokedex-no-owner,
  has-role pokedex-no-value;

pokedex-no-owner isa role-type;
pokedex-no-value isa role-type;

pokemon plays-role pokedex-no-owner;
pokedex-no plays-role pokedex-no-value;
```
```java
qb.insert(
  id("has-pokedex-no").isa("relation-type")
    .hasRole("pokedex-no-owner").hasRole("pokedex-no-value"),

  id("pokedex-no-owner").isa("role-type"),
  id("pokedex-no-value").isa("role-type"),

  id("pokemon").playsRole("pokedex-no-owner"),
  id("pokedex-no").playsRole("pokedex-no-value")
);
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
