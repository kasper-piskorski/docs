---
title: Quickstart Tutorial
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql]
summary: "This document will teach you how to use Graql to load a schema and some data into Grakn."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/quickstart-tutorial.html
folder: documentation
comment_issue_id: 18
---

{% include note.html content="This tutorial shows you how to use the Graql shell. The equivalent code, using the Graph API is described in the [Overview](https://grakn.ai/pages/documentation/core-api/overview.html) to the Graph API." %}

## Introduction
If you have not yet set up the Grakn environment, please see the [Setup guide](../get-started/setup-guide.html).
You can find the example ```philosophers.gql``` in the examples directory included in the Grakn download zip.

We are going model how philosophers are related to each other. If you are using Graql and don't want to type everything below to build up the schema, you can load it using:  

```bash
bin/graql.sh -f examples/philosophers.gql
```

By the way, don't worry if you've already loaded some different data into your graph. In Grakn, a graph can simultaneously contain as many schemas as you want.

## Concepts

Assuming that you are building up the graph from scratch, rather than loading it directly from the `philosophers.gql` file, let's first add an entity type `person`:

```bash
bin/graql.sh
```
```graql
insert person sub entity;
```

And a resource for people's names:

```graql
insert name sub resource-type, datatype string;
insert person has-resource name;
```

...and now some data in the form of concept instances: in our case, a bunch of ancient Greeks...

```graql
insert isa person, has name "Socrates";
insert isa person, has name "Plato";
insert isa person, has name "Aristotle";
insert isa person, has name "Alexander";
```

![](/images/phil.png)

So now we've created a simple graph, with one concept type and four
concept instances.

{% include note.html content="Every concept needs a type, using `isa`. <br/>   `Plato` `is a` `person`   <br/>  `person` `sub` `entity`" %}


{% include tip.html content="Graql shell maintains a history of past commands with the 'up' and 'down' arrows. You can also autocomplete keywords, type and variable names using tab!" %}

We'll quickly check that our data has loaded:

```graql
match $p isa person, has name $n;

$p isa person; $n value "Aristotle" isa name;
$p isa person; $n value "Plato" isa name;
$p isa person; $n value "Socrates" isa name;
$p isa person; $n value "Alexander" isa name;
```

{% include note.html content="Graql variables start with a `$`. They represent wildcards, and are returned as results in `match` queries. A variable name can contain alphanumeric characters, dashes and underscores." %}

Next, let's add some `schools` of thought:

```graql
insert school sub entity, has-resource name;
insert isa school, has name "Peripateticism";
insert isa school, has name "Platonism";
insert isa school, has name "Idealism";
insert isa school, has name "Cynicism";
```

And look up one:

```graql
match $c has name "Cynicism";

$c isa school;
```

## A note on commit
Nothing you have entered into the Graql shell has yet been committed to the graph, nor has it been validated. To save any changes you make to a graph, you need to type `commit` in the shell. It is a good habit to get into regularly committing what you have entered, so we will show this in the following example.

## Relations

How do we define a "philosopher"? Very smart people have argued for a very long time about this, but we're going to put our fingers in our ears and say a philosopher is someone who practices a philosophy.

A relation comprises of pairs of roles and role players. Relations and roles also have types, just like normal concepts.  

First, we define a `relation` called `practice` that relates a
`role-type` called `philosopher` to a `role-type` called `philosophy`:

```graql
insert practice sub relation;
insert philosopher sub role-type;
insert philosophy sub role-type;
insert practice has-role philosopher, has-role philosophy;
```

A `person` can have the role of `philosopher` and a `school` can have the role of `philosophy`:

```graql
insert person plays-role philosopher;
insert school plays-role philosophy;
```

Now let's relate some `philosophers` to their `philosophies`:

```graql
match $socrates has name "Socrates"; $platonism has name "Platonism"; insert (philosopher: $socrates, philosophy: $platonism) isa practice;
match $plato has name "Plato"; $idealism has name "Idealism"; insert (philosopher: $plato, philosophy: $idealism) isa practice;
match $plato has name "Plato"; $platonism has name "Platonism"; insert (philosopher: $plato, philosophy: $platonism) isa practice;
match $aristotle has name "Aristotle"; $peripateticism has name "Peripateticism"; insert (philosopher: $aristotle, philosophy: $peripateticism) isa practice;
commit;
```

The `match .... insert ...` syntax will perform the `insert` query for every result of the `match` query.

![](/images/practice.png)

Here, `Plato` is playing the role of `philosopher`, and `Idealism` is playing the role of `philosophy`.

Now we can query for all our Platonists:

```graql
match
$platonism has name "Platonism";
(philosopher: $phil, $platonism) isa practice;
```

This returns:   

```
$phil id "Socrates" isa person;
$phil id "Plato" isa person;
```

{% include note.html content="This query can be read as 'Get me philosophers in a practice relationship with Platonism'.  <br/> We didn't specify the role of `Platonism` in this query, or the type of `$phil`, which is totally fine! <br /> Roles and types can be omitted in queries. <br/> For example, the query `match ($x, $y)` is a valid query (that will find *everything* in a relationship with *anything*)." %}

Next let's talk about the relationships between our philosophers. Socrates
taught Plato, Plato taught Aristotle and Aristotle even taught Alexander the
Great!

First, extend our schema:

```graql
insert education sub relation;
insert teacher sub role-type;
insert student sub role-type;
insert education has-role teacher, has-role student;
insert person plays-role teacher, plays-role student;
```

Second, our data:

```graql
match $socrates has name "Socrates"; $plato has name "Plato"; insert (teacher: $socrates, student: $plato) isa education;
match $plato has name "Plato"; $aristotle has name "Aristotle"; insert (teacher: $plato, student: $aristotle) isa education;
match $aristotle has name "Aristotle"; $alexander has name "Alexander"; insert (teacher: $aristotle, student: $alexander) isa education;
commit;
```

{% include tip.html content="**Test Yourself** <br /> Try writing a query to see who taught Aristotle. Include all the roles and types (`teacher`, `student` and `education`), then remove them one by one to see when the results change! The answer is at the bottom of this page." %}

## Resources

Some people have special titles and epithets and we want to talk about that.
So, we'll create some resource types that can be attached to a person:

```graql
insert title sub resource-type, datatype string;
insert epithet sub resource-type, datatype string;
insert person has-resource title, has-resource epithet;
```


Let's make Alexander "Great"!

```graql
match $alexander has name "Alexander"; insert $alexander has epithet "The Great";
```


This is a quick way to add a relation between `Alexander` and an `epithet` with value `"The Great"`.

![](/images/epithet.png)

Let's add the rest of Alexander's titles while we're at it:

```graql
match $alexander has name "Alexander"; insert $alexander has title "Hegemon";
match $alexander has name "Alexander"; insert $alexander has title "King of Macedon";
match $alexander has name "Alexander"; insert $alexander has title "King of Persia";
match $alexander has name "Alexander"; insert $alexander has title "Pharaoh of Egypt";
match $alexander has name "Alexander"; insert $alexander has title "Lord of Asia";
commit;
```


Using Graql, we can query for people who have titles, and list them out.

```graql
match isa person, has title $y, has name $n;

$n value "Alexander" isa name; $y value "Shah of Persia" isa title;
$n value "Alexander" isa name; $y value "Hegeon" isa title;
$n value "Alexander" isa name; $y value "King of Macedon" isa title;
$n value "Alexander" isa name; $y value "Pharaoh of Egypt" isa title;
$n value "Alexander" isa name; $y value "Lord of Asia" isa title;
```


{% include tip.html content="**Test Yourself** <br /> When querying for an id, value or resource you can use predicates as well as direct values. For example, has epithet contains 'Great'. See if you can write a query for everyone with a title containing 'King'. The answer is at the bottom of the page." %}

## Relations as Role Players

Philosophers know lots of things. We should probably include this in our
schema.

```graql
insert knowledge sub relation;
insert thinker sub role-type;
insert thought sub role-type;
insert knowledge has-role thinker, has-role thought;
insert fact sub entity, plays-role thought, has-resource name;
insert description sub resource-type, datatype string;
insert fact has-resource description;
insert person plays-role thinker;
```

Aristotle knew some astronomy, Plato knew a lot about caves and Socrates didn't really know anything at all.

```graql
insert has name "sun-fact" isa fact, has description "The Sun is bigger than the Earth";
match $aristotle has name "Aristotle"; $sun-fact has name "sun-fact"; insert (thinker: $aristotle, thought: $sun-fact) isa knowledge;
insert has name "cave-fact" isa fact, has description "Caves are mostly pretty dark";
match $plato has name "Plato"; $cave-fact has name "cave-fact"; insert (thinker: $plato, thought: $cave-fact) isa knowledge;
insert has name "nothing" isa fact;
match $socrates has name "Socrates"; $nothing has name "nothing"; insert (thinker: $socrates, thought: $nothing) isa knowledge;
commit;
```

A relation is actually just a special kind of instance. Just as
`Socrates` can be a role player, relations themselves can also be role players.

For example, Socrates knew nothing, but he also *knew* that he knew nothing!

First, we have to state that someone can think about their own knowledge:

```graql
insert knowledge plays-role thought;
commit;
```

We can now give Socrates one final piece of knowledge:

```graql
match $socrates has name "Socrates"; $nothing has name "nothing"; $socratesKnowsNothing ($socrates, $nothing); insert (thinker: $socrates, thought: $socratesKnowsNothing) isa knowledge;
```

Here, `socratesKnowsNothing` is the relation between `Socrates` and `nothing`.
We then put `socratesKnowsNothing` in a *new* `knowledge` relation as a role
player.

Finally, we'll check out everything Socrates knows:

```graql
>>> match $socrates has name "Socrates"; ($socrates, $x) isa knowledge;
$x id "fact-e387d27c-4f5e-11e6-beb8-9e71128cae77" isa fact;
$x id "knowledge-e387d27c-4f5e-11e6-beb8-9e71128cae77" isa knowledge;
```

{% include note.html content="If you don't provide an ID for something such as a relation, it will get an automatically generated ID." %}

![](/images/knowledge.png)


## Test Yourself: Answers
We asked you to write a query to see who taught Aristotle.   
Graql answers:

```graql
match $aristotle has name "Aristotle"; (teacher: $teach, student: $aristotle) isa education;
```

We asked you to write a query for everyone with a title containing "King".   
Answer:  

```graql
match $king has title contains "King";
```

## Where Next?

Now you have started getting to grips with Grakn, please explore our additional [example code](../examples/examples.html) and documentation. We are always adding more and welcome ideas and improvement suggestions. Please [get in touch](https://grakn.ai/community.html)!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/18" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
