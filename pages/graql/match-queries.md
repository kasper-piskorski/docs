---
title: Match Queries
keywords: graql, query, match
last_updated: August 11, 2016
tags: [graql]
summary: "Graql Match Queries"
sidebar: home_sidebar
permalink: graql_match.html
folder: graql
---

A match query will search the graph for any subgraphs that match the given
[patterns](#variable-patterns). A result is produced for each match found,
containing any variables in the query. The results of the query can be modified with various [modifiers](#modifiers).

```sql
match
$x isa pokemon, id "Pikachu";
(pokemon-with-type $x, $y);
```
```java
qb.match(
    var("x").isa("pokemon").id("Pikachu"),
    var().rel("pokemon-with-type", "x").rel("y")
);
```


## Variable Patterns

A pattern is a pattern to match in the graph. Patterns can be combined into a
disjunction ('or') and grouped together with curly braces. Patterns are
separated by semicolons, each pattern is independent of the others.
A variable pattern is a pattern describing [properties](#properties) of a
particular concept. The variable pattern can optionally be bound to a variable
or an ID.

```sql
match
$x isa pokemon;
{
  $x id "Mew"
} or {
  ($x, $y);
  $y isa pokemon-type, id "water";
}
```
```java
qb.match(
  var("x").isa("pokemon"),
  or(
    var("x").id("Mew"),
    and(
      var().rel("x").rel("y"),
      var("y").isa("pokemon-type").id("water")
    )
  )
);
```



## Properties

### isa
Match instances that have the given type.

```sql
match $x isa pokemon
```
```java
qb.match(var("x").isa("pokemon"));
```



### id
Match concepts that have an `id` which matches the [predicate](#predicates).  

```sql
match $x id "Articuno"
```
```java
qb.match(var("x").id("Articuno"));
```



### value

Match concepts that have a `value`. If a [predicate](#predicates) is provided, the resource must match that predicate.  


```sql
match $x value contains "lightning";
```
```java
import static io.mindmaps.graql.api.query.ValuePredicate.*;

qb.match(var("x").value(contains("lightning")))
```


### has

Match concepts that have a resource of `type`. If a [predicate](#predicates) is provided, the resource must match that predicate.  

```sql
match $x has pokedex-no < 20
```
```java
import static io.mindmaps.graql.api.query.ValuePredicate.*;

qb.match(var("x").has("pokedex-no", lt(20)));
```


The above is equivalent to:

```sql
match
(pokedex-no-owner $x, pokedex-no-value $pokedex-no) isa has-pokedex-no;
$pokedex-no value < 20;
```
```java
qb.match(
  var().rel("pokedex-no-owner", "x").rel("pokedex-no-value", "pokedex-no")
    .isa("has-pokedex-no"),
  var("pokedex-no").value(lt(20))
);
```

### relation

Match concepts that are relations between the given variables. If a role type is provided, the role player must be playing that role type.

```sql
match
$x isa pokemon;
(ancestor $x, $y)
```
```java
qb.match(
  var("x").isa("pokemon"),
  var().rel("ancestor", "x").rel("y")
);
```


## Type Properties

The following properties only apply to types.

### ako
Match types that are a subclass of the given type.

```sql
match $x ako type
```
```java
qb.match(var("x").ako("type"))
```



### has-role
Match relation types that have the given role.

```sql
match evolution has-role $x
```
```java
qb.match(id("evolution").hasRole(var("x")));
```



### plays-role
Match concept types that play the given role.

```sql
match $x plays-role ancestor
```
```java
qb.match(var("x").playsRole("ancestor"));
```



### has-resource
Match concept types that can have the given resource types. 

```sql
match $x has-resource name
```
```java
qb.match(var("x").hasResource("name"));
```

The above is equivalent to:

```sql
match $x plays-role name-owner;
```
```java
qb.match(var("x").playsRole("name-owner"));
```

## Predicates

A predicate is a boolean function applied to values.

If a concept doesn't have a value, all predicates are considered false.

### Comparators

There are several standard comparators, `=`, `!=`, `>`, `>=`, `<` and `<=`. For
longs and doubles, these sort by value. Strings are ordered lexicographically.

```sql
match $x has height = 19, has weight > 1500
```
```java
import static io.mindmaps.graql.api.query.ValuePredicate.*;

qb.match(var("x").has("height", 19).has("weight", gt(1500)));
```


### Contains
Asks if the given string is a substring.

```sql
match $x has description contains "underground"
select $x(id, has description)
```
```java
import static io.mindmaps.graql.api.query.ValuePredicate.*;

qb.match(var("x").has("description", contains("underground")));
```



### Regex
Checks if the value matches a regular expression. This match is across the
entire string, so if you want to match something within a string, you must
surround the expression with `.*`.

```sql
match $x value /.*(fast|quick).*/
```
```java
import static io.mindmaps.graql.api.query.ValuePredicate.*;

qb.match(var("x").value(regex(".*(fast|quick).*")));
```



### And and Or
`and` and `or` allows combining predicates using boolean logic.

```sql
match $x has weight >20 and <30
```
```java
import static io.mindmaps.graql.api.query.ValuePredicate.*;

qb.match(var("x").has("weight", gt(20).and(lt(30))));
```


## Modifiers

```sql
match $x isa pokemon
select $x(has pokedex-no, id)
limit 30, offset 10, distinct, order by $x(has pokedex-no) asc
```
```java
qb.match(var("x").isa("pokemon"))
    .select("x")
    .limit(30)
    .offset(10)
    .distinct()
    .orderBy("x", "pokedex-no", true);
```

### limit
Limit the query to the given number of results.

### offset
Offset the query by the given number of results.

### distinct
Remove any duplicate results.

### order
Order the results by the given variable's degree. If a type is provided, order
by the resource of that type on that concept. Order is ascending by default.

### select

Indicates which variables to include in the results as well as optionally some
[getters](#getters), e.g. `$x(id)`. If no getters are provided, the query will
get the [id](#section-id), [value](#section-value) and [isa](#section-isa) from
each result.

#### Getters
A getter indicates a property to get from a variable. Getters are supported in
the Graql shell, but are not supported in [Java Graql](java_graql.html).

```sql
match $x isa pokemon
select $x(id, has pokedex-no, has description)
```


##### isa
Get the type of a concept.

##### id
Get the `id` of a concept.

##### value
Get the `value` of a concept.

##### has
Get all resources of the given type on this concept.

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