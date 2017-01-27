---
title: Defining a Basic Ontology
keywords: schema
last_updated: December, 2016
tags: [graph-api, java, advanced-grakn]
summary: "Demonstrates how to create a basic ontology"
sidebar: documentation_sidebar
permalink: /documentation/building-an-ontology/basic-ontology.html
folder: documentation
comment_issue_id: 22
---

{% include warning.html content="Please note that this page is in progress and subject to revision." %}

## Introduction

{% include links.html %}

In this section we are going to run through the construction of a basic ontology.
Before starting it is worth reading through [Grakn Basics](../the-basics/grakn-basics.html) to get a full understanding of ontology engineering.
 
The process we will follow is a general guideline as to how you may start designing your ontology.
The ontology we will be building will be used for a genealogy graph used for mapping out a family tree. 

It is worth noting that the ontology does not need to completely finalised before loading data. 
The ontology of a grakn graph can be expanded even after loading data.

## Identifying Entity Types

The first step is about identifying the categories of things that will be in your graph.
For example if you are modelling a retail store, valid categories may be `product`, `Electronics`, `Books`, etc . . . 
It is up to you to decide the granularity of your categories. 

For our genealogy graph we know that it will mostly be filled with people.
So we can create that entity type:

    person sub entity;
    
Naturally we could break this up into `man` and `woman` but for this example we are going to keep things simple.  

## Describing Entity Types

Grakn provides you with the ability to attach resources to entity types. 
These resources help in describing what a specific Entity Type is composed of.
For example a `car` Entity Type could include an `engine`, a `licence number`, and a `transmission type` as resources which help describe that car.

So what help describe a `person`? 
Philosophical debates aside let us go with something simple. A `person` typically has a `firstname`, a `lastname`, a `gender`, and an `age`.
We can model this and other resources which identify a person with:

    person sub entity
	    has-resource identifier
	    has-resource firstname
	    has-resource surname
	    has-resource middlename
	    has-resource picture
	    has-resource age
	    has-resource birth-date
	    has-resource death-date
	    has-resource gender;

Remember we still need to specify that these things are resources:

    identifier sub resource datatype string;
    firstname sub resource datatype string;
    surname sub resource datatype string;
    middlename sub resource datatype string;
    picture sub resource datatype string;
    age sub resource datatype long;
    birth-date sub resource datatype string;
    death-date sub resource datatype string;
    gender sub resource datatype string;

## Identifying Relationships And Their Roles

The next step is to ask how your data is connected.
What are the relationships between your data? 
This can be between different Entity Types, for example, a `person` **drives** a `car`, or even between the same entity types, for example, a `person` **marries** another `person`.
In a Grakn Graph N-ary relationships are also possible. For example, a `person` has a `child` with another `person`.
 
So what types of relationships exist in a family tree?  
For our example lets just go with `marriage` and `parenshiop`.
The next question is what roles typically make up the relationship? 
Well our `marriage`s will be between two spouses, `spouse1` and `spouse2`.
For `parenshiop` we will say it is composed of a `parent` and a `child`.

So now we can define these Relation Types more formally:

    marriage sub relation
	    has-role spouse1
	    has-role spouse2
	    has-resource picture;

    spouse1 sub role;
    spouse2 sub role;

    parentship sub relation
    	has-role parent
	    has-role child;

    parent sub role;
    child sub role;

## Allowing Roles To Be Played

Above we defined 4 roles, `spouse1`, `spouse2`, `parent`, and `child`. 
The next step is to give our Entity Types permission to play specific Roles. 
We do this explicitly so that we don't accidentally relate data which should not be related. 
For example, this will prevent us from accidentally saying that a `dog` and a `person` can have a child.
 
For this current example we only have one entity type which can play all our current roles so we explicitly state that with:  

    person sub entity
	    plays-role parent
	    plays-role child
	    plays-role spouse1
	    plays-role spouse2
	    
and with this final statement we have completed our basic genealogy ontology.

## The Complete Ontology

Your final ontology will now look something like this:

    # Entities

    person sub entity
	    plays-role parent
	    plays-role child
	    plays-role spouse1
	    plays-role spouse2
	    has-resource identifier
	    has-resource firstname
	    has-resource surname
	    has-resource middlename
	    has-resource picture
	    has-resource age
	    has-resource birth-date
	    has-resource death-date
	    has-resource gender;

    # Resources

    identifier sub resource datatype string;
    firstname sub resource datatype string;
    surname sub resource datatype string;
    middlename sub resource datatype string;
    picture sub resource datatype string;
    age sub resource datatype long;
    birth-date sub resource datatype string;
    death-date sub resource datatype string;
    gender sub resource datatype string;

    # Roles and Relations

    marriage sub relation
	    has-role spouse1
	    has-role spouse2
	    has-resource picture;

    spouse1 sub role;
    spouse2 sub role;

    parentship sub relation
	    has-role parent
    	has-role child;

    parent sub role;
    child sub role;
    
In this tutorial we described our entity type `person` across separate steps.
This was done to demonstrate the typical thought process when creating an ontology.
It is typically good practice to group entity type definitions together as above. 

Why not try loading data next and issuing some queries?

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/22" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
