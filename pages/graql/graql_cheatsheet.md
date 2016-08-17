---
title: Graql Cheatsheet
keywords: graql
last_updated: August 11, 2016
tags: [graql]
summary: "A reference card (cheatsheet) for Graql"
sidebar: home_sidebar
permalink: graql_cheatsheet.html
folder: graql
---

Graql is the declarative query language for MindmapsDB, more about which can be found at [mindmaps.io](http://www.mindmaps.io).  

Key principles and capabilities of Graql are as follows:  
* Graql is a pattern matching language that can search and modify a Mindmaps graph.
* Graql operates on concepts and relations between them.
* Graql will automatically execute the query in an efficient fashion, taking advantage of existing indexes.

Additional documentation about MindmapsDB and Graql is available on our developer documentation portal [mindmaps.io/docs](http://www.mindmaps.io/docs).


{% include note.html content="This version of the cheatsheet is for Graql version 0.1.0" %}


## match

<big><pre>
match
[ [pattern](#pattern) ; ... ]
\[ [select | limit | offset | distinct | order ...](#modifier) \]</pre></big>

```
match $x isa movie
```
Match a pattern in the graph.

```
match
$x isa movie, id "titanic";
(actor $a, $x);
```
Match several patterns together.

### modifier

<big><pre>
select \[ [variable](#variable) , ... \]</pre></big>

```
match $m isa movie; (actor $a1, $m); (actor $a2, $m);
select $a1, $a2
```
Select particular variables from the query.

---

<big><pre>
limit {integer}</pre></big>

```
match (director $x, $y)
limit 10
```
Limit the results of a query.

---

<big><pre>
offset {integer}</pre></big>

```
match (director $x, $y)
limit 10
offset 20
```
Skip some results in a query.

---

<big><pre>
distinct</pre></big>

```match $m isa movie, id 'dr-strangelove'; (actor $a, $m);
select $a
distinct
```
De-duplicate the results of a query.

---

<big><pre>
order by [variable](#variable) \[ (has [resource-type](#identifier)) \] \[ asc | desc \]</pre></big>

```
match $x isa person order by $x
```
Order by id in ascending order.

```
match $x isa person order by $x(has name) desc
```
Order by a resource in descending order.

## ask

<big><pre>
[match](#match) ask</pre></big>

```
match $x isa person, id 'james-cameron'; (actor $x)
ask
```
Return whether the match query has any results.

## insert

<big><pre>
insert \[ [pattern](#pattern) ; ... \]</pre></big>

```
insert 'finding-dory' isa movie;
```
Insert a concept into the graph.

---

<big><pre>
[match](#match) insert \[ [pattern](#pattern) ; ... \]</pre></big>

```
match $m isa movie; (director 'tim-burton', $m);
insert (actor 'johnny-depp', production-with-cast $m) isa has-cast;
```
Insert a relation for every result of a match query.

## delete

<big><pre>
[match](#match) delete \[ [pattern](#pattern) ; ... \]</pre></big>

```
match $x isa person;
delete $x;
```
Delete every instance of a type.

## pattern

<big><pre>
[identifier](#identifier) \[ [property](#property) , ... \]</pre></big>

```
$x isa person, value "Guillermo del Toro"
```
A variable with several properties.

---

<big><pre>
[pattern](#pattern) or [pattern](#pattern)</pre></big>

```
$x isa movie or $x isa person
```
Match either the left or right pattern.

---

<big><pre>
{ \[ [pattern](#pattern) ; ... \] }</pre></big>

```
$x isa movie or { (actor $x, $y); $y id 'the-martian'; }
```
Match either the left pattern or all the right patterns.

### variable

```
match (director $the-director, $theMovie); $theMovie isa movie;
```

Variables start with a `$`, followed by alphanumeric characters, underscores or dashes.

### identifier

<big><pre>
[variable](#variable) | [id](#identifier)</pre></big>

```
insert "TV Show" isa entity-type; movie isa entity-type;
```

An `id` is a sequence of alphanumeric characters, underscores and dashes, or a quoted string.

### property

<big><pre>
isa [type](#identifier)</pre></big>

```
match $x isa movie;
```
Specify the type of a concept.

```
match $x isa $y;
```
Match concepts and their types.

---

<big><pre>
id {string}</pre></big>

```
match $x id 'ridley-scott';
```
Match the concept with a particular ID.

---

<big><pre>
value [ = | != | < | <= | >= | > | contains ] {value}</pre></big>

```
match $m value contains "The Lord of the Rings";
```
Match concepts with a value that contains the given string.

---

<big><pre>
has [resource-type](#identifier) [ = | != | < | <= | >= | > | contains ] {value}</pre></big>

```
match $m isa movie, has runtime > 180;
```
Match concepts with a resource matching a predicate.

---

<big><pre>
( \[ \[ [role-type](#identifier) \] [role-player](#identifier) , ... \] )</pre></big>

```
match ($x, $y);
```
Match related concepts.

```
match ($p1, $p2) isa marriage;
```
Match concepts related with a particular relation type.

```
match (director $p, $m);
```
Match two related concepts where one plays a specified role type.

```
match (actor $p, character-being-played $c, production-with-cast $m);
```
Match concepts in a ternary relation.

---

<big><pre>
ako [type](#identifier)</pre></big>

```
insert blockbuster ako movie;
```
Insert a new type that is a subtype of an existing type.

---

<big><pre>
has-role [role-type](#identifier)</pre></big>

```
insert
director isa role-type;
production-with-director isa role-type;
directorship isa relation-type, has-role director, has-role production-with-director;
```
Insert a new relation type with two role types.

---

<big><pre>
plays-role [role-type](#identifier)</pre></big>

```
insert person plays-role director
```
Allow instances of a type to play a role in a relation.

---

<big><pre>
datatype ( string | long | double | boolean )</pre></big>

```
insert name isa resource-type, datatype string;
```
Insert a new resource type with the given datatype.

---

<big><pre>
has-resource [resource-type](#identifier)</pre></big>

```
insert person has-resource name;
```
Allow instances of a type to have a resource.

## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
    <tr>
        <td>v1</td>
        <td>11/08/2016</td>
        <td>New page for developer portal.</td>        
    </tr>
    
</table>
