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
        /** (@_@) */
        assertTrue(true);
        /** (^_^) */
    }

    @Koan @Vex
    public void shouldRemoveSolution(){
        /** (@_@) */
		/** (^_^) */
    }

    @Koan @Enlighten
    public void shouldProvideSolution(){
        int i = 0;
        /** (@_@) */
		i = 5;
		/** (^_^) */
        assertEquals(5, i);
    }

    @Koan
    public void shouldIgnoreIfKoanDoesNotHaveStartAndEnd(){

    }

    @Koan @Enlighten @Vex
    public void shouldIgnoreWhenVexedAndEnlightened(){
        /** (@_@) */
        /** (^_^) */
    }
}
