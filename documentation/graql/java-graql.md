# Java Graql

As well as the Graql shell, users can also construct and execute Graql queries
programmatically in Java.

Add the following to your `pom.xml`:

```xml
<dependency>
  <groupId>io.mindmaps.graql</groupId>
  <artifactId>graql-core</artifactId>
  <version>0.2.1</version>
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

A `QueryBuilder` is constructed by providing a Mindmaps Graph:

```java
MindmapsGraph graph = TinkerGraphFactory.getInstance().getGraph();
QueryBuilder qb = new QueryBuilder(graph);
```

The `QueryBuilder` class provides methods for building `select`, `ask`,
`insert` and `delete` queries.

## Select Queries

Select queries are constructed using the `select` and `where` methods and
include all modifiers:

```java
SelectQuery tallPokemon = qb.select("x").where(var("x").isa("pokemon").has("height", gt(10))).limit(50);
```

`SelectQuery` is `Iterable` and has a `stream` method. Each result is a
`Map<String, Concept>`, where the keys are the variable names in the query.

```java
for (Map<String, Concept> result : tallPokemon) {
  System.out.println(result.get("x").getValue());
}

tallPokemon.stream()
  .map(r -> r.get("x"))
  .map(Concept::getValue)
  .forEach(System.out::println);
```

## Ask Queries

Ask queries immediately return a boolean:

```java
if (qb.ask(var().isa("pokemon-type").value("dragon"))) {
  System.out.println("Dragons are real!");
}
```

## Insert Queries

Insert queries execute as soon as `execute()` or `where()` is called:

```java
InsertQuery addPichu = qb.insert(var().id("Pichu").isa("pokemon"));

addPichu.execute();

// Make everything dragons!
qb.insert(
  var().isa("has-type")
    .rel("pokemon-with-type", "x")
    .rel("type-of-pokemon", var().id("dragon"))
).where(
  var("x").isa("pokemon")
);
```

## Delete Queries

Delete queries are executed when `where()` is called:

```java
qb.delete(var("x")).where(var("x").id("Pichu"));
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

parser.parseSelectQuery("select $x where $x isa pokemon").forEach(
  result -> System.out.println(result.get("x"))
);

if (parser.parseAskQuery("ask water isa pokemon-type")) {
  System.out.println("Water is a pokemon type!");
}

parser.parseInsertQuery("insert id 'pichu' isa pokemon").execute();

parser.parseDeleteQuery("delete $x where $x isa pokemon");
```
