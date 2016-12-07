---
title: Match Queries
keywords: graql, query, match
last_updated: August 11, 2016
tags: [graql]
summary: "Graql Match Queries"
sidebar: documentation_sidebar
permalink: /documentation/graql/match-queries.html
folder: documentation
---

A match query will search the graph for any subgraphs that match the given
[patterns](#variable-patterns). A result is produced for each match found,
containing any variables in the query. The results of the query can be modified with various [modifiers](#modifiers).


## Variable Patterns

A pattern is a pattern to match in the graph. Patterns can be combined into a
disjunction ('or') and grouped together with curly braces. Patterns are
separated by semicolons, each pattern is independent of the others.
A variable pattern is a pattern describing [properties](#properties) of a
particular concept. The variable pattern can optionally be bound to a variable
or an ID.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell" data-toggle="tab">Graql</a></li>
    <li><a href="#java" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell">
<pre>match $x isa pokemon;
{
  $x id "Mew"
} or {
  ($x, $y);
  $y isa pokemon-type, id "water";
}</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java">
<pre>qb.match(
  var("x").isa("pokemon"),
  or(
    var("x").id("Mew"),
    and(
      var().rel("x").rel("y"),
      var("y").isa("pokemon-type").id("water")
    )
  )
);</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->



## Properties

### isa
Match instances that have the given type.
        
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell1" data-toggle="tab">Graql</a></li>
    <li><a href="#java1" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>match $x isa pokemon;</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>qb.match(var("x").isa("pokemon"));</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### id
Match concepts that have an `id` which matches the [predicate](#predicates).  
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell2" data-toggle="tab">Graql</a></li>
    <li><a href="#java2" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell2">
<pre>
match $x has name "Articuno";
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java2">
<pre>
qb.match(var("x").has("name", "Articuno"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### value

Match concepts that have a `value`. If a [predicate](#predicates) is provided, the resource must match that predicate.  

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell3" data-toggle="tab">Graql</a></li>
    <li><a href="#java3" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell3">
<pre>
match $x value contains "lightning";
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java3">
<pre>
qb.match(var("x").value(contains("lightning")))
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### has

Match concepts that have a resource of `type`. If a [predicate](#predicates) is provided, the resource must match that predicate.  

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell4" data-toggle="tab">Graql</a></li>
    <li><a href="#java4" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell4">
<pre>
match $x has pokedex-no < 20;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java4">
<pre>
qb.match(var("x").has("pokedex-no", lt(20)));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


The above is equivalent to:

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell5" data-toggle="tab">Graql</a></li>
    <li><a href="#java5" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell5">
<pre>
match
(has-pokedex-no-owner: $x, has-pokedex-no-value: $pokedex-no) isa has-pokedex-no;
$pokedex-no value < 20;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java5">
<pre>
qb.match(
  var().rel("pokedex-no-owner", "x").rel("pokedex-no-value", "pokedex-no")
    .isa("has-pokedex-no"),
  var("pokedex-no").value(lt(20))
);

</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### relation

Match concepts that are relations between the given variables. If a role type is provided, the role player must be playing that role type.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell6" data-toggle="tab">Graql</a></li>
    <li><a href="#java6" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell6">
<pre>
match
$x isa pokemon;
(ancestor: $x, $y);
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java6">
<pre>
qb.match(
  var("x").isa("pokemon"),
  var().rel("ancestor", "x").rel("y")
);
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

## Type Properties

The following properties only apply to types.

### sub
Match types that are a subclass of the given type.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell7" data-toggle="tab">Graql</a></li>
    <li><a href="#java7" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell7">
<pre>
match $x sub type;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java7">
<pre>
qb.match(var("x").sub("type"))
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### has-role
Match relation types that have the given role.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell8" data-toggle="tab">Graql</a></li>
    <li><a href="#java8" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell8">
<pre>
match evolution has-role $x;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java8">
<pre>
qb.match(name("evolution").hasRole(var("x")));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### plays-role
Match concept types that play the given role.
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell9" data-toggle="tab">Graql</a></li>
    <li><a href="#java9" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell9">
<pre>
match $x plays-role ancestor;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java9">
<pre>
qb.match(var("x").playsRole("ancestor"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### has-resource
Match concept types that can have the given resource types.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell10" data-toggle="tab">Graql</a></li>
    <li><a href="#java10" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell10">
<pre>
match $x has-resource name;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java10">
<pre>
qb.match(var("x").hasResource("name"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

The above is equivalent to:

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell11" data-toggle="tab">Graql</a></li>
    <li><a href="#java11" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell11">
<pre>
match $x plays-role has-name-owner;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java11">
<pre>
qb.match(var("x").playsRole("has-name-owner"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

## Predicates

A predicate is a boolean function applied to values. If a concept doesn't have a value, all predicates are considered false.

### Comparators

There are several standard comparators, `=`, `!=`, `>`, `>=`, `<` and `<=`. For
longs and doubles, these sort by value. Strings are ordered lexicographically.
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell12" data-toggle="tab">Graql</a></li>
    <li><a href="#java12" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell12">
<pre>
match $x has height = 19, has weight > 1500;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java12">
<pre>
qb.match(var("x").has("height", 19).has("weight", gt(1500)));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

If a concept doesn't have a value, all predicates are considered false. The query below matches everything where the predicate `>10` is true. So, it will find all concepts with value greater than 10. However, if a concept does not have a value at all, the predicate is considered false, so it won’t appear in the results.

```graql
match $x value >10;
``` 


### Contains
Asks if the given string is a substring.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell13" data-toggle="tab">Graql</a></li>
    <li><a href="#java13" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell13">
<pre>
match $x has description $desc;
$desc value contains "underground";

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java13">
<pre>
qb.match(
    var("x").has("description", var("desc")),
    var("desc").value(contains("underground"))
);
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Regex
Checks if the value matches a regular expression. This match is across the
entire string, so if you want to match something within a string, you must
surround the expression with `.*`.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell14" data-toggle="tab">Graql</a></li>
    <li><a href="#java14" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell14">
<pre>
match $x value /.*(fast|quick).*/;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java14">
<pre>
qb.match(var("x").value(regex(".*(fast|quick).*")));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

## Modifiers

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell16" data-toggle="tab">Graql</a></li>
    <li><a href="#java16" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell16">
<pre>
match $x isa pokemon, has pokedex-no $no;
select $x;
limit 10; offset 30; distinct; order by $no asc;

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java16">
<pre>
qb.match(var("x").isa("pokemon").has("pokedex-no", var("no")))
    .select("x")
    .limit(10)
    .offset(30)
    .distinct()
    .orderBy("no", Order.asc);
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### distinct
Remove any duplicate results.

### limit
Limit the query to the given number of results.

Note that the order in which you specify modifiers can be important. If you make a query and `limit` the results returned, say to 10 as in the example, then specify the `distinct` modifier _after_ the `limit`, you may find that `distinct` removes any non-unique results, so you end up with fewer than the 10 results you expected to be returned to you. To ensure that you receive _exactly_ 10 distinct results, you are better to use `distinct` before `limit`.
     
```graql
match $x isa pokemon, has pokedex-no $no;
select $x;
distinct; limit 10; offset 30; order by $no asc;
```

### offset
Offset the query by the given number of results.

### order
Order the results by the given variable's degree. If a type is provided, order
by the resource of that type on that concept. Order is ascending by default.

### select

Indicates which variables to include in the results.

{% include links.html %}

