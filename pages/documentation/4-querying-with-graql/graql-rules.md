---
title: Graql Rules
keywords: graql, reasoner
last_updated: January 2017
tags: [graql, reasoner]
summary: "Graql Rules"
sidebar: documentation_sidebar
permalink: /documentation/graql/graql-rules.html
folder: documentation
---

## Rule Objects
Grakn supports Graql-native, rule-based reasoning to allow automated capture and evolution of patterns within the graph. Graql reasoning is performed at query time and is guaranteed to be complete.

The rule objects are instances of inference rule RuleType and assume the following general form:

```
if [rule-body] then [rule-head]
```
or in Prolog/Datalog terms:

```
[rule-head] :- [rule-body].
```

In logical terms, we restrict the rules to be definite Horn clauses (i.e. disjunctions of atoms with at most one unnegated atom). In our system we define both the head and the body of rules as Graql patterns. Consequently, the rules are statements of the form:

```
p :- q1, q2, ..., qn
```

where p and q's are atoms that each correspond to a single Graql pattern.

## Graql Rule Syntax
In Graql we refer to the body of the rule as the left-hand-side of the rule and the head as the right-hand-side of the rule. Therefore in Graql terms we define rule objects in the following way:

```graql
$optional-name isa inference-rule,
lhs {
    ...;
    ...;
    ...;
},
rhs {
    ...;
};
```

In Graql the left-hand-side of the rule is required to be a conjunctive pattern, whereas the right-hand-side should contain a single pattern.

A classic reasoning example is the ancestor example: the two Graql rules R1 and R2 stated below define the ancestor relationship which can be understood as either happening between two generations directly between a parent and a child or between three generations when the first generation hop is expressed via a parentship relation and the second generation hop is captured by an ancestor relation.

```graql
$R1 isa inference-rule,
lhs {
    (parent: $x, child: $y) isa Parent;
},
rhs {
    (ancestor: $x, descendant: $y) isa Ancestor;
};

$R2 isa inference-rule,
lhs {
    (parent: $x, child: $z) isa Parent;
    (ancestor: $z, descendant: $y) isa Ancestor;
},
rhs {
    (ancestor: $x, descendant: $y) isa Ancestor;
};
```

When adding rules such as those defined above with Graql, we simply use an `insert` statement, and load the rules, saved as a *.gql* file, into the graph in a standard manner, much as for an ontology. 

The above defined rules correspond to the following definition of Prolog/Datalog clauses:

```
R1: ancestor(X, Y) :- parent(X, Y)  
R2: ancestor(X, Y) :- parent(X, Z), ancestor(Z, Y)
```

### Rule Java API
Rule objects are instances of type inference-rule which can be retrieved by:

```java
RuleType inferenceRule = graknGraph.getMetaRuleInference();
```

Rule objects can be added to the graph both through the Graph API as well as through Graql. With the use of the Java API, the two rules constituting the ancestor example can be added in the following way:

```java
Pattern r1Body = var().rel("parent", "x").rel("child", "y").isa("Parent");
Pattern r1Head = var().rel("ancestor", "x").rel("descendant", "y").isa("Ancestor");

Pattern r2Body = and(
        var().rel("parent", "x").rel("child", "y").isa("Parent')"),
        var().rel("ancestor", "z").rel("descendant", "y").isa("Ancestor")
);
Pattern r2Head = var().rel("ancestor", "x").rel("descendant", "y").isa("Ancestor");

Rule rule1 = inferenceRule.addRule(r1Body, r1Head);
Rule rule2 = inferenceRule.addRule(r2Body, r2Head);
```

## Allowed Graql Constructs in Rules
The tables below summarise Graql constructs that are allowed to appear in LHS
and RHS of rules.   

### Queries

| Description        | LHS | RHS
| -------------------- |:--|:--|
| atomic queries | ✓ | ✓ |
| conjunctive queries        | ✓ | x |
| disjunctive queries        | x | x |  

### Variable Patterns

| Description        | Pattern Example           | LHS | RHS
| -------------------- |:--- |:--|:--|
| `isa` | `$x isa pokemon;` | ✓ | x |
| `id`  | `$x id "Articuno";` | ✓ | indirect only  |
| `value` | `$x value contains "lightning";`  | ✓ | indirect only  |
| `has` | `$x has pokedex-no < 20;` | ✓ | ✓ |
| `relation` | `(ancestor: $x, descendant: $y) isa ancestorship;` | ✓ | ✓ |
| resource comparison | `$x value > $y;`  | ✓ | x |
| `!=` | `$x != $y;` | ✓ | x |
| `has-scope` | `($x, $y) has-scope $z;$x has-scope $y;`  | ✓ | x |

### Type Properties

| Description        | Pattern Example   | LHS | RHS
| -------------------- |:---|:--|:--|
| `sub`        | `$x sub type;` | ✓| x |
| `plays-role` | `$x plays-role ancestor;` |✓| x |
| `has-resource`        | `$x has-resource name;` | ✓ | x |  
| `has-role`   | `evolution has-role $x;` | ✓ | x |
| `is-abstract` | `$x is-abstract;` | ✓ | x |
| `datatype` | `$x isa resource, datatype string;` | ✓| x |
| `regex` | `$x isa resource, regex /hello/;` | ✓ | x |

## Configuration options
Graql offers certain degrees of freedom in deciding how and if reasoning should be performed. Namely it offers two options:

* **whether reasoning should be on**. This option is self-explanatory. If the reasoning is not turned on, the rules will not be triggered and no knowledge will be inferred. 
* **whether inferred knowledge should be materialised (persisted to the graph) or stored in memory**. Persisting to graph has a huge impact on performance when compared to in-memory inference, and, for larger graphs, materialisation should either be avoided or queries be limited by employing the _limit_ modifier, which allows termination in sensible time.

In Java, the two settings can be controlled by providing suitable parameters to the `QueryBuilder` objects in the following way:

### Switching reasoning on

```java
//graph is a GraknGraph instance
QueryBuilder qb = graph.graql().infer(true);
```

### Switching materialisation on

```java
//graph is a GraknGraph instance
QueryBuilder qb = graph.graql().infer(true).materialise(true);
```

Once the `QueryBuilder` has been defined, the constructed queries will obey the specified reasoning variants.
    
The table below summarises the available reasoning configuration options together with their defaults.

| Option       | Description | Default
| -------------------- |:--|:--|
| `QueryBuilder::infer(boolean)` | controls whether reasoning should be turned on | False=Off |
| `QueryBuilder::materialise(boolean)`       | controls whether inferred knowledge should be persisted to graph | False=Off |

{% include links.html %}
