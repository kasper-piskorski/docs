---
layout: documentation
title: Quickstart Tutorial
---

# Graql Quick Start Tutorial

This document will teach you how to use Graql to load an ontology and some data into a Mindmaps Graph. You can find the example ```philosophers.gql``` in the examples directory included in the mindmaps.zip.   
If you have not yet set up the Mindmaps environment, please see the Quick Set Up guide. LINK TO DO
If you want to get started using Java rather than Graql, please see the Java Quick Start tutorial. LINK TO DO

**TO DO: Insert table of contents**

We are going model how philosophers are related to each other. By the way, don't worry if you've already loaded some data into your graph. A Mindmaps Graph can contain as many ontologies as you want.

## Concepts

We'll first add an ontology element - an entity type: `person`...

```bash
bin/graql.sh
insert person isa entity-type;
```

...and now some data in the form of concept instances: in our case, a bunch of ancient Greeks...

```sql
insert "Socrates" isa person;
insert "Plato" isa person;
insert "Aristotle" isa person;
insert "Alexander" isa person;
```

![](/docs/images/phil.png)

So now we've created a super-simple graph, with one concept type and four 
concept instances.

> **Types**
> Every concept needs a type, using `isa`.   
> `Plato` `is a` `person`   
> `person``is a` `entity-type`


> **Ancient History**  
> Graql shell maintains a history of past commands with the 'up' and 'down'
> arrows. You can also autocomplete keywords, type and variable names using
> 'tab'!" 

We'll quickly check our data has loaded:

```sql
match $p isa person

$p id "Aristotle" isa person;
$p id "Plato" isa person;
$p id "Socrates" isa person;
$p id "Alexander" isa person;
```
TO DO - Where did we set the id? What does this represent?

> **Variables**
> Graql variables start with a `$`. They represent "wildcards", and are
> returned as results in `match` queries. A variable name can contain
> alphanumeric characters, dashes and underscores.

Next, let's add some `schools` of thought:

```sql
insert school isa entity-type;
insert "Peripateticism" isa school;
insert "Platonism" isa school;
insert "Idealism" isa school;
insert "Cynicism" isa school;
```

And look up one:

```sql
match $cyn id "Cynicism"

$cyn id "Cynicism" isa school;
```


## Relations

How do we define a "philosopher"? Very smart people have argued for a very long time about this, but we're going to put our fingers in our ears and say a philosopher is someone who practices a philosophy.

A relation comprises of pairs of role types and role players. Relations and roles also have types, just like normal concepts.  

First, we define a `relation-type` called `practice` that relates a
`role-type` called `philosopher` to a `role-type` called `philosophy`:

```sql
insert practice isa relation-type;
insert philosopher isa role-type;
insert philosophy isa role-type;
insert practice has-role philosopher, has-role philosophy;
```

> **I don't like typing!**
> Commas are totally optional in query patterns, so these pairs are equivalent:

> ```sql
> insert practice has-role philosopher, has-role philosophy;
> insert practice has-role philosopher has-role philosophy;
>  
> insert practice, isa relation-type;
> insert practice isa relation-type;
> ```


A `person` can have the role of `philosopher` and a `school` can have the role of `philosophy`:

```sql
insert person plays-role philosopher;
insert school plays-role philosophy;
```

> **graql commit -am "changed some stuff"**  
> The changes you've made to the graph haven't been saved yet!   
> Type `commit` in the Graql shell to commit any changes.

Now let's relate some `philosophers` to their `philosophies`:

```sql
insert (philosopher "Socrates", philosophy "Platonism") isa practice;
insert (philosopher "Plato", philosophy "Idealism") isa practice;
insert (philosopher "Plato", philosophy "Platonism") isa practice;
insert (philosopher "Aristotle", philosophy "Peripateticism") isa practice;
```

![](/docs/images/practice.png)

Here, `Plato` is playing the role of `philosopher`, and `Idealism` is playing the role of `philosophy`. 

Now we can query for all our Platonists:

```sql
match (philosopher $phil, "Platonism") isa practice;

$phil id "Socrates" isa person;
$phil id "Plato" isa person;
```

> **In Plain English...**  
> This query can be read as "Get me philosophers in a practice relationship
> with Platonism."
>
> We didn't specify the role of `Platonism` in this query, or the type of
> `$phil`, which is totally fine! Roles and types can be omitted in queries.  
> 
> For example, the query `match ($x, $y)` is a valid query (that will
> find *everything* in a relationship with *anything*).

Next let's talk about the relationships between our philosophers. Socrates
taught Plato, Plato taught Aristotle and Aristotle even taught Alexander the
Great!

First, extend our ontology:

```sql
insert education isa relation-type;
insert teacher isa role-type;
insert student isa role-type;
insert education has-role teacher, has-role student;
insert person plays-role teacher, plays-role student;
```


Second, our data:

```sql
insert (teacher "Socrates", student "Plato") isa education;
insert (teacher "Plato", student "Aristotle") isa education;
insert (teacher "Aristotle", student "Alexander") isa education;
```

> **Aristotle's Teacher**  
> Try writing a query to see who taught Aristotle. Include all the roles and
> types (`teacher`, `student` and `education`), then remove them one by one to see when the results change! The answer is at the bottom of this page.

## Resources

Some people have special titles and epithets and we want to talk about that.
So, we'll create some resource types that can be attached to a person:

```sql
insert title isa resource-type, datatype string;
insert epithet isa resource-type, datatype string;
insert person has-resource title, has-resource epithet;
```

Let's make Alexander "Great"!

```sql
insert "Alexander" has epithet "The Great";
```

This is a quick way to add a relation between `Alexander` and an `epithet` with value `"The Great"`.

![](/docs/images/epithet.png)

Let's add the rest of Alexander's titles while we're at it:

```sql
insert "Alexander" has title "Hegemon";
insert "Alexander" has title "King of Macedon";
insert "Alexander" has title "King of Persia";
insert "Alexander" has title "Pharaoh of Egypt";
insert "Alexander" has title "Lord of Asia";
```

Now we can query for people, listing their id and titles:  
**TO DO - Again, need to clarify id**

```sql
match $x isa person select $x(id, has title)

$x id "Socrates";
$x id "Plato";
$x id "Aristotle";
$x id "Alexander" has title "Pharaoh of Egypt" has title "Hegeon" has title "Shah of Persia" has title "King of Macedon" has title "Lord of Asia";
```

Wait, who's the Pharaoh again?

```sql
match $pharaoh has title "Pharaoh of Egypt"

$pharaoh id "Alexander" isa person;
```


> **Predicates**
> When querying for an id, value or resource you can use predicates as well as direct values. For example, `has epithet contains "Great"`. See if you can write a query for everyone with a title containing "King". Answers are at the bottom of the page!

## Relations as Role Players

Philosophers know lots of things. We should probably include this in our
ontology.

```sql
insert knowledge isa relation-type;
insert thinker isa role-type;
insert thought isa role-type;
insert knowledge has-role thinker, has-role thought;
insert fact isa entity-type, plays-role thought;
insert person plays-role thinker;
```

Aristotle knew some astronomy, Plato knew a lot about caves and Socrates didn't really know anything at all.

```sql
insert "sun-fact" isa fact, value "The Sun is bigger than the Earth";
insert (thinker "Aristotle", thought "sun-fact") isa knowledge;
insert "cave-fact" isa fact, value "Caves are mostly pretty dark";
insert (thinker "Plato", thought "cave-fact") isa knowledge;
insert "nothing" isa fact;
insert (thinker "Socrates", thought "nothing") isa knowledge;
```

TO DO - probably need to talk more about introducing "value" 

A relation is actually just a special kind of instance. Just as
`Socrates` can be a role player, relations themselves can also be role players.

TO DO - OK, I lost it here!

For example, Socrates knew nothing, but he also *knew* that he knew nothing!

First, we have to state that someone can think about their own knowledge:

```sql
insert knowledge plays-role thought;
```


We can now give Socrates one final piece of knowledge:

```sql
match $socratesKnowsNothing ("Socrates", "nothing") insert (thinker "Socrates", thought $socratesKnowsNothing) isa knowledge
```


Here, `socratesKnowsNothing` is the relation between `Socrates` and `nothing`.
We then put `socratesKnowsNothing` in a *new* `knowledge` relation as a role
player.

Finally, we'll check out everything Socrates knows:

```sql
match ("Socrates", $x) isa knowledge

$x id "nothing" isa fact;
$x id "knowledge-e387d27c-4f5e-11e6-beb8-9e71128cae77" isa knowledge;
```

> **knowledge-ohmygod-whatisthat-???**
> If you don't provide an ID for something such as a relation, it will get an automatically generated ID.

![](/docs/images/knowledge.png)

> **When Persisting The Data To Disk**
> Once you done make sure to use `mindmaps.sh stop && mindmaps.sh clean` if you would like to clean your graph quickly.   
> **Warning :** This will delete all your graphs.

## Answers
We asked you to write a query to see who taught Aristotle. Answer:
```sql
select $teach where (teacher $teach, $student Aristotle) isa education
```

We asked you to write a query for everyone with a title containing "King". Answer:
```sql
match $king has title contains "King"
```

## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
    <tr>
        <td>v1.01</td>
        <td>03/08/2016</td>
        <td>Updated content and formatting.</td>        
    </tr>
    </tr>
        <td>v1.02</td>
        <td>09/08/2016</td>
        <td>Clarify this is for Graql.</td>        
    </tr>
    
</table>
