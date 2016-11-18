---
title: Grakn Post Processing  
keywords: 
last_updated: November 17, 2016
tags: 
summary: "This page describes post processing by the Grakn engine."
sidebar: documentation_sidebar
permalink: /documentation/advanced-grakn/post-processing.html
folder: documentation
comment_issue_id: 
---


## Introduction

The distributed and concurrent nature of the Grakn system means that, sometimes, post processing is required to ensure the data remains consistent. This section will briefly discuss the post processing steps taken by the Grakn engine.

### Role Player Optimisation 

When allocating entities as role players to multiple relations for the first time it is possible to create duplicate associations. These associations do not affect the results of any queries or computations. For example, if in a new system we process simultaneously the following three statements in different transactions:    

```
1. insert $x has name 'Brad Pitt' isa person; $y has name 'Fury'; (actor: $x, movie: $y) isa acted-in;
2. insert $x has name 'Brad Pitt' isa person; $y has name 'Troy'; (actor: $x, movie: $y) isa acted-in;
3. insert $x has name 'Brad Pitt' isa person; $y has name 'Seven'; (actor: $x, movie: $y) isa acted-in;
```

It is possible for the system to record that `Brad Pitt` is an actor multiple times. The duplications will later be resolved and merged by Grakn engine.

### Merging Resources

{% include note.html content="This only happens when using a batch graph." %}

When using a batch graph, many safety checks are skipped in favour of speed. One such check is the possible existence of a resource before creating it. So if the following transactions are executed simultaneously while batch loading: 

```
1. insert $a has unique-id '1'
2. insert $b has unique-id '1'
3. insert $c has unique-id '1'
```

It would be possible to create multiple resources of the type `unique-id` with the value `1`. These duplicate resources are similarly merged and resolved by Grakn engine.

