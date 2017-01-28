---
title: Grakn Overview
keywords: overview
last_updated: September 13, 2016
tags: [overview, faq]
sidebar: overview_sidebar
permalink: /overview/faq.html
folder: overview
---

### What is a Graph Database?
As [Wikipedia](https://en.wikipedia.org/wiki/Graph_database) explains, a graph database is "*...a database that uses graph structures for semantic queries with nodes, edges and properties to represent and store data. A key concept of the system is the graph (or edge or relationship), which directly relates data items in the store. The relationships allow data in the store to be linked together directly, and in most cases retrieved with a single operation.*".

Put another way: a graph database can store, manage and query complex and highly-connected data (that is, data where there are a high number of relationships between the elements). A graph database is well-suited for uncovering common attributes and anomalies within a given volume of data, and are designed to express the relatedness of that data, allowing them to  uncover patterns that are otherwise difficult to detect.    

A graph is a structure composed of vertices and edges, which can have an arbitrary number of key/value-pairs called properties.

Vertices represent entities: discrete objects such as a person, a place, or an event.

Edges represent relationships between those objects. For instance, a person may know another person, have been involved in an event, or lives at a particular place.

Properties express non-relational information about the vertices and edges. Example properties include a vertex having a name, an age and an edge having a timestamp and/or a weight.



### What is the difference between relational databases and graph databases?

Relational and graph databases differ in the way data is stored and accessed. The primary difference is how relationships between objects are prioritized and managed. A relational database connects entities in a secondary fashion using foreign keys, but in a graph database, the relationships between them (the 'edges') are of first order importance. The ability to map any-to-any relationships is what makes graph databases so powerful. A graph database can be likened to a pre-joined RDBMS.

Graph computing can offer algorithms that support complex reasoning: path analysis, vertex clustering and ranking, subgraph identification, and more.

<!--
**Temporary graphic - would need to redraw**
![](http://www.pwc.com/content/dam/pwc/us/en/technology-forecast/2015/remapping-database-landscape/features/assets/mw-15-1351-the-power-of-graph-databases-in-public-health-modal-chart-2-modal.png)
-->

### What is the difference between other NoSQL databases and graph databases?   

<!--**<<< to do - Another short section to describe the difference in brief (maybe just use a diagram) >>>**

**Temporary graphic - discuss and redraw**

![](https://www.datastax.com/wp-content/uploads/2016/07/databases.jpg)

-->

### Why would you use a graph database?

As we've discussed above, relationships are explicit in a graph data model.  If tracking the relationships between your data entities is a primary concern, it is often a good fit to use a graph database.

Some common use cases for graph databases include:

- Recommendation and Personalization   - Security and Fraud Detection   
- IoT and Networking

### Why would you specifically choose Grakn?   
<!--
**<<< to do - Not a promotional thing here - what is the technical reason for choosing it rather than orientdb or neo4j? What are the problems that we specifically solve? >>> **-->


### When a graph database is not a good solution   

Much depends on your use case.  If the goal is to model or integrate large, interconnected systems, a graph store is invaluable.

As described by [PWC's recent database technology forecast](http://www.pwc.com/us/en/technology-forecast/2015/remapping-database-landscape/public-health-graph--databases.html): "*Depending on the use case, native graph stores can be overkill. If the immediate purpose is to capture or cache the data, then a key-value or column store is more appropriate. If the purpose is aggregation, then a document store may be best, at least for initial data ingestion. If transactional integrity and concurrency are critical requirements, then an RDBMS or a NewSQL store fits best.*"


### Database types compared   

<!--**Temporary graphic - discuss and redraw**   

![](https://www.datastax.com/wp-content/uploads/2016/07/rdbmsgraphcompare2.jpg)-->
