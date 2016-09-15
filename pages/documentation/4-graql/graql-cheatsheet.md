---
title: Graql Cheatsheet
keywords: graql
last_updated: August 17, 2016
tags: [graql]
summary: "A reference card (cheatsheet) for Graql"
sidebar: documentation_sidebar
permalink: /documentation/graql/graql-cheatsheet.html
folder: documentation
---

Graql is the declarative query language for MindmapsDB.  The key principles and capabilities of Graql are as follows:  

* Graql is a pattern matching language that can search and modify a MindmapsDB graph.   
* Graql operates on concepts and relations between them.   
* Graql will automatically execute the query in an efficient fashion, taking advantage of existing indexes.

This page is intended to be a quick reference guide for those already familiar with Graql. For further information and additional documentation, please see the Graql section on our developer portal [mindmaps.io/docs](http://www.mindmaps.io/docs/graql/overview.html).


{% include note.html content="This version of the cheatsheet is for Graql version 0.1.1" %}


## match

A match query will search the graph for any subgraphs that match the given
[pattern](#pattern). A result is produced for each match to the [variables](#variables) in the query. The results of the query can be modified with various [modifiers](#modifiers): [select](#select), [limit](#limit), [offset](#offset), [distinct](#distinct) and [order](#order).

```sql
match  
[ pattern; ... ]  
[ select | limit | offset | distinct | order ... ]
```

Match a pattern in the graph.

```sql
match $x isa movie
```
Match several patterns together.

```sql
match
$x isa movie, id "titanic";
(actor $a, $x);
```


### modifiers

#### select
```sql
select [ variable, ... ]
```
Select particular variables from the query.   

```sql
match $m isa movie; (actor $a1, $m); (actor $a2, $m);
select $a1, $a2
```

#### limit

```sql
limit {integer}
```
Limit the number of results returned by a query.

```sql
match (director $x, $y)
limit 10
```

#### offset

```sql
offset {integer}
```
Skip some results in a query.

```sql
match (director $x, $y)
limit 10
offset 20
```

#### distinct

```sql
distinct
```
De-duplicate the results of a query.

```sql
match $m isa movie, id 'dr-strangelove'; (actor $a, $m);
select $a
distinct
```

#### order

```sql
order by variable [ (has resource-type) ] [ asc | desc ]
```
Order by id in ascending order.

```sql
match $x isa person order by $x
```

Order by a resource in descending order.

```sql
match $x isa person order by $x(has name) desc
```


## ask

An ask query will return whether the given match query has any results.

```sql
[match] ask
```
Return whether the match query has any results.

```sql
match $x isa person, id 'james-cameron'; (actor $x)
ask
```


## insert

An insert query will insert the specified variable patterns into the graph.


```sql
insert [ pattern ; ... ]
```
Insert a concept into the graph.

```sql
insert 'finding-dory' isa movie;
```
If a match query is provided, the query will insert the given variable patterns for every result of the match query.

```sql
match  insert [ pattern ; ... ]
```

Insert a relation for every result of a match query.

```sql
match $m isa movie; (director 'tim-burton', $m);
insert (actor 'johnny-depp', production-with-cast $m) isa has-cast;
```


## delete

A delete query will delete the specified variable patterns for every result of the match query.

```sql
match  delete [ pattern ; ... ]
```
Delete every instance of a type.

```sql
match $x isa person;
delete $x;
```


## pattern

```sql
identifier [ property, ... ]
```
A variable with several properties.

```sql
$x isa person, value "Guillermo del Toro"
```

Match either the left or right pattern.

```sql
pattern or pattern
```

```sql
$x isa movie or $x isa person
```

Match either the left pattern or all the right patterns.

```sql
{ [ pattern ; ... ] }
```


```sql
$x isa movie or { (actor $x, $y); $y id 'the-martian'; }
```


### variables

Variables start with a `$`, followed by alphanumeric characters, underscores or dashes.

```sql
match (director $the-director, $theMovie); $theMovie isa movie;
```



### identifier

An `id` is a sequence of alphanumeric characters, underscores and dashes, or a quoted string.

```sql
variable | id
```

```sql
insert "TV Show" isa entity-type; movie isa entity-type;
```



## type properties

Specify the type of a concept.

```sql
isa type
```

```sql
match $x isa movie;
```

Match concepts and their types.

```sql
match $x isa $y;
```

Match the concept with a particular ID.

```sql
id {string}
```

```sql
match $x id 'ridley-scott';
```


Match concepts with a value that contains the given string.

```sql
value [ = | != | < | <= | >= | > | contains ] {value}
```

```sql
match $m value contains "The Lord of the Rings";
```

Match concepts with a resource matching a predicate.

```sql
has resource-type [ = | != | < | <= | >= | > | contains ] {value}
```

```sql
match $m isa movie, has runtime > 180;
```

Match related concepts.

```sql
( [ [ role-type ] role-player , ... ] )
```

```sql
match ($x, $y);
```

Match concepts related with a particular relation type.

```sql
match ($p1, $p2) isa marriage;
```

Match two related concepts where one plays a specified role type.

```sql
match (director $p, $m);
```


Match concepts in a ternary relation.

```sql
match (actor $p, character-being-played $c, production-with-cast $m);
```

### ako


```sql
ako type
```
Insert a new type that is a subtype of an existing type.

```sql
insert blockbuster ako movie;
```

### has-role

```sql
has-role role-type
```
Insert a new relation type with two role types.

```sql
insert
director isa role-type;
production-with-director isa role-type;
directorship isa relation-type, has-role director, has-role production-with-director;
```

### plays-role

```sql
plays-role role-type
```

Allow instances of a type to play a role in a relation.

```sql
insert person plays-role director
```

### has-resource

```sql
has-resource resource-type
```
Allow instances of a type to have a resource.

```sql
insert person has-resource name;
```

### datatype

```sql
datatype ( string | long | double | boolean )
```
Insert a new resource type with the given datatype.

```sql
insert name isa resource-type, datatype string;
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
        <td>v0.1.1.1</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>
