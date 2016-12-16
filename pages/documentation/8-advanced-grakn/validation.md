---
title: Grakn Data Validation
keywords: validation
last_updated: November 17, 2016
tags: [advanced-grakn]
summary: "The page describes schema validation."
sidebar: documentation_sidebar
permalink: /documentation/advanced-grakn/validation.html
folder: documentation
comment_issue_id: 
---


## Introduction

This page discusses the structural validation rules that are enforced in a Grakn graph. 
The following consistency checks are executed upon `commit` depending on what is being committed:

### Plays Role Validation 

This validation rule simply checks if an _Entity_ (which is a role player in a relation) is allowed to play the role it has been allocated to. For example, this is the rule that would prevent a dog from being a driver in a car.

### Type Validation

This validation rule ensures that abstract types do not have any instances. For example if we declare the type `vehicle` to be abstract, with `car` and `motorbike` to be sub types of `vehicle`, then only cars and motorbikes are allowed to have instances. 

### Role Type Validation

This rule checks that non abstract _Role Types_ are part of a _Relation Type_. For example if we declare the _Role Type_ `husband` and forget to link it to any `Relation Type`, then this check will fail.

### Relation Validation

A relation is valid if all of the role players of the relation are allowed to play their corresponding roles. 
This also includes checking that the relation is an instance of a valid _Relation Type_. A _Relation Type_ is valid if it has at minimum two roles. 

### Relation Type Validation

This checks that non abstract relation types have at least 2 roles. 
For example, a _Relation Type_ `marriage` with only one role `husband` would fail this check.

### An Example of Validation

Let us say that we want to model a marriage between a man `Bob` and woman `Alice`.
This will be our first attempt:

```
insert
  'name' sub resource-type datatype string;
  'person' is-abstract sub entity-type;
  'man' is-abstract sub 'person';
  'woman' sub 'person';
  'husband' sub role-type;
  'wife' sub role-type;
  woman plays-role wife;
  person has-resource name;
  'husband' sub role-type;
  'wife' sub role-type;
  'marriage' sub relation-type;
  marriage has-role husband;
  $x has name 'Bob' isa man;
  $y has name 'Alice' isa woman;
  (husband: $x, wife: $y) isa marriage;
```
        
This first attempt was horrible as we ended up failing all the validation rules.         
On commit we will see an error similar to this:

```
A structural validation error has occurred. Please correct the [`5`] errors found.
RoleType ['wife'] does not have exactly one has-role connection to any RelationType.
The abstract Type ['man'] should not have any instances
Relation Type ['marriage'] does not have two or more roles
The relation ['RELATION-marriage-2b58b138-2c33-478c-8e8c-e7b357a20941'] has an invalid structure. This is either due to having more role players than roles or the Relation Type ['marriage'] not having the correct has-role connection to one of the provided roles. The provided roles('2'): ['husband,wife,']The provided role players('2'): ['husband,wife,']
The type ['man'] of role player ['ENTITY-man-2482cb91-1f12-40ea-b659-49d07d06ddf1'] is not allowed to play RoleType ['husband']
```
    
Lets see why:

1. **Role Type Validation** failed because the role `wife` is not connected to any _Relation Type_
2. **Relation Type Validation** failed because `marriage` only has one role `husband`.
3. **Type Validation** failed because we accidental made `man` abstract and we declared `Bob` to be an instance of `man`.
4. **Plays Role Validation** failed because we forgot to say that a `man` can play the role of `husband`.
5. **Relation Validation** failed because `Alice` is playing the role of a `wife` which is not part of a `marriage` and `Bob` is playing the role of a `husband`, which as a man he is not allowed to do.

Let's fix these issues and try again:

```
insert
  'name' sub resource-type datatype string;
  'person' sub entity-type;                   # Fix (4)
  'man' sub 'person';
  'woman' sub 'person';
  'husband' sub role-type;
  'wife' sub role-type;
  man plays-role husband;                     # Fix (3) and part of (5)
  woman plays-role wife;
  person has-resource name;
  'husband' sub role-type;
  'wife' sub role-type;
  'marriage' sub relation-type;
  marriage has-role husband;
  marriage has-role wife;                     # Fix (1) and (2) and part of (5)
  $x has name 'Bob' isa man;
  $y has name 'Alice' isa woman;
  (husband: $x, wife: $y) isa marriage;
```

Now we are correctly modelling the marriage between `Alice` and `Bob`.


