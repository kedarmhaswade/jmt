jmt
===

Some concurrency tests for java.util.concurrent and other classes, mainly from an "understanding" standpoint.

Introduction
============

Concurrency is hard. Testing concurrent programs is harder. But perhaps it's easier to understand that concurrency 
is hard if you write tests for concurrency classes. Doug Lea's tck for JSR 166 is execellent (and I really hate to say
that they are not mavenized -- I still hate to see why maven had to be that hard to set up) but these are some independent
tests that I have decided to write. Maybe I will understanding concurrency better that way?
