---
title: Grakn Basics
keywords: setup, getting started, basics
last_updated: August 24, 2016
tags: [getting-started]
summary: "Introducing the basics of the Grakn object model."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/grakn-basics.html
folder: documentation
comment_issue_id: 17
---
{% include note.html content="This page is deprecated and will soon be removed. Please see the replacement page about [the Grakn Knowledge Model](./grakn-knowledge-model.html)" %}

## Schema and Data

In Grakn, a graph is made of two layers: the database schema (which you can also think of as an ontology) and the data layer.

***The schema should be clearly defined before loading data into the graph.*** The schema is used to formalise the data within a specific domain. If data is about healthcare, then we need to design a schema specific to healthcare. If data is about movies, then we need to define an schema specific to the movie industry.   

Example schema concepts could include `car`, `person`, `movie`.

***The data is categorised by the schema.*** It is essentially everything
we wish to model. 

Example data concepts could include `a VW Golf TDI`, `Tom Cruise`, `Mission: Impossible`.

## The Object Model - Schema

The Grakn stack provides a formal structure to a graph database.  The following figure represents the inheritance structure of the schema elements. These are used to design our domain specific schema. 

![The schema object model](/images/ontology_object_model.png)


### Concept

Just as a [graph](https://en.wikipedia.org/wiki/Graph_theory) is defined by vertices and the edges between them, in Grakn, a
graph is defined by concepts and the relationships between those
concepts.  Every vertex is a concept and must, at minimum, have a unique ID. This is a string that identifies the concept.    

### Concept Type

A schema element that represents something that can have instances, and is extended into the following types:   

* Entity Types  
* Relation Types  
* Role Types  
* Resource Types  


### Entity Type

An Entity Type is used to represent a category e.g. `person`, `vehicle`, `movie` or `country`. 

### Resource Type

A Resource Type is used to represent an attribute associated with an Entity Type, e.g. `birthdate` or `name`. Resource Types are effectively properties, and consist of primitive types and values. They are very much like "data properties" in OWL.

Resource types have the following properties:

* **Datatype** - Indicates the datatype of the resource. For example if the resource type is `age` the datatype would be `long`.   

* **Regex** - Optional. Can be used to constrain string data types to specific regex patterns.   

* **Unique** - A boolean which indicates if the resource should be unique
across the graph.

### Relation Type and Role Type

In Grakn, the graph enables us to model complex n-ary relationships. To do this we define a Relation Type, which is used to model relationships, and an associated pair of Role Types that are used to represent the two roles within that relationship. There is one Role Type for each entity to explain fully how the two relate to one another. 

For example, to model that a `person` acted in a `movie` we need to use a Relation Type such as `has-cast`. The Role Type for the `person` is  `actor`, while the Role Type for the movie is `production-with-cast`. 

Role Types are associated with a particular Relation Type between Entity Types, and constrain it to ensure that only instances of the correct types are involved. In the movie domain, for example, the Relation Type `has-cast` involves a `person` Entity Type playing the Role Type `actor`. An Entity Type `dog` is implicitly excluded from playing the role `actor` - to include a dog in the cast of a movie, the `dog` Entity Type must explicitly be given the role `actor` too.

The Role Types associated with a Relation Type can only be used for that relationship.

### Subclasses

Instances of the four meta-types described above can be subclassed. For example, subclasses of `person` (which is an instance of the meta-type EntityType) could be `man` and `woman`.  This simple subclassing allows us to expand and deepen our schema to whatever granularity of detail is needed.  

{% include note.html content="In Grakl, we use the shorthand `sub ('subclass')` when referring to sub-typing or specialisation. <br />
For example, `man sub person`. <br />
We use the shorthand `isa` ('is a') to represent the type relationship. <br />
For example, `car isa vehicle`.
" %}

For example a simple schema would be `car` `isa` `vehicle` but with subclassing we could go as far as `a VW Gold TDI 2014` `isa` `VW` `sub` `car` `isa` `vehicle`.

Types can have an additional property, `abstract`, which indicates the type cannot have direct instances. This is useful when we want to define a deeper schema. For example if we have `person` and its subclasses `man` and `woman`, then we may want to define `person` as `abstract` to prohibit it from having any direct instances, whereas the concrete types `man` and `woman` can have direct instances.


## The Object Model - Data
The following figure represents the inheritance structure of the data elements.  

![The data object model](/images/data_object_model.png)

### Instance

Instances are data elements. They are named as such because they are instances of the Types described above. For example: a `BMW` is an instance of the Entity Type `car` and `09/10/1988` is an instance of Resource Type `date`.

### Entity

An instance of the Entity Type (which is known as an Entity)  represents an actual item of data. For example, Entity Type `car` could have Entities `a BMW`, `this Mercedes`, and `that Volkswagen`. Grakn enables us to model relationships between these entities, once they have been defined.

### Resource

Resources are instances of the Resource Type described above. They allow us to attribute data to a specific instance. For example, if `Alice` is an instance of `person` we could model Alice's birthday `09/10/1988` as an instance of the Resource Type `birthdate`.  The Resource object allows us to impose some additional constraints, such as ensuring that it conforms to a regex pattern or is of a particular data type.

### Relation

A Relation represents an instance of a Relation Type. Essentially a Relation describes a relationship between instances of a specific type. For example, a Relation between `Tom Cruise` (isa `person`) and `Mission: Impossible` (isa `movie`) where `Tom Cruise` plays the Role Type `actor` and `Mission: Impossible` plays the Role Type `production-with-crew`. 

## An Example Graph Structure

### Subclasses

Consider the following example of male and female philosopers.

![Male and Female philosophers](/images/example_super.png)

### Roles
We can define the following structure where people can now play the roles of teachers or students:

![A simple example of allowing a Type to play specific Role
Types.](/images/example_playsrole.png)

### Relation Types

Expanding our philosophers example we could define a Relation Type, `education` to encapsulate the needed roles:

![Expanding the example to illustrate how a relation type is linked with Role
Types.](/images/example_relationtype.png)

### Defining Relations

Finally, we can expand our philosophers example to define that Plato taught Aristotle:

![An example of a Relation. The dark blue represents the Relation which is an
instance of
`Education`](/images/example_relation.png)

{% include note.html content="When creating a relation you must ensure that the type of the Instances are allowed to play the Role Types. For example, in the above graph if we forgot to define that the Type `person` is allowed to play the Role `teacher` then the Relation between `Plato` and `Aristotle` would not be valid." %}


<!-- Removed a section on the meta ontology. We could create a separate article about this later if we think it's useful --> 

## Where Next?
Our [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) will show you how to load a schema and data into Grakn using Graql.

You can find additional example code and documentation on this portal. We are always adding more and welcome ideas and improvement suggestions. Please get in touch!

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/17" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.