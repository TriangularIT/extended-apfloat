package org.apfloat;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ApcomplexTest {

    @Test
    public void testIs() throws Exception {
        Apfloat oneNinth = new Aprational(Apint.ONE, new Apint(9)).precision(30);
        Apfloat nineNinths = Apfloat.ZERO;

        for(int i = 0; i < 9; i++) {
            nineNinths = nineNinths.add(oneNinth);
        }

        assertFalse(nineNinths.equals(Apfloat.ONE));
        assertTrue(nineNinths.is(Apfloat.ONE));
    }

    @Test
    public void testIs2() throws Exception {
        Apfloat a = new Apfloat(".534");
        Apfloat b = new Apfloat(".5");

        assertTrue(a.is(b));
    }
}