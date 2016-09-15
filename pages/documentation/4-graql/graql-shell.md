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


The Graql shell is contained in the `bin` folder.

After starting the MindmapsDB server, you can execute the shell without any
parameters to open a REPL (Read Evaluate Print Loop):

```bash
bin/graql.sh
```

The user can enter a query and press enter for it to be evaluated:   

```bash
>>> match $x id "Pikachu";
$x id "Pikachu" isa pokemon;
```

| Query | Description                                   |
| ----------- | --------------------------------------------- |
| `match`     | Returns the first 100 results.                        |
| `ask`       | Returns `true` or `false`.                           |
| `insert`    | Outputs the concept IDs inserted. |
| `delete`    | Executes with no output.                           |

   
The interactive shell commits to the graph only when the user types `commit`.

The REPL features several special commands:  

| Query | Description                                   |
| ----------- | --------------------------------------------- |
| `commit`     | Commits and validates the graph. If validation fails, the graph will not commit.                          |
| `edit`       | Opens the user's default text editor, specified by the `$EDITOR` environment variable. By default set to `vim`. When the editor exits, the query is executed in the shell.                           |
| `load <filename>`    | Executes the given file containing a Graql query. |
| `clear`    | Clears the console window.                          |
| `license`    | Prints the license                           |


## Arguments

The Graql shell accepts several arguments:

| Long Option | Option | Description                                   |
| ----------- | ------ | --------------------------------------------- |
| `--name`      | `-n`     | The name of the graph.                        |
| `--execute`   | `-e`     | A query to execute.                           |
| `--file`      | `-f`     | A path to a file containg a query to execute. |
| `--help`      | `-h`     | Print usage message                           |
| `--version`   | `-v`     | Print version                                 |

If either `--execute` or `--file` is provided, the REPL will not open and the
graph will be automatically committed.

{% include links.html %}

## Document Changelog  


<table>
    <tr>
        <td>Version</td>
        <td>Date</td>
        <td>Description</td>        
    </tr>
        <tr>
        <td>v0.1.0.1</td>
        <td>03/09/2016</td>
        <td>First release.</td>        
    </tr>

</table>
