---
title: Graph API Overview
keywords: overview
last_updated: August 10, 2016
tags: [java, graph-api]
summary: "A basic example of how to use MindmapsDB Graph API."
sidebar: documentation_sidebar
permalink: /documentation/core-api/overview.html
folder: documentation
comment_issue_id: 20
---

If you have not yet set up the MindmapsDB environment, please see the [Setup guide](../get-started/setup-guide.html). This article assumes that you are set up and have already looked at the [Quickstart Tutorial](../the-basics/quickstart-tutorial.html) that covers Graql queries.   
You can find the code below in the example code directory of the MindmapsDB distribution zip.

## Introduction

MindmapsDB Graph API is the implementation of the object model discussed in
[MindmapsDB Basics](../the-basics/mindmaps-basics.html). It allows you to create MindmapsDB Graphs using Java 8. 

### Initialising a MindmapsDB Graph in Java    

Make sure you have MindmapsDB Engine running locally by using `mindmaps.sh start`.
Once you have done that, a thread-bound graph can be retrieved by:

```java
MindmapsGraph mindmapsGraph = Mindmaps.factory(Mindmaps.DEFAULT_URI, "my-graph").getGraph();
```

Alternatively an in-memory graph can be created with:

```java
MindmapsGraph mindmapsGraph = Mindmaps.factory(Mindmaps.IN_MEMORY, "my-graph").getGraph();
```

{% include note.html content="Make sure that your project depends on `mindmaps-titan-factory` at a minimum." %}

```
<dependency>
    <groupId>io.mindmaps</groupId>
    <artifactId>mindmaps-titan-factory</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Modelling The Philosophers

With a graph in place lets go through and repeat the [Quickstart
Tutorial](../the-basics/quickstart-tutorial) using the Java Graph API.

### Entity Types

First we create some philosophers, so we define the category of `people`:

```java
    EntityType person = mindmapsGraph.putEntityType("person");
```

Next we specify who our people are:

```java
    Entity socrates = mindmapsGraph.putEntity("Socrates", person);
    Entity plato = mindmapsGraph.putEntity("Plato", person);
    Entity aristotle = mindmapsGraph.putEntity("Aristotle", person);
    Entity alexander = mindmapsGraph.putEntity("Alexander", person);
```

![](/images/phil.png)

We can query for all the people:

```java
System.out.println("Instances of Person:");
 person.instances().forEach(i -> System.out.println(i.getId()));
```

Next, let's add some `schools` of thought:

```java
    EntityType school = mindmapsGraph.putEntityType("school");
    Entity peripateticism = mindmapsGraph.putEntity("Peripateticism", school);
    Entity platonism = mindmapsGraph.putEntity("Platonism", school);
    Entity idealism = mindmapsGraph.putEntity("Idealism", school);
    Entity cynicism = mindmapsGraph.putEntity("Cynicism", school);
```

And look one up:

```java
mindmapsGraph.getEntity("Cynicism").getId();
```   

### Relation Types

Now that we have some data lets specify how these philosophers relate to these schools. First we create the `Relation Type` and its `Role Types`:

```java
RoleType philosopher = mindmapsGraph.putRoleType("philosopher");
RoleType philosophy = mindmapsGraph.putRoleType("philosophy");
RelationType practice = mindmapsGraph.putRelationType("practice").
  hasRole(philosopher).hasRole(philosophy);
```

Next we can relate our philosophers and schools to each other:

```java
    mindmapsGraph.addRelation(practice).  
      putRolePlayer(philosopher, socrates).
      putRolePlayer(philosophy, platonisim);

    mindmapsGraph.addRelation(practice).
      putRolePlayer(philosopher, plato).
      putRolePlayer(philosophy, idealism);

    mindmapsGraph.addRelation(practice).
      putRolePlayer(philosopher, plato).
      putRolePlayer(philosophy, platonisim);

    mindmapsGraph.addRelation(practice).
      putRolePlayer(philosopher, aristotle).
      putRolePlayer(philosophy, peripateticism);
```

![](/images/practice.png)

Now we can define how our philosophers relate to each other:

```java
    RoleType teacher = mindmapsGraph.putRoleType("teacher");
    RoleType student = mindmapsGraph.putRoleType("student");
    RelationType education = mindmapsGraph.putRelationType("education").
      hasRole(teacher).hasRole(student);

    mindmapsGraph.putRelation(education).putRolePlayer(teacher, socrates).
      putRolePlayer(student, plato);
    mindmapsGraph.putRelation(education).putRolePlayer(teacher, plato).
      putRolePlayer(student, aristotle);
    mindmapsGraph.putRelation(education).putRolePlayer(teacher, aristotle).
      putRolePlayer(student, alexander);
```

## Resources

Some people have special titles and epithets and we want to talk about that.
So, we'll create some resource types that can be attached to a person:

```java
ResourceType<String> title = mindmapsGraph.putResourceType("title", Data.STRING);
ResourceType<String> epithet = mindmapsGraph.putResourceType("epithet", Data.STRING);

person.playsRole(hasResourceTarget);
title.playsRole(hasResourceValue);
epithet.playsRole(hasResourceValue);
```

Let's make Alexander "Great"!

```java
Resource<String> theGreat = mindmapsGraph.putResource("The Great", epithet);

mindmapsGraph.putRelation(hasResource).
  putRolePlayer(hasResourceTarget, alexander).
  putRolePlayer(hasResourceValue, theGreat);
```

This is a quick way to add a relation between `Alexander` and an `epithet` with value `"The Great"`.

![](/images/epithet.png)

Let's add the rest of Alexander's titles while we're at it:

```java
Resource<String> hegemon = mindmapsGraph.putResource("Hegemon", title);
Resource<String> kingOfMacedon = mindmapsGraph.putResource("King of Macedon", title);
Resource<String> shahOfPersia = mindmapsGraph.putResource("Shah of Persia", title);
Resource<String> pharaohOfEgypt = mindmapsGraph.putResource("Pharaoh of Egypt", title);
Resource<String> lordOfAsia = mindmapsGraph.putResource("Lord of Asia", title);

mindmapsGraph.putRelation(hasResource).
  putRolePlayer(hasResourceTarget, alexander).
  putRolePlayer(hasResourceValue, hegemon);

mindmapsGraph.putRelation(hasResource).
  putRolePlayer(hasResourceTarget, alexander).
  putRolePlayer(hasResourceValue, kingOfMacedon);

mindmapsGraph.putRelation(hasResource).
  putRolePlayer(hasResourceTarget, alexander).
  putRolePlayer(hasResourceValue, shahOfPersia);

mindmapsGraph.putRelation(hasResource).
  putRolePlayer(hasResourceTarget, alexander).
  putRolePlayer(hasResourceValue, pharaohOfEgypt);

mindmapsGraph.putRelation(hasResource).
  putRolePlayer(hasResourceTarget, alexander).
  putRolePlayer(hasResourceValue, lordOfAsia);
```

## Where Next?
If you want to find out more about using the Graph API, please take a look at the documentation about how to define a [simple schema](../core-api/simple-schema-definition.html) or an [advanced schema](../core-api/advanced-schema-definition.html) for a MindmapsDB graph.

{% include links.html %}

## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
        <tr>
        <td>v0.1.0</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>

## Comments
Want to leave a comment? Visit <a href="https://github.com/mindmapsdb/docs/issues/20" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.