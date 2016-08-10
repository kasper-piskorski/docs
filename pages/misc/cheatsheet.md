# match

<big><pre>
match [ [variable](#variable) | [pattern](#pattern) ; ... ]
      \[ [select | limit | offset | distinct | order ...](#modifier) \]</pre></big>

```
match $x isa movie
```
Match patterns in the graph.

```
match $x isa movie, id "titanic"; (actor $a, $x);
```
Match multiple properties together.

## pattern

<big><pre>
[variable](#variable)</pre></big>

```
match $x isa person, value "Guillermo del Toro"
```
A pattern is often just a single variable.

<big><pre>
[pattern](#pattern) or [pattern](#pattern)</pre></big>

```
match $x isa movie or $x isa person
```
Two patterns can be combined into a disjunction with `or`

<big><pre>
{ [pattern](#pattern) ; ... }</pre></big>

```
match $x isa movie or { (actor $x, $y); $y id 'the-martian'; }
```
Several patterns can be combined into a conjunction with `{ }`

## modifier

<big><pre>
select [name](#name) , ...</pre></big>

```
match $m isa movie; (actor $a1, $m); (actor $a2, $m);
select $a1, $a2
```
The selected variables can be explicitly defined.

<big><pre>
limit {integer}</pre></big>

```
match (director $x, $y)
limit 10
```
The results can be limited.

<big><pre>
offset {integer}</pre></big>

```
match (director $x, $y)
limit 10
offset 20
```
Some results can be skipped.

<big><pre>
distinct</pre></big>

```
match $m isa movie, id 'dr-strangelove'; (actor $a, $m);
select $a
distinct
```
The results can be made distinct (de-duplicated).

<big><pre>
order by [name](#name) \[ (has [resource-type](#identifier)) \] \[ asc | desc \]</pre></big>

```
match $x isa person order by $x
```
Order by id in ascending order.

```
match $x isa person order by $x(has name) desc
```
Order by name in descending order.

# ask

<big><pre>
[match](#match) ask</pre></big>

```
match $x isa person, id 'james-cameron'; (actor $x)
ask
```
Return whether the match query has any results.

# insert

<big><pre>
insert [variable](#variable) ; ...</pre></big>

```
insert 'finding-dory' isa movie;
```
Insert concepts into the graph.

<big><pre>
[match](#match) insert [variable](#variable) ; ...</pre></big>

```
EXAMPLE
```
description

# delete

<big><pre>
[match](#match) delete [variable](#variable) ; ...</pre></big>

```
EXAMPLE
```
description

# variable

<big><pre>
[identifier](#identifier) [property](#property) , ...</pre></big>

```
EXAMPLE
```
description

## name

```
EXAMPLE
```

Names start with a `$`, followed by alphanumeric characters, underscores
or dashes.

## identifier

<big><pre>
[name](#name) | [id](#identifier)</pre></big>

```
EXAMPLE
```

An `id` is a sequence of alphanumeric characters, underscores and dashes, or a quoted string.

## property

<big><pre>
isa [type](#identifier)</pre></big>

```
EXAMPLE
```
description

<big><pre>
id {string}</pre></big>

```
EXAMPLE
```
description

<big><pre>
value [ = | != | < | <= | >= | > | contains ] {value}</pre></big>

```
EXAMPLE
```
description

<big><pre>
has [resource-type](#identifier) [ = | != | < | <= | >= | > | contains ] {value}</pre></big>

```
EXAMPLE
```
description

<big><pre>
( \[ [role-type](#identifier) \] [role-player](#identifier) , ... )</pre></big>

```
EXAMPLE
```
description

<big><pre>
ako [type](#identifier)</pre></big>

```
EXAMPLE
```
description

<big><pre>
has-role [role-type](#identifier)</pre></big>

```
EXAMPLE
```
description

<big><pre>
plays-role [role-type](#identifier)</pre></big>

```
EXAMPLE
```
description

<big><pre>
has-resource [resource-type](#identifier)</pre></big>

```
EXAMPLE
```
description
