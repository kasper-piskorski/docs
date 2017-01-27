---
title: What is Grakn and Graql?
keywords: intro
last_updated: September 13, 2016
summary: false
tags: [overview]
sidebar: platform_sidebar
permalink: ./platform/index.html 
folder: platform
toc: false
---

*GRAKN.AI is a knowledge graph data platform that stores data in a way that allows machines to understand the meaning of information. Grakn performs machine reasoning through Graql, a knowledge-oriented graph query language capable of reasoning and graph analytics.*

Here is our CEO and Founder, Haikal Pribadi, presenting GRAKN.AI at a recent event.

<iframe width="560" height="315" src="https://www.youtube.com/embed/OeFrudRlXAM?list=PLDaQNzoeb9L7UZDPq7z1Gd2Rc0m_oeSDQ" frameborder="0" allowfullscreen></iframe>


## Grakn

Grakn is a platform that stores data in a way that allows machines to understand the meaning of information in the context of their relationships. Consequently, Grakn allows computers to process complex information more intelligently with less human intervention.

This is possible because Grakn allows the definition of highly expressive domain models and rules, i.e. [knowledge ontologies](https://en.wikipedia.org/wiki/Ontology_(information_science)), to provide a semantic layer over data. The ontology functions as a richer translation of data as well as a schema that guarantees information consistency. The ontology could also flexibly evolve as your application grows.

Grakn builds on top of several graph computing and distributed computing platforms, such as [Apache TinkerPop](https://tinkerpop.apache.org/) and [Apache Spark](http://spark.apache.org/). Grakn is designed to be sharded and replicated over a network of distributed machines.

Effectively, Grakn turns your dataset into a distributed graph of knowledge.

## Graql

Graql is a [declarative](https://en.wikipedia.org/wiki/Declarative_programming), knowledge-oriented graph query language that uses machine reasoning for retrieving explicitly stored and implicitly derived knowledge from the Grakn graph.

In the past, database queries have to define the data patterns they are looking for explicitly. Graql, on the other hand, will translate a query pattern into all of its logical equivalence and evaluate them against the database. As a result, Graql allows you to derive implicit information that is hidden in your dataset, as well as reduce the complexity of expressing intelligent questions to concise and intuitive statements.

Graql is also capable of performing distributed graph analytics as part of the language, which allows you to perform analytics over large graphs out of the box. These types of analytics are usually not possible without developing custom distributed graph algorithms that are unique to a use case.
