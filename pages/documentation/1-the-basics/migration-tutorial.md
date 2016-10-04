---
title: Migrating to MindmapsDB
keywords: setup, getting started
last_updated: August 10, 2016
tags: [getting-started, graql]
summary: "This document will teach you how to load data in different formats to populate a MindmapsDB graph."
sidebar: documentation_sidebar
permalink: /documentation/the-basics/migration-tutorial.html
folder: documentation
comment_issue_id: 32
---

## Introduction
This tutorial shows you how to migrate content in different formats to populate a MindmapsDB graph. If you have not yet set up the MindmapsDB environment, please see the [Setup guide](../get-started/setup-guide.html).

## Migration Shell Script
The migration shell script can be found in `mindmaps-dist/bin` after it has been unzipped. Usage is specific to the type of migration being performed:

### SQL Migration

```
usage: ./migration.sh sql -driver <jdbcDriver> -user <username> -pass <password> -database <url> -graph <graphname> [engine <url>]
       -driver           JDBC driver 
       -user             username for SQL database
       -pass             password for SQL database
       -database         URL to SQL database
       -graph            graph name
       -engine           MindmapsDBengine URL, default localhost
```


### CSV Migration

```
usage: ./migration.sh csv -file <file> -graph <graphname> [-engine <url] [-as <type>]
      -file             csv file to migrate
      -graph            graph name
      -engine           MindmapsDB engine URL, default localhost
      -as               entity type of this file
```

### JSON Migration

```
usage: ./migration json -schema <schema> -data <path> -graph <name> [-engine <url>]
       -schema           json schema file or directory
       -data             json data file or directory
       -graph            graph name
       -engine           MindmapsDB engine URL, default localhost
```

### OWL Migration

```
usage: ./migration.sh owl -file <path> [-graph <name>] [-engine <url>]
       -file             OWL file
       -graph            graph name
       -engine           MindmapsDB engine URL, default localhost
```


## SQL Migration Example

One of the most common use cases of the migration component will be to move data from an RDBMS into MindmapsDB. MindmapsDB relies on the JDBC API to connect to any RDBMS that uses the SQL language. The example that follows is written in MySQL, but MindmapsDB SQL migration will work with any database it can connect to using a JDBC driver. This has been tested on MySQL, Oracle and PostgresQL. 

### SQL Schema Migration

Let's say you have an SQL table with the following schema:

```sql
CREATE TABLE pet
(
  name    VARCHAR(20),
  owner   VARCHAR(20),
  species VARCHAR(20),
  sex     CHAR(1),
  birth   DATE,
  death   DATE
);

DROP TABLE IF EXISTS event;

CREATE TABLE event
(
  name        VARCHAR(20),
  date        DATE,
  eventtype   VARCHAR(15),
  remark      VARCHAR(255)
);

ALTER TABLE event ADD FOREIGN KEY ( name ) REFERENCES pet ( name );
```

Each SQL table is mapped to one MindmapsDB entity type. Each column of a table can be mapped as a resource type of that entity type. So the SQL tables above can be directly mapped to a MindmapsDB schema:

```sql
insert

pet isa entity-type,
	has-resource name,
	has-resource owner,
	has-resource species,
	has-resource sex,
	has-resource birth,
	has-resource death;

event isa entity-type,
	has-resource name,
	has-resource date,
	has-resource eventtype,
	has-resource remark;

```

In SQL, a `foreign key` is a column that references another column. The MindmapsDB equivalent is a relationship type. Because SQL `foreign key` are not explicitly named, as they are in Mindmaps, the relation and roles have auto-generated names.

The SQL schema line `ALTER TABLE event ADD FOREIGN KEY ( name ) REFERENCES pet ( name );` would be migrated as:

```sql
event-child isa role-type;
event-parent isa role-type;

event-relation isa relation-type, 
 	has-role event-child,
 	has-role event-parent;

event plays-role event-parent;
pet plays-role event-child;
```

### SQL Data Migration

Lets imagine that the data in the SQL database is as follows:

pet:

```
--------------------------------------------------------------- 
| name     | owner  | species | sex | birth      | death      |
--------------------------------------------------------------- 
| Bowser   | Diane  | dog     | m   | 1979-08-31 | 1995-07-29 |
--------------------------------------------------------------- 
| Fluffy   | Harold | cat     | f   | 1993-02-04 | NULL       |
--------------------------------------------------------------- 
| Claws    | Gwen   | cat     | m   | 1994-03-17 | NULL       |
--------------------------------------------------------------- 
| Buffy    | Harold | dog     | f   | 1989-05-13 | NULL       |
--------------------------------------------------------------- 
| Fang     | Benny  | dog     | m   | 1990-08-27 | NULL       |
--------------------------------------------------------------- 
| Puffball | Diane  | hamster | f   | 1999-03-30 | NULL       |
--------------------------------------------------------------- 
```

event:

```
-----------------------------------------------------------------------
| name   | date       | eventtype| remark                             |
-----------------------------------------------------------------------
| Bowser | 1991-10-12 | kennel   | NULL                               |
-----------------------------------------------------------------------
| Fang   | 1991-10-12 | kennel   | NULL                               |
-----------------------------------------------------------------------
| Fluffy | 1995-05-15 | litter   | 4 kittens, 3 female, 1 male        |
-----------------------------------------------------------------------
| Buffy  | 1993-06-23 | litter   | 5 puppies, 2 female, 3 male        |
-----------------------------------------------------------------------
| Buffy  | 1994-06-19 | litter   | 3 puppies, 3 female                |
-----------------------------------------------------------------------
| Fang   | 1998-08-28 | birthday | Gave him a new chew toy            |
-----------------------------------------------------------------------
| Claws  | 1998-03-17 | birthday | Gave him a new flea collar         |
-----------------------------------------------------------------------
```

Similar to how the schema is migrated, each row of data in an SQL table can be mapped to a single entity instance and each column value can be mapped as a resource. 

Another feature of the migration component is that it will turn any `primary key` of a row into the ID of that instance. This works for combined keys as well. 

This is how the first two rows of the `pet` and `event` tables would look if written in Graql:

```sql
insert

$x isa pet id "Bowser",
	has owner "Diane",
	has species "dog",
	has sex "m",
	has birth "1979-08-31",
	has death "1995-07-29";

$y isa event
	has date "1991-10-12",
	has eventtype "kennel";

``` 

If the value of a column is `NULL`, that resource is not added to the graph.

The `name` column of the event instance was not migrated as a resource. This is because that column is a foreign key. The migration component will detect when one of the columns is a `foreign key` and create a relation between those two instances:

```sql
(event-child $x, event-parent $y) isa event-relation;
```

### In Java

While the migration seems rather lengthy when written out in Graql, you only need a few lines of code to accomplish this migration in Mindmaps:

```java
// get the JDBC connection

Connection connection = DriverManager.getConnection(jdbcDBUrl, jdbcUser, jdbcPass);

// get the MindmapsDB connection

MindmapsGraph graph = Mindmaps.factory(Mindmaps.DEFAULT_URI).getGraph("sql-test-graph");
Loader loader = new BlockingLoader("sql-test-graph");

// create migrators and perform migration

SQLSchemaMigrator schemaMigrator = new SQLSchemaMigrator();
SQLDataMigrator dataMigrator = new SQLDataMigrator();

schemaMigrator
        .graph(graph)
        .configure(connection)
        .migrate(loader)
        .close();

dataMigrator
        .graph(graph)
        .configure(connection)
        .migrate(loader)
        .close();

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