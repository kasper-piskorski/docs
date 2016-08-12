---
title: Ask Queries
keywords: graql, query, ask
last_updated: August 10, 2016
tags: [graql]
summary: "Graql Ask Queries"
sidebar: home_sidebar
permalink: graql/ask-questies
folder: graql
---

An ask query will return whether the given [match query](graql_match.html) has any results.

```sql
match dragon isa pokemon-type
ask
```
```java
qb.match(id("dragon").isa("pokemon-type")).ask().execute();
```


## Document Changelog  

<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
    <tr>
        <td>v1</td>
        <td>11/08/2016</td>
        <td>New page for developer portal.</td>        
    </tr>
    
</table>
