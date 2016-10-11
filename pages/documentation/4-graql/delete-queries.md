---
title: Delete Queries
keywords: graql, query, delete
last_updated: August 11, 2016
tags: [graql]
summary: "Graql Delete Queries"
sidebar: documentation_sidebar
permalink: /documentation/graql/delete-queries.html
folder: documentation
---

A delete query will delete the specified [variable
patterns](#variable-patterns) for every result of the [match
query](graql_match.html). If a variable pattern indicates just a variable, then
the whole concept will be deleted. If it is more specific (such as indicating
the `id` or `isa`) it will only delete the specified properties.
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell1" data-toggle="tab">Graql</a></li>
    <li><a href="#java1" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match $x isa pokemon; delete $x;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(var("x").isa("pokemon")).delete("x").execute();
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


## Variable Patterns

A variable pattern in a delete query describes [properties](#properties) to
delete on a particular concept. The variable pattern is always bound to a
variable name.

If a variable pattern has no properties, then the concept itself is deleted.
Otherwise, only the specified properties are deleted.

## Properties

### has-role
Removes the given role from the relation type.
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell2" data-toggle="tab">Graql</a></li>
    <li><a href="#java2" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell2">
<pre>
match $x id "evolution"; delete $x has-role descendant;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java2">
<pre>
qb.match(var("x").id("evolution")).delete(var("x").hasRole("descendant"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### plays-role
Disallows the concept type from playing the given role.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell3" data-toggle="tab">Graql</a></li>
    <li><a href="#java3" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell3">
<pre>
match $x id "type"; delete $x plays-role attacking-type;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java3">
<pre>
qb.match(var("x").id("type")).delete(var("x").playsRole("attacking-type"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### has
Deletes the resources of the given type on the concept. If a value is given,
only delete resources matching that value.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell4" data-toggle="tab">Graql</a></li>
    <li><a href="#java4" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell4">
<pre>
match $x id "Bulbasaur"; delete $x has weight;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java4">
<pre>
qb.match(var("x").id("Bulbasaur")).delete(var("x").has("weight"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

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
