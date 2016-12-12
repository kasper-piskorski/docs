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

![The concept types and their permitted associations via Grakn ontology constructs.](/images/knowledge-model-fig1.png)

*Figure 1: The concept types and their permitted associations via Grakn ontology constructs.*

<!-- This section may possibly be better if moved into a different, more low level page -->
A well-formed Grakn ontology is further required to satisfy the following structural properties:

* each concept type can have at most one direct supertype,
* each role type must be involved in exactly one relation type,
* each relation type must involve at least two distinct role types, <!-- I think an example would help here--> 
* each relation type must involve the same number of roles as its direct supertype. In such a case, every role type involved in the subtype relation must be a (possibly indirect) subtype of exactly one role type involved in the supertype relation.   <!-- definitely need an example or diagram -->

### Inheritance 

Inheritance allows the domain to be modelled as expressively as possible.  Grakn supports inheritance of `has-resource` and `plays-role` constraints downwards, via the `sub` hierarchies, to enable the more succinct representation of the ontology structure. Every (possibly indirect) subtype of an entity or a relation type implicitly inherits all its `has-resource` and `plays-role` constraints. 
   
For example, in Figure~2, the entity type `woman` inherits the constraints `has-resource name`, `plays-role child`, `plays-role m-child` and `plays-role mother` from its supertype `person`. Similarly, the relation type `motherhood` inherits the constraint `has-resource since` from `parenthood`. 


![A sample of a Grakn ontology involving entity, relation and role types.](/images/knowledge-model-fig2.png)

*Figure 2: A sample of a Grakn ontology involving entity, relation and role types.*


Note that no other forms of inheritance are currently supported in Grakn. All the explicitly represented constraints, together with the inherited ones, form complete schema templates for particular entity and relation types, which are used to guide the data entry process and to validate the consistency of the data with respect to the ontology (see details below).  


## Data

The instance data is expressed via assertions about the types of specific entities, their relations, roles they play in those relations, and concrete resources they are associated with. The unique identifiers for all instances are assigned internally within the Grakn system. There are three basic types of data assertion (for precise Graql syntax see Page~):

* **Entities**: assertions about types of entity instances, for example, an assertion about `Elizabeth Niesz` being of type `woman`.

* **Relations**: assertions about relation instances and their types, for example, a `marriage` Relation between `Elizabeth Niesz` (in role `wife`) and `William Titus` (in role `husband`).

* **Resources**: assertions about resources being associated with particular entities or relation instances, for example, resource with value "Elisabeth Niesz" of type `name` being associated with the entity representing Elizabeth Niesz. 

Unlike the entity, resource and relation instances, there are no instances of Role Types in the data layer.


### Validation 

To ensure data is correctly structured (i.e., consistent) with respect to the ontology, all data assertions are validated against the ontology constraints. A set of assertions about an instance of a specific entity or relation type is valid, whenever the resource types, role types, and (possibly inferred) types of the involved role players match the schema template of that entity or relation type. 

For example, data assertions in Figure~3 violate the schema templates depicted in Figure~2, by:
 
1. including the resource of type `maiden name` on type `person` (which is not allowed in the ontology).
2. by using an entity with the asserted type `person` in the `mother` role (while only those of type `woman` are accepted by the ontology). 

![Validation of data against the ontology.](/images/knowledge-model-fig3.png)

*Figure 3: Validation of data against the ontology.*

## Rule and Sub-Type Inference

Inference is a process of extracting implicit information from explicitly asserted data. Grakn supports two inference mechanisms: 

1. type inference, based on the semantics of the `sub` hierarchies included in the ontology
2. rule-based inference involving user-defined IF-THEN rules. 

Both mechanisms are employed by default when querying the knowledge graph with Graql, thus supporting retrieval of both explicit and implicit information at query time.      

### Type Inference
The type inference is based on a simple graph traversal along the `sub` edges. Every instance of a given concept type is automatically classified as an instance of all (possibly indirect) supertypes of that type. In case of roles, every instance playing a given role is inferred to also play all its (possibly indirect) super-roles. 

### Rule-Based Inference
The rule-based inference exploits a set of user-defined Horn rules and is conducted by means of the rule reasoner built naitively into Grakn. 

A rule is an expression of the form `IF G1 THEN G2`, where `G1` and `G2` are a pair of `match-insert` Graql patterns. Whenever the pattern `G1` is found in the data the pattern `G2` is inserted, for example the rule:
 
```
IF (child:X, mother:Y); (sister:Y brother:Z) 
THEN (nephew:X, uncle:Z) isa uncle-relation;
``` 

expresses that whenever X has a mother Y and Y has a brother Z, then we can infer that Z is an uncle of X, and X is a nephew of Z. 

For precise rule syntax see [Graql Rules](../graql/graql-rules.html).


## Where Next?
Our [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) will show you how to load an ontology, rules and data into Grakn using Graql, and to make basic queries.

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
