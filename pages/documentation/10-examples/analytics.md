---
title: An Example of Graql Analytics
keywords: analytics, machine-learning
last_updated: March 2017
tags: [analytics, examples]
summary: "An example to illustrate Graql analytics"
sidebar: documentation_sidebar
permalink: /documentation/examples/analytics.html
folder: documentation
comment_issue_id: 27
---


## Introduction

This example illustrates the use of Graql analytics on a simple dataset, to show how it can be used to calculate statistics and illustrate the use of subgraphs to partition and study the data. The code for this example can be found on our [sample-projects](https://github.com/graknlabs/sample-projects/tree/master/example-analytics-mtcars) repository, and is also included in the *examples* folder of the Grakn distribution from v0.12.0.

For a detailed overview of Graql analytics, we recommend that you take a look at the [documentation](https://grakn.ai/pages/documentation/graql-analytics/analytics-overview.html).  

## Data

This example takes a dataset that will be familiar to students of R - [mtcars (Motor Trend Car Road Tests) data](https://stat.ethz.ch/R-manual/R-devel/library/datasets/html/mtcars.html).  The data was extracted from the 1974 Motor Trend US magazine, and comprises fuel consumption and 10 other aspects of automobile design and performance for 32 automobiles (1973–74 models). We have created a csv file of the data and added two columns to indicate the car maker's name and region that the car was made in (Europe, Japan or North America).

## Ontology

We have provided the following ontology to represent the data, although many other variations are possible:

```graql
insert

# Entities

vehicle sub entity
	is-abstract;

car sub vehicle
	is-abstract

	has-resource model
	has-resource mpg
	has-resource cyl
	has-resource disp
	has-resource hp
	has-resource wt
	has-resource gear
	has-resource carb
	plays-role made;

automatic-car sub car;
manual-car sub car;

carmaker sub entity
	is-abstract
	has-resource maker-name
	plays-role maker;

japanese-maker sub carmaker;
american-maker sub carmaker;
european-maker sub carmaker;

# Resources

model sub resource datatype string;
maker-name sub resource datatype string;
mpg sub resource datatype double;
cyl sub resource datatype long;
disp sub resource datatype double;
hp sub resource datatype long;
wt sub resource datatype double;
gear sub resource datatype long;
carb sub resource datatype long;

# Roles and Relations

manufactured sub relation
	has-role maker
	has-role made;

maker sub role;
made sub role;
``` 

To load *ontology.gql* into Grakn, make sure the engine is running and choose a clean keyspace in which to work (here we use the default keyspace, so we are cleaning it before we get started). 

```bash
<relative-path-to-Grakn>/bin/grakn.sh clean
<relative-path-to-Grakn>/bin/grakn.sh start
<relative-path-to-Grakn>/bin/graql.sh -f ./ontology.gql
```		

## Data Migration

We migrated the CSV data using template Graql files, but for ease of use, we provide a single data file that you can load to populate a graph.

```bash
<relative-path-to-Grakn>/bin/graql.sh -f ./data.gql
```		

Spin up the [Grakn visualiser](../grakn-dashboard/visualiser.html) by pointing your browser to [http://localhost:4567/](http://localhost:4567/). You can submit queries to check the data, or explore it using the Types dropdown menu.

Some sample queries:

```graql
# Cars where the model name contains "Merc"
match $x has model contains "Merc";

# Cars with more than 4 gears
match $x has gear > 4;

# Japanese-made cars that are manual
match $x isa manual-car; $y isa japanese-maker;

# European cars that are automatic
match $x isa automatic-car; $y isa european-maker;

```

At this point, you are ready to start investigating statistics within the data using Graql analytics.

## Compute Queries

Graql [compute](../graql/compute-queries.html) queries are used to determine values such as mean, minimum and maximum. For example:

```graql
# Number of automatic and manual cars
compute count in automatic-car; # 19
compute count in manual-car; # 13

# Number of Japanese car makers
compute count in japanese-maker; # 4

# mean mpg for automatic and manual cars
compute mean of mpg in automatic-car; # 20.09
compute mean of mpg in manual-car; # 20.09

# Mean number of cylinders in manual and automatic cars
compute mean of cyl in manual-car; # 6.1875
compute mean of cyl in automatic-car; # 6.1875

# Mean displacement in cars from Japanese and American car makers
compute mean of disp in car, japanese-maker; # 230.721875
compute mean of disp in car, american-maker; # 230.721875

# Median number of cylinders (all cars)
compute median of cyl;  # 6

# Minimum number of gears (all cars)
compute min of gear; # 3

# Maximum number of carburetors (all cars)
compute max of carb; # 8
```

## Where Next?

If you haven't already, we recommend that you review the documentation about [Graql analytics](https://grakn.ai/pages/documentation/graql-analytics/analytics-overview.html) and [compute queries](../graql/compute-queries.html).  

This example was based on CSV data migrated into Grakn. Having read it, Yyou may want to further study our documentation about [CSV migration](../migration/CSV-migration.html) and [Graql templating](https://grakn.ai/pages/documentation/graql/graql-templating.html).  

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/27" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
