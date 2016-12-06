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

An aggregate query applies an operation on a [match query](match-queries.html).

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell1" data-toggle="tab">Graql</a></li>
    <li><a href="#java1" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
$x isa pokemon-type;
($x, $y) isa has-type;
aggregate group $x count;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon-type"),
    var().rel("x").rel("y").isa("has-type")
).aggregate(group("x", count()));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

## Aggregate Functions

### Count

Count the number of results of the match query or aggregate result.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa pokemon; aggregate count;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(var("x").isa("pokemon")).aggregate(count());
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Sum

Sum the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
$x isa pokemon, has weight $w;
aggregate sum $w;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon").has("weight", var("w"))
).aggregate(sum("w"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Max

Find the maximum of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
$x isa pokemon, has height $h;
aggregate max $h;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon").has("height", var("h"))
).aggregate(max("h"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Min

Find the minimum of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
$x isa pokemon, has name $n;
aggregate min $n;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon").has("name", var("n"))
).aggregate(min("n"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Average

Find the average (mean) of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
$x isa pokemon, has height $h;
aggregate average $h;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon").has("height", var("h"))
).aggregate(average("h"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Median

Find the median of the given resource variable.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
$x isa pokemon, has weight $w;
aggregate median $w;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon").has("weight", var("w"))
).aggregate(median("w"));
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
match
$x isa pokemon-type;
$y isa pokemon-type;
(attacking-type: $x, defending-type: $y) isa super-effective;
aggregate group $x;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon-type"),
    var("y").isa("pokemon-type"),
    var().rel("attacking-type", "x").rel("defending-type", "y").isa("super-effective")
).aggregate(group("x"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### Select

Select and name multiple aggregates.

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match
$x isa pokemon, has weight $w, has height $h;
aggregate (min $w as minWeight, max $h as maxHeight);
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("x").isa("pokemon").has("weight", var("w")).has("height", var("h")),
).aggregate(select(min("w").as("minWeight"), max("h").as("maxHeight")));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


{% include links.html %}


