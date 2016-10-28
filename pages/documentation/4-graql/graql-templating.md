---
title: Graql Templating
keywords: graql, java
last_updated: October 14, 2016
tags: [graql]
summary: "How to use templating on top of Graql"
sidebar: documentation_sidebar
permalink: /documentation/graql/graql-templating.html
folder: documentation
---

## Basic Syntax
 
Graql Templates are Graql statements with the addition of functions and variables. Graql templates are primarily used for migrating data into a Mindmaps graph, although as a component of graql can be used whevever writing Graql in java. You can find more examples of their usage in the Migration section of the documentaion. 

{% include note.html content=" For the moment, Graql Template usage is limited to Graql java. It will not work in the shell. " %}

Templates are used to expand Graql queries given data. They allow you to control the flow of information.

Accessing a single value:
```graql
$x isa thing has value <value>
```

Value in a nested context:
```graql
$x isa person has name <person.name.firstName>
```

## Replacement

Replacement occurs inside the `<` `>` characters or when executing a macro that is not a condition. 

{% include note.html content=" When replacing values in the output, the templating engine will automatically surround any `string` in quotes. It will not for any `int`, `double` or `boolean` " %}

A quick example of what the replacement looks like:

```graql
first is a <string>, second a <long>, third a <double>, fourth a <boolean>
```

```graql
first is a "string", second a 40, third a 0.001, fourth a false
```

## Logic 

### Expressions

Expressions can be used as the conditions in `if`/`elseif` statements or as the contents of `macro` statements. 

#### Boolean expressions

Boolean expressions only accept `true` and `false` (they will not work with `0/1` or `1/-1`). 

|  Expression | Definition     | Usage | 
|-------------|---------------|---| --- |
| `and`          | `this && that`  |  `if (and this that) do { ... }` |
| `or`          | `this || that`  |  `if (or this that) do { ... }` |
| `not`          | `!this`   |  `if (not this) do { ... }` | 

#### Comparison

|  Expression | Definition     | Usage | Notes
|-------------|---------------|---| --- |
| `eq`          | `this == that`  |  `if (eq this that) do { ... }` |
| `ne`          | `this != that`  |  `if (ne this that) do { ... }` |
| `gt`          | `this > that`   |  `if (gt this that) do { ... }` | operates on numbers
| `ge`          | `this >= that`  |  `if (ge this that) do { ... }` | operates on numbers
| `lt`          | `this < that`   |  `if (lt this that) do { ... }` | operates on numbers
| `le`          | `this <= that`  |  `if (le this that) do { ... }` | operates on numbers

#### Grouping expressions

Expressions can be grouped using `(` and `)`. 

//TODO

### Iteration

Graql Templates allow you to iterate over a maps or lists.

Example 1: `for` over a list
```graql
for (whale in whales)
do { $x isa whale has name <whale>; }
```

Example 2: `for` over a map
```graql
insert $x isa person;
    for (name in names) do {
        $x has nickname <name.nickname> ;
    }
```

Example 3: "enhanced" `for` over a map
```graql
insert $x isa person;
    for (names) do {
        $x has nickname <nickname> ;
    }
```

When iterating over a map as in Example 3, you are allowed to use the "enhanced" for syntax. In this loop, it is not required to provide the item name. The properties within the `do` block context are inferred to be the first level children of tbe property in the `for` statement.

Example 5: doubly nested `for`
```graql
for (people) do { 
insert $x isa person has name <name>;
    for (addresses) do {
    insert $y isa address ;
        $y has street <street> ;
        $y has number <number> ;
        ($x, $y) isa resides;
    }
}
```

### Conditionals 

`if`, `else` and `elseif` are the included commands that provide conditional logic. 


Example 6: `if`
```graql
if (ne dog null)
do { insert $dog isa dog; }
```

Example 7: `if`...`else`
```graql
if (ne firstName null) do {
    insert $person has name <firstName>;
} else {
    insert $person;
}
```


### Macros

Macros are denoted by an `@` symbol prefixing the name of the macro function. Mindmaps has included the following functions to the basic templating logic.  

#### noescp

Macro `noescp` is short for "no escape". This function will not add quotes or escape the characters inside the value when doing replacement. Accepts exactly one argument.

e.g.
```graql
insert $person has introduction "Hi! My name is @noescp(firstname) @noescp(lastname)";
```

#### int

`int` converts the contents of the data to an interger. Accepts exactly one argument.

e.g.
```graql
match $x isa thing has value @int(value);
```

#### double

`double` converts the contents of the data to a double. Accepts exactly one argument.

e.g.
```graql
match $x isa thing has value @double(value);
```

#### equals

The `equals` macro returns a boolean and as such can be used in conditional statements. It can also be used in the Graql statment. Requires at least two arguments. 

e.g. in Graql
```graql
insert $x isa hasEquivalentResource value @equals(this that other)
```

e.g. in conditional
```graql
if (@equals(this that)) do { equals } else { not }"
```

### Scopes

When using iterators Graql variables will be automatically suffixed with an aggregate index of the loop. This is necessary because otherwise Graql would confuse variables belonging to different statements when running the queries. 

For example, the following loop:
```graql
insert $x isa person has name <name>;
    
for (addresses) do {
    insert $y isa address ;
        $y has street <street> ;
        $y has number <number> ;
        ($x, $y) isa resides;
}

$y isa person, id 1234;
($x, $y) isa friends;
```

would result in the expanded Graql query:
```graql
insert $x0 isa person has name "Elmo";
    
    insert $y0 isa address ;
        $y0 has street "North Pole" ;
        $y0 has number 100 ;
        ($x0 $y0) isa resides;
    insert $y1 isa address ;
        $y1 has street "South Pole" ;
        $y1 has number -100 ;
        ($x0 $y1) isa resides;

    $y2 isa person, id 1234;
    ($x0, $y2) isa friends; 

insert $x1 isa person has name "Flounder";
    
    insert $y3 isa address ;
        $y3 has street "Under the sea" ;
        $y3 has number 22 ;
        ($x1 $y3) isa resides;

    $y4 isa person, id 1234;
    ($x1, $y4) isa friends; 
```

## Usage in Java

For the moment, Graql templating can only be used through the java API:

```java
String template = "" +
                "for (whale in whales ) do {" +
                "   $x isa whale has name <whale>;" +
                "}";

Map<String, Object> data = Collections.singletonMap("whales", Arrays.asList("shamu", "dory"));

Graql.parseTemplate(template, data);
```

The data argument to the `parseTemplate()` function is a `Map<String, Object>`, where the `Object` is one of a `List<Object>`, `String`, `Double`, `Integer`, `Boolean` or another nested `Map<String, Object`.

{% include links.html %}

