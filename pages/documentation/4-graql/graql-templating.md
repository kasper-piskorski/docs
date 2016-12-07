---
title: Graql Templating
keywords: graql, java
last_updated: November 4, 2016
tags: [graql]
summary: "How to use templating on top of Graql"
sidebar: documentation_sidebar
permalink: /documentation/graql/graql-templating.html
folder: documentation
---

## Basic Syntax
 
Graql templates are Graql statements with the addition of functions and variables. Graql templates are primarily used for migrating data into Grakn, although as a component of Graql, they can be used whevever writing Graql in Java. You can find more examples of their usage in the [Migration](../migration/migration.html) section of the documentation. 

{% include note.html content=" For the moment, Graql Template usage is limited to Graql Java. It will not work in the Graql shell. " %}

Templates are used to expand Graql queries, given data. They allow you to control the flow of information.

Accessing a single value:

```graql-template
$x isa thing has value <value>
```

Value in a nested context:

```graql-template
$x isa person has name <person.name.firstName>
```

## Replacement

Replacement occurs inside the `<` `>` characters or when executing a macro that is not a condition. 

{% include note.html content=" When replacing values in the output, the templating engine will automatically surround any `string` in quotes. It will not for any `int`, `double` or `boolean` " %}

A quick example of what the replacement looks like:

```graql-template
first is a <string>, second a <long>, third a <double>, fourth a <boolean>
```

```text
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


### Iteration

Graql Templates allow you to iterate over a maps or lists.

Example 1: `for` over a list:

```graql-template
for (whale in whales)
do { $x isa whale has name <whale>; }
```

Example 2: `for` over a map:

```graql-template
insert $x isa person;
    for (name in names) do {
        $x has nickname <name.nickname> ;
    }
```

Example 3: "enhanced" `for` over a map:

```graql-template
insert $x isa person;
    for (names) do {
        $x has nickname <nickname> ;
    }
```

When iterating over a map as in Example 3, you are allowed to use the "enhanced" for syntax. In this loop, it is not required to provide the item name. The properties within the `do` block context are inferred to be the first level children of the property in the `for` statement.

Example 5: doubly nested `for`:

```graql-template
insert

for (people) do { 
$x isa person has name <name>;
    for (addresses) do {
    $y isa address ;
        $y has street <street> ;
        $y has number <number> ;
        ($x, $y) isa resides;
    }
}
```

### Conditionals 

`if`, `else` and `elseif` are the included commands that provide conditional logic. 

Example 6: `if`:

```graql-template
if (ne dog null)
do { insert $dog isa dog; }
```

Example 7: `if`...`else`:

```graql-template
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

```graql-template
insert $person has introduction "Hi! My name is @noescp(firstname) @noescp(lastname)";
```

#### int

`int` converts the contents of the data to an integer. Accepts exactly one argument.

e.g.

```graql-template
match $x isa thing has value @int(value);
```

#### double

`double` converts the contents of the data to a double. Accepts exactly one argument.

e.g.

```graql-template
match $x isa thing has value @double(value);
```

#### equals

The `equals` macro returns a boolean and as such can be used in conditional statements. It can also be used in the Graql statment. Requires at least two arguments. 

e.g. in Graql

```graql-template
insert $x isa hasEquivalentResource value @equals(this that other)
```

e.g. in conditional

```graql-template
if (@equals(this that)) do { equals } else { not }"
```

### Scopes

During iteration Graql variables will be automatically suffixed with an index.  

For example, the following loop:

```graql-template
insert $x isa pokemon has description <name>;
    
for (types) do {
    $y isa pokemon-type ;
        $y has description <type-description> ;
        (pokemon-with-type: $x, type-of-pokemon: $y) isa has-type;
}

$y isa pokemon;
(ancestor: $x, descendent: $y) isa evolution;
```

would result in the expanded Graql queries:

```graql
insert $x0 isa pokemon has description "Pikachu";
    
    $y0 isa pokemon-type ;
        $y0 has description "Electric" ;
        (pokemon-with-type: $x0, type-of-pokemon: $y0) isa has-type;
    $y1 isa pokemon-type ;
        $y1 has description "Mouse" ;
        (pokemon-with-type: $x0, type-of-pokemon: $y1) isa has-type;

    $y2 isa pokemon;
    (ancestor: $x0, descendent: $y2) isa evolution; 
```

```graql
insert $x1 isa pokemon has description "Raichu";
    
    $y3 isa pokemon-type ;
        $y3 has description "Electric" ;
        (pokemon-with-type: $x1, type-of-pokemon: $y3) isa has-type;

    $y4 isa pokemon;
    (ancestor: $x1, descendent: $y4) isa evolution; 
```

## Usage in Java

For the moment, Graql templating can only be used through the Java API:

```java
String template = "insert " +
                "for (whale in whales ) do {" +
                "   $x isa whale has name <whale>;" +
                "}";

Map<String, Object> data = Collections.singletonMap("whales", Arrays.asList("shamu", "dory"));

Graql.parseTemplate(template, data);
```

The data argument to the `parseTemplate()` function is a `Map<String, Object>`, where the `Object` is one of a `List<Object>`, `String`, `Double`, `Integer`, `Boolean` or another nested `Map<String, Object`.

{% include links.html %}

