---
title: FAQ about MindmapsDB
keywords: troubleshooting
last_updated: September 22, 2016
tags: [getting-started]
summary: "Frequently asked questions about MindmapsDB."
sidebar: documentation_sidebar
permalink: /documentation/resources/faq.html
folder: documentation
comment_issue_id: 23
---

{% include warning.html content="While MindmapsDB is still in an early adopters phase, you may encounter some exceptions. This page attempts to help you debug any problems you may experience. Please feel free to use our [discussion forums](http://discuss.mindmaps.io) for any specific queries." %}

## Why does MindmapsDB hang when I try to start it?
Problem: You run `mindmaps.sh start` and it hangs on `Starting Cassandra`. 

Solution: This may be because you have cloned the mindmapsdb repo into a directory which has a space in its name (e.g. `/mindmaps DB`). You can build our code successfully, but when you start `mindmaps.sh`, it hangs because Cassandra needs you to have single word pathnames. Remove the spaces (e.g. `/mindmapsDB`) and try again.

## Why am I getting a strange exception?

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


## Why am I getting ghost vertices?

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
        <td>v0.1.0</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>
    <tr>
        <td>v0.1.1</td>
        <td>22/09/2016</td>
        <td>Renamed page and rearranged it into FAQ.</td>        
    </tr>

</table>

## Comments
Want to leave a comment? Visit <a href="https://github.com/mindmapsdb/docs/issues/23" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
