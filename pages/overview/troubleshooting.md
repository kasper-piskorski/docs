---
title: Troubleshooting
keywords: troubleshooting
last_updated: August 10, 2016
tags: [getting_started]
summary: "Troubleshooting guide for MindmapsDB."
sidebar: home_sidebar
permalink: overview/troubleshooting
folder: overview
---

{% include warning.html content="While MindmapsDB is still in an early adopters phase, you may encounter some exceptions. This page attempts to help you debug any problems you may experience. Please feel free to use our [discussion forums](http://discuss.mindmaps.io) for any specific queries." %}

 

## Strange Exceptions

```bash
java.lang.RuntimeException: java.lang.IllegalStateException: The vertex or type is not associated with this transaction [v[24584]]
```

This exception occurs when you try to create links between concepts in
different graphs. The following code reproduces this exception:

```java
MindmapsTitanGraphFactory factory = MindmapsTitanGraphFactory.getInstance();
//Configuration pointing to keyspace one
MindmapsGraph mindmapsGraph1 = factory.newGraph("conf1.properties");
//Configuration pointing to keyspace two
MindmapsGraph mindmapsGraph2 = factory.newGraph("conf2.properties");

ConceptType conceptType1 = mindmapsGraph1.putConceptType("1");
ConceptType conceptType2 = mindmapsGraph2.putConceptType("2");

conceptType1.superConcept(conceptType2);
```

## Other Issues

### Ghost Vertices

In a  transaction based environment it is possible to have one transaction
removing a concept while another concurrently modifies the same concept. Both
transactions may successfully commit if the backend is eventually consistent
(e.g. [Titan
Cassandra](http://s3.thinkaurelius.com/docs/titan/1.0.0/common-questions.html)
). The concept is likely to still exist with only the modified properties.
When using the Titan Cassandra backend it is possible to safeguard against
this by setting the `checkInternalVertexExistence` property to true.   
However, this will result in slower transaction as more reads will be necessary.

{% include links.html %}

## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
    <tr>
        <td>v1</td>
        <td>10/08/2016</td>
        <td>Checked for release to developer portal.</td>        
    </tr>
    
</table>
