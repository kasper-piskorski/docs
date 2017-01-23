---
title: Aggregate Queries
keywords: graql, query, aggregate
last_updated: Tuesday 6, 2016
tags: [graql]
summary: "Graql Aggregate Queries"
sidebar: documentation_sidebar
permalink: /documentation/graql/aggregate-queries.html
folder: documentation
---

An aggregate query applies an operation onto a [match query](match-queries.html), to return information about the results (e.g. a count). To follow along, or experiment further, with the examples given below, please load the *basic-genealogy.gql* file, which can be found in the *examples* directory of the Grakn installation zip, or on [Github]().

```bash
<relative-path-to-Grakn>/bin/grakn.sh start 
<relative-path-to-Grakn>/bin/graql.sh -f <relative-path-to-Grakn>/examples/basic-genealogy.gql
```



## Aggregate Functions

### Count

Count the number of results of the match query or aggregate result.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa person; aggregate count;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(var("x").isa("person")).aggregate(count());
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Sum

Sum the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa person, has age $a; aggregate sum $a;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("person").has("age", var("a"))
).aggregate(sum("a"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Max

Find the maximum of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa person, has age $a; aggregate max $a;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("person").has("age", var("a"))
).aggregate(max("h"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Min

Find the minimum of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa person, has firstname $n; aggregate min $n;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("person").has("firstname", var("n"))
).aggregate(min("n"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Average

Find the average (mean) of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa person, has age $a; aggregate average $a;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("person").has("age", var("a"))
).aggregate(average("a"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Median

Find the median of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
match $x isa person, has age $a; aggregate median $a;

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("person").has("age", var("a"))
).aggregate(median("a"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Group

Group the results by the given variable.

The group aggregate can optionally accept a second argument which is another 
aggregate operation, e.g. `count`.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa person; $y isa person; (parent: $x, child: $y) isa parentship; aggregate group $x;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("person"),
    var("y").isa("person"),
    var().rel("parent", "x").rel("child", "y").isa("parentship")
).aggregate(group("x"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Select

Select and name multiple aggregates.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa person, has age $a, has gender $g; aggregate (min $a as minAge, max $g as maxGender);
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("person").has("age", var("w")).has("gender", var("h")),
).aggregate(select(min("w").as("minAge"), max("h").as("maxGender")));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

## When to Use `aggregate` and When to Use `compute`

Aggregate queries are computationally light and run single-threaded on a single machine, but are more flexible than the equivalent [compute query](./compute-queries.html).

For example, you can use an aggregate query to filter results by resource. The following  aggregate query, allows you to match the number of people of a particular name:

```
match $x has identifier contains "Elizabeth"; aggregate count;
```

Compute queries are computationally intensive and run in parallel on a cluster (so are good for big data).

```
compute count of person; 
```

Can be used to calculate the number of people in the graph very fast, but you can't filter the results to determine the number of people with a certain name.

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/42" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.


{% include links.html %}


