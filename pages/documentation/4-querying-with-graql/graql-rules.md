---
title: Graql Rules
keywords: graql, reasoner
last_updated: December 9th 2016
tags: [graql, reasoner]
summary: "Graql Rules"
sidebar: documentation_sidebar
permalink: /documentation/graql/graql-rules.html
folder: documentation
---


<!--Page to comprise: 
(1) section that talks about rules, 
(2) description of their syntax, what is allowed in their conclusion and what is not, 
(3) configuration options, whether to materialize or not, 
(4) maybe cover the future options for a choice of different reasoning algorithms etc?

?? Comparison of FC and BC?

Look through documentation on internal wiki:
http://wiki.grakn.ai/docs/mindmaps-reasoner-project-definition
and associated pages
-->

## Rule Objects

We define the following hierarchy of rule objects - concepts:

{ConceptType}<--(sub)--{RuleType}
{RuleType}<--(sub)--{Inference Rule}
{RuleType}<--(sub)--{Ronstraint Rule}
{inference rule}<--(isa)--{instance of inference rule, e.g. if daughter+brother then niece}
{constraint rule}<--(isa)--{instance of constraint rule, e.g. if daughter then not married}

We introduce a single rule object in the Object Model being able to accommodate inference rules and constraint rules:

```
class Rule extends ConceptInstance - wrapped around a tinkerpop vertex class
// to check for possible inference/satisfiability, we always evaluate if(lhs){evaluate rhs}
// action specifies action executed on RHS pattern if expectation is not met
{
Query (String?) LHS
Query (String?) RHS

//only for constraint rules, this may be removed after graql implements negation
Boolean expectation {true, false}
Boolean materialise {true, false}

//specifies whether it is an inference or constraint rule
// this inherited from Concept.java
String type 

//add an edge with label "hypothesis" from conceptType to Rule concept
addHypothesis(ConceptType conceptType)

//add an edge with label "conclusion" from Rule concept to conceptType
addConclusion(ConceptType conceptType)
}
```


## Rule Examples

### Example Rule: "All Swedish women are pretty"
 
```Graql
Rule:
LHS: match
   $x isa person;
	 $x has gender "female";
   $x has nationality Sweden";

RHS: insert
     $x has beauty-score 9;
```

Upon addition of this rule instance, the concept types of Person, Gender, and Nationality will be linked to it. 

Once an instance of Gender or Nationality is added to the graph, it can be checked for being a partial instantiation of the rule by checking whether the value of the resource is met after downloading the relevant connected Rule Instance. If it is a partial instantiation then the LHS is executed with a where chain specifying the given instance.

### Example Rule: If two companies have the same location and their directors are related, flag them as "suspicious"
 
```Graql
select match
    $x isa company;
    $y isa company;
    $x has address "address";
    $y has address "address";
    ($x, $u) isa directorship;
    ($y, $v) isa directorship;
    ($u, $v) select $x, $y;

insert
    $x has flag "suspicious";
    $y has flag "suspicious";
```

Upon addition of this rule instance, the concept types of Company, Address and Directorship will be linked to it. 

### Example Rule: "If a Person's parent has a brother, the person has an uncle"
 
```Graql
Rule:
LHS: match
  	 ($x, $y);
     ($y, $z);
  
     $x isa person;
     $y isa person;
     $z isa person;
     $a (child $x, parent $z) isa hasParent;

     ($z, $y) isa hasBrother select $x, $y;
  
RHS: insert 
     isa hasUncle(nephew/niece $x, uncle $y);
```

### Example Rule: "Transitivity of a specific relation"
 
```Graql
Rule:
LHS: match
  	 (roleA $x, roleB $y) isa someRelationType;
     (roleA $y, roleB $z) isa someRelationType select $x, $z;
     
RHS: insert 
		 isa someRelationType(roleA $x, roleB $z);
```

### Example Rule: "Two concepts being related results in something"
 
```Graql
Rule:
LHS: match
     ($x, $y)
  
RHS: insert
     isa someRelationType($x, $y);
```


## Constraint examples

### Example Constraint: "Every person that is employed by a company must have a contract between them"

```Graql
Constraint:
IF (LHS)
        match
        $x isa person
        $y isa person
        (employee $x, employer $y) isa employment
THEN (RHS)
        match
        ($x, $y) isa contract ask;
    
expectation = true
```


Suppose the following piece of information is added:
```
$x = 'bob'
```

We then execute the constraint LHS on 'bob':

```java-test-ignore
Check whether bob is an instantiation of LHS:

        Query.parser("match
        $x isa person;$y isa person;
        (employee $x, employer $y) isa employment").where($x id 'bob')
       
        we also need to check for:
       
        Query.parser("match
        $x isa person;$y isa person;
        (employee $x, employer $y) isa employment").where($y id 'bob')
       
        put the result of all queries above in to a list of (x,y) pairs called LHS
then
check if RHS meets the expectation:
                     
        for every item i(x,y) in LHS
        Constraint.expectation ?= Query.parser("match
        ($x, $y) isa contract ask").where($x id i.x, $y id i.y);
```

Alternatively we can process data on a relation basis, say the following information is added:

```
Employment(bob, alice)        
```

```java-test-ignore
Check whether added relation leads to instantiation of LHS:

(x, y) = Query.parser(Constraint.LHS).where($x id 'bob', $y id 'alice')
  
Then check if RHS meets the expectation:
Constraint.expectation ?= Query.parser(Constraint.RHS).where($x id x, $y id y)
```


## Support of Graql Constructs

### Queries

| Description        | Example  | Input | Body | Head | Level of support  
| -------------------- |:---:|:---|:--|:--| -----:|
| conjunctive queries        | - |  ✓ | ✓ | x | supported |
| disjunctive queries        | - |  ✓ | x | x |  supported |
| nested queries | queries with multiply nested conjunctions/disjunctions | ✓ | x | x | supported |
| reified queries           | queries with complex constructs represented as variables (e.g. relations as roleplayers )  | ✓ | ✓ | x| experimental support |


### Variable Patterns

| Description        | Example           | Body | Head | Level of support  
| -------------------- |:--- |:--|:--|-----:|
| isa | match $x isa pokemon; | ✓ | x | supported |
| id  | match $x id "Articuno";  | ✓ | indirect only  | supported |
| value | match $x value contains "lightning";  | ✓ | indirect only  | supported |
| has | match $x has pokedex-no < 20; | ✓ |  |  supported |
| relations with specified types | match (ancestor: $x, descendant: $y) isa ancestorship; | ✓ | ✓ | supported |
| relations with incomplete roles | match (ancestor: $x, $y) isa ancestorship; | ✓ | ✓, not recommended | supported |
| relations without type | match (ancestor: $x, $y); | ✓ | ✓ | supported |
| match all relations | match ($x, $y); | ✓| x | supported |
| relations with rscs in one line | match (ancestor: $x, $y) isa rel-typel has resource 'value'; |✓| x | supported |
| relations with user specified var | match $r ($x, $y); |✓| ✓ | supported |
| relations with roles as variables | match ($r: $x, $y) isa rel-type;  | ✓ | ✓ | experimental support |
| relations with type var | match (ancestor: $x, $y) isa $rel; | ✓ | ✓ | supported |
| resource comparison | match $x value > $y;  | ✓ | x | supported |
| != | match $x != $y; | ✓ | x | supported |
| has-scope | match ($x, $y) has-scope $z;match $x has-scope $y;  | ✓ | x | supported |

### Type Properties

| Description        | Example   | Body | Head | Level of support  
| -------------------- |:---|:--|:--|-----:|
| sub        | match $x sub type; | ✓| x | supported |
| plays-role | match $x plays-role ancestor; |✓| x | supported   |
| has-resource        | match $x has-resource name; | ✓ | x |  supported   |
| has-role   | match evolution has-role $x; | ✓ | x | supported |
| is-abstract | match $x is-abstract; | ✓ | x | supported |
| datatype | match $x isa resource, datatype string; | ✓| x |  supported |
| regex | match $x isa resource, regex /hello/; | ✓ | x | supported |

### Predicates

| Description        | Example           | Level of support  
| -------------------- |:---| -----:|
| Comparators        | match $x has height = 19, has weight > 1500;               | supported|
| Contains | match $x has description $desc;$desc value contains "underground";    | supported  |
| Regex        | match $x value /.\*(fast&#124;quick).\*/;      | supported  |
| And and Or   | match $x has weight >20 and <30;        | supported |

### Modifiers

| Description        | Level of support  
| -------------------- | -----:|
| limit        | supported |
| offset | supported  |
| distinct | supported  |
| order   | supported |
| select | supported |


{% include links.html %}

