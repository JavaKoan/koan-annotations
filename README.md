Koan Annotations for Java (^_^)
-------------------------------

This library offers a lightweight mechanism for creating Koan style learning exercises.

As an extension of JUnit the framework students can complete classes in their natural environment, the IDE.

Exercises are marked with the @Koan annotation and should have a learning objective documented in the methods Javadoc as well as a meaningful method name.

Koans are executed like unit tests within the in the IDE.  A Koan implementation without a solution or an incorrect solution should fail - a JUnit red bar.  After a student has meditated and provided the correct solution running the test should cause execution success - JUnit green bar.

Teachers should provide both a solution and problem files to the Koans they provide.  Students can leverage the solutions or problems by marking a method with @Enlighten or @Vex.  The framework will then amend the source code to either provide the solution or starting problem respectively.

Simple Example:


    @Koan
    public void makeTheProductOfIAndJ(){
        int i = 10;
        int j = 5;
        int product = 0;

        /* (@_@) Meditation starts here*/

        /* (^_^) */

        assertThat(product, is(50));
    }

For more information please see the [wiki](https://github.com/SmiddyPence/koan-annotations/wiki)

Example project implementation see my [java-koans](https://github.com/SmiddyPence/java-koans)

