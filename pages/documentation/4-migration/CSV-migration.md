---
title: CSV Migration to MindmapsDB
keywords: setup, getting started
last_updated: August 10, 2016
tags: [migration]
summary: "This document will teach you how to migrate CSV data into a MindmapsDB graph."
sidebar: documentation_sidebar
permalink: /documentation/migration/CSV-migration.html
folder: documentation
comment_issue_id: 32
---

## Introduction
This tutorial shows you how to populate a MindmapsDB graph with CSV data. If you have not yet set up the MindmapsDB environment, please see the [Setup guide](../get-started/setup-guide.html).

## Migration Shell Script for CSV
The migration shell script can be found in `mindmaps-dist/bin` after it has been unzipped. Usage is specific to the type of migration being performed. For CSV:

```bash
usage: ./migration.sh csv -template <arg> -file <arg> [-help] [-delimiter <arg>] [-batch <arg>] [-uri <arg>] [-keyspace <arg>]

OPTIONS
   -b,--batch <arg>       number of row to load at once
   -d,--delimiter <arg>   delimiter of columns in input file
   -f,--file <arg>        csv file
   -h,--help              print usage message
   -k,--keyspace <arg>    keyspace to use
   -t,--template <arg>    graql template to apply over data
   -u,--uri <arg>         uri to engine endpoint
```

## CSV Migration Basics

CSV Migration makes heavy use of the Graql templating language. You should have a solid foundation in Graql templating before continuing, so please read through our [templating documentation](../graql/graql-templating.html).

#### What about the ontology?

YOU must write the ontology!

There are limitations on the CSV format that prevent it from expressing the semantics of the data. If we were to automatically migrate the ontology, your data would remain as unhelpful as in CSV, and we want our users to have the full benefit of a knowledge graph.

Once you have written an ontology for your domain, you will template Graql statements that instruct the migrator how your data can be mapped to your ontology.

#### How do I write a template?

The CSV migrator will apply the template to each row of data in the CSV file. If you are familiar with the Graql templating language, you are aware that it replaces the indicated sections in the template with provided data. In this migrator, the column header is the key and the content of each row at that column the value.

#### Simple example

**cars.csv**:
```csv
Year,Make,Model,Description,Price
1997,Ford,E350,"ac, abs, moon",3000.00
1999,Chevy,"Venture",,4900.00
1999,Chevy,"Venture","Another description",4900.00
1996,Jeep,Grand Cherokee,"MUST SELL! air, moon roof, loaded",4799.00
```

*template*:
```graql-template
$x isa car id <Make>-<Model>
    has year <Year>
    has price @double(Price)
    if (ne Description null) do { has description <Description>};
```

This template will create a `car` entity for each row. It will attach `year` and `price` resources to each of these entities. If the `description` resource is present in the data, it will attach the approriate `description` to the `car`.

The template is applied to each row, and the resulting Graql statement, if printed out, would look like:

```graql
insert
$x0 isa car id "Ford-E350"
    has year "1997"
    has price 3000.00
    has description "ac, abs, moon";
$x1 isa car id "Chevy-Venture"
    has year "1999"
    has price 4900.00;
$x2 isa car id "Chevy-Venture"
    has year "1999"
    has price 4900.00
    has description "Another description";
$x3 isa car id "Jeep-GrandCherokee"
    has year "1996"
    has price 4799.00
    has description "MUST SELL! air, moon roof, loaded";
```

You will note that `$x1` is missing the `description` resource because it is not present in the data.

This is the query that the migrator will send to the loader and eventually will be persisted in a graph.

### Delimiter

The `delimiter` option allows you to specify the column separator. With this we are able to migrate a wider range of formats, including TSV.


This file would be migrated in the same way as the previous example when you specify the `\t` delimiter:
```tsv
Year  Make  Model Description Price
1997  Ford  E350  "ac  abs   moon"  3000.00
1999  Chevy "Venture" ""  4900.00
1999  Chevy "Venture" "Another description" 4900.00
1999  Chevy "Venture Large"   5000.00
1996  Jeep  Grand Cherokee  "MUST SELL!
air  moon roof   loaded"  4799.00
```

### Multiple Files

This small example will demonstract creating one Mindmaps graph from three CSV data files using the templating language.

To start, we define the three data files. Each file needs a template that to tell the Migrator how to deal with the data in each row.

**pokemon.csv**

```csv
id,identifier,species_id,height,weight
4,charmander,4,6,85
5,charmeleon,5,11,190
6,charizard,6,17,905
```

*template*:

```graql-template
$x isa pokemon id <id>-pokemon
  has description <identifier>
  has pokedex-no @int(id);
```

To keep this example simple, here we've chosen to only migrate the data from the `id` and the `identifier` columns. The template creates one pokemon per row, attaching their descriptions and pokemon numbers as resources.

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

*template*:
```graql-template
$x isa pokemon-type id <id>-type has description <identifier>;
```

Notice that in the pokemon template we have appended the tag `-pokemon` to the value of the `id` column (`$x isa pokemon id <id>-pokemon`). We have also done so with the type: `isa pokemon-type id <id>-type`. This is to avoid conflicts in ids across multiple files.

Let's imagine the scenario where we do not make the id unique: Migrating would create three pokemon, with ids `1`, `2`, and `3` respectively. Migrating the types would also create 10 `pokemon-type` entities, which would have ids `1`-`10`.

You can see the obvious conflict here. You've tried to create entities with the same ids! By appending to the id value we avoided this conflict. These are all things that any user of CSV migration must keep in mind.

**edges.csv**
```csv
pokemon_id,type_id,slot
4,10,1
5,10,1
6,10,1
6,3,2
```

*template*:
```graql-template
(pokemon-with-type: <pokemon_id>-pokemon, type-of-pokemon: <type_id>-type) isa has-type;"
```

In this final template we create a `has-type` relationship between the values of the `pokemon_id` and `type_id` columns. You'll notice that to ensure the relationship is between the correct entities, we've had to modify the ids in the same way as above.

{% include note.html content="It is the responsibility of the user to ensure that all entities exist in the graph before they insert relations." %}

#### This example in Java

This example is very simple in java. First we assign each template to a variable. Then we can call the `migrator.migrate()` function on each of the template/datafile pairs.

```java test-ignore
LoadingMigrator migrator = new LoadingMigrator(loader, new CSVMigrator());

String pokemonTemplate = "" +
                "$x isa pokemon id <id>-pokemon \n" +
                "    has description <identifier>\n" +
                "    has pokedex-no @int(id)\n";


String pokemonTypeTemplate = "$x isa pokemon-type id <id>-type has description <identifier>;";


String edgeTemplate = "(pokemon-with-type: <pokemon_id>-pokemon, type-of-pokemon: <type_id>-type) isa has-type;";


migrator.migrate(pokemonTemplate, new File("pokemon.csv"));
migrator.migrate(pokemonTypeTemplate, new File("types.csv"));
migrator.migrate(edgeTemplate, new File("edges.csv"));
```

## Where Next?
You can find further documentation about migration in our API reference documentation (which is in the `/docs` directory of the distribution zip file, and also online [here](https://mindmaps.io/pages/api-reference/latest/index.html)).


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
        <td>28/09/2016</td>
        <td>First doc release.</td>
    </tr>

</table>

## Comments
Want to leave a comment? Visit <a href="https://github.com/mindmapsdb/docs/issues/32" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.