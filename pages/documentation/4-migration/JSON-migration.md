---
title: JSON Migration to Grakn
keywords: setup, getting started
last_updated: November 14th 2016
tags: [migration]
summary: "This document will teach you how to migrate JSON data into Grakn."
sidebar: documentation_sidebar
permalink: /documentation/migration/JSON-migration.html
folder: documentation
comment_issue_id: 32
---

## Introduction
This tutorial shows you how to populate Grakn with JSON data. If you have not yet set up the Grakn environment, please see the [setup guide](../get-started/setup-guide.html).

## Migration Shell Script for JSON
The migration shell script can be found in `/bin` directory of your Grakn environment. Usage is specific to the type of migration being performed.    

```bash
usage: migration.sh json -template <arg> -input <arg> [-help] [-no] [-batch <arg>] [-uri <arg>] [-keyspace <arg>]
 -b,--batch <arg>      number of row to load at once
 -h,--help             print usage message
 -i,--input <arg>      input json data file or directory
 -k,--keyspace <arg>   keyspace to use
 -n,--no               dry run- write to standard out
 -t,--template <arg>   graql template to apply over data
 -u,--uri <arg>        uri to engine endpoint
```

## JSON Migration Basics
JSON Migration makes heavy use of the Graql templating language. You should have a solid foundation in Graql templating before continuing, so please read through our [templating documentation](../graql/graql-templating.html) to find out more.

<!-- JSON Migration will apply your template to each [Alex - something missing here?] -->

#### What about the ontology?
YOU must write the ontology! Once you have written an ontology for your domain, you will template Graql statements that instruct the migrator on how your data can be mapped to your ontology.

#### How do you write a template?
Approach each JSON file as though you were inserting a single query. The migrator cannot handle multiple queries per file, so take care that there is not more than one `match` or `insert` command in your template. Take a look at our other migration guides for more ideas on how to use Graql templates with data. 

{% include note.html content="The JSON migrator can handle either a directory or a file as the -input parameter!" %}

#### Looping over a JSON array
**types.json**    

```json
{
    "types": [
    {"id":1", "type": "normal" },
    {"id":2", "type": "fighting" },
    {"id":3", "type": "flying" },
    {"id":4", "type": "poison" },
    {"id":5", "type": "ground" },
    {"id":6", "type": "rock" },
    {"id":7", "type": "bug" },
    {"id":8", "type": "ghost" },
    {"id":9", "type": "steel" },
    {"id":10" "type": ,"fire" }]
}
```

To migrate all of these types, we need to iterate over the array:    

```graql-template
insert
for(types) do {
    $x isa pokemon-type
        has description <type>
        has type-id <id>;
}
```

Which will resolve as:    

```graql
insert $x0 has type-id "1" has description "normal" isa pokemon-type;
$x1 has description "fighting" has type-id "2" isa pokemon-type;
$x2 has type-id "3" has description "flying" isa pokemon-type;
$x3 has type-id "4" has description "poison" isa pokemon-type;
$x4 has description "ground" has type-id "5" isa pokemon-type;
$x5 has description "rock" has type-id "6" isa pokemon-type;
$x6 has description "bug" has type-id "7" isa pokemon-type;
$x7 has description "ghost" has type-id "8" isa pokemon-type;
$x8 has type-id "9" has description "steel" isa pokemon-type;
$x9 has description "fire" isa pokemon-type has type-id "10";
```

#### Match-Inserts with loops
In some situations, you'll need to look up references to existing entities so that you can refer to them when inserting data. 

Once the above types are migrated, we can move on to the pokemon JSON file.

**pokemon.json**    

```json
{
    "pokemon": [
        {
            "identifier":"Charmander",
            "id":4,
            "species_id":4,
            "height":6,
            "weight":85,
            "types":[
                "1"
            ]
        },{
            "identifier":"Charmeleon",
            "id":"5",
            "species_id":5,
            "height":11,
            "weight":190,
            "types":[
                "1"
            ]
        },{
            "identifier":"Charizard",
            "id":"6",
            "species_id":6,
            "height":17,
            "weight":905,
            "types":[
                "1", "2"
            ]
        }
    ]
}
```

This template is rather complicated. The first `match` portion is necessary to look up all of the already migrated pokemon types in the graph. You can then refer to these variables in your `insert` statement while creating the relationships.   

```graql-template
match
   for(pokemon) do {
        for(t in types) do {
            $<t> has type-id <t>;
        }
   }

insert
for(pokemon) do {
    $p isa pokemon
        has weight <weight>
        has height <height>
        has pokedex-no <id>
        has description <identifier>;

    for(t in types) do {
        (pokemon-with-type: $p, type-of-pokemon: $<t>) isa has-type;
    }
}
```

It will resolve as:    

```graql
match $1 has type-id "1"; $2 has type-id "2";
insert $p0 has height 6 has weight 85 isa pokemon has description "Charmander" has pokedex-no "4";
isa has-type (pokemon-with-type: $p0, type-of-pokemon: $1);
$p1 has weight 190 has height 11 has pokedex-no "5" has description "Charmeleon" isa pokemon;
(pokemon-with-type: $p1, type-of-pokemon: $1) isa has-type;
$p2 has weight 905 has pokedex-no "6" has description "Charizard" isa pokemon has height 17;
isa has-type (pokemon-with-type: $p2, type-of-pokemon: $1);
isa has-type (pokemon-with-type: $p2, type-of-pokemon: $2);
```

## Where Next?
You can find further documentation about migration in our API reference documentation (which is in the `/docs` directory of the distribution zip file, and also online [here](https://grakn.ai/pages/api-reference/latest/index.html).

{% include links.html %}


## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/32" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
