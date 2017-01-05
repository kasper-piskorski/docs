---
title: Graph API Overview
keywords: overview
last_updated: August 10, 2016
tags: [java, graph-api]
summary: "A basic example of how to use Grakn's Graph API."
sidebar: documentation_sidebar
permalink: /documentation/building-an-ontology/overview.html
folder: documentation
comment_issue_id: 20
---

{% include warning.html content="This page is deprecated and will soon be removed." %}

If you have not yet set up the Grakn environment, please see the [Setup guide](../get-started/setup-guide.html). This article assumes that you are set up and have already looked at the [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) that covers Graql queries.   
You can find the code below in the example code directory of the Grakn distribution zip.

## Introduction

In Grakn, the Graph API is the implementation of the object model discussed in
[Grakn Basics](../the-basics/grakn-basics.html). It allows you to create graphs using Java 8. 

### Initialising Grakn in Java    

Make sure you have Grakn Engine running locally by using `grakn.sh start`.
Once you have done that, a thread-bound graph can be retrieved by:    

```java-test-ignore
GraknGraph graknGraph = Grakn.factory(Grakn.DEFAULT_URI, "my-graph").getGraph();
```

Alternatively an in-memory graph can be created with:     

```java-test-ignore
GraknGraph graknGraph = Grakn.factory(Grakn.IN_MEMORY, "my-graph").getGraph();
```

{% include note.html content="Make sure that your project depends on `grakn-titan-factory` at a minimum." %}

```
<dependency>
    <groupId>ai.grakn</groupId>
    <artifactId>grakn-titan-factory</artifactId>
    <version>0.9.0</version>
</dependency>
```

## Modelling The Philosophers

With a graph in place lets go through and repeat the [Quickstart
Tutorial](../the-basics/quickstart-tutorial.html) using the Java Graph API.

### Entity Types

First we create some philosophers, so we define the category of `people`:

```java
    EntityType person = graknGraph.putEntityType("person");
```

Next we specify who our people are:

```java
    ResourceType<String> name = graknGraph.putResourceType("name", ResourceType.DataType.STRING);
    person.hasResource(name);

    Resource<String> socratesName  = name.putResource("Socrates");
    Resource<String> platoName     = name.putResource("Plato");
    Resource<String> aristotleName = name.putResource("Aristotle");
    Resource<String> alexanderName = name.putResource("Alexander");

    Entity socrates  = person.addEntity();
    socrates.hasResource(socratesName);
    Entity plato     = person.addEntity();
    plato.hasResource(platoName);
    Entity aristotle = person.addEntity();
    aristotle.hasResource(aristotleName);
    Entity alexander = person.addEntity();
    alexander.hasResource(alexanderName);
```

![](/images/phil.png)

We can query for all the people:

```java
System.out.println("Instances of Person:");
for (Entity i : person.instances()) {
    System.out.println(i.resources(name).iterator().next());
}
```

Next, let's add some `schools` of thought:

```java
    EntityType school = graknGraph.putEntityType("school");
    school.hasResource(name);

    Resource<String> peripateticismName  = name.putResource("Peripateticism");
    Resource<String> platonismName       = name.putResource("Platonism");
    Resource<String> idealismName        = name.putResource("Idealism");
    Resource<String> cynicismName        = name.putResource("Cynicism");

    Entity peripateticism = school.addEntity();
    peripateticism.hasResource(peripateticismName);
    Entity platonism      = school.addEntity();
    platonism.hasResource(platonismName);
    Entity idealism       = school.addEntity();
    idealism.hasResource(idealismName);
    Entity cynicism       = school.addEntity();
    cynicism.hasResource(cynicismName);
```

And look one up:

```java
name.getResource("Cynicism").owner();
```   

### Relation Types

Now that we have some data lets specify how these philosophers relate to these schools. First we create the `Relation Type` and its `Role Types`:

```java
RoleType philosopher = graknGraph.putRoleType("philosopher");
RoleType philosophy = graknGraph.putRoleType("philosophy");
RelationType practice = graknGraph.putRelationType("practice").
  hasRole(philosopher).hasRole(philosophy);
```

Next we can relate our philosophers and schools to each other:

```java
    practice.addRelation().  
      putRolePlayer(philosopher, socrates).
      putRolePlayer(philosophy, platonism);

    practice.addRelation().
      putRolePlayer(philosopher, plato).
      putRolePlayer(philosophy, idealism);

    practice.addRelation().
      putRolePlayer(philosopher, plato).
      putRolePlayer(philosophy, platonism);

    practice.addRelation().
      putRolePlayer(philosopher, aristotle).
      putRolePlayer(philosophy, peripateticism);
```

![](/images/practice.png)

Now we can define how our philosophers relate to each other:

```java
    RoleType teacher = graknGraph.putRoleType("teacher");
    RoleType student = graknGraph.putRoleType("student");
    RelationType education = graknGraph.putRelationType("education").
      hasRole(teacher).hasRole(student);

    education.addRelation().putRolePlayer(teacher, socrates).
      putRolePlayer(student, plato);
    education.addRelation().putRolePlayer(teacher, plato).
      putRolePlayer(student, aristotle);
    education.addRelation().putRolePlayer(teacher, aristotle).
      putRolePlayer(student, alexander);
```

## Resources

Some people have special titles and epithets and we want to talk about that.
So, we'll create some resource types that can be attached to a person:

```java
ResourceType<String> title = graknGraph.putResourceType("title", ResourceType.DataType.STRING);
ResourceType<String> epithet = graknGraph.putResourceType("epithet", ResourceType.DataType.STRING);
person.hasResource(title).hasResource(epithet);
```

Let's make Alexander "Great"!

```java
Resource<String> theGreat = epithet.putResource("The Great");
alexander.hasResource(theGreat);
```

This is a quick way to add a relation between `Alexander` and an `epithet` with value `"The Great"`.

![](/images/epithet.png)

Let's add the rest of Alexander's titles while we're at it:

```java
Resource<String> hegemon = title.putResource("Hegemon");
Resource<String> kingOfMacedon = title.putResource("King of Macedon");
Resource<String> shahOfPersia = title.putResource("Shah of Persia");
Resource<String> pharaohOfEgypt = title.putResource("Pharaoh of Egypt");
Resource<String> lordOfAsia = title.putResource("Lord of Asia");

alexander.hasResource(hegemon);
alexander.hasResource(kingOfMacedon);
alexander.hasResource(shahOfPersia);
alexander.hasResource(pharaohOfEgypt);
alexander.hasResource(lordOfAsia);
```

### How to Get Resources

#### Getting Resources When not Knowing the Entity:

Suppose you have an instance with a resource, which in Graql would look something like `my-thing has-resource name`:

How do you retrieve the name using the Graph API? There are three ways to get resources. The simplest is via its id, if that is known:
    
```java-test-ignore
<V> Resource<V> getResource(String id);
```

Another method is to use the literal value, but this returns a collection of resources:
   
```java-test-ignore
<V> Collection<Resource<V>> getResourcesByValue(V value);
```
```java
//Example:
graknGraph.getResourcesByValue("bob"); // Will get you all the resources with the value "bob"
```   

Finally you can get resource by its resource type, which returns a single resource because resources are unique to their type:

```java-test-ignore
<V> Resource<V> getResource(V value);
```
```java
//For Example:
graknGraph.getResourceType("name").getResource("bob"); //This will return a single resource with the value bob of type name
```

#### Getting Resources When Knowing The Entity

If you have an entity and want to get a resource of a particular type attached to that entity you can do so with:
   
```java-test-ignore
Collection<Resource<?>> resources(ResourceType ... resourceTypes);
```
```java
//Example
myThing = graknGraph.getEntityType("pokemon").instances().iterator().next();
resources = myThing.resources(graknGraph.getResourceType("name"));
```

#### Getting From The Resource To The Entity

If you have a resource you can get the entitie(s) attached to it with:
   
```java   
resource = resources.iterator().next();
resource.owner();
resource.ownerInstances();
```

## Where Next?
If you want to find out more about using the Graph API, please take a look at the documentation about how to define a [simple schema](./simple-schema-definition.html) or an [advanced schema](./advanced-schema-definition.html).

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/20" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
