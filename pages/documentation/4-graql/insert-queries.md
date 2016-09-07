---
title: Insert Queries
keywords: graql, query, insert
last_updated: August 10, 2016
tags: [graql]
summary: "Graql Insert Queries"
sidebar: documentation_sidebar
permalink: /documentation/graql/insert-queries.html
folder: documentation
---


An insert query will insert the specified [variable
patterns](#variable-patterns) into the graph. If a [match
query](match-queries.html) is provided, the query will insert the given variable
patterns for every result of the match query.

A variable pattern in an insert query describes [properties](#properties) to
set on a particular concept. The variable pattern can optionally be bound to a
variable or an ID.
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell1" data-toggle="tab">Graql</a></li>
    <li><a href="#java1" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
insert
$p isa pokemon, id "Pichu", has pokedex-no 172;
(descendant Pikachu, ancestor $p) isa evolution;

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.insert(
    var("p").isa("pokemon").id("Pichu").has("pokedex-no", 172),
    var().isa("evolution")
        .rel("descendant", var().id("Pikachu"))
        .rel("ancestor", "p")
).execute();",

</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


## Properties

### isa

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell2" data-toggle="tab">Graql</a></li>
    <li><a href="#java2" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell2">
<pre>
insert "Totodile" isa pokemon;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java2">
<pre>
qb.insert(id("Totodile").isa("pokemon"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


Set the type of this concept.

### id

Create a concept with the given id, or retrieve it if one with that id exists.
The created or retrieved concept can then be modified with further properties.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell3" data-toggle="tab">Graql</a></li>
    <li><a href="#java3" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell3">
<pre>
insert "Pikachu" isa pokemon
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java3">
<pre>
qb.insert(id("Pikachu").isa("pokemon"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### value

Set the value of the concept.
<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell4" data-toggle="tab">Graql</a></li>
    <li><a href="#java4" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell4">
<pre>
insert trained-by isa relation-type, value "Trained By";
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java4">
<pre>
qb.insert(id("trained-by").isa("relation-type").value("Trained By"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

### has

Add a resource of the given type to the concept.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell5" data-toggle="tab">Graql</a></li>
    <li><a href="#java5" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell5">
<pre>
insert "Pichu" isa pokemon has height 30;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java5">
<pre>
qb.insert(id("Pichu").isa("pokemon").has("height", 30));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


The above example is equivalent to:

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell6" data-toggle="tab">Graql</a></li>
    <li><a href="#java6" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell6">
<pre>
insert
$owner id "Pichu" isa pokemon;
$value isa height, value 30;

(height-owner $owner, height-value $value) isa has-height;

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java6">
<pre>
qb.insert(
  var("owner").id("Pichu").isa("pokemon"),
  var("value").isa("height").value(30),
  var().rel("height-owner", "owner").rel("height-value", "value").isa("has-height")
);
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### relation

Make the concept a relation that relates the given role players, playing the
given roles.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell7" data-toggle="tab">Graql</a></li>
    <li><a href="#java7" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell7">
<pre>
insert (pokemon-with-type "Pichu", type-of-pokemon "electric") isa has-type;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java7">
<pre>
qb.insert(
  var()
    .rel("pokemon-with-type", id("Pichu"))
    .rel("type-of-pokemon", id("electric"))
    .isa("has-type")
);",

</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


## Type Properties

The following properties only apply to types.

### ako

Set the supertype of this concept type.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell8" data-toggle="tab">Graql</a></li>
    <li><a href="#java8" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell8">
<pre>
insert gen2-pokemon ako pokemon;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java8">
<pre>
qb.insert(id("gen2-pokemon").ako("pokemon"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### has-role
Add a role to this relation type.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell9" data-toggle="tab">Graql</a></li>
    <li><a href="#java9" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell9">
<pre>
insert
trained-by isa relation-type, has-role trainer, has-role pokemon-trained;

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java9">
<pre>
qb.insert(
  id("trained-by").isa("relation-type")
    .hasRole("trainer").hasRole("pokemon-trained")
);
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### plays-role
Allow the concept type to play the given role.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell10" data-toggle="tab">Graql</a></li>
    <li><a href="#java10" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell10">
<pre>
insert pokemon plays-role pokemon-trained;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java10">
<pre>
qb.insert(id("pokemon").playsRole("pokemon-trained"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### has-resource

Allow the concept type to have the given resource.

This is done by creating a specific relation type relating the concept type
and resource.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell11" data-toggle="tab">Graql</a></li>
    <li><a href="#java11" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell11">
<pre>
insert pokemon has-resource pokedex-no;
</pre>
<p>The above example is equivalent to:</p>
<pre>
insert

has-pokedex-no isa relation-type,
  has-role pokedex-no-owner,
  has-role pokedex-no-value;

pokedex-no-owner isa role-type;
pokedex-no-value isa role-type;

pokemon plays-role pokedex-no-owner;
pokedex-no plays-role pokedex-no-value;
</pre>

</div>
<div role="tabpanel" class="tab-pane" id="java11">
<pre>
qb.insert(id("pokemon").hasResource("pokedex-no"));
</pre>
<p>The above example is equivalent to:</p>
<pre>
qb.insert(
  id("has-pokedex-no").isa("relation-type")
    .hasRole("pokedex-no-owner").hasRole("pokedex-no-value"),

  id("pokedex-no-owner").isa("role-type"),
  id("pokedex-no-value").isa("role-type"),

  id("pokemon").playsRole("pokedex-no-owner"),
  id("pokedex-no").playsRole("pokedex-no-value")
);
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
        <td>v0.1.0.1</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>