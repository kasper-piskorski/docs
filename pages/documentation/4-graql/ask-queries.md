---
title: Ask Queries
keywords: graql, query, ask
last_updated: August 10, 2016
tags: [graql]
summary: "Graql Ask Queries"
sidebar: documentation_sidebar
permalink: /documentation/graql/ask-queries.html
folder: documentation
---

An ask query will return whether the given [match query](match-queries.html) has any results.

<ul id="profileTabs" class="nav nav-tabs">
    <li class="active"><a href="#shell1" data-toggle="tab">Graql</a></li>
    <li><a href="#java1" data-toggle="tab">Java</a></li>
</ul>

<div class="tab-content">
<div role="tabpanel" class="tab-pane active" id="shell1">
<pre>
match dragon isa pokemon-type;
ask;
</pre>
</div>
<div role="tabpanel" class="tab-pane" id="java1">
<pre>
qb.match(name("dragon").isa("pokemon-type")).ask().execute();
</pre>
</div> <!-- tab-pane -->
</div> <!-- tab-content -->


{% include links.html %}


