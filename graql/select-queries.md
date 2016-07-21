# Select Queries

```sql
select $x, $y where
$x isa pokemon, id "Pikachu";
(pokemon-with-type $x, $y);
```
```java
qb.select("x", "y").where(
    var("x").isa("pokemon").id("Pikachu"),
    var().rel("pokemon-with-type", "x").rel("y")
);
```

A select query will search the graph for any subgraphs that match the given
[patterns](patterns.md). A result is produced for each match found, where any
variables are bound to the given [selectors](#selectors). The results of the
query can be further modified with various [modifiers](#modifiers).

> To find out what to put after the `where` keyword, head to the [Query
> Patterns](patterns.md) page.

## Selectors

A [selector](#selectors) indicates a variable to select from each query result,
e.g. `$x` as well as optionally some [getters](#getters), e.g. `$x(id)`. If no
getters are provided, the query will get the [id](#section-id),
[value](#section-value) and [isa](#section-isa) from each result.

## Getters

```sql
select $x(id, has pokedex-no, has description) where $x isa pokemon
```

A getter indicates a property to get from a variable. Getters are supported in
the Graql shell, but are not supported in [Java Graql](java-graql.md).

### isa
Get the type of a concept.

### id
Get the `id` of a concept.

### value
Get the `value` of a concept.

### has
Get all resources of the given type on this concept.

## Modifiers

```sql
select $x(has pokedex-no, id) where $x isa pokemon
limit 30, offset 10, distinct, order by $x(has pokedex-no) asc
```
```java
qb.select("x").where(var("x").isa("pokemon"))
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
