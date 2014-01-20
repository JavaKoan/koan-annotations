package com.nps.koan;

import com.nps.koan.fixture.annotation.Enlighten;
import com.nps.koan.fixture.annotation.Koan;
import com.nps.koan.fixture.annotation.Vex;
import com.nps.koan.fixture.KoanRunner;
import org.junit.runner.RunWith;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by nicholas_smith on 02/01/14.
 */

@RunWith(KoanRunner.class)
public class KoanRunnerKoan {

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


    @Koan
    public void shouldIgnoreIfKoanDoesNotHaveStartAndEnd(){
        fail("This Koan should be ignored as it has no start (@_@) and end (^_^) markers");
    }

    @Koan @Vex @Enlighten
    public void shouldIgnoreWhenVexedAndEnlightened(){
        /* (@_@) */
        fail("This Koan should be ignored as it is both Vexed and Enlightened");
        /* (^_^) */
    }
}
