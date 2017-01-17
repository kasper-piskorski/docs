---
title: Java Graql
keywords: graql, java
last_updated: August 11, 2016
tags: [graql, java]
summary: "How to construct and execute Graql queries programmatically in Java."
sidebar: documentation_sidebar
permalink: /documentation/developing-with-java/java-graql.html
folder: documentation
---

As well as the Graql shell, users can also construct and execute Graql queries programmatically in Java. The Java Graql API expresses the concepts and functionality of the Graql language in the syntax of Java. It is useful if you want to make queries using Java, without having to construct a string containing the appropriate Graql expression.

To use the API, add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>ai.grakn</groupId>
  <artifactId>grakn-graql</artifactId>
  <version>0.10.0</version>
</dependency>
```

## QueryBuilder

`Graql` contains several useful static methods such as `var` and `eq`, so it's recommended that you use a static import:

```java
import static ai.grakn.graql.Graql.*;
```

A `QueryBuilder` is constructed from a `GraknGraph`:

```java-test-ignore
GraknGraph graknGraph = Grakn.factory(Grakn.DEFAULT_URI, "my-graph").getGraph();
QueryBuilder qb = graknGraph.graql();
```

The user can also choose to not provide a graph with `Graql.withoutGraph()`.
This can be useful if you need to provide the graph later (using `withGraph`),
or you only want to construct queries without executing them.

The `QueryBuilder` class provides methods for building `match` and `insert`
queries. `ask`, `insert` and `delete` queries can all be built from `match`
queries.

## Match Queries

Match queries are constructed using the `match` method. This will produce a
`MatchQuery` instance, which includes additional methods that apply modifiers
such as `limit` and `distinct`:

```java
MatchQuery tallPokemon = qb.match(var("x").isa("pokemon").has("height", gt(10))).limit(50);
```

`MatchQuery` is `Iterable` and has a `stream` method. Each result is a
`Map<String, Concept>`, where the keys are the variable names in the query.

A `MatchQuery` will only execute when it is iterated over.

```java
for (Map<String, Concept> result : tallPokemon) {
  System.out.println(result.get("x").getId());
}
```

If you're only interested in one variable name, it also includes a `get` method
for requesting a single variable:

```
tallPokemon.get("x").forEach(x -> System.out.println(x.getValue()));
```

## Ask Queries

```java
if (qb.match(var().isa("pokemon-type").has("name", "dragon")).ask().execute()) {
  System.out.println("Dragons are real!");
}
```

## Insert Queries

```java
InsertQuery addPichu = qb.insert(var().isa("pokemon").has("name", "Pichu"));

addPichu.execute();

// Make everything dragons!
qb.match(
  var("x").isa("pokemon"),
  var("dragon").has("name", "dragon")
).insert(
  var().isa("has-type")
    .rel("pokemon-with-type", "x")
    .rel("type-of-pokemon", "dragon")
).execute();
```

## Delete Queries

```java
qb.match(var("x").has("name", "Pichu")).delete("x").execute();
```

## Query Parser

```xml
<dependency>
  <groupId>ai.grakn.graql</groupId>
  <artifactId>graql-parser</artifactId>
  <version>0.8.0</version>
</dependency>
```

The `QueryBuilder` also allows the user to parse Graql query strings into Java Graql
objects:

```java
for (Concept x : qb.<MatchQuery>parse("match $x isa pokemon;").get("x")) {
    System.out.println(x);
}

if (qb.<AskQuery>parse("match has name 'water' isa pokemon-type; ask;").execute()) {
  System.out.println("Water is a pokemon type!");
}

qb.parse("insert isa pokemon, has name 'Pichu';").execute();

qb.parse("match $x isa pokemon; delete $x;").execute();
```

{% include links.html %}

