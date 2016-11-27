---
title: What is Grakn?
keywords: intro
last_updated: September 13, 2016
summary: false
tags: [overview]
sidebar: platform_sidebar
permalink: /platform/index.html
folder: platform
toc: false
---

Grakn is a knowledge graph data platform that stores data in a way that allows machines to understand the meaning of information, and equipped with a knowledge-oriented query language capable of real-time analytics and reasoning.

Let's break that down a bit and explain what it means.

**_"Grakn is a knowledge graph data platform ..."_**

Grakn is a knowledge graph data platform that enables the programmatic definition of highly expressive domain models and rules, i.e. knowledge ontologies. Your ontology could flexibly evolve as your application grows while it acts as a schema that guarantees the consistency of your data. This allows Grakn to store data in a way that allows machines to understand the meaning of information and their relationships, which would allow computers to process complex information more intelligently with less human intervention. Grakn is built using various graph computing technologies which allows it to be sharded and replicated over a network of distributed machines. Effectively, Grakn turns your dataset into a distributed knowledge graph data platform.

**_"... and a knowledge-oriented query language ..."_**

Our query language, Graql, is a [declarative](https://dzone.com/articles/imperative-vs-declarative-query-languages-whats-th), knowledge-oriented query language that uses machine reasoning for retrieving explicitly stored and implicitly derived knowledge. Graql allows you to express complex queries in simple and short pattern matching statements and it leverages the inherent semantics of data.

**_"... capable of real-time analytics ..."_**

Graql is capable of performing distributed computation over large amount of data to answer commonly asked analytics queries. These types of analytics queries is usually not possible to perform on big data without developing custom distributed algorithms.

**_"... and reasoning."_**

Graql is capable of validating constraints and inferring new information in a Grakn graph. In the past, queries that are executed on a database have to explicitly define the relationship they are looking for. Graql, on the other hand, will infer all relationships that matches a query pattern, as well as relationships that are semantically equivalent.
