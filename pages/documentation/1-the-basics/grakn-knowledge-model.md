---
title: Grakn Knowledge Model
keywords: setup, getting started, basics
last_updated: December 7th, 2016
tags: [getting-started]
summary: "Introducing the basics of the Grakn knowledge model."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/grakn-knowledge-model.html
folder: documentation
comment_issue_id: 17
---

{% include note.html content="Please note that this page is in progress and subject to revision." %}

In Grakn, a graph is made of two layers: the ontology layer and the data layer.

## Ontology

The ontology is used to formalise the data within its domain, and it categorises the data.

Grakn provides a formal knowledge model to define the ontology, which comprises of four types:

* **Entity Types**: An Entity Type is used to represent a thing, for example, a `person`, and can be thought of as a noun.
* **Relation Types**: A Relation Type is used to model a relationship between two Entity Types, for example, `marriage`, and can be thought of as a verb.
* **Role Types**: Two role types are used to represent the two roles within a Relationship Type. There is one Role Type for each Entity Type to explain fully how the two relate to one another, for example, `husband` and `wife`.
* **Resource Types**: A Resource Type is used to represent an attribute associated with an Entity Type, for example, `name`. These are properties consisting of primitive types and values.


### Inheritance

The four types described above can be subclassed. For example, subclasses of `person` could be `man` and `woman`.  This simple subclassing allows us to expand and deepen our ontology to whatever granularity of detail is needed, and it allows the domain to be modelled as expressively as possible.  

The property `abstract` can be applied to a type to indicate that it cannot have direct instances. For example if we have `person` and its subclasses `man` and `woman`, then we may want to define `person` as `abstract` to prohibit it from having any direct instances, whereas the concrete types `man` and `woman` can have direct instances.


## Data

Data elements are named instances to reflect that they are concrete pieces of data, corresponding to the ontology types described above. There are three data instances:

* **Entity**: A data instance of the Entity Type. For example, `William Titus` is an Entity corresponding to the Entity Type `man`.

* **Resource**: A data instance of the Resource Type, which reflects a property of a specific Entity. For example, if `Elizabeth Niesz` is an instance of a `woman` then we can model her date of birth as a Resource, an instance of the Resource Type `birthdate`.  


* **Relation**: A data instance of a Relation Type, which essentially describes a relationship between Entities. For example, a `marriage` Relation between `Elizabeth Niesz` (isa `woman`) and `William Titus` (isa `man`).

Note that `Elizabeth Niesz` plays the Role Type `wife` and `William Titus` plays the Role Type `husband`. Unlike the Entity, Resource and Relation instances, there are no instances of Role Types in the data layer.


## Validation 

Some examples


## Rules

Given a set of inference rules, the Grakn reasoner is able to infer new information. The rules are defined as IF...THEN pairs, such that IF something is verified, THEN something else must be true. The reasoner checks if the first part of the rule is verified and, if it is, infers that the second part of the rule is true.

As an example: **IF** X has a mother and the mother has a brother **THEN** X has an uncle.




## Where Next?
Our [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) will show you how to load an ontology, rules and data into Grakn using Graql, and to make basic queries.

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.