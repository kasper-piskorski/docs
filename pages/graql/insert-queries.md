---
title: Insert Queries
---
## Insert Queries

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

An insert query will insert the specified [variable
patterns](#variable-patterns) into the graph. If a [match
query](match-queries.md) is provided, the query will insert the given variable
patterns for every result of the match query.

A variable pattern in an insert query describes [properties](#properties) to
set on a particular concept. The variable pattern can optionally be bound to a
variable or an ID.

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

```sql
insert "Pikachu" isa pokemon
```
```java
qb.insert(id("Pikachu").isa("pokemon"));
```

Create a concept with the given id, or retrieve it if one with that id exists.
The created or retrieved concept can then be modified with further properties.

### value

```sql
insert trained-by isa relation-type, value "Trained By";
```
```java
qb.insert(id("trained-by").isa("relation-type").value("Trained By"));
```

Set the value of the concept.

### has

```sql
insert "Pichu" isa pokemon has height 30;
```
```java
qb.insert(id("Pichu").isa("pokemon").has("height", 30));
```

Add a resource of the given type to the concept. The above example is
equivalent to:

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

Make the concept a relation that relates the given role players, playing the
given roles.

## Type Properties

The following properties only apply to types.

### ako

```sql
insert gen2-pokemon ako pokemon;
```
```java
qb.insert(id("gen2-pokemon").ako("pokemon"));
```

Set the super type of this concept type.

### has-role

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

Add a role to this relation type.

### plays-role

```sql
insert pokemon plays-role pokemon-trained;
```
```java
qb.insert(id("pokemon").playsRole("pokemon-trained"));
```

Allow the concept type to play the given role.

### has-resource

```sql
insert pokemon has-resource pokedex-no;
```
```java
qb.insert(id("pokemon").hasResource("pokedex-no"));
```

Allow the concept type to have the given resource.

This is done by creating a specific relation type relating the concept type
and resource. The above example is equivalent to:

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
