---
title: Mindmaps Basics
keywords: setup, getting started, basics
last_updated: August 24, 2016
tags: [getting_started]
summary: "Introducing the basics of the Mindmaps object model."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/mindmaps-basics.html
folder: documentation
---
# Mindmaps Basics

## Ontology and Data

A Mindmaps graph is made of two layers: the ontology and the data layer.

***A Mindmaps ontology is analogous to a database schema***. Ideally, it should be clearly defined before loading data into the graph. The ontology is used to formalise the data within a specific domain. If data is about healthcare, then we need to design an ontology specific to healthcare. If data is about movies, then we need to define an ontology
specific to the movie industry.   

Example ontological concepts could include `Car`, `Person`, `Movie`.

The **data** is categorised by the ontology. It is essentially everything
we wish to model. 

Example data concepts could include `a VW Golf TDI`, `Tom Cruise`, `Mission: Impossible`.

## The Object Model - Ontology

The Mindmaps stack provides a formal structure to a graph database.  The following figure represents the inheritance structure of the ontological elements. These are used to design our domain specific ontology. 

**GRAPHIC TO DO**

### Concept

Just as a graph is defined by vertices and the edges between them, a
Mindmaps graph is defined by concepts and the relationships between those
concepts.  Every vertex is a concept and has the following properties:

#### Unique Properties

Properties which must be unique throughout the entire graph.

**Id** - A concept must, at minimum, have a unique ID. This is a string which identifies the concept. For example if a concept is representing a person it could be their id number.   
**Subject** - An optional unique string which can be used to refer to
external resources relating to the concept. For example if the concept is used to represent a country or city the subject could point to the wikipedia page of that city.   

#### Additional Properties

Properties that store additional information about the concept and do not have to be unique across the graph.

**Value** - A string value which can be used to store an additional piece of data. For example this could represent a person's name, a movie title, or any representative string.

### Type

A Type is an ontological element that represents something that can have instances, and is extended into the following types:   
-  Entity Types  
-  Relation Types  
-  Role Types  
-  Resource Types  

Types can be subclassed. For example, subclasses for the Type, `Person` could be `Man` and `Woman`.  This simple subclassing allows us to expand and deepen our ontology to whatever granularity of detail is needed. For example a simple ontology would be `Car` `is a` `Vehicle` but with subclassing we could go as far as `a VW Gold TDI 2014` `isa` `VW` `ako` `Car` `isa` `Vehicle`.

Types can have an additional property, `Abstract`, which indicates the type cannot have instances. This is useful when we want to define a deeper ontology. For example if we have three ontology elements: `Person` and its subclasses `Man` and `Woman`, then we may want to define `Person` as `Abstract` to prohibit it from having any direct instances.


### Entity Type

A Entity Type is used to represent a category of entities, e.g. `Vehicle`, `Movie` or `Country`. 

### Resource Type

A Resource Type is used to represent a category of resources. e.g. `birthdate` or `name`. Resource Types have additional properties including:

1. **Datatype** - Indicates the datatype of the resource. For example if the resource is `Age` the the datatype would be **integer**
2. **Regex** - Used to constrain string data types to specific regex patterns.
3. **Unique** - A boolean which indicates if the resource should be unique
across the graph.

### Relation Type

A Relation Type is used to model relationships. For example, to model that `Tom Cruise` is an `Actor` in `Mission: Impossible` we need to use a Relation Type such as `Has Cast`. This allows us to model that
`Mission: Impossible` `Has Cast` `Tom Cruise`.

A Mindmaps graph enables us to model complex n-ary relationships. To do this we define a Relation Type and link it with roles which flesh out the details of the relationship. For example, in the movie domain a valid Relation Type could be `Cast` which has the Role Types `Actor` and `Movie With Cast`.

### Role Type

A Role Type is used to represent a role in a relationship, and how concepts relate to one another. For example, if we wish to model that `Jon Voight` is an `Actor` in `Mission: Impossible` then we need to define a Role Type `Actor`. However, `Jon Voight` would have the role of `Father` relating to his daughter `Angelina Jolie`.

Types constrain the roles that can be played to ensure that only instances of the correct type are involved in the correct relationships. For example, in the movie domain, a `Person` Type can play the role `Actor`. This would mean that Instances of the Type `Person`, such as `Al Pacino` or `Leonardo DiCaprio` are allowed to be `Actors` in the relationship involving the `Actor` role. At the same time this would implicitly
define that an Instance of Concept Type `Dog` would not be allowed to play the role `Actor`.


## The Object Model - Data
The following figure represents the inheritance structure of the data elements.  

** GRAPHIC TO DO **

### Instance

Instances are data elements. They are named as such because they are instances of the Types described above. For example: a `BMW` is an instance of the Entity Type `Car` and `09/10/1988` is an instance of Resource Type `Date`.

### Entity

An instance of the Entity Type (which is known as an Entity)  represents an actual item of data. For example, Entity Type `Car` could have Entities `a BMW`, `this Mercedes`, and `that Volkswagen`. The Mindmaps graph enables us to model relationships between these entities, once they have been defined.

### Resource

Resources are instances of the Resource Type described above. They allow us to attribute data to a specific instance. For example, if `Alice` is an instance of `Person` we could model Alice's birthday `09/10/1988` as an instance of the Resource Type `Birthdate`.  The Resource object allows us to impose some additional constraints, such as ensuring that it conforms to a regex pattern or is of a particular data type.

### Relation

A Relation represents an instance of a Relation Type. We can use a Relation to model multiple relationships between instances. For example, if `Alice` and `Bob` are married then we would create a Relation object `Marriage`, which is an instance of Relation Type, to model that relationship. At the same time, Alice could work for an organisation `Apache`. Similarly, we would create an instance of the Relation
Type `Works For` to model that relationship.

Essentially a Relation describes a relationship of a specific type between
instances of a specific type. For example, one Relation can define that the Instance `Tom Cruise` plays the Role Type `Actor` with the movie Instance `Mission: Impossible` which plays the Role Type `Movie With Cast`.


## An Example Graph Structure

### Subclasses

Consider the following example of male and female philosopers.

![Male and Female philosophers](/images/example_super.png)

{% include note.html content="In a Mindmaps graph, we use the shorthand `isa` when representing the type relationship. <br /> 
For example, `Aristotle` `is a` `Man`.   <br/>
We use the shorthand `ako` when referring to a Type being the sub type of another Type. <br />
For example, `Man` `a kind of (ako)` `Person`." %}

### Roles
We can define the following structure where people can now play the roles of teachers or students:

![A simple example of allowing a Type to play specific Role
Types.](/images/examples_playsroles.png)

### Relation Types

Expanding our philosophers example we could define a Relation Type, `Education` to encapsulate the needed roles:

![Expanding the example to illustrate how a relation type is linked with Role
Types.](/images/example_relationtype.png)

### Defining Relations

Finally, we can expand our philosophers example to define that Plato taught Aristotle:

![An example of a Relation. The dark blue represents the Relation which is an
instance of
`Education`](/images/example_relation.png)

{% include note.html content="When creating a relation you must ensure that the type of the Instances are allowed to play the Role Types. For example, in the above graph if we forgot to define that the Type `Person` is allowed to play the Role `Teacher` then the Relation between `Plato` and `Aristotle` would not be valid." %}


<!-- Removed a section on the meta ontology. We could create a separate article about this later if we think it's useful --> 

{% include links.html %}
## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
        <tr>
        <td>v1.01</td>
        <td>24/08/2016</td>
        <td>Updated content and formatting.</td>        
    </tr>

</table>