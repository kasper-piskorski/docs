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

## `match-insert`
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
match
$pk has name "Pikachu";
insert
$p isa pokemon, has name "Pichu", has pokedex-no 172;
(descendent: $pk, ancestor: $p) isa evolution;

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(
    var("pk").has("name", "Pikachu")
).insert(
    var("p").isa("pokemon").has("name", "Pichu").has("pokedex-no", 172),
    var().isa("evolution")
        .rel("descendent", "pk")
        .rel("ancestor", "p")
).execute();

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
insert has name "Totodile" isa pokemon;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java2">
<pre>
qb.insert(var().has("name", "Totodile").isa("pokemon"));
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
insert has name "Pikachu" isa pokemon;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java3">
<pre>
qb.insert(var().has("name", "Pikachu").isa("pokemon"));
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
insert value "Ash" isa name;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java4">
<pre>
qb.insert(var().value("Ash").isa("name"));
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
insert isa pokemon, has name "Pichu" has height 30;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java5">
<pre>
qb.insert(var().isa("pokemon").has("name", "Pichu").has("height", 30));
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
$owner isa pokemon, has name "Pichu";
$value isa height, value 30;

(has-height-owner: $owner, has-height-value: $value) isa has-height;

</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java6">
<pre>
qb.insert(
  var("owner").isa("pokemon").has("name", "Pichu"),
  var("value").isa("height").value(30),
  var().rel("has-height-owner", "owner").rel("has-height-value", "value").isa("has-height")
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
match
$p has name "Pichu";
$e has name "electric";
insert
(pokemon-with-type: $p, type-of-pokemon: $e) isa has-type;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java7">
<pre>
qb.match(
  var("p").has("name", "Pichu"),
  var("e").has("name", "electric")
).insert(
  var()
    .rel("pokemon-with-type", "p")
    .rel("type-of-pokemon", "e")
    .isa("has-type")
);

</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


## Type Properties

The following properties only apply to types.

### sub

Set up the hierarchy.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell8" data-toggle="tab">Graql</a></li>
    <li><a href="#java8" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell8">
<pre>
insert gen2-pokemon sub pokemon;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java8">
<pre>
qb.insert(name("gen2-pokemon").sub("pokemon"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### has-role
Add a role to this relation.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell9" data-toggle="tab">Graql</a></li>
    <li><a href="#java9" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell9">
<pre>
insert trained-by sub relation, has-role trainer, has-role pokemon-trained;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java9">
<pre>
qb.insert(
  name("trained-by").sub("relation")
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
qb.insert(name("pokemon").playsRole("pokemon-trained"));
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


### has-resource

Allow the concept type to have the given resource.

This is done by creating a specific relation relating the concept and resource.

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

has-pokedex-no sub relation,
  has-role has-pokedex-no-owner,
  has-role has-pokedex-no-value;

has-pokedex-no-owner sub role;
has-pokedex-no-value sub role;

pokemon plays-role has-pokedex-no-owner;
pokedex-no plays-role has-pokedex-no-value;
</pre>

</div>
<div role="tabpanel" class="tab-pane" id="java11">
<pre>
qb.insert(name("pokemon").hasResource("pokedex-no"));
</pre>
<p>The above example is equivalent to:</p>
<pre>
qb.insert(
  name("has-pokedex-no").sub("relation")
    .hasRole("has-pokedex-no-owner").hasRole("has-pokedex-no-value"),

  name("has-pokedex-no-owner").sub("role"),
  name("has-pokedex-no-value").sub("role"),

  name("pokemon").playsRole("has-pokedex-no-owner"),
  name("pokedex-no").playsRole("has-pokedex-no-value")
);
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/42" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.


{% include links.html %}
