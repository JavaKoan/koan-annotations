/**
 * Copyright 2014 Nicholas Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nps.koan;

import com.nps.koan.fixture.annotation.Enlighten;
import com.nps.koan.fixture.annotation.Koan;
import com.nps.koan.fixture.annotation.Vex;
import com.nps.koan.fixture.KoanRunner;
import org.junit.runner.RunWith;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(KoanRunner.class)
public class KoanRunnerKoan {

    /**
     * In this exercise students will learn how to use the multiplication operator in Java
     */
    @Koan
    public void makeTheProductOfIAndJ(){
        int i = 10;
        int j = 5;
        int product = 0;

        /* (@_@) */
        product = i * j;
        /* (^_^) */

        assertThat(product, is(50));
    }

    /**
     * In this exercise students will learn how to use @Enlighten annotation from the koan-annotation framework
     */
    @Koan @Enlighten
    public void provideTheSolutionToTheProductOfIAndJ(){
        int i = 10;
        int j = 5;
        int product = 0;

        /* (@_@) */
        product = i * j;
        /* (^_^) */

        assertThat(product, is(50));
    }

    /**
     * In this exercise students will learn how to use @Vex annotation from the koan-annotation framework
     */
    @Koan @Vex
    public void provideTheStartingProblemForCalculatingAFibonacciNumberUsingALoop(){

        int seventhFibonacciNumber = 0;

        /* (@_@) */
        int previous1 = 1;
        int previous2 = 1;
        int currentFibonacci = 1;

        for(int i= 3; i<= 7; i++){


        }

        seventhFibonacciNumber = currentFibonacci;
        /* (^_^) */

        assertThat(seventhFibonacciNumber, is(13));
    }

    /**
     * In this exercise students will learn that Koans that do not have start and end markers will be ignored by the framework
     */
    @Koan
    public void shouldIgnoreIfKoanDoesNotHaveStartAndEnd(){
        fail("This Koan should be ignored as it has no start (@_@) and end (^_^) markers");
    }

    /**
     * In this exercise students will learn that Koans that are have both @Vex and @Enlighten will be ignored by the framework
     */
    @Koan @Vex @Enlighten
    public void shouldIgnoreWhenVexedAndEnlightened(){
        /* (@_@) */
        fail("This Koan should be ignored as it is both Vexed and Enlightened");
        /* (^_^) */
    }
}
