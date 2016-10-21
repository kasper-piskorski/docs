---
title: Migrating to MindmapsDB
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql, migration]
summary: "Landing page for documentation about loading data in different formats to populate a MindmapsDB graph."
sidebar: documentation_sidebar
permalink: /documentation/migration/migration.html
folder: documentation
comment_issue_id: 32
---

## Introduction
This page introduces migration of data, stored in different formats, to populate a MindmapsDB graph. If you have not yet set up the MindmapsDB environment, please see the [Setup guide](../get-started/setup-guide.html).

## Migration Shell Script
The migration shell script can be found in `mindmaps-dist/bin` after it has been unzipped. Usage is specific to the type of migration being performed:

### SQL Migration

```
usage: ./migration.sh sql -driver <jdbcDriver> -user <username> -pass <password> -database <url> -graph <graphname> [engine <url>]
       -driver           JDBC driver
       -user             username for SQL database
       -pass             password for SQL database
       -database         URL to SQL database
       -graph            graph name (or defaults to the default keyspace)
       -engine           MindmapsDB engine URL, default localhost
```

Please see the [SQL migration documentation](./SQL-migration.html) for further information.

### CSV Migration

```
usage: ./migration.sh csv -file <file> -graph <graphname> [-engine <url] [-as <type>]
      -file             csv file to migrate
      -graph            graph name (or defaults to the default keyspace)
      -engine           MindmapsDB engine URL, default localhost
      -as               entity type of this file
```

### JSON Migration

```
usage: ./migration json -schema <schema> -data <path> -graph <name> [-engine <url>]
       -schema           json schema file or directory
       -data             json data file or directory
       -graph            graph name (or defaults to the default keyspace)
       -engine           MindmapsDB engine URL, default localhost
```

### OWL Migration

```
usage: ./migration.sh owl -file <path> [-graph <name>] [-engine <url>]
       -file             OWL file
       -graph            graph name (defaults to the name of the file with spaces replaced by _)
       -engine           MindmapsDB engine URL, default localhost
```

Please see the [OWL migration documentation](./OWL-migration.html) for further information.


## Where Next?
You can find further documentation about migration in our API reference documentation (which is in the `/docs` directory of the distribution zip file, and also online [here](https://mindmaps.io/pages/api-reference/latest/index.html)).

Please take a look at our examples to further illustrate [SQL migration](../examples/SQL-migration.html) and [OWL migration](../examples/OWL-migration.html).
{% include links.html %}


## Comments
Want to leave a comment? Visit <a href="https://github.com/mindmapsdb/docs/issues/32" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
