---
title: Graph API Overview
keywords: overview
last_updated: August 10, 2016
tags: [java, graph-api]
summary: "A basic example of how to use Grakn's Graph API."
sidebar: documentation_sidebar
permalink: /documentation/core-api/overview.html
folder: documentation
comment_issue_id: 20
---

If you have not yet set up the Grakn environment, please see the [Setup guide](../get-started/setup-guide.html). This article assumes that you are set up and have already looked at the [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) that covers Graql queries.   
You can find the code below in the example code directory of the Grakn distribution zip.

## Introduction

In Grakn, the Graph API is the implementation of the object model discussed in
[Grakn Basics](../the-basics/mindmaps-basics.html). It allows you to create graphs using Java 8. 

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
    <groupId>io.grakn</groupId>
    <artifactId>grakn-titan-factory</artifactId>
    <version>0.6.0</version>
</dependency>
```

## Modelling The Philosophers

With a graph in place lets go through and repeat the [Quickstart
Tutorial](../the-basics/quickstart-tutorial) using the Java Graph API.

### Entity Types

First we create some philosophers, so we define the category of `people`:

```java
    EntityType person = graknGraph.putEntityType("person");
```

Next we specify who our people are:

```java
    ResourceType<String> name = graknGraph.putResourceType("name", ResourceType.DataType.STRING);

    Resource<String> socratesName  = graknGraph.putResource("Socrates", name);
    Resource<String> platoName     = graknGraph.putResource("Socrates", name);
    Resource<String> aristotleName = graknGraph.putResource("Socrates", name);
    Resource<String> alexanderName = graknGraph.putResource("Socrates", name);

    Entity socrates  = graknGraph.addEntity(person);
    socrates.hasResource(socratesName);
    Entity plato     = graknGraph.addEntity(person);
    plato.hasResource(platoName);
    Entity aristotle = graknGraph.addEntity(person);
    aristotle.hasResource(aristotleName);
    Entity alexander = graknGraph.addEntity(person);
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

    Resource<String> peripateticismName  = graknGraph.putResource("Peripateticism", name);
    Resource<String> platonismName       = graknGraph.putResource("Platonism", name);
    Resource<String> idealismName        = graknGraph.putResource("Idealism", name);
    Resource<String> cynicismName        = graknGraph.putResource("Cynicism", name);

    Entity peripateticism = graknGraph.addEntity(school);
    peripateticism.hasResource(peripateticismName);
    Entity platonism      = graknGraph.addEntity(school);
    platonism.hasResource(platonismName);
    Entity idealism       = graknGraph.addEntity(school);
    idealism.hasResource(idealismName);
    Entity cynicism       = graknGraph.addEntity(school);
    cynicism.hasResource(cynicismName);
```

And look one up:

```java
graknGraph.getResource("Cynicism", name).owner();
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
    graknGraph.addRelation(practice).  
      putRolePlayer(philosopher, socrates).
      putRolePlayer(philosophy, platonism);

    graknGraph.addRelation(practice).
      putRolePlayer(philosopher, plato).
      putRolePlayer(philosophy, idealism);

    graknGraph.addRelation(practice).
      putRolePlayer(philosopher, plato).
      putRolePlayer(philosophy, platonism);

    graknGraph.addRelation(practice).
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

    graknGraph.addRelation(education).putRolePlayer(teacher, socrates).
      putRolePlayer(student, plato);
    graknGraph.addRelation(education).putRolePlayer(teacher, plato).
      putRolePlayer(student, aristotle);
    graknGraph.addRelation(education).putRolePlayer(teacher, aristotle).
      putRolePlayer(student, alexander);
```

## Resources

Some people have special titles and epithets and we want to talk about that.
So, we'll create some resource types that can be attached to a person:

```java
ResourceType<String> title = graknGraph.putResourceType("title", ResourceType.DataType.STRING);
ResourceType<String> epithet = graknGraph.putResourceType("epithet", ResourceType.DataType.STRING);
```

Let's make Alexander "Great"!

```java
Resource<String> theGreat = graknGraph.putResource("The Great", epithet);
alexander.hasResource(theGreat);
```

This is a quick way to add a relation between `Alexander` and an `epithet` with value `"The Great"`.

![](/images/epithet.png)

Let's add the rest of Alexander's titles while we're at it:

```java
Resource<String> hegemon = graknGraph.putResource("Hegemon", title);
Resource<String> kingOfMacedon = graknGraph.putResource("King of Macedon", title);
Resource<String> shahOfPersia = graknGraph.putResource("Shah of Persia", title);
Resource<String> pharaohOfEgypt = graknGraph.putResource("Pharaoh of Egypt", title);
Resource<String> lordOfAsia = graknGraph.putResource("Lord of Asia", title);

alexander.hasResource(hegemon);
alexander.hasResource(kingOfMacedon);
alexander.hasResource(shahOfPersia);
alexander.hasResource(pharaohOfEgypt);
alexander.hasResource(lordOfAsia);
```

## Where Next?
If you want to find out more about using the Graph API, please take a look at the documentation about how to define a [simple schema](../core-api/simple-schema-definition.html) or an [advanced schema](../core-api/advanced-schema-definition.html).

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/20" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.