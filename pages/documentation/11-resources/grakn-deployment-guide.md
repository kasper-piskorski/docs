---
title: Grakn Deployment Guide
keywords: setup, getting started, download
last_updated: December 8th 2016
tags: [getting-started]
summary: "A guide to setting up a production deployment of GRAKN.AI."
sidebar: documentation_sidebar
permalink: /documentation/resources/grakn-deployment-guide.html
folder: documentation
comment_issue_id: 
---


## Introduction

This guide offers advice on how to:
- run Grakn in a production environment
- upgrade to the latest version of the distribution

The releases from our GitHub repository are self-contained packages and has all the components you need to run Grakn:
- Grakn stack
- Kafka and Zookeeper
- Cassandra

You can run all these component separately depending on the redundancy levels you required.

## Setting up Grakn
### Standalone Grakn
You can run a standalone instance of Grakn by starting it with `grakn.sh start`. This will produce a working environment for importing and analysing your data.
By default Grakn stores your data in the extracted directory, under `db/cassandra/`.
We recommend changing this to another location to make upgrading Grakn easier in the future.

You can specify the location in the `conf/cassandra/cassandra.yaml` config file:
- data_file_directories: eg. /var/lib/cassandra/data
- commitlog_directory: eg. /var/lib/cassandra/commitlog
- saved_caches_directory: eg. /var/lib/cassandra/saved_caches

You will need to amend this file with every new version.

### Clustered database
To add reliability and redudancy on the database layer, you can run Cassandra (or other storage backends) separately and point Grakn to use the node/cluster.
Running Cassandra requires an entire guide in itself - our brief recommendation is having at the minimum a cluster of 3 nodes with replication factor (RF) 3.

Specify an external database cluster in `conf/main/grakn.properties`:
- storage.hostname: eg. 10.0.10.1,10.0.10.2,10.0.10.3

For further information on running Cassandra, see:
- [Cassandra documentation](http://cassandra.apache.org/doc/latest/operating/index.html)
- [AWS Cassandra whitepaper](https://d0.awsstatic.com/whitepapers/Cassandra_on_AWS.pdf)

### Distributed
For a fully scalable and distributed setup, you will need to run each component of Grakn in a separate cluster.
This provides the highest level of redundancy and availability; you will want this for high throughput systems and to make the most out of Grakn. 

The database layer will need to be configured similarly to the "clustered database" setup above.

Grakn Engine uses the [Apache Kafka](https://kafka.apache.org/) streaming platform for distributed task execution. Kafka keeps track of its brokers' state in [Zookeeper](https://zookeeper.apache.org/).
Configure Grakn to use an external Kafka and Zookeeper cluster in `conf/main/grakn-engine.properties`:
- tasks.kafka.bootstrap-servers: eg. 10.0.20.1:9092,10.0.20.2:9092,10.0.20.3:9092
- tasks.zookeeper.servers: eg. 10.0.30.1:2181,10.0.30.2:2181,10.0.3.30:2181

Once the configuration files are correct, you can start multiple instances of Grakn Engine only with `bin/grakn-engine.sh start`.

## Upgrading Grakn
### Standalone


### Distributed


## Comments
