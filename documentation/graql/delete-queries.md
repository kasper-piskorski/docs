# Delete Queries

```sql
delete $x where $x isa pokemon
```
```java
qb.delete("x").where(var("x").isa("pokemon"));
```

A delete query will delete the specified [variable
patterns](#variable-patterns) for every result of the `where`
[patterns](patterns.md). If a variable pattern indicates just a variable, then
the whole concept will be deleted. If it is more specific (such as indicating
the `id` or `isa`) it will only delete the specified properties.

## Variable Patterns

> **Deleting Only!**
> 
> The following definitions only apply when patterns are being used to delete.
> For patterns after the `where` clause, look [here](patterns.md).

A variable pattern is a pattern describing [properties](#properties) to delete
on a particular concept. The variable pattern is always bound to a variable
name.

If a variable pattern has no properties, then the concept itself is deleted.
Otherwise, only the specified properties are deleted.

## Properties

### has-role

```sql
delete $x has-role descendant where $x id "evolution";
```

Removes the given role from the relation type.

### plays-role

```sql
delete $x plays-role attacking-type where $x id "type";
```

Disallows the concept type from playing the given role.

### has

```sql
delete $x has weight where $x id "Bulbasaur";
```

Deletes the resources of the given type on the concept. If a value is given,
only delete resources matching that value.
