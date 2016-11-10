---
title: CSV Migration to Grakn
keywords: setup, getting started
last_updated: November 2nd 2016
tags: [migration]
summary: "This document will teach you how to migrate CSV data into Grakn."
sidebar: documentation_sidebar
permalink: /documentation/migration/CSV-migration.html
folder: documentation
comment_issue_id: 32
---

## Introduction
This tutorial shows you how to populate Grakn with CSV data. If you have not yet set up the Grakn environment, please see the [setup guide](../get-started/setup-guide.html).

## Migration Shell Script for CSV
The migration shell script can be found in `/bin` directory of your Grakn environment. Usage is specific to the type of migration being performed. For CSV:

```bash
usage: ./migration.sh csv -template <arg> -input <arg> [-help] [-no] [-separator <arg>] [-batch <arg>] [-uri <arg>] [-keyspace <arg>]

OPTIONS
 -b,--batch <arg>       number of row to load at once
 -h,--help              print usage message
 -i,--input <arg>       input csv file
 -k,--keyspace <arg>    keyspace to use
 -n,--no                dry run- write to standard out
 -s,--separator <arg>   separator of columns in input file
 -t,--template <arg>    graql template to apply over data
 -u,--uri <arg>         uri to engine endpoint
```

## CSV Migration Basics

CSV Migration makes heavy use of the Graql templating language. You should have a solid foundation in Graql templating before continuing, so please read through our [templating documentation](../graql/graql-templating.html) to find out more.

#### What about the ontology?

YOU must write the ontology!

There are limitations on the CSV format that prevent it from expressing the semantics of the data. If we were to automatically migrate the ontology, your data would remain as unhelpful as it is in CSV, and we want our users to have the full benefit of a knowledge graph.

Once you have written an ontology for your domain, you will template Graql statements that instruct the migrator how your data can be mapped to your ontology.

#### How do I write a template?

The CSV migrator will apply the template to each row of data in the CSV file. If you are familiar with the Graql templating language, you are aware that it replaces the indicated sections in the template with provided data. In this migrator, the column header is the key and the content of each row at that column the value.

#### Simple example

```graql
insert

car isa entity-type
    has-resource name
    has-resource year
    has-resource description
    has-resource price;

name isa resource-type datatype string;
year isa resource-type datatype string;
description isa resource-type datatype string;
price isa resource-type datatype double;

```

Above is the ontology for the following example

**cars.csv**   

```csv
Year,Make,Model,Description,Price
1997,Ford,E350,"ac, abs, moon",3000.00
1999,Chevy,"Venture",,4900.00
1996,Jeep,Grand Cherokee,"MUST SELL! air, moon roof, loaded",4799.00
```

**template**   

```graql-template
insert 

$x isa car 
    has name <Make>-<Model>
    has year <Year>
    has price @double(Price)
    if (ne Description null) do { has description <Description>};
```

This template will create a `car` entity for each row. It will attach `year` and `price` resources to each of these entities. If the `description` resource is present in the data, it will attach the approriate `description` to the `car`.

The template is applied to each row, and the resulting Graql statement, if printed out, looks like:

```graql
insert $x0 isa car has name "Ford" has description "ac, abs, moon" has price 3000.0 has year "1997";
insert $x0 isa car has name "Chevy" has price 4900.0 has year "1999";
insert $x0 isa car has description "MUST SELL! air, moon roof, loaded" has price 4799.0 has name "Jeep" has year "1996";
```

You will note that the third Graql insert is missing the `description` resource. This is because that value is not present in the data and the template uses an `if` statement to check if it exists.

### Separator

The `separator` option allows you to specify the column separator. With this we are able to migrate a wider range of formats, including TSV.

This file would be migrated in the same way as the previous example when you specify the `\t` separator:

```tsv
Year  Make  Model Description Price
1997  Ford  E350  "ac  abs   moon"  3000.00
1999  Chevy "Venture" ""  4900.00
1996  Jeep  Grand Cherokee  "MUST SELL!
air  moon roof   loaded"  4799.00
```

### Multiple Files

This small example will demonstrate creating one graph from three CSV data files using the templating language.

To start, we must write the ontology for this example.

```graql
insert

pokemon isa entity-type
    plays-role pokemon-with-type
    has-resource pokedex-no
    has-resource description;

type-id isa resource-type datatype string;
pokedex-no isa resource-type datatype long;
description isa resource-type datatype string;

pokemon-type isa entity-type
    has-resource description
    has-resource type-id
    plays-role type-of-pokemon;

has-type isa relation-type
    has-role pokemon-with-type
    has-role type-of-pokemon;
pokemon-with-type isa role-type;
type-of-pokemon isa role-type;
```

We define the three data files. Each file needs a template that to tell the Migrator how to deal with the data in each row.

**pokemon.csv**

```csv
id,identifier,species_id,height,weight
4,charmander,4,6,85
5,charmeleon,5,11,190
6,charizard,6,17,905
```

**template**

```graql-template
insert 

$x isa pokemon 
  has description <identifier>
  has pokedex-no @int(id);
```

To keep this example simple, here we've chosen to only migrate the data from the `id` and the `identifier` columns. The template creates one pokemon per row, attaching the descriptions and pokemon numbers as resources. The migrated insert statements look like:

```graql
insert $x0 has pokedex-no 4 isa pokemon has description "charmander";
insert $x0 has description "charmeleon" has pokedex-no 5 isa pokemon;
insert $x0 has pokedex-no 6 isa pokemon has description "charizard";
```

**types.csv**   

```csv
id,identifier
1,normal
2,fighting
3,flying
4,poison
5,ground
6,rock
7,bug
8,ghost
9,steel
10,fire
```

**template**:   

```graql-template
insert

$x isa pokemon-type 
  has type-id <id>
  has description <identifier>;
```

This is another very simple example that results in the following inserts: 

```graql
insert $x0 has description "normal" has type-id "1" isa pokemon-type;
insert $x0 has type-id "2" has description "fighting" isa pokemon-type;
insert $x0 has type-id "3" isa pokemon-type has description "flying";
insert $x0 has description "poison" has type-id "4" isa pokemon-type;
insert $x0 has type-id "5" has description "ground" isa pokemon-type;
insert $x0 has description "rock" has type-id "6" isa pokemon-type;
insert $x0 has description "bug" has type-id "7" isa pokemon-type;
insert $x0 has type-id "8" isa pokemon-type has description "ghost";
insert $x0 has description "steel" isa pokemon-type has type-id "9";
insert $x0 has description "fire" has type-id "10" isa pokemon-type;
```


**edges.csv**

```csv
pokemon_id,type_id,slot
4,10,1
5,10,1
6,10,1
6,3,2
```

**template**:   

```graql-template
match
  $pokemon has pokedex-no <pokemon_id>;
  $type has type-id <type_id>;
insert (pokemon-with-type: $pokemon, type-of-pokemon: $type) isa has-type;

```

In this final template we create a `has-type` relationship between the values of the `pokemon_id` and `type_id` columns. You'll notice that to ensure the relationship is between the correct entities, we've had to find them in the graph before inserting using a `match-insert` query. 

{% include note.html content="It is the responsibility of the user to ensure that all entities exist in the graph before they insert relations." %}

## Where Next?
You can find further documentation about migration in our API reference documentation (which is in the `/docs` directory of the distribution zip file, and also online [here](https://grakn.ai/pages/api-reference/latest/index.html).

{% include links.html %}


## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/32" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
