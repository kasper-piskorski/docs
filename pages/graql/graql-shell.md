---
title: Graql Shell
---
## Graql Shell

The Graql shell is used to execute Graql queries from the command line, or to
let Graql be invoked from other applications.

## Interactive Shell

The Graql shell is contained in the `bin` folder.

After starting the Mindmaps server, you can execute the shell without any
parameters to open a REPL (Read Evaluate Print Loop):

```bash
bin/graql.sh
>>> match $x id "Pikachu";
$x id "Pikachu" isa pokemon;
```

The user can enter a query and press enter for it to be evaluated. `match`
queries will return the first hundred results, `ask` queries will return `true`
or `false`. `insert` queries will output the concept IDs they are inserting.
`delete` queries will execute with no output.

The interactive shell commits to the graph only when the user type `commit`.

The REPL features several special commands:
- `commit` - Commits and validates the graph. If validation fails, the graph
  will not commit.
- `edit` - Opens the user's default text editor, specified by the `$EDITOR`
  environment variable. By default set to `vim`. When the editor exits, the
  query is executed in the shell.
- `load <filename>` - Executes the given file containing a Graql query.
- `clear` - Clears the console window
- `license` - Prints the license

## Arguments

The Graql shell accepts several arguments:

| Long Option | Option | Description                                   |
| ----------- | ------ | --------------------------------------------- |
| --name      | -n     | The name of the graph.                        |
| --execute   | -e     | A query to execute.                           |
| --file      | -f     | A path to a file containg a query to execute. |
| --help      | -h     | Print usage message                           |
| --version   | -v     | Print version                                 |

If either `--execute` or `--file` is provided, the REPL will not open and the
graph will be automatically committed.
