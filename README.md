GOES-PRWEB-Automation
=====================

##Description

The GOES-PRWEB Automation is a Java program built to automate as a daily process the GOES-PRWEB algorithm, developed by Dr. Eric Harmsen at University of Puerto Rico. This algorithm uses data from NEXRAD radar and GOES satellite to make calculations of multiple hydroclimate parameters.

## Prerequisites

* Maven - Download and Install [Apache Maven](http://maven.apache.org/).
* Matlab
* PostgreSQL - Download and install [PostgreSQL] (http://www.postgresql.org/download/).

### Additional Packages

Additional dependencies are defined using Maven inside the [pom.xml](/pom.xml) file. 

## Quick Install

The easiest way to get started with the GOES-PRWEB Automation is to:

Clone & Run:
	
	git clone https://github.com/GreenDev-PR/GOES-PRWEB-Automation.git
	cd GOES-PRWEB-Automation
	maven package
	cd /target

This will generate a jar file with dependencies included. When executing the jar you need to specify the start date (-start), end date (-end) and path to properties file (-properties).

## Configuration

All system configuration is specified in an external file. A sample configuration is included in the [goesProperties.json](/src/main/java/com/greendev/pragma/main/properties/goesProperties.json) file.