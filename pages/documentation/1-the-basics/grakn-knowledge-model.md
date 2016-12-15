---
title: Grakn Knowledge Model
keywords: setup, getting started, basics
last_updated: December 7th, 2016
tags: [getting-started, reasoner, graql]
summary: "Introducing the basics of the Grakn knowledge model."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/grakn-knowledge-model.html
folder: documentation
comment_issue_id: 17
---

{% include warning.html content="Please note that this page is in progress and subject to revision." %}

In Grakn, a graph is made of two layers: the ontology layer and the data layer.

## Ontology

The ontology is a formal specification of all the relevant concepts and their meaningful associations in a given application domain. It allows objects and relationships to be categorised into distinct types, and for generic properties about those types to be expressed. Specifying the ontology enables logical reasoning over the represented knowledge, such as the extraction of implicit information from explicit data (inference) or discovery of inconsistencies in the data (validation).   

Grakn ontologies use four types of `concepts` for modeling domain knowledge:   
 
* **Entity Types**: Types of objects or things in the domain, for example, `person`, `man`, `woman`.    
* **Relation Types**: Types of n-ary relationships between different domain instances, for example, `marriage`, which is a relationship between two instances of enity types `woman` and `man`, playing roles of `wife` and `husband`, respectively. 
* **Role Types**: Types of roles involved in specific relationships, for example: `wife`, `husband`.     
* **Resource Types**: Types of attributes associated with domain instances, for example, `name`, or `age`.    

The categorization of concept types is effectively enforced in Grakn knowledge model by declaring every concept type as a subtype (i.e., an extension) of exactly one of the four corresponding, built-in concept types: `enity`, `relation`, `role`, `resource`, for example, `person sub entity`, `marriage sub relation`, `wife sub role`, `name sub resource`.

Different concept types can be related to each other via four constructs (see Figure 1): 

* **sub**:  expressing that a concept type is a subtype (i.e., an extension) of another one, for example, `woman sub person`, `man sub person`.
* **has-resource**: expressing that a concept type can be associated with a given resource type, for example, `person has-resource name`.
* **plays-role**: expressing that instances of a given concept type are allowed to play a specific role, for example, `woman plays-role wife`, `man plays-role husband`. 
* **has-role**: expressing that a given relation type involves a specific role, for example, `marriage has-role wife`, `marriage has-role husband`.

Additionally, all concept types can be declared as `abstract`, meaning that they cannot have any direct instances. For example, `person is-abstract` expresses that the only instances of `person` can be those that belong to more specific types, e.g., `man` or `woman`. 

![The concept types and their permitted associations via Grakn ontology constructs.](/images/knowledge-model-fig1.png)

*Figure 1: The concept types and their permitted associations via Grakn ontology constructs.*

<!-- This section may possibly be better if moved into a different, more low level page -->
A well-formed Grakn ontology is required to satisfy the following structural properties (for examples see the following section):

* each concept type can have at most one direct supertype,
* each role type must be involved in exactly one relation type,
* each relation type must involve at least two distinct role types, <!-- I think an example would help here--> 
* each relation type must involve the same number of roles as its direct supertype. In such a case, every role type involved in the subtype relation must be a (possibly indirect) subtype of exactly one role type involved in the supertype relation.   <!-- definitely need an example or diagram -->



### Inheritance 

As in object-oriented programming, the inheritance mechanism in Grakn enables subtypes to automatically take on some properties of their supertypes. This simplifies the construction of ontologies and helps keeping them succinct. Grakn knowledge model imposes inheritance of all `has-resource` and `plays-role` constraints on entity, relation and resource types.  
   
For example, in Figure 2, the entity types `woman` and `man` inherit the constraints `has-resource name`, `plays-role partner1`, `plays-role partner2` from their supertype `person`. Similarly, the relation type `marriage` inherits the constraint `has-resource since` from `partnership`. 


![A sample of a Grakn ontology.](/images/knowledge-model-fig2.png)

*Figure 2: An example of Grakn ontology.*

The ontology in Figure 2 consists of the following constraints:

**Type declarations** (omitted in the figure):
* `person sub entity`
* `partnership sub relation`
* `partner1 sub role` 
* `partner2 sub role`
* `name sub resource`
* `since sub resource`

Note, that because of the transitivity of the `sub` construct, the type declarations for the remaining concept types in the ontology follow implicitly. 


**Explicit constraints**:
* `person has-resource name`
* `person plays-role partner1`
* `person plays-role partner2`
* `woman sub person`
* `woman plays-role wife`
* `man sub person`
* `man plays-role husband`
* `marriage sub partnership`
* `wife sub partner1`
* `husband sub partner2`
* `partnership has-resource since`
* `partnership has-role partner1`
* `partnership has-role partner2`
* `marriage has-role wife`
* `marriage has-role husband`

**Inherited constraints**:
* `woman has-resource name`
* `woman plays-role partner1`
* `woman plays-role partner2`
* `man has-resource name`
* `man plays-role partner1`
* `man plays-role partner2`
* `marriage has-resource since`


## Data

The data is expressed by instantiating specific types of entities, relations, concrete resources they are associated with, and assigning roles the instances play in particular relations. The unique identifiers for all instances are defined internally within the Grakn system. There are three types of data instances:

* **Entities**: instances of entity types, for example, `insert $x isa woman` creates an instance of the entity type `woman`, 

* **Relations**: instances of relation types, for example, `insert (wife:$x, husband:$y) isa marriage` creates an instance of the relation type `marriage` between some instances `$x`, playing the role of `wife`, and `$y`, playing the role of `husband`.

* **Resources**: instances of resource types being associated with particular instances, for example, `insert $x has-name "Elisabeth Niesz"` creates an instance of the resource type `name` with value "Elizabeth Niesz" associated with some instance `$x`. 

Note, that unlike in the case of entity, relation and resource types, there are no instances of role types in the data layer.


### Validation 

To ensure data is correctly structured (i.e., consistent) with respect to the ontology, all data instances are validated against the ontology constraints. All the explicitly represented ontology constraints, together with the inherited ones, form complete schema templates for particular concept types, which guide the validation and, consequently, the data entry process. For example, the ontology in Figure 2, establishes the following schema templates for the concept types `woman` and `marriage`:

**`woman`**:
* `has-resource name`
* `plays-role wife`
* `plays-role partner1`
* `plays-role partner2`

**`marriage`**:
* `has-resource since`
* `has-role wife`
* `has-role husband`

 A data instance `$x` of a concept type `C` is valid, whenever the following conditions are satisfied:
 * `$x` instantiates only `C` (and indirectly all supertypes of `C`),
 * all types of resources associated with `$x` belong to the schema template of `C`,
 * for every role type `R` that `$x` plays in any relation instance in the data, there is a constraint `plays-role R` in the schema template of `C`,
 * whenever `$x` is a relation instance involving the role type `R` there is a constraint `has-role R` in the schema template of `C`.

For example, data illustrated in Figure 3 violates the ontology from Figure 2, by:
 
1. associating a resource of type `maiden name` with an instance of the type `woman` (which is not allowed in the ontology).
2. by using an instance of the base type `person` in the `wife` role (while only those of type `woman` are accepted by the ontology). 

![Validation of data against the ontology.](/images/knowledge-model-fig3.png)

*Figure 3: Validation of data against the ontology.*

## Rule and Sub-Type Inference

Inference is a process of extracting implicit information from explicit data instances. Grakn supports two inference mechanisms: 

1. type inference, based on the semantics of the `sub` hierarchies included in the ontology
2. rule-based inference involving user-defined IF-THEN rules. 

Both mechanisms are employed by default when querying the knowledge graph with Graql, thus supporting retrieval of both explicit and implicit information at query time.      

### Type Inference
The type inference is based on a simple graph traversal along the `sub` edges. Every instance of a given concept type is automatically classified as an instance of all (possibly indirect) supertypes of that type. In case of roles, every instance playing a given role is inferred to also play all its (possibly indirect) super-roles. 

### Rule-Based Inference
The rule-based inference exploits a set of user-defined datalog rules and is conducted by means of the  reasoner built naitively into Grakn. Every rule is declared as an instance of a built-in Grakn type `inference-rule`. 

A rule is an expression of the form `lhs G1 rhs G2`, where `G1` and `G2` are a pair of Graql patterns. Whenever the left-hand-side (lhs) pattern `G1` is found in the data, the right-hand-side (rhs) pattern `G2` is inserted. For example, the following rule:
 

`lhs
(child:$x, mother:$y) isa motherhood;
(sister:$y brother:$z) isa siblings;
rhs
(nephew:$x, uncle:$z) isa uncle-relation;`
 

expresses that whenever instance $x has a mother $y and $y has a brother $z, then one can infer that $z is an uncle of $x, while $x is a nephew of $z. 

For more detailed documentation on rules see [Graql Rules](../graql/graql-rules.html).


## Where Next?
Our [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) will show you how to load an ontology, rules and data into Grakn using Graql, and to make basic queries.

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
