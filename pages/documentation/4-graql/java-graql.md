---
title: Java Graql
keywords: graql, java
last_updated: August 11, 2016
tags: [graql, java]
summary: "How to construct and execute Graql queries programmatically in Java."
sidebar: documentation_sidebar
permalink: /documentation/graql/java-graql.html
folder: documentation
---

As well as the Graql shell, users can also construct and execute Graql queries programmatically in Java. The Java Graql API expresses the concepts and functionality of the Graql language in the syntax of Java. It is useful if you want to make queries using Java, without having to construct a string containing the appropriate Graql expression.

To use the API, add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>io.mindmaps</groupId>
  <artifactId>mindmaps-graql</artifactId>
  <version>0.1.1</version>
</dependency>
```

## QueryBuilder

The `QueryBuilder` class is how you begin building Graql queries.

`QueryBuilder` and `ValuePredicate` contains several useful static methods such
as `var` and `eq`, so it's recommended that you use a static import:

```java
import io.mindmaps.graql.api.query.QueryBuilder;
import static io.mindmaps.graql.api.query.QueryBuilder.*;
import static io.mindmaps.graql.api.query.ValuePredicate.*;
```

A `QueryBuilder` is constructed by providing a `MindmapsTransaction`:

```java
MindmapsGraph graph = MindmapsClient.getGraph("my-graph");
MindmapsTransaction transaction = graph.newTransaction();
QueryBuilder qb = QueryBuilder.build(transaction);
```

The user can also choose to not provide a transaction. This can be useful if
you need to provide the transaction later (using `withTransaction`), or you
only want to construct queries without executing them.

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
  System.out.println(result.get("x").getValue());
}
```

If you're only interested in one variable name, it also includes a `get` method
for requesting a single variable:

```
tallPokemon.get("x").forEach(x -> System.out.println(x.getValue()));
```

## Ask Queries

```java
if (qb.ask(var().isa("pokemon-type").value("dragon"))) {
  System.out.println("Dragons are real!");
}
```

## Insert Queries

```java
InsertQuery addPichu = qb.insert(var().id("Pichu").isa("pokemon"));

addPichu.execute();

// Make everything dragons!
qb.match(
  var("x").isa("pokemon")
).insert(
  var().isa("has-type")
    .rel("pokemon-with-type", "x")
    .rel("type-of-pokemon", var().id("dragon"))
).execute();
```

## Delete Queries

```java
qb.match(var("x").id("Pichu")).delete("x").execute();
```

## Query Parser

```xml
<dependency>
  <groupId>io.mindmaps.graql</groupId>
  <artifactId>graql-parser</artifactId>
  <version>0.2.1</version>
</dependency>
```

The `QueryParser` allows the user to parse Graql query strings into Java Graql
objects:

```java
QueryParser parser = new QueryParser(graph);

parser.parseMatchQuery("match $x isa pokemon").getMatchQuery().get("x").forEach(System.out::println);

if (parser.parseAskQuery("match water isa pokemon-type ask").execute()) {
  System.out.println("Water is a pokemon type!");
}

parser.parseInsertQuery("insert id 'pichu' isa pokemon").execute();

parser.parseDeleteQuery("match $x isa pokemon delete $x");
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
        <td>v0.1.0</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>
