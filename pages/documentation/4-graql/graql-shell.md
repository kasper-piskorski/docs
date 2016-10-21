---
title: Graql Shell
keywords: graql, shell
last_updated: August 11, 2016
tags: [graql]
summary: "
The Graql shell is used to execute Graql queries from the command line, or to let Graql be invoked from other applications."
sidebar: documentation_sidebar
permalink: /documentation/graql/graql-shell.html
folder: documentation
---

The Graql shell is contained in the `bin` folder. After starting the MindmapsDB server, you can load data into a MindmapsDB graph as follows:

```
bin/graql.sh -f examples/pokemon.gql
```

Then you can enter a query and press enter for it to be evaluated:   

```bash
>>> match $x id "Pikachu";
$x id "Pikachu" isa pokemon;
```

If you already have a graph loaded, you can just execute the shell without any parameters to open a REPL (Read Evaluate Print Loop):

```bash
bin/graql.sh
```

To load an additional graph into a different keyspace, that is, to load a graph with a different name to any already loaded, you can specify the graph name:

```
./graql.sh -n <graphname>
``` 


| Query | Description                                   |
| ----------- | --------------------------------------------- |
| `match`     | Returns the first 100 results.                        |
| `ask`       | Returns `true` or `false`.                           |
| `insert`    | Outputs the concept IDs inserted. |
| `delete`    | Executes with no output.                           |
| `compute`   | Returns either a value or a map from concept to value.        |

   
The interactive shell commits to the graph only when the user types `commit`.

The REPL features several special commands:  

| Query        | Description                                            |
| -----------  | ------------------------------------------------------ |
| `commit`     | Commits and validates the graph. If validation fails, the graph will not commit. |
| `rollback`   | Rolls back the transaction, undoing everything that hasn't been committed. |
| `edit`       | Opens the user's default text editor, specified by the `$EDITOR` environment variable. By default set to `vim`. When the editor exits, the query is executed in the shell. Useful for executing a large chunk of Graql without typing it all in (e.g. cut and paste from an example).                           |
| `load <filename>` | Executes the given file containing a Graql query. |
| `clear`      | Clears the console window.                             |
| `exit`       | Exits the REPL                                         |
| `license`    | Prints the license                                     |


## Arguments

The Graql shell accepts several arguments:

| Long Option   | Option   | Description                                      |
| ------------- | -------- | ------------------------------------------------ |
| `--name`      | `-n`     | The name of the graph.                           |
| `--execute`   | `-e`     | A query to execute.                              |
| `--file`      | `-f`     | A path to a file containg a query to execute.    |
| `--uri`       | `-u`     | The URI to connect to engine.                    |
| `--batch`     | `-b`     | A path to a file containg a query to batch load. |
| `--help`      | `-h`     | Print usage message                              |
| `--uri`       | `-u`     | Uri to connect to Engine                         |
| `--version`   | `-v`     | Print version                                    |

If `--execute`, `--file` or `--batch` is provided, the REPL will not open and the
graph will be automatically committed.

{% include links.html %}
