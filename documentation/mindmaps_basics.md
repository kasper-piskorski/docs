---
title: Mindmaps Basics
---
# Mindmaps Basics

## Ontology Vs Data

A Mindmaps graph is made of two layers. The ontology and the data layer. 

The ontology is used to formalise the data within a specific domain. If our
data is about healthcare then we need to design an ontology specific to
healthcare. If our data is about movies, then we need to define an ontology
specific to the movie industry. A Mindmaps ontology is analogous to a database
schema. Ideally, It should be clearly defined before loading data into the
graph. Example of Ontological concepts could include `Car`, `Person`, `Movie`.

The data layer is categorised within the ontology. It is essentially everything
we wish to model. Examples of data concept could include `a VW Golf TDI`, `Al
Pacino`, `Godfather`. 

## The Graph Structure

A graph is defined by vertices and the edges between them. Similarly, a
Mindmaps graph is defined by concepts and the relationships between those
concepts. In the following section we will describe the ways in which concepts
can relate to one another.

### Concepts Have Types

All concepts must have a type. The type defines the category which a concept
falls within. It allows us to categorise Instances withi Types  For example the
type of an Instance, `a BMW` could be a Type, `Car`. Similarly, the type of
`Aristotle` is the Type `Man`.

The following is an example of how Instances can have a Type:

![An example of the type relationship between some Instances  and a
Type](example_types.png)

> In a Mindmaps graph we use the shorthand `isa` when representing the type
> relationship. For example, `Aristotle` `is a` `Man`.

### Types Can Be Subclassed

Mindmaps provides the ability to define rich semantic ontologies. One way of
achieving this is to allow Types to be subclassed. For example, valid
subclasses for the Type, `Vehicle` could include `Car`, `Truck`, and
`Motorbike`. 

If we expand our example above to include female philosophers we would have the
following graph:

![Expanding the previous example to include female
philosophers.](example_no_super.png)

By using subclassing we can more richly model this graph as follows:

![Expanding the example further to include
subtypes.](example_super.png)

> In a Mindmaps graph we use the shorthand `ako` when referring to a Type being
> the sub type of another Type. For example, `Man` `a kind of (ako)` `Person`.

This simple subclassing allows us to expand and deepen our ontology to whatever
granularity of detail is needed. For example a simple ontology would be `Car`
`is a` `Vehicle` but with subclassing we could go as far as `a VW Gold TDI
2014` `isa` `VW` `ako` `Car` `isa` `Vehicle`.

### Types Constrain Roles that can be Played

A Role allows us to define how concepts can relate to one another for example.
The instance `Al Pacino` could have the Role `Actor` when relating to to the
movie `Godfather`. However, `Al Pacino` would have the Role of `Father` when
relating to his daughter `Olivia Pacino`.

Types allow us to constrain the types of roles Instances can play. This allows
us to ensure that only instances of the correct type are involved in the
correct relationships. For example, in the movie domain, a `Person` Type can
play the role `Actor`. This would mean that Instances of the Type `Person`,
such as `Al Pacino` or `Leonardo DiCaprio` are allowed to be `Actors` in the
relationship involving the `Actor` role. At the same time this would implicitly
define that an Instance of Concept Type `Dog` would not be allowed to play the
role `Actor`.

Going back to our philosophers example we could define the following structure
where people can now play the roles of teachers and students:

![A simple example of allowing a Type to play specific Role
Types.](example_playsrole.png)

### Relation Types are Associated with Roles

A Mindmaps graph enables us to model complex n-ary relationships. To do this we
define a Relation Type and link it with roles which flesh out the details of
the relationship. For example, in the movie domain a valid Relation Type could
be `Cast` which has the Role Types `Actor` and `Movie With Cast`. 

Expanding our philosophers example we could define a Relation Type, `Education`
which encapsulates the needed roles:

![Expanding the example to illustrate how a relation type is linked with Role
Types.](example_relationtype.png)

### Defining Relations

Relations allow us to represent different instances of Relation Types. For
example one Relation could represent `Al Pacino` as an `Actor` in the movie
`Godfather`, while another Relation could represent `Al Pacino` as an `Actor`
in the movie `S1m0ne`. 

Essentially they encapsulate a relationship of a specific type between
instances of a specific type. For example, one Relation can define that the
Instance `Al Pacino` plays the Role Type `Actor` with the movie Instance
`Godfather` which plays the Role Type `Movie With Cast`. 

Finally, we can expand our philosophers example to define that Plato taught
Aristotle:

![An example of a Relation. The dark blue represents the Relation which is an
instance of
`Education`](example_relation.png)

> When creating a relation you must ensure that the type of the Instances are
> allowed to play the Role Types. For example, in the above graph if we forgot
> to define that the Type `Person` is allowed to play the Role `Teacher` then
> the Relation between `Plato` and `Aristotle` would not be valid.

## The Object Model

One of the benefits of using the Mindmaps stack is the ability of Mindmaps to
provide a formal structure to a graph database. This section focuses on
understanding that structure. You will be using this object model as a Mindmaps
developer.

The following figure represents the inheritance structure of the Mindmaps
object model:

![The Mindmaps Object
Model.](object_model.png)

> Types (The right branch in the above diagram) are ontological elements. They
> are used to design our domain specific ontology. For example, within the
> domain of movies valid types include: `Movie`, `Actor`, and `Director`.

> Instances (The left branch in the above diagram) are data elements. They are
> named such because they are instances of Types. For example, within the
> domain of movies, valid instances could include: `The Godfather`, `Al
> Pacino`, and `Coppola`.

### Concepts

A concept represents a thing, for example, a movie, a category etc . .  In the
Mindmaps stack every vertex is a concept. Each concept has the following
properties:

> A concept must at minimum have a unique ID. All other properties are
> optional.

#### Unique Properties

Properties which must be unique throughout the entire graph.

1. **Id** - A unique string which identifies the concept. For example if a
concept is representing a person it could be their id number.
2. **Subject** - An optional unique string which can be used to refer to
external resources relating to the concept. For example if the concept is used
to represent a country or city the subject could point to the wikipedia page of
that city.

#### Additional Properties

Properties which store additional information about the concept and do not have
to be unique across the graph.

1. **Value** - A string value which can be used to store an additional piece of
data. For example this could represent a person's name, a movie title, or any
representative string.

### Types

A Type is an ontological element represents something which can have instances.
To provider a richer ontology Instances can fall into one of the following
Types: Entity Types, Relation Types, Role Types, and Resource Types.

Types can have an additional property, Abstract, which indicates the type
cannot have instances. This is useful when we want to define a deeper ontology.
For example if we have two ontology elements `Car` which is a subclass
`Vehicle` then we may want to define `Vehicle` as abstract to prohibit it have
any direct instances. 

### Entity Types

A Entity Type is used to represent a category of entities. "Vehicle", "Movie",
"Country". These are valid examples of what a Entity type can be used to
represent.

### Resource Types

A Resource Type is used to represent a category of resources. A valid example
could include a `birthdate` or a `name`. Resource Types have additional
properties including:

1. **Data Type** - Indicates the data type of the resource. For example if the
resource is `Age` the the data type would be **integer**
2. **Regex** - Used to constrain string data types to specific regex patterns.
3. **Unique** - A boolean which indicates if the resource should be unique
across the graph.

### Relation Types

A Mindmaps graph not only models the data but also models the relationships
between the data. These relationships are categorised using Relation Types.

For example, to model that `Al Pacino` was an `Actor` in `Godfather` we would
need an instance of the Relation Type `Has Cast`. This allows us to model that
`Godfather` `Has Cast` `Al Pacino`.

### Role Types

A Role Type is used to represent a role in a relationship that a concept can
play. For example, if we wish to model that `Al Pacino` was in `Godfather` then
we define a Role Type `Actor` which `Al Pacino` can play.

### Instances

An instance represents a concept which falls under a type. For example: `a BMW`
would be an instance of the Entity Type `Car` and `09/10/1988` could be an
instance of Resource Type `Date`.

### Entities

An Instance of an Entity Type (i.e. an Entity)  represents an actual item or
piece of data which falls under a category. For example, the Entity Type `Car`
could have entities `a BMW`, `this Mercedes`, and `that Volkswagen`. One of the
most important features of a Mindmaps graph is that it enables us to model
relationships between these entities, once they have been defined.

### Resources

Resources are instances of Resource Types and they allow us to attribute data
to a specific instance. For example, if `Alice` is an instance of `Person` we
could model Alice's birthday `09/10/1988` as an instance of the Resource Type
`Birthdate`.  The resource object allows us to impose some additional
constraints on the resource, such as ensuring the resource conforms to a regex
pattern or is of a particular data type.

### Relations

A relation represents an instance of a Relation Type. They allow us to model
multiple relationships between instances. For example, if instances `Alice` and
`Bob` are married then we would create an instance of Relation Type `Marriage`
to model that relationship. At the same time, Alice could work for an
organisation Apache. Similarly, we would create an instance of the Relation
Type Works For to model that relationship.

## The Meta Ontology

The backbone of any Mindmaps graph is the domain specific ontology which you
design. The domain specific ontology also has its own meta ontoloty. The meta
ontology is a set of concepts which layout the overall structure of a Mindmaps
graph.

> Just as all roads lead to Rome, all edges lead to the meta ontology.

Lets see how the meta ontology fits in with a philosophers example:
![The meta ontology of a Mindmaps graph. The light blue concepts represent the
Mindmaps Meta
ontology.](example_meta.png)
