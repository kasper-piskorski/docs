# Query Patterns

> **Querying Only!**
>
> The following descriptions only apply when patterns are being used in a
> `where` clause or an [ask query](ask-queries.md), not to
> [insert](insert-queries.md) or [delete](delete-queries.md).

```sql
select $x where
$x isa pokemon;
{
  $x id "Mew"
} or {
  ($x, $y);
  $y isa pokemon-type, id "water";
}
```
```java
qb.select("x").where(
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

A pattern is a pattern to match in the graph. Patterns can be combined into a
disjunction ('or') and grouped together with curly braces. Patterns are
separated by semicolons, each pattern is independent of the others.

## Variable Patterns

A variable pattern is a pattern describing [properties](#properties) of a
particular concept. The variable pattern can optionally be bound to a variable
or an ID.

## Properties

### isa

```sql
select $x where $x isa pokemon
```
```java
qb.select("x").where(var("x").isa("pokemon"));
```

Match instances that have the given type.

### id

```sql
select $x where $x id "Articuno"
```

Match concepts that have an `id` which matches the [predicate](#predicates).

### value

```sql
select $x where $x value contains "lightning";
```

Match concepts that have a `value`. If a [predicate](#predicates) is provided,
the resource must match that predicate.

### has

```sql
select $x where $x has pokedex-no < 20
```

Match concepts that have a resource of `type`. If a [predicate](#predicates) is
provided, the resource must match that predicate.

### relation

```sql
select $x, $y where
$x isa pokemon;
(ancestor $x, $y)
```
```java
qb.select("x", "y").where(
  var("x").isa("pokemon"),
  var().rel("ancestor", "x").rel("y")
);
```

Match concepts that are relations between the given variables. If a role type
is provided, the role player must be playing that role type.

## Type Properties

The following properties only apply to types.

### ako

```sql
select $x where $x ako mm-type
```

Match types that are a subclass of the given type.

### has-role

```sql
select $x where evolution has-role $x
```

Match relation types that have the given role.

### plays-role

```sql
select $x where $x plays-role ancestor
```

Match concept types that play the given role.

## Predicates

A predicate is a boolean function applied to values.

If a concept doesn't have a value, all predicates are considered false.

### Comparators

```sql
select $x where $x has height = 19, has weight > 1500
```

There are several standard comparators, `=`, `!=`, `>`, `>=`, `<` and `<=`. For
longs and doubles, these sort by value. Strings are ordered lexicographically.

### Contains

```sql
select $x(id, has description) where $x has description contains "underground"
```

Asks if the given string is a substring.

### Regex

```sql
select $x where $x value /.*(fast|quick).*/
```

Checks if the value matches a regular expression. This match is across the
entire string, so if you want to match something within a string, you must
surround the expression with `.*`.

### And and Or

```sql
select $x where $x has weight >20 and <30
```

`and` and `or` allows combining predicates using boolean logic.
