---
title: Quickstart Tutorial
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql]
summary: "This document will teach you how to use Graql to load a schema and some data into a MindmapsDB Graph."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/quickstart-tutorial.html
folder: documentation
comment_issue_id: 18
---

{% include note.html content="This tutorial shows you how to use the Graql shell. The equivalent code, using the Graph API is described in the [Overview](https://mindmaps.io/pages/documentation/core-api/overview.html) to the Graph API." %}

## Introduction
If you have not yet set up the MindmapsDB environment, please see the [Setup guide](../get-started/setup-guide.html).
You can find the example ```philosophers.gql``` in the examples directory included in the MindmapsDB download zip.

We are going model how philosophers are related to each other. If you are using Graql and don't want to type everything below to build up the schema, you can load it using:  

```bash
bin/graql.sh -f examples/philosophers.gql
```

By the way, don't worry if you've already loaded some different data into your graph. A MindmapsDB graph can simultaneously contain as many schemas as you want.

## Concepts

We'll first add an entity type `person`:

```bash
bin/graql.sh
>>>insert person isa entity-type;
```

...and now some data in the form of concept instances: in our case, a bunch of ancient Greeks...

```sql
>>>insert "Socrates" isa person;
>>>insert "Plato" isa person;
>>>insert "Aristotle" isa person;
>>>insert "Alexander" isa person;
```

![](/images/phil.png)

So now we've created a simple graph, with one concept type and four
concept instances.

{% include note.html content="Every concept needs a type, using `isa`. <br/>   `Plato` `is a` `person`   <br/>  `person` `is a` `entity-type`" %}


{% include tip.html content="Graql shell maintains a history of past commands with the 'up' and 'down' arrows. You can also autocomplete keywords, type and variable names using tab!" %}

We'll quickly check that our data has loaded:

```sql
>>>match $p isa person

$p id "Aristotle" isa person;
$p id "Plato" isa person;
$p id "Socrates" isa person;
$p id "Alexander" isa person;
```

{% include note.html content="Graql variables start with a `$`. They represent wildcards, and are returned as results in `match` queries. A variable name can contain alphanumeric characters, dashes and underscores." %}

Next, let's add some `schools` of thought:

```sql
>>>insert school isa entity-type;
>>>insert "Peripateticism" isa school;
>>>insert "Platonism" isa school;
>>>insert "Idealism" isa school;
>>>insert "Cynicism" isa school;
```

And look up one:

```sql
>>>match $cyn id "Cynicism"

$cyn id "Cynicism" isa school;
```


## Relations

How do we define a "philosopher"? Very smart people have argued for a very long time about this, but we're going to put our fingers in our ears and say a philosopher is someone who practices a philosophy.

A relation comprises of pairs of roles and role players. Relations and roles also have types, just like normal concepts.  

First, we define a `relation-type` called `practice` that relates a
`role-type` called `philosopher` to a `role-type` called `philosophy`:

```sql
>>>insert practice isa relation-type;
>>>insert philosopher isa role-type;
>>>insert philosophy isa role-type;
>>>insert practice has-role philosopher, has-role philosophy;
```

{% include note.html content="Commas are totally optional in query patterns, so these pairs are equivalent:<br /> ```
>>>insert practice has-role philosopher, has-role philosophy;
>>>insert practice has-role philosopher has-role philosophy;
and  
>>>insert practice, isa relation-type;
>>>insert practice isa relation-type;
```" %}


A `person` can have the role of `philosopher` and a `school` can have the role of `philosophy`:

```sql
>>>insert person plays-role philosopher;
>>>insert school plays-role philosophy;
```

{% include warning.html content="The changes you've made to the graph haven't been saved yet!  <br /> Type `commit` in the Graql shell to commit any changes." %}

Now let's relate some `philosophers` to their `philosophies`:

```sql
>>>insert (philosopher "Socrates", philosophy "Platonism") isa practice;
>>>insert (philosopher "Plato", philosophy "Idealism") isa practice;
>>>insert (philosopher "Plato", philosophy "Platonism") isa practice;
>>>insert (philosopher "Aristotle", philosophy "Peripateticism") isa practice;
```

![](/images/practice.png)

Here, `Plato` is playing the role of `philosopher`, and `Idealism` is playing the role of `philosophy`.

Now we can query for all our Platonists:

```sql
>>>match (philosopher $phil, "Platonism") isa practice;

$phil id "Socrates" isa person;
$phil id "Plato" isa person;
```

{% include note.html content="This query can be read as 'Get me philosophers in a practice relationship with Platonism'.  <br/> We didn't specify the role of `Platonism` in this query, or the type of `$phil`, which is totally fine! <br /> Roles and types can be omitted in queries. <br/> For example, the query `match ($x, $y)` is a valid query (that will find *everything* in a relationship with *anything*)." %}

Next let's talk about the relationships between our philosophers. Socrates
taught Plato, Plato taught Aristotle and Aristotle even taught Alexander the
Great!

First, extend our schema:

```sql
>>>insert education isa relation-type;
>>>insert teacher isa role-type;
>>>insert student isa role-type;
>>>insert education has-role teacher, has-role student;
>>>insert person plays-role teacher, plays-role student;
```

Second, our data:

```sql
>>>insert (teacher "Socrates", student "Plato") isa education;
>>>insert (teacher "Plato", student "Aristotle") isa education;
>>>insert (teacher "Aristotle", student "Alexander") isa education;
```

{% include tip.html content="**Test Yourself** <br /> Try writing a query to see who taught Aristotle. Include all the roles and types (`teacher`, `student` and `education`), then remove them one by one to see when the results change! The answer is at the bottom of this page." %}

## Resources

Some people have special titles and epithets and we want to talk about that.
So, we'll create some resource types that can be attached to a person:

```sql
>>>insert title isa resource-type, datatype string;
>>>insert epithet isa resource-type, datatype string;
>>>insert person has-resource title, has-resource epithet;
```


Let's make Alexander "Great"!

```sql
>>>insert "Alexander" has epithet "The Great";
```


This is a quick way to add a relation between `Alexander` and an `epithet` with value `"The Great"`.

![](/images/epithet.png)

Let's add the rest of Alexander's titles while we're at it:

```sql
>>>insert "Alexander" has title "Hegemon";
>>>insert "Alexander" has title "King of Macedon";
>>>insert "Alexander" has title "King of Persia";
>>>insert "Alexander" has title "Pharaoh of Egypt";
>>>insert "Alexander" has title "Lord of Asia";
```


Using Graql, we can query for people, listing their id and titles.

```sql
>>>match $x isa person select $x(id, has title)

$x id "Socrates";
$x id "Plato";
$x id "Aristotle";
$x id "Alexander" has title "Pharaoh of Egypt" has title "Hegeon" has title "Shah of Persia" has title "King of Macedon" has title "Lord of Asia";
```

Wait, who's the Pharaoh again?

```sql
>>>match $pharaoh has title "Pharaoh of Egypt"

$pharaoh id "Alexander" isa person;
```


{% include tip.html content="**Test Yourself** <br /> When querying for an id, value or resource you can use predicates as well as direct values. For example, has epithet contains 'Great'. See if you can write a query for everyone with a title containing 'King'. The answer is at the bottom of the page." %}

## Relations as Role Players

Philosophers know lots of things. We should probably include this in our
schema.

```sql
>>>insert knowledge isa relation-type;
>>>insert thinker isa role-type;
>>>insert thought isa role-type;
>>>insert knowledge has-role thinker, has-role thought;
>>>insert fact isa entity-type, plays-role thought;
>>>insert person plays-role thinker;
```

Aristotle knew some astronomy, Plato knew a lot about caves and Socrates didn't really know anything at all.

```sql
>>>insert "sun-fact" isa fact, value "The Sun is bigger than the Earth";
>>>insert (thinker "Aristotle", thought "sun-fact") isa knowledge;
>>>insert "cave-fact" isa fact, value "Caves are mostly pretty dark";
>>>insert (thinker "Plato", thought "cave-fact") isa knowledge;
>>>insert "nothing" isa fact;
>>>insert (thinker "Socrates", thought "nothing") isa knowledge;
```

A relation is actually just a special kind of instance. Just as
`Socrates` can be a role player, relations themselves can also be role players.

For example, Socrates knew nothing, but he also *knew* that he knew nothing!

First, we have to state that someone can think about their own knowledge:

```sql
>>>insert knowledge plays-role thought;
```

We can now give Socrates one final piece of knowledge:

```sql
>>>match $socratesKnowsNothing ("Socrates", "nothing") insert (thinker "Socrates", thought $socratesKnowsNothing) isa knowledge
```

Here, `socratesKnowsNothing` is the relation between `Socrates` and `nothing`.
We then put `socratesKnowsNothing` in a *new* `knowledge` relation as a role
player.

Finally, we'll check out everything Socrates knows:

```sql
>>>match ("Socrates", $x) isa knowledge

$x id "nothing" isa fact;
$x id "knowledge-e387d27c-4f5e-11e6-beb8-9e71128cae77" isa knowledge;
```

{% include note.html content="If you don't provide an ID for something such as a relation, it will get an automatically generated ID." %}

![](/images/knowledge.png)


## Test Yourself: Answers
We asked you to write a query to see who taught Aristotle.   
Graql answers:

```sql
match (teacher $teach, $student Aristotle) isa education
```

We asked you to write a query for everyone with a title containing "King".   
Answer:  

```sql
match $king has title contains "King"
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
        <td>v0.1.0</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>

## Comments
Want to leave a comment? Visit <a href="https://github.com/mindmapsdb/docs/issues/18" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.