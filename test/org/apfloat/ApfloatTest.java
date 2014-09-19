package org.apfloat;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ApfloatTest {
    Apfloat nineNinths;

    @BeforeTest
    private void setUp() {
        Apfloat oneNinth = new Aprational(Apint.ONE, new Apint(9)).precision(30);
        nineNinths = Apfloat.ZERO;

        for(int i = 0; i < 9; i++) {
            nineNinths = nineNinths.add(oneNinth);
        }
    }

    @Test
    public void testClean() throws Exception {
        assertEquals(nineNinths.clean(), Apfloat.ONE);
        assertEquals(nineNinths.multiply(new Apfloat("1e100", 30)).clean(), new Apfloat("1e100"));
        assertFalse(new Apfloat(".99").clean().equals(Apfloat.ONE));
        assertEquals(new Apfloat(".999").clean(), Apfloat.ONE);
    }

    @Test
    public void testIs() throws Exception {
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