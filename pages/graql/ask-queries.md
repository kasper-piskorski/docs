---
title: Ask Queries
keywords: graql, query, ask
last_updated: August 10, 2016
tags: []
summary: "Graql Ask Queries"
sidebar: home_sidebar
permalink: graql_ask.html
folder: graql
---
## Ask Queries

```sql
match dragon isa pokemon-type
ask
```
```java
qb.match(id("dragon").isa("pokemon-type")).ask().execute();
```

An ask query will return whether the given [match query](match-query.md) has
any results.
