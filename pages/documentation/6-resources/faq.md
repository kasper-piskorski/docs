---
title: FAQ about Grakn
keywords: troubleshooting
last_updated: September 22, 2016
tags: [getting-started]
summary: "Frequently asked questions about Grakn."
sidebar: documentation_sidebar
permalink: /documentation/resources/faq.html
folder: documentation
comment_issue_id: 23
---

{% include note.html content="This page contains some of the questions were are mostly commonly asked, and is updated regularly. Please feel free to use our [discussion forums](http://discuss.grakn.ai) to ask us for help or advice." %}

## Bugs and strange behaviour

### Why does Grakn hang when I try to start it?   

I am running `mindmaps.sh start` but it hangs on `Starting Cassandra`. Why?

This may be because you have cloned the Grakn repo into a directory which has a space in its name (e.g. `/mindmaps DB`). You can build our code successfully, but when you start `mindmaps.sh`, it hangs because Cassandra needs you to have single word pathnames. Remove the spaces (e.g. `/mindmapsDB`) and try again.

There are other possible reasons why Grakn hangs starting Cassandra. One may be that some other application is using the port 7199, which Cassandra needs.  To find out what is using port 7199:
`lsof -i tcp:7199`

From there, you'll see the PID of application using that port. Check if you can safely kill it or change its port. It may be that another instance of Cassandra is blocking it, and you can simply kill it using:
`pkill -9 java`

Then try `mindmaps.sh start` again.

Failing that, you can often find out more information by looking in the `/logs` directory under your Grakn installation.  

Please see the answer to the question below "Can I run Grakn on an existing Cassandra Platform?" if you are already using Cassandra and want to run Grakn on a different instance of Cassandra to our default.

### Why am I getting ghost vertices?

In a  transaction based environment it is possible to have one transaction removing a concept while another concurrently modifies the same concept. Both
transactions may successfully commit if the backend is eventually consistent, e.g. [Titan Cassandra](http://s3.thinkaurelius.com/docs/titan/1.0.0/common-questions.html.

The concept is likely to still exist with only the modified properties. When using the Titan Cassandra backend it is possible to safeguard against
this by setting the `checkInternalVertexExistence` property to true. However, this will result in slower transaction as more reads will be necessary.

## Working with  Grakn

### Which OS can I use with Grakn?

You can use Mac OS X or Linux right now. We plan to support Windows at a later date.

### How do I load data into Grakn?

There are several ways to load data into Grakn. For small amounts of data (<1000 lines), you an load it directly via the Graql shell. For example, the following loads up the Pokemon example data:

```bash
bin/graql.sh -f examples/pokemon.gql
```

If you have a larger file, you will need to batch load it. The file will be divided in batches that will be committed concurrently. This differs from a regular load, where the whole file is committed in a single chunk when you call commit. See the example below, which loads the Graql file FILENAME.gql, from PATH.

```bash
bin/graql.sh -b PATH/FILENAME.gql
```

In order to check the status of the loading, you can open a new terminal window, navigate to the logs directory of your Grakn installation and run the command:

```bash
tail -f mindmaps.log
```

### Can I run Grakn on an existing Cassandra Platform?

By default, Grakn is shipped with TitanDB, which in turn relies on Cassandra. When you call `mindmaps.sh start`, this starts a Cassandra instance and then starts the Mindmaps server.  You are not bound to use our instance of Cassandra, and can make adjustments to the settings in the `.properties` file in the `conf/main` directory of the Grakn, e.g. to make Titan use your Cassandra instance.

Specifically you should change the following parameters:

```bash
# Host Location
storage.hostname=127.0.0.1
```

You can also, for example, add the following to specify a custom port:

```bash
storage.port = 1234
```

Please refer to the [Titan documentation](http://s3.thinkaurelius.com/docs/titan/1.0.0/titan-config-ref.html#_storage) for more information.


### Do applications written on top of Grakn have to be in Java?

Currently, there is no official support for languages other than Java. But we are open source and would be very willing to accept proposals from our community, and work with contributors, to create bindings to other languages.

### How do I visualise a graph?

Grakn comes with a basic visualiser, with a web-interface. We appreciate any feedback you give us about it via the [discussion boards](https://discuss.grakn.ai/t/visualise-my-data/57).

Once you have started Grakn, you will see a message in the console, like:

```bash
Started ServerConnector@7aa5814d{HTTP/1.1,[http/1.1]}{0.0.0.0:4567}
```

(We are working to improve this message, in fact in the new release it will be something more similar to `Mindmaps Engine is ready. Listening on [http://0.0.0.0:4567]`.)

You can then open your browser and connect to the address printed on the console (http://0.0.0.0:4567). Select the Graql Visualiser tab and you will see a Graql shell, from which you can input queries, for example:

```graql
match $x isa type;
```

In result, you will see the resulting nodes and relations displayed inside the graql visualiser on the same page.

### How do I clear a graph?

I want to clear the graph I've been experimenting with and try something with a new, different schema and dataset. How do I do it?

If you are using the Java API, it's a simple as:

```java-test-ignore
graph = Mindmaps.factory(Mindmaps.DEFAULT_URI, "my-graph").getGraph();
graph.clear();
```

If you are using the Graql shell and have not committed what you have in the graph, you can just quit the shell and restart it, and all is clean.

If you've committed, then you must currently remove your installation and re-create it.  First, call

```bash
pkill -9 java
```

Then delete the Grakn install directory, and unzip a fresh copy from the distribution zip. It isn't very elegant at present, and we are working on a simpler way to clear out a graph.

{% include links.html %}

## Comments
Want to leave a comment? Visit <a href="https://github.com/graknlabs/docs/issues/23" target="_blank">the issues on Github for this page</a> (you'll need a GitHub account). You are also welcome to contribute to our documentation directly via the "Edit me" button at the top of the page.
