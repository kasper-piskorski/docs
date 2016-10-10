---
title: An Example of Migrating SQL to MindmapsDB
keywords: migration
last_updated: September 19, 2016
tags: [migration]
summary: "A short example to illustrate migration of SQL to MindmapsDB"
sidebar: documentation_sidebar
permalink: /documentation/examples/SQL-migration.html
folder: documentation
comment_issue_id: 42
---

If you have not yet set up the MindmapsDB environment, please see the [Setup guide](../get-started/setup-guide.html). For a comprehensive guide to migration, please see the [Migration Tutorial](../the-basics/migration-tutorial.html).

## Migrating the World

In this example, we will show you how to import SQL data, from a relational database, to a MindmapsDB graph. The code for this example can be found in our [github repo](https://github.com/mindmapsdb/sample-projects/tree/master/example-sql-migration). 

### Prerequisites

#### MySQL

To run this example, you should have set up [MySQL](http://dev.mysql.com/doc/mysql-getting-started/en/) and the [MySQL world](http://dev.mysql.com/doc/world-setup/en/world-setup-installation.html) example. 

One you have the SQL database loaded, you need to allow the MindmapsDB migration default user (username: `mindmaps`, password: `mindmaps`) access, which you can do with the following command from within the MySQL shell:

```sql
CREATE USER 'mindmaps'@'localhost' IDENTIFIED BY 'mindmaps';
GRANT ALL PRIVILEGES ON *.* TO 'mindmaps'@'localhost'
	WITH GRANT OPTION;
```

This will allow the migration example access to your new MySQL world database. 

#### MindmapsDB Engine

MindmapsDB Engine needs to be running for this example to work. If you need help starting Engine, please see the [Setup guide](../get-started/setup-guide.html).

### Running the Example

You can run this example by running the [`Main`](https://github.com/mindmapsdb/sample-projects/blob/master/example-sql-migration/src/main/java/Main.java) class. Check out the `SqlWorldMigrator` class for the bulk of the migration code.  

We run a few queries in the example to prove that the data has been migrated. After running the example, you should be able to answer the following questions (see the bottom of the page for answers - but please don't peek until you've tried it!):

+ What are the Types in the World MindmapsDB graph?
+ How many countries are in the world?
+ How may cities are in the world?
+ What are the cities in Niger?

(We do not provide any guarantees for data integrity. Data is provided by MySQL.)

### In-Memory Example (Lazy folks here, please)

If you are feeling lazy, and do not want to install MySQL to test this out, we do provide an in-memory SQL database option.

You can run this example by running the [`MainInMemory`](https://github.com/mindmapsdb/sample-projects/blob/master/example-sql-migration/src/main/java/MainInMemory.java) class. It demonstrates the same functionality by pre-loading the World data into an [H2](http://www.h2database.com/html/main.html) SQL database.

## Test Yourself Answers

**What are the Types in the World MindmapsDB graph?**   
Answer:
```
country
city 
countrylanguage
```     	

**How many countries are in the world?**   
Answer: `239`

**How may cities are in the world?**   
Answer: `4079`

**What are the cities in Niger?**   
Answer: 
```
Niamey
Zinder
Maradi
```


## Where Next?

After running this SQL migration, check out the [Graql documentation](../graql/overview.html) and the [Graph API documentation](../core-api/overview.html) for more instructions on how you can explore **the world**.


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
        <td>05/10/2016</td>
        <td>First release.</td>        
    </tr>

</table>

## Comments
Want to leave a comment? Visit <a href="https://github.com/mindmapsdb/docs/issues/42" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.

