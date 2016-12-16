---
title: OWL Migration to Grakn
keywords: setup, getting started
last_updated: August 10, 2016
tags: [migration]
summary: "This document will teach you how to load OWL into Grakn."
sidebar: documentation_sidebar
permalink: /documentation/migration/OWL-migration.html
folder: documentation
comment_issue_id: 32
---

## Introduction
This tutorial shows you how to migrate OWL into Grakn. If you have not yet set up the Grakn environment, please see the [setup guide](../get-started/setup-guide.html).

## Migration Shell Script for OWL
The migration shell script can be found in the `bin` directory after the Grakn distribution file has been unzipped. Usage is specific to the type of migration being performed. For OWL:

```bash
usage: migration.sh owl -input <arg> [-help] [-no] [-batch <arg>] [-uri <arg>] [-keyspace <arg>] [-v]

 -b,--batch <arg>      number of rows to load at once
 -h,--help             print usage message
 -i,--input <arg>      input owl file
 -k,--keyspace <arg>   keyspace to use
 -n,--no               dry run - write to standard out - not currently supported by the OWL migrator
 -u,--uri <arg>        uri to engine endpoint
 -v,--verbose          print counts of migrated data.
```

When you have read the following, you may find our extended example of [OWL migration](../examples/OWL-migration.html) useful.

Consider the following OWL ontology:

```xml
<rdf:RDF xmlns="http://www.co-ode.org/roberts/family-tree.owl#"
     xml:base="http://www.co-ode.org/roberts/family-tree.owl"
     xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
     xmlns:owl="http://www.w3.org/2002/07/owl#"
     xmlns:xml="http://www.w3.org/XML/1998/namespace"
     xmlns:owl2xml="http://www.w3.org/2006/12/owl2-xml#"
     xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
     xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#">

    <owl:Class rdf:about="http://www.co-ode.org/roberts/family-tree.owl#Person"/>
    <owl:ObjectProperty rdf:about="http://www.co-ode.org/roberts/family-tree.owl#isAncestorOf"/>
    <owl:ObjectProperty rdf:about="http://www.co-ode.org/roberts/family-tree.owl#isParentOf"/>
    <owl:ObjectProperty rdf:about="http://www.co-ode.org/roberts/family-tree.owl#hasParent"/>

    <owl:ObjectProperty rdf:about="http://www.co-ode.org/roberts/family-tree.owl#hasAncestor">
        <owl:inverseOf rdf:resource="http://www.co-ode.org/roberts/family-tree.owl#isAncestorOf"/>
        <rdf:type rdf:resource="http://www.w3.org/2002/07/owl#TransitiveProperty"/>
        <rdfs:domain rdf:resource="http://www.co-ode.org/roberts/family-tree.owl#Person"/>
        <owl:propertyChainAxiom rdf:parseType="Collection">
             <rdf:Description rdf:about="http://www.co-ode.org/roberts/family-tree.owl#hasParent"/>
             <rdf:Description rdf:about="http://www.co-ode.org/roberts/family-tree.owl#hasAncestor"/>
        </owl:propertyChainAxiom>
    </owl:ObjectProperty>

    <owl:NamedIndividual rdf:about="#Witold">
        <rdf:type rdf:resource="http://www.co-ode.org/roberts/family-tree.owl#Person"/>
    </owl:NamedIndividual>

    <owl:NamedIndividual rdf:about="#Stefan">
        <rdf:type rdf:resource="http://www.co-ode.org/roberts/family-tree.owl#Person"/>
        <isParentOf rdf:resource="#Witold"/>
    </owl:NamedIndividual>
<\rdf:RDF>
```

The ontology defines a single class (type) `Person` as well as two instances of the class - individuals `Witold` and `Stefan`. The ontology defines properties `hasAncestor` and its inverse `isAncestorOf` as well as `hasParent` and `isParentOf` properties. The `hasAncestor` property is defined as transitive and additionally defines a property chain which corresponds to the rule:

```
hasAncestor(X, Y) :- hasParent(X, Z), hasAncestor(Z, Y);
```

Upon migration, the OWL ontology will be mapped to the Grakn Model. The following Graql statement inserts an equivalent structure to the one obtained through the migration mapping:

```graql
insert

"tPerson" sub entity;
$eWitold isa tPerson;
$eStefan isa tPerson;

"owl-subject-op-isAncestorOf" sub role-type;
"owl-object-op-isAncestorOf" sub role-type;
"op-isAncestorOf" isa relation-type, has-role owl-subject-op-isAncestorOf, has-role owl-object-op-isAncestorOf;
tPerson plays-role owl-subject-op-isAncestorOf, plays-role owl-object-op-isAncestorOf;

"owl-subject-op-hasAncestor" sub role-type;
"owl-object-op-hasAncestor" sub role-type;
"op-hasAncestor" isa relation-type, has-role owl-subject-op-hasAncestor, has-role owl-object-op-hasAncestor;
tPerson plays-role owl-subject-op-hasAncestor, plays-role owl-object-op-hasAncestor;

"owl-subject-op-isParentOf" sub role-type;
"owl-object-op-isParentOf" sub role-type;
"op-isParentOf" sub relation-type, has-role owl-subject-op-isParentOf, has-role owl-object-op-isParentOf;
tPerson plays-role owl-subject-op-isParentOf, plays-role owl-object-op-isParentOf;

"owl-subject-op-hasParent" sub role-type;
"owl-object-op-hasParent" sub role-type;
"op-hasParent" sub relation-type, has-role owl-subject-op-hasParent, has-role owl-object-op-hasParent;
tPerson plays-role owl-subject-op-hasParent, plays-role owl-object-op-hasParent;

(owl-subject-op-isParentOf: $eStefan, owl-object-op-isParentOf: $eWitold) isa op-isParentOf;

$inv-op-hasAncestor isa inference-rule,
lhs {
(owl-subject-op-hasAncestor: $x, owl-object-op-hasAncestor: $y) isa hasAncestor;},
rhs {
(owl-subject-op-isAncestorOf: $y, owl-object-op-isAncestorOf: $x) isa isAncestorOf;};

$inv-op-isAncestorOf isa inference-rule,
lhs {
(owl-subject-op-isAncestorOf: $x, owl-object-op-isAncestorOf: $y) isa isAncestorOf;},
rhs {
(owl-subject-op-hasAncestor: $y, owl-object-op-hasAncestor: $x) isa hasAncestor;};

$trst-op-hasAncestor isa inference-rule,
lhs {
(owl-subject-op-hasParent: $x, owl-object-op-hasParent: $z) isa hasAncestor;
(owl-subject-op-hasAncestor: $z, owl-object-op-hasAncestor: $y) isa hasAncestor;},
rhs {
(owl-subject-op-hasAncestor: $x, owl-object-op-hasAncestor: $y) isa hasAncestor;};

$pch-op-hasAncestor isa inference-rule,
lhs {
(owl-subject-op-hasParent: $x, owl-object-op-hasParent: $z) isa hasParent;
(owl-subject-op-hasAncestor: $z, owl-object-op-hasAncestor: $y) isa hasAncestor;},
rhs {
(owl-subject-op-hasAncestor: $x, owl-object-op-hasAncestor: $y) isa hasAncestor;};
```

## Where Next?
You can find further documentation about migration in our API reference documentation (which is in the `docs` directory of the distribution zip file, and also online [here](https://grakn.ai/javadocs.html).

Please take a look at our examples to further illustrate [OWL migration](../examples/OWL-migration.html).
{% include links.html %}


## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/32" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
