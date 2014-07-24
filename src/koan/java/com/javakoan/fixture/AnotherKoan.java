package com.javakoan.fixture;

import com.javakoan.fixture.annotation.Enlighten;
import com.javakoan.fixture.annotation.Koan;
import com.javakoan.fixture.annotation.Vex;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by nicholas_smith on 24/07/2014.
 */

@RunWith(KoanRunner.class)
public class AnotherKoan {


    @Koan
    public void shouldVexWithNoProblemFile(){

        int i = 0;

        // (@_@)
        // (^_^)

        assertThat(i, is(5));
    }

}
