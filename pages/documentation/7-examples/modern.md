---
title: Test your Graql
keywords: graql, query
last_updated: September 19, 2016
tags: [graql]
summary: "A short example to illustrate Graql queries"
sidebar: documentation_sidebar
permalink: /documentation/examples/modern.html
folder: documentation
---

If you have not yet set up the MindmapsDB environment, please see the [Setup guide](../get-started/setup-guide.html). For a comprehensive guide to all Graql keywords, please see the [Graql documentation](https://mindmaps.io/pages/documentation/graql/overview.html).

## Introduction
We have a few examples on how to work with Graql using different datasets. For example, see our [blog posts](https://medium.com/p/e1125e02dc85) and the [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) that introduces how to make Graql queries.

This example takes a very simple example from [TinkerPop3 Documentation](http://tinkerpop.apache.org/docs/3.0.1-incubating/), called TinkerPop Modern. Here it is, shown diagrammatically:

![](/images/example-tinkerpop-modern.png)

The image above is used from the documentation provided for TinkerPop3, and licensed by the [Apache Software Foundation](http://www.apache.org). 


We have chosen this example as it may already be familiar, and is simple enough to demonstrate some of the fundamentals of Graql. We walk through the entities ("things") and relationships between them and show how to represent them in a MindmapsDB graph using Graql to define a schema. We then use Graql to add the data to the graph.  The main purpose of this example, however, is to use it for practice at making sample queries on the graph. 

## Defining a Schema

In the example, we have a number of entities: 

* people (marko, vadas, josh and peter), each of which has an age.    
* software (lop and ripple), each of which has an associated programming language. 

The relationships between the entities are straightforward: 

* there are two connections between marko and other people who he "knows" (josh and vadas). 
* there are four connections between people and software, such that the people "created" the software. Lop was created by marko, peter and josh, which ripple was created by josh.

In this example, we use the name of the person or software to make up their id, rather than ascribe them numbers (as shown in the diagram). We will not use the "weight" along each connection as it is shown in the diagram.

### Entity Types

Here, we add person and software entity types:

```sql
insert person isa entity-type;
insert software isa entity-type;
```


### Resource Types

To assign resources, which you can think of as attributes, to the entities, we use resource types. First, we define what they are (age is a number, programming language is a string, which represents the language's name), then we allocate them to the entity in question:

```sql
insert age isa resource-type, datatype long;
insert person has-resource age;

insert lang isa resource-type, datatype string;
insert software has-resource lang;
```

### Relation Types

Let's first define the relationship between people. The diagram shows that marko knows vadas, but we don't have any information about whether the inverse is true (though it seems likely that vadas probably also knows marko). Let's set up a relationship called `knows`, which has two role-types (`knower` (for marko) and `known-about` (for vadas):

```sql
insert knows isa relation-type;
insert knower isa role-type;
insert known-about isa role-type
insert knows has-role knower, has-role known-about;
insert person plays-role knower;
insert person plays-role known-about;
```

We can set up a similar relationship between software and the people that created it:

```sql
insert programming isa relation-type;
insert programmer isa role-type;
insert programmed isa role-type;
insert programming has-role programmer, has-role programmed;
insert person plays-role programmer;
insert software plays-role programmed;
```

And that's it. At this point, we have defined the schema of the MindmapsDB graph.

## Adding the Data
Now we have a schema, we can move on to adding in the data, which is pretty much just a typing exercise:

```sql
insert "marko" isa person;
insert "vadas" isa person;
insert "josh" isa person;
insert "peter" isa person;
insert "marko" has age 29;
insert "josh" has age 32;
insert "vadas" has age 27;
insert "peter" has age 35;
insert (knower "marko", known-about "josh") isa knows;
insert (knower "marko", known-about "vadas") isa knows;
```


```sql
insert "lop" isa software;
insert "ripple" isa software;
insert "lop" has lang "java";
insert "ripple" has lang "java";
insert (programmer "marko", programmed "lop") isa programming;
insert (programmer "peter", programmed "lop") isa programming;
insert (programmer "josh", programmed "lop") isa programming;
insert (programmer "josh", programmed "ripple") isa programming;
```
   
   
## Querying

This example is designed to make you get up close and personal with Graql queries. It will run through a few basic examples, then ask you a set of "Test Yourself" questions. The answers are available at the end of the page, but please don't look at them immediately! The best way to find out how much you understand about using Graql is to challenge yourself to work out the queries without a prompt. 

OK, so if you've followed the above, you should now have a schema and some data in a graph. How do you go about using the graph to answer your queries? That's where the `match` statement comes in. 

As with any query language, you use a variable to receive the results of the match query, which you must prefix with a `$`. So, to make the query "List every person in the graph", you would use the following in Graql:

```sql
match $x isa person

$x id "marko" isa person; 
$x id "vadas" isa person; 
$x id "josh" isa person; 
$x id "peter" isa person; 
```
 
In Graql, a match is formed of three parts: the `match` statement, an optional `select` statement, and any optional [modifiers](../graql/match-queries.html#modifiers) that you choose to apply to the listing of results. Only the first part of a match query is actually needed: the select and modifier parts are optional.   In the above query we are not using any select or delimiters, so let's show how to add them now.

In the query below, we add a `select` statement to ask Graql to list out every person and to include their id (which is their name) and age. We use `order by` to modify how the results are listed out - in this case, we order them by ascending age, so the youngest person is shown first.

```sql
match $x isa person select $x(id, has age) order by $x(has age) asc

$x id "vadas" has age "27"; 
$x id "marko" has age "29"; 
$x id "josh" has age "32"; 
$x id "peter" has age "35";
```

Now let's look at querying for a relationship. We can query to find out which entities have a particular role-type associated with a relation-type. For example, we can match everyone with a `knower` role who knows someone else:

```sql
match (knower $x) isa knows; 
```

## Test Yourself

1. List every person with their name and age in ascending age order


2. List every person who has an age over 30


3. List every person who knows someone else, and every person who is known about


4. List every person that Marko knows


5. List every item of software and the language associated with it


6. List everything that Josh has programmed


7. List everyone who has programmed Lop


8. List everything you know about Marko


9. List everything you know about every person



## Complete Example
Here is the complete example - the code to define the Schema and insert the data into a graph, followed by the queries for the Test Yourself section.


```sql 

insert age isa resource-type, datatype long;
insert person isa entity-type;
insert person has-resource age;

insert "marko" isa person;
insert "vadas" isa person;
insert "josh" isa person;
insert "peter" isa person;
insert "marko" has age 29;
insert "josh" has age 32;
insert "vadas" has age 27;
insert "peter" has age 35;

insert knows isa relation-type;
insert knower isa role-type;
insert known-about isa role-type

insert knows has-role knower, has-role known-about;
insert person plays-role knower;
insert person plays-role known-about;

insert (knower "marko", known-about "josh") isa knows;
insert (knower "marko", known-about "vadas") isa knows;

insert lang isa resource-type, datatype string;
insert software isa entity-type;
insert software has-resource lang;

insert "lop" isa software;
insert "ripple" isa software;

insert "lop" has lang "java";
insert "ripple" has lang "java";

insert programming isa relation-type;
insert programmer isa role-type;
insert programmed isa role-type;
insert programming has-role programmer, has-role programmed;
insert person plays-role programmer;
insert software plays-role programmed;

insert (programmer "marko", programmed "lop") isa programming;
insert (programmer "peter", programmed "lop") isa programming;
insert (programmer "josh", programmed "lop") isa programming;
insert (programmer "josh", programmed "ripple") isa programming;

```   

## Answers to the Test Yourself section

Here are our solutions to the queries we asked you to form. Please do have a go at them yourself first, before you peek below!

1. List every person with their name and age in ascending age order

```sql
match $x isa person select $x(id, has age) order by $x(has age) asc
```

2. List every person who has an age over 30, showing their name and age

```sql
match $x isa person, has age > 30, select $x(id, has age)
```

3. List every person who knows someone else, and every person who is known about

```sql
match (knower $x) isa knows; 
match (known-about $x) isa knows;
```

4. List every person that Marko knows

```sql
match (known-about $x, "marko") isa knows;
```

5. List every item of software and the language associated with it

```sql
match $x isa software select $x(id, has lang)
```

6. List everything that Josh has programmed

```sql
match (programmed $x, "josh") isa programming
```

7. List everyone who has programmed Lop

```sql
match (programmer $x, "lop") isa programming
```

8. List everything you know about Marko

```sql
AARRRRRGH!

```

9. List everything you know about every person

```sql
AARRRRRGH!AARRRRRGH!AARRRRRGH!
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
        <td>v0.1.1</td>
        <td>19/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>



