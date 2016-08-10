# match

 <pre>
match \[ [variable](#variable) | [pattern](#pattern); ... \]
      \[ [select | limit | offset | distinct | order ...](#modifier) \]
</pre>

```
match $x isa movie
```
Match patterns in the graph.

```
match $x isa movie, id "titanic"; (actor $a, $x);
```
Match multiple properties together.

## pattern

 <pre>
[variable](#variable)
</pre>

```
match $x isa person, value "Guillermo del Toro"
```
A pattern is often just a single variable.

 <pre>
[pattern](#pattern) or [pattern](#pattern)
</pre>

```
match $x isa movie or $x isa person
```
Two patterns can be combined into a disjunction with `or`

 <pre>
{ [pattern](#pattern) ; ... }
</pre>

```
match $x isa movie or { (actor $x, $y); $y id 'the-martian'; }
```
Several patterns can be combined into a conjunction with `{ }`

## modifier

 <pre>
select [name](#name) , ...
</pre>

```
match $m isa movie; (actor $a1, $m); (actor $a2, $m);
select $a1, $a2
```
The selected variables can be explicitly defined.

 <pre>
limit {integer}
</pre>

```
match (director $x, $y)
limit 10
```
The results can be limited.

 <pre>
offset {integer}
</pre>

```
match (director $x, $y)
limit 10
offset 20
```
Some results can be skipped.

 <pre>
distinct
</pre>

```
match $m isa movie, id 'dr-strangelove'; (actor $a, $m);
select $a
distinct
```
The results can be made distinct (de-duplicated).

 <pre>
order by [name](#name) \[ (has [resource-type](#identifier)) \] \[ asc | desc \]
</pre>

```
match $x isa person order by $x
```
Order by id in ascending order.

```
match $x isa person order by $x(has name) desc
```
Order by name in descending order.

# ask

 <pre>
[match](#match) ask
</pre>

```
match $x isa person, id 'james-cameron'; (actor $x)
ask
```
Return whether the match query has any results.

# insert

 <pre>
insert [variable](#variable) ; ...
</pre>

```
insert 'finding-dory' isa movie;
```
Insert concepts into the graph.

 <pre>
[match](#match) insert [variable](#variable) ; ...
</pre>

```
match $x isa person, has 
```
description

# delete

 <pre>
[match](#match) delete [variable](#variable) ; ...
</pre>

```
EXAMPLE
```
description

# variable

 <pre>
[identifier](#identifier) [property](#property) , ...
</pre>

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

 <pre>
[name](#name) | [id](#identifier)
</pre>

```
EXAMPLE
```

An `id` is a sequence of alphanumeric characters, underscores and dashes, or a quoted string.

## property

 <pre>
isa [type](#identifier)
</pre>

```
EXAMPLE
```
description

 <pre>
id {string}
</pre>

```
EXAMPLE
```
description

 <pre>
value [ = | != | < | <= | >= | > | contains ] {value}
</pre>

```
EXAMPLE
```
description

 <pre>
has [resource-type](#identifier) [ = | != | < | <= | >= | > | contains ] {value}
</pre>

```
EXAMPLE
```
description

 <pre>
( \[ [role-type](#identifier) \] [role-player](#identifier) , ... )
</pre>

```
EXAMPLE
```
description

 <pre>
ako [type](#identifier)
</pre>

```
EXAMPLE
```
description

 <pre>
has-role [role-type](#identifier)
</pre>

```
EXAMPLE
```
description

 <pre>
plays-role [role-type](#identifier)
</pre>

```
EXAMPLE
```
description

 <pre>
has-resource [resource-type](#identifier)
</pre>

```
EXAMPLE
```
description
