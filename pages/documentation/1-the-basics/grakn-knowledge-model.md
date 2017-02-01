---
title: Grakn Knowledge Model
keywords: setup, getting started, basics
last_updated: January 2017
tags: [getting-started, reasoning, graql]
summary: "Introducing the basics of the Grakn knowledge model."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/grakn-knowledge-model.html
folder: documentation
comment_issue_id: 17
---

{% include warning.html content="Please note that this page is in progress and subject to revision." %}

In Grakn, a graph is made of two layers: the ontology layer and the data layer. 

## Ontology

The ontology must be clearly defined before loading data into the graph. It is a formal specification of all the relevant concepts and their meaningful associations in a given domain. It allows objects and relationships to be categorised into distinct types, and for generic properties of those types to be expressed. Specifying the ontology enables automated reasoning over the represented knowledge, such as the extraction of implicit information from explicit data ([inference](./grakn-knowledge-model.html#rule-and-sub-type-inference)) or discovery of inconsistencies in the data ([validation](./grakn-knowledge-model.html#data-validation)). 

Grakn ontologies use four types of concepts for modeling domain knowledge. The categorization of concept types is enforced in the Grakn knowledge model by declaring every concept type as a subtype (i.e. an extension) of exactly one of the four corresponding, built-in concept types:

**`entity`**: Objects or things in the domain. For example, `person`, `man`, `woman`.    

**`relation`**: Relationships between different domain instances. For example, `marriage`, which is typically a relationship between two instances of entity types (`woman` and `man`), playing roles of `wife` and `husband`, respectively.

**`role`**: Roles involved in specific relationships. For example, `wife`, `husband`.     

**`resource`**: Attributes associated with domain instances. For example, `name`. Resources consist of primitive types and values. They are very much like “data properties” in OWL, and have the following properties:

- Datatype - Indicates the datatype of the resource. For example if the resource type is age the datatype would be long.
- Regex - Optional. Can be used to constrain string data types to specific regex patterns.
- Unique - A boolean which indicates if the resource should be unique across the graph.   

<br /> <img src="/images/knowledge-model1.png" style="width: 600px;"/> <br />

### Building an Ontology

In this section, we build up a simple ontology to illustrate the concept types in the Grakn knowledge model. 

We define two entities, `person` and `company`, each of which have a `name` resource.

```graql
insert
  person sub entity,
  has-resource name;
  
  company sub entity,
  has-resource name;
  
  name sub resource, datatype string;
  
```

<br /> <img src="/images/knowledge-model2.png" style="width: 400px;"/> <br />

<br />

We subtype the entities:

```graql
insert
  person sub entity,
  has-resource name;
    
  company sub entity,
  has-resource name;
  
  customer sub person,
  has-resource rating;

  startup sub company,
  has-resource funding;
  
  name sub resource, datatype string;
  rating sub resource, datatype double;
  funding sub resource, datatype long;
```

<br /> <img src="/images/knowledge-model3.png" style="width: 400px;"/> <br />

<br />

We introduce a relation between a `company` and a `person`:

```graql
insert
  person sub entity,
  has-resource name,
  plays-role employee;
    
  company sub entity,
  has-resource name,
  plays-role employer;
  
  customer sub person,
  has-resource rating;

  startup sub company,
  has-resource funding;
  
  name sub resource, datatype string;
  rating sub resource, datatype double;
  funding sub resource, datatype long;
  
  employment sub relation,
    has-role employee, has-role employer; 	
  employee sub role;
  employer sub role;
``` 

<br /> <img src="/images/knowledge-model4.png" style="width: 400px;"/> <br />

<br /> 

In the simple example above, we have illustrated the four constructs that relate Grakn concept types to each other:

**`sub`**:  expresses that a concept type is a subtype of (i.e., inherits from) another one. 

* For example, `customer sub person`, `startup sub company`.    

**`has-resource`**: expresses that a concept type can be associated with a given resource type. 

* For example, `person has-resource name`.    

**`plays-role`**: expresses that instances of a given concept type are allowed to play a specific role. 

* For example, `person plays-role employee`, `company plays-role employer`.    

**`has-role`**: expresses that a given relation type involves a specific role. 

* For example, `employment has-role employee`, `employment has-role employer`.    

### Relations

Relationships are inherently non-directional and are defined in terms of roles of entities in the relation. Relations can have multiple attributes. Here we give the employment relation a date resource.

```graql
insert
  person sub entity,
  has-resource name,
  plays-role employee;
    
  company sub entity,
  has-resource name,
  plays-role employer;  
  
  name sub resource, datatype string;
  date sub resource, datatype string;
  
  employment sub relation,
    has-role employee, has-role employer,
    has-resource date; 
    	
  employee sub role;
  employer sub role;
```

<br /> <img src="/images/knowledge-model6.png" style="width: 400px;"/> <br />

<br />

N-ary relationships are also allowed by Grakn. For example, a three way `employment` relationship that has `employer`, `employee` and `office` roles:

```graql
insert
  employment sub relation,
    has-role employee, 
    has-role employer, 
    has-role office; 
    	
  employee sub role;
  employer sub role; 
  office sub role;
```

<br /> <img src="/images/knowledge-model8.png" style="width: 400px;"/> <br />

### Inheritance

As in object-oriented programming, the inheritance mechanism in Grakn enables subtypes to automatically take on some of the properties of their supertypes. This simplifies the construction of ontologies and helps keep them succinct. 

<br />The Grakn knowledge model imposes inheritance of all `has-resource` and `plays-role` constraints on entity, relation and resource types. As a result, the entity type `customer` inherits `has-resource name` and `plays-role employee` from the `person` supertype, as shown in the diagram below. 

Likewise, the `startup` entity type inherits `has-role name` and `plays-role employer` from the `company` supertype.

<br /> <img src="/images/knowledge-model5.png" style="width: 400px;"/> <br />

<br />

Therefore, the `employment` relation between a `company` and `person` is also, implicitly, between `startup` and `customer`.

{% include note.html content="Concept types can be declared as `is-abstract`, meaning that they cannot have any direct instances. For example, `person sub entity is-abstract` expresses that the only instances of `person` can be those that belong to more specialised subtypes of `person`, e.g., `customer`." %}

### Structural Properties
A well-formed Grakn ontology is required to satisfy the following structural properties:

* each concept type can have at most one direct supertype,
* each role type must be involved in exactly one relation type,
* each relation type must involve at least two distinct role types, 
* each relation type must involve the same number of roles as its direct supertype. In such a case, every role type involved in the subtype relation must be a (possibly indirect) subtype of exactly one role type involved in the supertype relation.

## Data

The data is expressed by instantiating specific types of entities, relations, and concrete resources they are associated with, and assigning roles to the instances played for particular relations. There are three types of data instances:

**Entities**: instances of entity types, for example, `insert $x isa person` creates an instance of the entity type `person`,

**Resources**: instances of resource types being associated with particular instances, for example, `insert $x isa person, has name "Elisabeth Niesz"` creates an instance of a `person` with the resource type `name` given the value "Elizabeth Niesz". The unique identifiers for all instances are defined internally within the Grakn system.

**Relations**: instances of relation types, for example, `insert (employee:$x, employer:$y) isa employment` creates an instance of the relation type `employment` between `$x`, playing the role of `employee`, and `$y`, playing the role of `employer`.

{% include note.html content="There are no instances of role types in the data layer." %}

```graql
insert
  $x isa person, has name "Elizabeth Niesz";
  $y isa company, has name "Grakn Labs";
  (employee: $x, employer: $y) isa employment;
```

<br /> <img src="/images/knowledge-model7.png" style="width: 400px;"/> <br />


### Data Validation

To ensure data is correctly structured (i.e. consistent) with respect to the ontology, all data instances are validated against the ontology constraints. All the explicitly represented ontology constraints, together with the inherited ones, form complete schema templates for particular concept types, which guide the validation. The following insertion will fail, because it is attempting to form an `employment` relationship between two `person` entities, rather than a `person` and a `company`:

```graql
insert
  $x isa person, has name "Elizabeth Niesz";
  $y isa person, has name "John Niesz";
  (employee: $x, employer: $y) isa employment;
```


## Rule and Sub-Type Inference

Inference is a process of extracting implicit information from explicit data. Grakn supports two inference mechanisms:

1. type inference, based on the semantics of the `sub` hierarchies included in the ontology
2. rule-based inference involving user-defined IF-THEN rules.

Both mechanisms can be employed when querying the knowledge graph with Graql, thus supporting retrieval of both explicit and implicit information at query time.      

### Type Inference
The type inference is based on a simple graph traversal along the `sub` edges. Every instance of a given concept type is automatically classified as an (indirect) instance of all (possibly indirect) supertypes of that type. For example, whenever `customer sub person` is in the ontology, every instance of `customer` will be retrieved on the query `match $x isa person`. 

Similarly for roles, every instance playing a given role is inferred to also play all its (possibly indirect) super-roles. <!--For example, whenever `inst` plays the role of wife in a relation of the type `marriage`, the system will infer that `inst` plays also the role of `partner1` in that relation, given the ontology from Figure 2.-->

The type inference is set ON by default when querying Grakn.  

### Rule-Based Inference
The rule-based inference exploits a set of user-defined datalog rules and is conducted by means of the  reasoner built natively into Grakn. Every rule is declared as an instance of a built-in Grakn type `inference-rule`.

A rule is an expression of the form `lhs G1 rhs G2`, where `G1` and `G2` are a pair of Graql patterns. Whenever the left-hand-side (lhs) pattern `G1` is found in the data, the right-hand-side (rhs) pattern `G2` can be assumed to exist and optionally materialised (inserted). For example:

```graql
insert
  location sub entity;
  
  located-in sub relation,
    has-role located-subject, has-role subject-location;
    	
  located-subject sub role;
  subject-location sub role;

  transitive-location sub rule,
    lhs {
      ($x, $y) isa located-in;
      ($y, $z) isa located-in;
    }
    rhs {
      ($x, $z) isa located-in;
    }

```

<br /> <img src="/images/knowledge-model9.png" style="width: 600px;"/> <br />

<br />

The rule above expresses that, if `$x` has a `located-in` relation with `$y`, and `$y` has a `located-in` relation with `$z`, then `$x` has a `located-in` relation with `$z`. As a concrete example: King's Cross is in London, and London is in the UK, so one can infer that King's Cross is in the UK.

The rule-based inference is currently set OFF by default when querying Grakn, and can be activated as it is needed. For more detailed documentation on rules see [Graql Rules](../graql/graql-rules.html).


## Where Next?
Our [Quickstart Tutorial](../get-started/quickstart-tutorial.html) will show you how to load an ontology, rules and data into Grakn using Graql, and to make basic queries.

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
