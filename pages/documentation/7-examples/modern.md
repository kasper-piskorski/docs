---
title: Modern Grakn Example
keywords: graql, query
last_updated: September 19, 2016
tags: [graql]
summary: "A short example to illustrate Graql queries"
sidebar: documentation_sidebar
permalink: /documentation/examples/modern.html
folder: documentation
comment_issue_id: 26
---

If you have not yet set up the Grakn environment, please see the [Setup guide](../get-started/setup-guide.html). For a comprehensive guide to all Graql keywords, please see the [Graql documentation](https://grakn.ai/pages/documentation/graql/overview.html).

## Introduction
We have a few examples on how to work with Graql using different datasets. For example, see our [blog posts](https://medium.com/p/e1125e02dc85) and the [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) that introduces how to make Graql queries.

This example takes a very simple example from [TinkerPop3 Documentation](http://tinkerpop.apache.org/docs/3.0.1-incubating/), called TinkerPop Modern. Here it is, shown diagrammatically:

![](/images/example-tinkerpop-modern.png)

The image above is used from the documentation provided for TinkerPop3, and licensed by the [Apache Software Foundation](http://www.apache.org). 

We have chosen this example as it may already be familiar, and is simple enough to demonstrate some of the fundamentals of Graql. We walk through the entities ("things") and relationships between them and show how to represent them using Graql to define a schema. We then use Graql to add the data to the graph.  The main purpose of this example, however, is to use it for practice at making sample queries on the graph. 

### Starting Graql

If it is not already running, start Grakn, then open a Graql shell:

```bash
cd [your Grakn install directory]
bin/mindmaps.sh start
bin/graql.sh
```

## Defining a Schema

In the example, we have a number of entities: 

* people (marko, vadas, josh and peter), each of which has an age.    
* software (lop and ripple), each of which has an associated programming language. 

The relationships between the entities are straightforward: 

* there are two connections between marko and other people who he "knows" (josh and vadas). 
* there are four connections between people and software, such that the people "created" the software. Lop was created by marko, peter and josh, which ripple was created by josh.

In this example, we use the name of the person or software to make up their id, as in real life, rather than ascribe them the numbers used in the diagram.  

### Entity Types

Here, we add person and software entity types to the graph, via the Graql shell:

```graql
insert person isa entity-type;
insert software isa entity-type;
```


### Resource Types

To assign resources to the entities, which you can think of as attributes, we use resource types. First, we define what they are (age is a number, programming language is a string that represents the language's name), then we allocate them to the entity in question:

```graql
insert age isa resource-type, datatype long;
insert person has-resource age;

insert lang isa resource-type, datatype string;
insert software has-resource lang;

insert weight isa resource-type, datatype double;
```

### Relation Types

Let's first define the relationship between people. The diagram shows that marko knows vadas, but we don't have any information about whether the inverse is true (though it seems likely that vadas probably also knows marko). Let's set up a relationship called `knows`, which has two role-types - `knower` (for marko) and `known-about` (for vadas):

```graql
insert knower isa role-type;
insert known-about isa role-type;
insert person plays-role knower;
insert person plays-role known-about;
insert knows isa relation-type, has-role knower, has-role known-about, has-resource weight;	
```

Note that the  `knows` relation type also has an attribute, in the form of a resource called `weight` (though it's not clear from the TinkerPop example what this represents).

We can set up a similar relationship between software and the people that created it:

```graql
insert programmer isa role-type;
insert programmed isa role-type;

insert person plays-role programmer;
insert software plays-role programmed;

insert programming isa relation-type, has-role programmer, has-role programmed, has-resource weight;
```

And that's it. At this point, we have defined the schema of the graph.

## Adding the Data
Now we have a schema, we can move on to adding in the data, which is pretty much just a typing exercise:

```graql
insert "marko" isa person;
insert "vadas" isa person;
insert "josh" isa person;
insert "peter" isa person;
insert "marko" has age 29;
insert "josh" has age 32;
insert "vadas" has age 27;
insert "peter" has age 35;
insert (knower: "marko", known-about: "josh") isa knows has weight 1.0;
insert (knower: "marko", known-about: "vadas") isa knows has weight 0.5;
```


```graql
insert "lop" isa software;
insert "ripple" isa software;
insert "lop" has lang "java";
insert "ripple" has lang "java";
insert (programmer: "marko", programmed: "lop") isa programming has weight 0.4;
insert (programmer: "peter", programmed: "lop") isa programming has weight 0.2;
insert (programmer: "josh", programmed: "lop") isa programming has weight 0.4;
insert (programmer: "josh", programmed: "ripple") isa programming has weight 1.0;
```
   
   
## Querying

This example is designed to get you up close and personal with Graql queries. It will run through a few basic examples, then ask you a set of "Test Yourself" questions. The answers are available at the end of the page, but please don't look at them immediately! The best way to find out how much you understand about using Graql is to challenge yourself to work out the queries without a prompt. 

OK, so if you've followed the above, you should now have a schema and some data in a graph. How do you go about using the graph to answer your queries? That's where the `match` statement comes in. 

As with any query language, you use a variable to receive the results of the match query, which you must prefix with a `$`. So, to make the query "List every person in the graph", you would use the following in Graql:

```graql
match $x isa person;

$x id "marko" isa person; 
$x id "vadas" isa person; 
$x id "josh" isa person; 
$x id "peter" isa person; 
```
 
In Graql, a match is formed of three parts: the `match` statement, an optional `select` statement, and any other optional [modifiers](../graql/match-queries.html#modifiers) that you choose to apply to the listing of results. Only the first part of a match query is needed: the modifier parts are optional.   

In the `match $x isa person` query we are not using any select or delimiters, so let's add some now.  We can add a `select` statement to ask Graql to list out every person and to include their id (which is their name) and age. We use `order by` to modify how the results are listed out - in this case, we order them by ascending age, so the youngest person is shown first.

```graql
>>> match $x isa person, has age $a; select $x, $a; order by $a asc;

$x id "vadas" has age "27"; 
$x id "marko" has age "29"; 
$x id "josh" has age "32"; 
$x id "peter" has age "35";
```

Now let's look at querying for a relationship. We can query to find out which entities have a particular role-type associated with a relation-type. For example, we can match everyone with a `knower` role who knows someone else:

```graql
match $x plays-role knows; 
```

We can also match every role that a `person` can play:
```graql
match person plays-role $x;
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


## Complete Example
Here is the complete example - the code to define the schema and insert the data into a graph. You can load this directly into Graql, if you don't want to type it out for yourself. Cut and paste the Graql below and start Graql:

```bash
bin/graql.sh
```

Then type edit, which will open up the systems default text editor where you can paste your chunk of text. Upon exiting the editor, the Graql will execute.


```graql 
insert 
age isa resource-type
	datatype long;
person isa entity-type;
person has-resource age;

"marko" isa person;
"vadas" isa person;
"josh" isa person;
"peter" isa person;
"marko" has age 29;
"josh" has age 32;
"vadas" has age 27;
"peter" has age 35;

weight isa resource-type
	datatype double;

knower isa role-type;
known-about isa role-type;

person plays-role knower;
person plays-role known-about;

knows isa relation-type
	has-role knower
	has-role known-about
	has-resource weight;

(knower: "marko", known-about: "josh") isa knows has weight 1.0;
(knower: "marko", known-about: "vadas") isa knows has weight 0.5;

lang isa resource-type
	datatype string;
software isa entity-type;
software has-resource lang;

"lop" isa software;
"ripple" isa software;

"lop" has lang "java";
"ripple" has lang "java";

programmer isa role-type;
programmed isa role-type;

person plays-role programmer;
software plays-role programmed;

programming isa relation-type
	has-role programmer
	has-role programmed
	has-resource weight;

(programmer: "marko", programmed: "lop") isa programming has weight 0.4;
(programmer: "peter", programmed: "lop") isa programming has weight 0.2;
(programmer: "josh", programmed: "lop") isa programming has weight 0.4;
(programmer: "josh", programmed: "ripple") isa programming has weight 1.0;
```   

## Answers to the Test Yourself section

Here are our solutions to the queries we asked you to form. Please do have a go at them yourself first, before you peek below!

List every person with their name and age in ascending age order

```graql
match $x isa person, has age $a; select $x, $a; order by $a asc;
```

List every person who has an age over 30, showing their name and age

```graql
match $x isa person, has age $a, has age > 30; select $x, $a;
```

List every person who knows someone else, and every person who is known about

```graql
match (knower: $x) isa knows;
match (known-about: $x) isa knows;
```

List every person that marko knows

```graql
match (known-about: $x, "marko") isa knows;
```

List every item of software and the language associated with it

```graql
match $x isa software, has lang $lang;
```

List everything that josh has programmed

```graql
match (programmed: $x, "josh") isa programming;
```

List everyone who has programmed Lop

```graql
match (programmer: $x, "lop") isa programming;
```

List everything you know about marko

```graql
match $relation("marko", $y); $y isa $z; $z isa entity-type; $relation isa $reltype; select $y, $reltype;

```


## Where next?

This example should give you some confidence at looking at the basic types and queries used in a MindmapDB graph. Take a look at the landing page for the Grakn Examples, for more complex examples, such as the Moogi movies dataset.

{% include links.html %}


## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/26" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.

