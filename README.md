#Koan Annotations for Java (^_^)

This library offers a lightweight mechanism for creating Koan style learning exercises.

As an extension of JUnit the framework students can complete classes in their natural environment, the IDE.

Exercises are marked with the @Koan annotation and should have a learning objective documented in the methods Javadoc as well as a meaningful method name.

Koans are executed like unit tests within the in the IDE.  A Koan implementation without a solution or an incorrect solution should fail - a JUnit red bar.  After a student has meditated and provided the correct solution running the test should cause execution success - JUnit green bar.

Teachers should provide both a solution and problem file to the Koans they provide.  Students can leverage the solutions or problems by marking a method with @Enlighten or @Vex.  The framework will then amend the source code to either provide the solution or starting problem respectively.

Simple Example:
---------------
```Java
@RunWith(KoanRunner.class)
public class MyKoans {  

    /**
    * Meditate on making the product of two numbers.
    * Hint: You will need to use an arithmetic operator
    */
    @Koan
    public void makeTheProductOfIAndJ(){
        int i = 10;
        int j = 5;
        int product = 0;

        /* (@_@) Meditation starts here */
        
        /* (^_^) Enlightenment reached here */

        assertThat(product, is(50));
    }
}
```

More Information
----------------
Please see the [wiki](https://github.com/JavaKoan/koan-annotations/wiki)

Dependency
----------

```xml
<dependency>
    <groupId>com.javakoan</groupId>
    <artifactId>koan-annotations</artifactId>
    <version>1.0.3</version>
</dependency>
```

Example Uses
------------
[github.com/JavaKoan/java-koans](https://github.com/JavaKoan/java-koans)



Status
------
[![build status](https://github.com/JavaKoan/koan-annotations/actions/workflows/maven-package.yml/badge.svg?branch=master)](https://github.com/JavaKoan/koan-annotations/actions)

