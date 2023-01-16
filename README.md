# iptiQ Test

### Introduction
In this project a load balancer component is implemented, this is not a real implementation, it is a basic implementation of ***random*** and ***round-robin*** algorithm.

### Approach
For reducing complexity of data structure and keeping the providers, a custom ***circular queue*** is implemented with basic methods
(***peek***, ***add***, ***addAll***,...) which let us to store, and read the providers.
The approach for round-robin algorithm is FIFO (First in First out) and when a provider is picked up, then it goes to the end of the circular queue.

### Assumption
It is supposed, to register a provider which is already is existed in the queue, it will be replaced.

### Technologies
- Java 17
- Maven (dependency management)
- JUnit 5 (Test)

### How to test
I have written some useful test classes under the “ipitQ/src/test/java” folder.
```
      mvn test
```
Or open the project in Intellij idea or your IDE and go to test/java folder, and right click on Run All Tests.

### To Do
This is not a runnable project or production ready, so I ignored logging, monitoring, documenting, and also it is possible to implement new algorithms for the load balancer.
Some properties like queue size, and maximum capacity limit, ect can be configured. Remove, register, and deregister of providers are not implemented.