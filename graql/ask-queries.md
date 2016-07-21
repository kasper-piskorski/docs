# Ask Queries

```sql
ask dragon isa pokemon-type
```
```java
qb.ask(getType("dragon").isa("pokemon-type"));
```

An ask query will search the graph and return `true` or `false` depending on
whether the given [patterns](patterns.md) can be matched in the graph.
