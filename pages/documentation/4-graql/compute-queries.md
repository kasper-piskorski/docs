---
title: Compute Queries
keywords: graql, compute, match
last_updated: September 28, 2016
tags: [graql]
summary: "Graql Compute Queries"
sidebar: documentation_sidebar
permalink: /documentation/graql/compute-queries.html
folder: documentation
---

A compute query executes a Pregel algorithm to determine information about the graph in parallel. The general syntax is:

```
compute algorithm in subgraph;
```

where algorithm can be any of the [available](#available-algorithms) options. Subgraph is a comma separated list of
type IDs that defines the instances that will be visited by the pregel algorithm. For example,

```
compute count in person;
```

will return the number of instances of the concept type person.

## Available Algorithms

The available algorithms currently perform simple tasks and the idea is to add to these as demand dictates. Please get
in touch on our [discussion](https://discuss.mindmaps.io/) page to request any features that are of particular interest
to you. A summary of the algorithms is given in the table below.

| Algorithm | Description                                   |
| ----------- | --------------------------------------------- |
| [`count`](#count)     | Count the number of instances.                        |
| [`degrees`](#degrees)       | Compute the degree of the instances.                           |
| [`degreesAndPersist`](#degrees-and-persist)    | Compute the degree of the instances and persist this in the graph. |
| [`mean`](#mean)    | Compute the mean value of a resource.                           |
| [`min`](#minimum)    | Compute the minimum value of a resource. |
| [`max`](#maximum)    | Compute the maximum value of a resource. |
| [`std`](#standard-deviation)    | Compute the standard deviation of a resource. |
| [`sum`](#sum)    | Comoute the sum of a resource. |

For further information see the individual sections below.

### Count

The default behaviour of count is to return a single value that gives the number of instances present in the graph. It
is possible to also count subsets of the instances in the graph using the [subgraph](#subgraph) syntax.

### Degrees

The `degrees` algorithm computes how many edges there are attached to a given instances in the graph. A map is returned
that displays an instance ID and its degree.

### Degrees and Persist

This algorithm starts by calling [`degrees`](#degrees). Instead of returning the map of results it persists each degree
to its respective instance in the graph using a resource.

### Mean

Computes the mean value of a given resource type. This algorithm requires the [subgraph](#subgraph) syntax to be used.
For example,

```
compute mean in age;
```

would compute the mean of the value persisted in instances of the resource type age. It is also possible to provide a
set of resource types.

```
compute mean in user-rating, expert-rating;
```

which would compute the mean of the union of the instances of the two resource types.

### Minimum

Computes the minimum value of a given resource type, similar to [mean](#mean).

### Maximum

Computes the maximum value of a given resource type, similar to [mean](#mean).

### Standard Deviation

Computes the standard deviation of a given resource type, similar to [mean](#mean).

### Sum

Computes the sum of a given resource type, similar to [mean](#mean).

## Subgraph

The subgraph syntax is provided to more accurately control the data that a chosen [algorithm](#available-algorithms) is
operating on. The algorithms, by default, include every instance in the calculation. Using the `in` keyword followed by
a comma separated list of type IDs will restrict the calculations to instances of those types only.

{% include links.html %}

## Document Changelog  


<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
        <tr>
        <td>v0.2.0</td>
        <td>01/10/2016</td>
        <td>First release.</td>        
    </tr>

</table>
