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

The ontology is a formal specification of all the relevant concepts and their meaningful associations in a given application domain. It allows objects and relationships to be categorised into distinct types, and for generic properties about those types to be expressed. Specifying the ontology enables logical reasoning over the represented knowledge, such as the extraction of implicit information from explicit data (inference) or discovery of inconsistencies in the data (validation).   

Grakn ontologies use four types of concepts for modeling domain knowledge:   
 
* **Entity Types**: Types of objects or things in the domain, for example, a `person`.    
* **Relation Types**: Types of n-ary relationships between different objects (or possibly other relations), for example, `motherhood`, which is a relationship between two people playing roles of `mother` and `child`.    
* **Role Types**: Types of roles involved in specific relationships, for example: `mother`, `child`.     
* **Resource Types**: Types of attributes associated with entities and relations, for example, `name`, or `age`.    

In Grakn, different concept types can be related to each other via four constructs (see Figure~1): 

* **sub**: used to express that a concept type is a subtype of another one, for example, `woman sub person`.
* **has-resource**: used to express that a concept type can be associated with a given resource type, for example, `person has-resource name`.
* **plays-role**: used to express that instances of a given concept type are allowed to play a specific role, for example, `woman plays-role mother`, 
* **has-role**: used to express that a given relation type involves a specific role, for example, `motherhood has-role mother`.

Additionally, entity types and relation types can be declared as `abstract`, meaning that they cannot have any direct instances. For example, `person is-abstract` expresses that the only instances of `person` can be those that belong to more specific types, e.g., `man` or `woman`. 

[... here comes a figure]
Figure~1: The concept types and their permitted associations via Grakn ontology constructs.

<!-- This section may possibly be better if moved into a different, more low level page -->
A well-formed Grakn ontology is further required to satisfy the following structural properties:

* each concept type can have at most one direct supertype,
* each role type must be involved in exactly one relation type,
* each relation type must involve at least two distinct role types, <!-- I think an example would help here--> 
* each relation type must involve the same number of roles as its direct supertype. In such a case, every role type involved in the subtype relation must be a (possibly indirect) subtype of exactly one role type involved in the supertype relation.   <!-- definitely need an example or diagram -->

### Inheritance 

Inheritance allows the domain to be modelled as expressively as possible.  Grakn supports inheritance of `has-resource` and `plays-role` constraints downwards, via the `sub` hierarchies, to enable the more succinct representation of the ontology structure. Every (possibly indirect) subtype of an entity or a relation type implicitly inherits all its `has-resource` and `plays-role` constraints. 
   
For example, in Figure~2, the entity type `woman` inherits the constraints `has-resource name`, `plays-role child`, `plays-role m-child` and `plays-role mother` from its supertype `person`. Similarly, the relation type `motherhood` inherits the constraint `has-resource since` from `parenthood`. 


[... here comes a figure]
Figure~2: A sample of a Grakn ontology involving entity, relation and role types.


Note that no other forms of inheritance are currently supported in Grakn. All the explicitly represented constraints, together with the inherited ones, form complete schema templates for particular entity and relation types, which are used to guide the data entry process and to validate the consistency of the data with respect to the ontology (see details below).  


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
