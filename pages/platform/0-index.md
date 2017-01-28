---
title: Introduction to GRAKN.AI
keywords: intro
last_updated: September 13, 2016
summary: GRAKN.AI is the database for AI. It is a database in the form of a knowledge graph that uses machine reasoning to simplify data processing challenges for AI applications.
tags: [overview]
sidebar: platform_sidebar
permalink: ./platform/index.html
folder: platform
toc: false
---

For applications to be more intelligent, it needs to know more. To know more, applications need to collect more information. When collecting and integrating more information, the resulting dataset becomes increasingly complex.

What makes dataset complex?

Data model makes datasets complex.  Real world models are filled with hierarchies and hyper-relationships, but our current modelling techniques are all based on binary relations. Querying these datasets are challenging because query languages are only able to retrieve explicitly stored data, but not implicitly derived information.

GRAKN.AI is the database solution for working with complex data, that intelligent applications will increasingly face in the current day and age. Grakn allows your organisation to grow their competitive advantage by uncovering hidden knowledge that is too complex for human cognition, and evolve their data model regularly to build a lasting advantage as your business “learns”, all while reducing engineering time, cost and complexity.

# Meet Grakn and Graql
GRAKN.AI is composed of Grakn - the storage, and Graql - the language.

## Grakn

Grakn is a database in the form of a knowledge graph, that uses an intuitive ontology to model extremely complex datasets. It stores data in a way that allows machines to understand the meaning of information in the complete context of their relationships. Consequently, Grakn allows computers to process complex information more intelligently with less human intervention.

> Grakn allows you to model the real world and all the hierarchies and hyper-relationships contained in it.

Grakn's ontology modelling constructs include but not limited to data type hierarchy, relation type hierarchy, bidirectional relationships, multi-type relationships, N-ary relationships, relationships in relationships, and so on. In other words, Grakn allows you to model the real world and all the hierarchies and hyper-relationships contained in it.

Grakn's ontology is also flexible, allowing your data model to evolve regularly even when you have lots of data, building a lasting advantage as your business “learns”. Later in the year, we will show who you can have a machine learning system adapt and grow your Grakn ontology.

Grakn is built using several graph computing and distributed computing platforms, such as [Apache TinkerPop](https://tinkerpop.apache.org/) and [Apache Spark](http://spark.apache.org/). Grakn is designed to be sharded and replicated over a network of distributed machines.

## Graql

Graql is a [declarative](https://en.wikipedia.org/wiki/Declarative_programming), knowledge-oriented graph query language that uses machine reasoning for retrieving explicitly stored and implicitly derived knowledge from Grakn.

> Graql allows you to derive implicit information that is hidden in your dataset, as well as reduce the complexity

In the past, database queries have to define the data patterns they are looking for explicitly. Graql, on the other hand, will translate a query pattern into all of its logical equivalence and evaluate them against the database. This includes but not limited to the inference of types, relationships, context, and pattern combination. In other words, Graql allows you to derive implicit information that is hidden in your dataset, as well as reduce the complexity of expressing intelligent questions to concise and intuitive statements.

Graql is also capable of performing distributed graph analytics as part of the language, which allows you to perform analytics over large graphs out of the box. These types of analytics are usually not possible without developing custom distributed graph algorithms that are unique to a use case.

One may consider Graql as an OLKP (OnLine Knowledge Processing) language, which combines both OLTP (OnLine TransactionProcessing) and OLAP (OnLine Analytical Processing).

####  Here is our CEO and Founder, Haikal Pribadi, presenting GRAKN.AI at a recent event.

<iframe style="width: 100%; height: 400px" src="https://www.youtube.com/embed/OeFrudRlXAM?list=PLDaQNzoeb9L7UZDPq7z1Gd2Rc0m_oeSDQ" frameborder="0" allowfullscreen></iframe>
