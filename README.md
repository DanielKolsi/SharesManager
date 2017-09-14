SharesManager - V 1.0 - Daniel Kolsi - kolsi.daniel@gmail.com (C)

## INTRODUCTION



The purpose of this Java program is to display a classified report of share
entities. The report consists of different parts which are: 
 - a report of amount of USD settled incoming everyday (for each day, not total!)
 - a report of amount of USD settled out coming everyday (for each day, not total!)
 - all entities (not on daily basis!) ranked based on incoming amount, highest 
   at the top of the list (ranked first) 
 - all entities (not on daily basis!) ranked based on outgoing amount, highest 
   at the top of the list (ranked first)


## Installation and running the program


To run and test the program Java Standard Edition (SE) Runtime environment 8 is 
required. Testing and running should be done by executing the unit tests as
it also displays the required report. All data is populated within the unit
test's @BeforeClass init method. (No specific main method was added.)

.project and .classpath files are provided in the SharesManager/SharesManager
folder for importing this project to Eclipse.

I developed and executed the program from Eclipse Neon (4.6.2).
No external jars besides JUnit 4 are required (junit.jar)
    
      

## Made assumptions  


The technical solution was restricted by both time limit and other 
limits (e.g. only 1-2 external .jars) given in the assignment. The solution
tries to obey the given instructions as well as possible. However, some assumptions
are made mostly due to the interpretation of the assignment text. In real life
situation some clarification from the customer would have been asked to further 
progress within the solution. 

	- Assumption 1: Settled date 

I made the assumption that the settled date can be initially on a "holiday", that is,
in that case it needs to be changed to be the next working day (+ 1-2 days). (Before
checking the sample dates from my Calendar, I thought that the instructed date was 
actually the initial settled date that becomes the settled date if it's a working day,
and if not, then the days are added. But the sample date proved that this cannot be the case.)     
 
	- Assumption 2: "settled incoming everyday"

I found this expression confusing. I was wondering whether this meant total settled incoming or
settled incoming (after possible working day correction!) for each day. I made the assumption that
the latter is more rational for the report, as it's probably more meaningful to see daily amounts summed up 
than all "historic" settled amounts summed up. 
 
	- Assumption 3: Entity information printed in the report
 
 As the instruction wasn't very specific what entity information should be in the report, I selected:
 Rank, Entity id, Amount of trade (daily) and Settlement date
 More information could have been easily added based on the given input entity data.
 
	- Assumption 4: Used time for the test

The exam was instructed to be done in "three hours or so...". I found it difficult or even impossible to 
reach "production quality" code within this time limit. Especially, when it took time figuring out what
was the correct interpretation of the assignment. IMHO the quality could be continually improved (e.g. adding
more and better unit tests, commenting and documenting better, adding more sample data etc., moving even
more towards Java8 with lambdas etc.) by just using more time. However, I decided I'll return it the next day
as it wasn't supposed that more than one working day is used for the exam. Anyway, I can defend my solution
and made assumptions any given moment :) 

## Example report 

 Below is an example report which shows all data displayed (From 7 sample Entities).
 The Income and Outgoing settlement dates are ordered by newest date first. Rank
 is based the amount of trade. Technically the ordering is done by using Java's 
 Comparable and Comparator strategy design patterns within Collections.sort(). 
```java 
 **** BEGINNING OF THE REPORT ****

$$$$$ ENTITY INCOME REPORT $$$$$ 

Income settlement date: 10 Sep 2017 settled incoming (USD): 400.0 (day total income) 
Income settlement date: 04 Jan 2017 settled incoming (USD): 20368.125 (day total income) 
Income settlement date: 04 Jan 2016 settled incoming (USD): 1002.5 (day total income)

$$$$$ Income entities rates as highest settlement first $$$$$

Rank:1 | Entity id: entity4 | Amount of trade: 18112.5 | Settlement date: 04 Jan 2017
Rank:2 | Entity id: entity3 | Amount of trade: 2255.625 | Settlement date: 04 Jan 2017
Rank:3 | Entity id: bar | Amount of trade: 1002.5 | Settlement date: 04 Jan 2016
Rank:4 | Entity id: entity7 | Amount of trade: 400.0 | Settlement date: 10 Sep 2017


$$$$$ ENTITY Outgoing REPORT $$$$$ 

Outgoing settlement date: 12 Sep 2017 settled incoming (USD): 32602.5 (day total Outgoing) 
Outgoing settlement date: 08 Mar 2017 settled incoming (USD): 7245.0 (day total Outgoing) 
Outgoing settlement date: 04 Jan 2016 settled incoming (USD): 10025.0 (day total Outgoing)

$$$$$ Outgoing entities rates as highest settlement first $$$$$

Rank:1 | Entity id: entity5 | Amount of trade: 32602.5 | Settlement date: 12 Sep 2017
Rank:2 | Entity id: foo | Amount of trade: 10025.0 | Settlement date: 04 Jan 2016
Rank:3 | Entity id: entity6 | Amount of trade: 7245.0 | Settlement date: 08 Mar 2017

**** END OF THE REPORT ****
```

## Code folder and package structure

 folder: SharesManager/src/com/sharesmanager/main (package com.sharesmanager.main)
 		   - Entity.java  			(class)
 		   - EntityManager.java	    (interface)
		   - EntityManagerImpl.java (class)
		   - Properties.java        (class)	
 folder: SharesManager/src/com/sharesmanager/test (package com.sharesmanager.test)
 		  - TestEntityManagerImpl.java (unit tests for testing all main units) 
 		  
Comparator and Comparable are used for sorting both displayed report date order
(newest date first) and settlement trade amount / day (highest amount first).  		  
 		  

## About unit tests

Due to the pretty simple nature of the assignment, all unit tests are located 
in one class called TestEntityManagerImpl.java

It consists of 6 different unit test cases (methods) which test the basic functionality of the
program including:
  - checking the correctness of some Entity fields
  - checking the processing of sample entity data and ensuring it's correctly
    divided between S's and B's (income and outgoing flags)
 - check that the settlement is adjusted correctly as for a working day
   if the currency is AED or SGP (requires two and one days to be added)   
 - test that both income and outgoing daily trade amount sum is calculated correctly
 - test both income and outgoing record entity order (higher settled trade amount first)


 ## How to improve this solution?

The current solution should function correctly and give a correct report, but to
decrease the likelihood of bugs larger sample data should be generated and 
more unit test cases added. This is a matter of time...
