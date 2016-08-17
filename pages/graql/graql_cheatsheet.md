---
title: Graql Cheatsheet
keywords: graql
last_updated: August 17, 2016
tags: [graql, getting_started]
summary: "A reference card (cheatsheet) for Graql"
sidebar: home_sidebar
permalink: graql/graql_cheatsheet.html
folder: graql
---

Graql is the declarative query language for MindmapsDB, more about which can be found at [mindmaps.io](http://www.mindmaps.io).  

Key principles and capabilities of Graql are as follows:  
* Graql is a pattern matching language that can search and modify a Mindmaps graph.   
* Graql operates on concepts and relations between them.   
* Graql will automatically execute the query in an efficient fashion, taking advantage of existing indexes.

This page is intended to be a quick reference guide for those already familiar with Graql. For further information and additional documentation, please see the Graql section on our developer portal [mindmaps.io/docs](http://www.mindmaps.io/docs/graql/overview.html).


{% include note.html content="This version of the cheatsheet is for Graql version 0.1.0" %}


## match

A match query will search the graph for any subgraphs that match the given
[pattern](#pattern). A result is produced for each match to the [variables](#variables) in the query. The results of the query can be modified with various [modifiers](#modifiers): [select](#select), [limit](#limit), [offset](#offset), [distinct](#distinct) and [order](#order).

```sql
match  
[ pattern; ... ]  
[ select | limit | offset | distinct | order ... ]
```

Match a pattern in the graph.

```
match $x isa movie
```
Match several patterns together.

```
match
$x isa movie, id "titanic";
(actor $a, $x);
```


### modifiers

#### select
```
select [ variable, ... ]
```
Select particular variables from the query.   

```
match $m isa movie; (actor $a1, $m); (actor $a2, $m);
select $a1, $a2
```

#### limit

```
limit {integer}
```
Limit the number of results returned by a query.

```
match (director $x, $y)
limit 10
```

#### offset

```
offset {integer}
```
Skip some results in a query.

```
match (director $x, $y)
limit 10
offset 20
```

#### distinct

```
distinct
```
De-duplicate the results of a query.

```
match $m isa movie, id 'dr-strangelove'; (actor $a, $m);
select $a
distinct
```

#### order

```
order by variable [ (has resource-type) ] [ asc | desc ]
```
Order by id in ascending order.

```
match $x isa person order by $x
```

Order by a resource in descending order.

```
match $x isa person order by $x(has name) desc
```


## ask

An ask query will return whether the given match query has any results.

```
[match] ask
```
Return whether the match query has any results.

```
match $x isa person, id 'james-cameron'; (actor $x)
ask
```


## insert

An insert query will insert the specified variable patterns into the graph. 


```
insert [ pattern ; ... ]
```
Insert a concept into the graph.

```
insert 'finding-dory' isa movie;
```
If a match query is provided, the query will insert the given variable patterns for every result of the match query.

```
match  insert [ pattern ; ... ]
```

Insert a relation for every result of a match query.

```
match $m isa movie; (director 'tim-burton', $m);
insert (actor 'johnny-depp', production-with-cast $m) isa has-cast;
```


## delete

A delete query will delete the specified variable patterns for every result of the match query. 

```
match  delete [ pattern ; ... ]
```
Delete every instance of a type.

```
match $x isa person;
delete $x;
```


## pattern

```
identifier [ property, ... ]
```
A variable with several properties.

```
$x isa person, value "Guillermo del Toro"
```

Match either the left or right pattern.

```
pattern or pattern
```

```
$x isa movie or $x isa person
```

Match either the left pattern or all the right patterns.

```
{ [ pattern ; ... ] }
```


```
$x isa movie or { (actor $x, $y); $y id 'the-martian'; }
```


### variables

Variables start with a `$`, followed by alphanumeric characters, underscores or dashes.

```
match (director $the-director, $theMovie); $theMovie isa movie;
```



### identifier

An `id` is a sequence of alphanumeric characters, underscores and dashes, or a quoted string.

```
variable | id
```

```
insert "TV Show" isa entity-type; movie isa entity-type;
```



## type properties

Specify the type of a concept.

```
isa type
```

```
match $x isa movie;
```

Match concepts and their types.

```
match $x isa $y;
```

Match the concept with a particular ID.

```
id {string}
```

```
match $x id 'ridley-scott';
```


Match concepts with a value that contains the given string.

```
value [ = | != | < | <= | >= | > | contains ] {value}
```

```
match $m value contains "The Lord of the Rings";
```

Match concepts with a resource matching a predicate.

```
has resource-type [ = | != | < | <= | >= | > | contains ] {value}
```

```
match $m isa movie, has runtime > 180;
```

Match related concepts.

```
( [ [ role-type ] role-player , ... ] )
```

```
match ($x, $y);
```

Match concepts related with a particular relation type.

```
match ($p1, $p2) isa marriage;
```

Match two related concepts where one plays a specified role type.

```
match (director $p, $m);
```


Match concepts in a ternary relation.

```
match (actor $p, character-being-played $c, production-with-cast $m);
```

### ako


```
ako type
```
Insert a new type that is a subtype of an existing type.

```
insert blockbuster ako movie;
```

### has-role

```
has-role role-type
```
Insert a new relation type with two role types.

```
insert
director isa role-type;
production-with-director isa role-type;
directorship isa relation-type, has-role director, has-role production-with-director;
```

### plays-role

```
plays-role role-type
```

Allow instances of a type to play a role in a relation.

```
insert person plays-role director
```

### has-resource

```
has-resource resource-type
```
Allow instances of a type to have a resource.

```
insert person has-resource name;
```

### datatype

```
datatype ( string | long | double | boolean )
```
Insert a new resource type with the given datatype.

```
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
        <td>v1</td>
        <td>17/08/2016</td>
        <td>New page for developer portal.</td>        
    </tr>
    
</table>
