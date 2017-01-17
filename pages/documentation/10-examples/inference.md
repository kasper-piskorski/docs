---
title: An Example of Inference using GRAKN.AI
keywords: migration
last_updated: January 2017
tags: [reasoner, examples]
summary: "An example to illustrate inference using genealogy data."
sidebar: documentation_sidebar
permalink: /documentation/examples/inference.html
folder: documentation
comment_issue_id: 
---

## Introduction

This is an example of how to use Grakn's reasoner to infer information about family relationships from a simple dataset of genealogy data. The data has been used previously as the basis of a [blog post](https://blog.grakn.ai/family-matters-1bb639396a24#.2e6h72y0m) to illustrate the fundamentals of the Grakn visualiser, reasoner and analytics components. 

As the blog post explained, the original data was a [document](http://www.lenzenresearch.com/titusnarrlineage.pdf) from [Lenzen Research](http://www.lenzenresearch.com/) that described the family history of Catherine Niesz Titus for three generations of her maternal lineage. Our team gathered together a set of CSV files containing basic information about the family, such as names, dates of birth, death and marriage, who was a parent of who, and who married who.
 
The full genealogy-graph example can be found on Grakn's [sample-datasets](https://github.com/graknlabs/sample-datasets/tree/master/genealogy-graph). repository on Github. In this example, we will explore how to use Grakn to make inferences and find information from the data that is not explicit in the dataset. You can find documentation about writing rules for the Grakn reasoner [here](https://grakn.ai/pages/documentation/graql/graql-rules.html).


## Example Ontology

On GRAKN.AI, the first step when working with a dataset is to define its ontology in Graql. The ontology is a way to describe the entities and their relations, so the underlying graph can store them as nodes and edges. You can find out more in our guide to the Grakn Knowledge Model. The ontology allows Grakn to perform:

* logical reasoning over the represented knowledge, such as the extraction of implicit information from explicit data (inference)
* discovery of inconsistencies in the data (validation).

The complete ontology for the genealogy-graph demo is in our sample-datasets repository, named [ontology.gql](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/ontology.gql). 

At a very high-level, the ontology contains the following entities:

* `person` 
* `event`     
	* `birth`, a subclass of an `event`   
	* `death`, a subclass of an `event`   
	* `wedding`, a subclass of an `event`   
* `document`, which we will not consider in this example.


The ontology also describes possible family relationships between `person` entities, such as parent/child, siblings, grandparent/grandchild, which will discuss shortly. 

In addition, there is an `event-protagonist` relation:

* The role of the person in the event is described by a resource named `function` (which is of datatype string to indicate newborn, spouse, parent or deceased).
* The relation two associated roles: `happening` (played by an `event` entity such as a `birth` or `wedding`) and `protagonist` (played by a `person` entity)
 		
Finally, there is another relation, which is between `document` and `event` entities, the `conclusion-evidence` relation, which we will not consider in this example, as it is not relevant to the concepts we are discussing.


To load the *ontology.gql* file into Grakn, make sure the engine is running and choose a clean keyspace in which to work (in the example below we use the default keyspace, so we are cleaning it before we get started). Pull down the [sample-datasets repo](https://github.com/graknlabs/sample-datasets), and call the following from the terminal, from within the *genealogy-graph* directory:

```bash
<relative-path-to-Grakn>/bin/grakn.sh clean
<relative-path-to-Grakn>/bin/grakn.sh start
<relative-path-to-Grakn>/bin/graql.sh -f ./ontology.gql
```
		
## Data

The data for this example is discussed more fully in our [example on CSV migration](./CSV-migration.html), which discusses how the original data is laid out in CSV files and migrated into GRAKN.AI. For the purposes of this example, we can simply load the data from a .gql file into the graph:

```bash
<relative-path-to-Grakn>/bin/graql.sh -f ./data.gql
```

When the terminal prompt returns, the data will have been loaded into the default keyspace, and you can start to look it using the [Grakn visualiser](../the-basics/visualiser.html), by navigating to [http://localhost:4567](http://localhost:4567) in your browser.

## Inferring Family Relations

By default, inference is switched off, and the only data in the graph is what we have loaded, which is a set of people, event and document entities and a set of relations between people/events and documents/events. For example, if you submit the following query to the visualiser:

```graql
match (child: $c, parent: $p) isa parentship;
```

You will receive no results at all. To find family relations between people, we need to:

* Load a set of rules for the reasoner, which we will discuss further shortly
* Activate inference.

The following loads the rules for this dataset from a file called *rules.gql*:

```bash
<relative-path-to-Grakn>/bin/graql.sh -f ./rules.gql
```

To activate inference in the Grakn visualiser you need to open the Config settings, found on the left hand side of the screen. When the page opens you will see the "Activate Inference" checkbox. Check it, and Grakn is ready to start building some new information about the family. Try the query again:

```graql
match (child: $c, parent: $p) isa parentship;
```

You should see something similar to the screenshot below in your visualiser window.

![Person query](/images/match-isa-parentship.png)

Alternatively, you can make queries in the graql shell. You will need to start it as follows, to switch on inference and materialisation:

```bash
<relative-path-to-Grakn>/bin/graql.sh -n -m
```

## Inference Rules

Inference is the process of deducing information from incomplete data. For example, given the following statements:

```
If grass is not an animal.
If vegetarians only eat things which are not animals.
If sheep only eat grass.
```

It is possible to infer the following:

```
Then sheep are vegetarians.
```

The initial statements can be seen as a set of rules with a particular structure: IF something is verified, THEN something else must be true.

This is how the Grakn reasoner works. It checks all whether the rules in the first block can be verified and, if they can, infers the statement in the second block. The rules are written in Graql, and we call the first set of statements (the IF part or, if you prefer, the antecedent) simply the left hand side (LHS). The second part, not surprisingly, is the right hand side (RHS). Using Graql, both sides of the rule are enclosed in curly braces and preceded by, respectively, the keywords `lhs` and `rhs`. 

{% include note.html content="The full documentation for writing rules for the Grakn reasoner is available from [here](https://grakn.ai/pages/documentation/graql/graql-rules.html)." %}

### Example 1: A `parentship` relation

The following code is part of the set of rules in [*rules.gql*](https://github.com/graknlabs/sample-datasets/blob/master/genealogy-graph/rules.gql) that we loaded into the graph earlier. 

```graql
# Naming the rule is optional

$materializeParentChildRelations isa inference-rule
lhs
{$b isa birth has confidence "high";
$rel1 (happening: $b, protagonist: $p) isa event-protagonist;
$rel1 has function "parent";
$rel2 (happening: $b, protagonist: $c) isa event-protagonist;
$rel2 has function "newborn";}
rhs
{(parent: $p, child: $c) isa parentship;};
```

Here, the left hand side rules check these statements:

* does a `birth` event (`$b`) have a `confidence` resource set to "high" (which means that it has been added by the Grakn migrator from CSV data, so is reliable)?
* does an `event-protagonist` relation between this `birth` event (`$b`) and a `person` entity (`$p`) have a `function` resource called "parent"? 
* does an `event-protagonist` relation between this `birth` event (`$b`) and a `person` entity (`$c`) have a `function` resource called "newborn"?

If so, the right hand side of the rules state that:

* the two `person` entities `$p` and `$c` are in a `parentship` relation where `$p` is the parent and `$c` is the child.

Use of inference has made information that was in the dataset, but not explicit, easy to retrieve. For example, we can make a query to determine children who have the same name as their parents:

```graql
match (parent: $p, child: $c); $p has firstname $n; $c has firstname $n;
```

In the genealogy-graph example, there should be three results returned. William, John and Mary are names shared between pairs of parents and children.

### Example 2: Specific relations between parents and children

We have just described the Grakn rules to infer `parentship` relations between `person` entities, but we have not identified the specific details of the parent (are they the mother or the father?) nor of the child (a son or a daughter?). The following rules add new roles to the `parentship` and assign the to the appropriate entities:

```graql
$genderizeParentships1 isa inference-rule
lhs
{(parent: $p, child: $c) isa parentship;
$p has gender "male";
$c has gender "male";
}
rhs
{(father: $p, son: $c) isa parentship;};

$genderizeParentships2 isa inference-rule
lhs
{(parent: $p, child: $c) isa parentship;
$p has gender "male";
$c has gender "female";
}
rhs
{(father: $p, daughter: $c) isa parentship;};

$genderizeParentships3 isa inference-rule
lhs
{(parent: $p, child: $c) isa parentship;
$p has gender "female";
$c has gender "male";
}
rhs
{(mother: $p, son: $c) isa parentship;};

$genderizeParentships4 isa inference-rule
lhs
{(parent: $p, child: $c) isa parentship;
$p has gender "female";
$c has gender "female";
}
rhs
{(mother: $p, daughter: $c) isa parentship;};
```

The four rules can be broken down as follows:

* LHS: In the `parentship` relation between child `$c` and parent `$p`, do they both have a `gender` resource that is `male`?
	* RHS: The `parentship` relation is between `father` and `son`
* LHS: In the `parentship` relation between child `$c` and parent `$p`, does `$c` have a `gender` resource that is `female` and `$p` have a `gender` resource that is `male`?
	* RHS: The `parentship` relation is between `father` and `daughter`
* LHS: In the `parentship` relation between child `$c` and parent `$p`, does `$p` have a `gender` resource that is `male` and `$p` have a `gender` resource that is `female`?
	* RHS: The `parentship` relation is between `mother` and `son`
* LHS: In the `parentship` relation between child `$c` and parent `$p`, does `$p` have a `gender` resource that is `male`?
	* RHS: The `parentship` relation is between `mother` and `daughtr`

Now we have even more information from the data. Previously we made a query to determine children who have the same name as their parents. Now, we can make a query to determine just the sons who have the same name as their fathers:

```graql
match (father: $p, son: $c) isa parentship; $p has firstname $n; $c has firstname $n;
```

In the genealogy-graph example, there should be two results returned. William and John are names shared between father/son pairs.

### Example 3: A `grandparentship` relation

The *rules.gql* file for the genealogy dataset contains a number of rules for setting up family relationships, such as siblings, cousins, in-laws and the following, which sets up a relation called `grandparentship`:

```graql
$parentsOfParentsAreGrandparents isa inference-rule
lhs
{(parent:$p, child: $gc) isa parentship;
(parent: $gp, child: $p) isa parentship;
}
rhs
{(grandparent: $gp, grandchild: $gc) isa grandparentship;};
```

Here, the left hand side rules check two statements:

* does `$p` play the `parent` role in a `parentship` relation, with (`$gc`) playing the `child` role?
* does `$p` also play the `child` role in a `parentship` relation, with (`$gp`) playing the `parent` role?

If so, the right hand side of the rules state that:

* `$gp` and `$gc` are in a `grandparentship` relation, where `$gp` plays the `grandparent` role and `$gc` plays the `grandchild` role.

Some additional rules can add more specifics to the `grandparentship` and assign the entities to the roles `grandson`, `granddaughter`, `grandmother` and `grandfather`:

```graql
$grandParents1 isa inference-rule
lhs
{($p, son: $gc) isa parentship;
(father: $gp, $p) isa parentship;
}
rhs
{(grandfather: $gp, grandson: $gc) isa grandparentship;};

$grandParents2 isa inference-rule
lhs
{($p, daughter: $gc) isa parentship;
(father: $gp, $p) isa parentship;
}
rhs
{(grandfather: $gp, granddaughter: $gc) isa grandparentship;};

$grandParents3 isa inference-rule
lhs
{($p, daughter: $gc) isa parentship;
(mother: $gp, $p) isa parentship;
}
rhs
{(grandmother: $gp, granddaughter: $gc) isa grandparentship;};

$grandParents4 isa inference-rule
lhs
{($p, son: $gc) isa parentship;
(mother: $gp, $p) isa parentship;
}
rhs
{(grandmother: $gp, grandson: $gc) isa grandparentship;};
```

This allows us to query, for example, which grandfather/grandson pairs share the same name:

```graql
match (grandfather: $x, grandson: $y) isa grandparentship; $x has firstname $n; $y has firstname $n;
```

## Additional Queries

Let's look at how to make a few further queries on the genealogy graph:

* People with no children
* People who have never married
* People with no grandchildren


## Where Next?

This example has illustrated the basics of the inference rules used by the Grakn reasoner. Having read it, you may want to further study our documentation about [Graql Rules](../graql/graql-rules.html) and further investigate the [example that imports the genealogy data from CSV format into Grakn](./CSV-migration.html).

We will be using the genealogy data throughout our documentation. For an overview, please see our [Family Matters](https://blog.grakn.ai/family-matters-1bb639396a24#.uelgekrn2) blog post.

{% include links.html %}

## Comments
