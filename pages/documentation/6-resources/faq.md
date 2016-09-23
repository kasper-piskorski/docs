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

## Bugs and strange behaviour

### Question: Why does MindmapsDB hang when I try to start it?   

I am running `mindmaps.sh start` but it hangs on `Starting Cassandra`. Why?

### Answer
This may be because you have cloned the mindmapsdb repo into a directory which has a space in its name (e.g. `/mindmaps DB`). You can build our code successfully, but when you start `mindmaps.sh`, it hangs because Cassandra needs you to have single word pathnames. Remove the spaces (e.g. `/mindmapsDB`) and try again.

There are other possible reasons why MindmapsDB hangs starting Cassandra. One may be that some other application is using the port 7199, which Cassandra needs.  To find out what is using port 7199:
`lsof -i tcp:7199` 

From there, you'll see the PID of application using that port. Check if you can safely kill it or change its port. It may be that another instance of Cassandra is blocking it, and you can simply kill it using:
`pkill -9 java`

Then try `mindmaps.sh start` again.

Failing that, you can often find out more information by looking in the `/logs` directory under your MindmapsDB installation.  


### Question: Why am I getting a strange exception?

Filipe: Please edit.

I am seeing the following exception. Why?
```bash
java.lang.RuntimeException: java.lang.IllegalStateException: The vertex or type is not associated with this transaction [v[24584]]
```

### Answer

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

### Question: Why am I getting ghost vertices?

Filipe: Please edit.
My graph is showing ghost vertices (Jo says - what does this mean, how do they manifest?). Why is it happening and how can I stop it?

### Answer

In a  transaction based environment it is possible to have one transaction
removing a concept while another concurrently modifies the same concept. Both
transactions may successfully commit if the backend is eventually consistent
(e.g. [Titan
Cassandra](http://s3.thinkaurelius.com/docs/titan/1.0.0/common-questions.html)
). The concept is likely to still exist with only the modified properties.
When using the Titan Cassandra backend it is possible to safeguard against
this by setting the `checkInternalVertexExistence` property to true.   
However, this will result in slower transaction as more reads will be necessary.

## Working with  MindmapsDB

### Question: Do applications written on top of MindmapsDB have to be in Java?

Currently, there is no official support for languages other than Java. But we are open source and would be very willing to accept proposals from our community, and work with contributors, to create bindings to other languages.

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
