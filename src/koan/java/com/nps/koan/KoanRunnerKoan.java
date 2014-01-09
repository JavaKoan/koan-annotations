package com.nps.koan;

import com.nps.koan.annotation.Enlighten;
import com.nps.koan.annotation.Koan;
import com.nps.koan.annotation.Vex;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by nicholas_smith on 02/01/14.
 */

@RunWith(KoanRunner.class)
public class KoanRunnerKoan {

    @Koan
    public void shouldRun(){
        /* (@_@) 1 */
        assertTrue(true);
        /* (^_^) 1 */
    }

    @Koan @Vex
    public void shouldRemoveSolution(){
        /* (@_@) 2 */
		/* (^_^) 2 */
    }

    @Koan @Enlighten
    public void shouldProvideSolution(){
        int i = 0;
        /* (@_@) 3 */
        i = 5;
		/* (^_^) 3 */
        assertEquals(5, i);
    }

    @Koan @Enlighten
    public void shouldProvideMultiLineSolution(){
        int i = 0;
        int j = 0;
        /* (@_@) 4 */
        j = 3;
        i = 3;
		/* (^_^) 4 */
        assertEquals(9, i*j);
    }

    @Koan
    public void shouldIgnoreIfKoanDoesNotHaveStartAndEnd(){

    }

    @Koan @Enlighten @Vex
    public void shouldIgnoreWhenVexedAndEnlightened(){
        /* (@_@) 5 */
        /* (^_^) 5 */
    }
}
