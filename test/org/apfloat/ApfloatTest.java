package org.apfloat;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ApfloatTest {
    Apfloat nineNinths;
    Apfloat[] nums = new Apfloat[] {
            new Apfloat(".5"),
            new Apfloat(".25", Apcomplex.INFINITE),
            new Aprational(1, 3)
    };


    @BeforeTest
    private void setUp() {
        Apfloat oneNinth = new Aprational(1, 9).precision(30);
        nineNinths = Apfloat.ZERO;

        for(int i = 0; i < 9; i++) {
            nineNinths = nineNinths.add(oneNinth);
        }
    }

    @DataProvider
    private Object[][] addData() {
        Apfloat[][] expected = new Apfloat[][] {
                {
                        Apint.ONE,
                        new Apfloat(".7"),
                        new Apfloat(".8")
                },
                {
                        null,
                        new Aprational(1, 2),
                        new Aprational(7, 12)
                },
                {
                        null,
                        null,
                        new Aprational(2, 3)
                }
        };

        int numOfCases = nums.length * nums.length;
        int k = 0;
        Object[][] cases = new Object[numOfCases][3];

        for(int i = 0; i < nums.length; i++) {
            for(int j = 0; j <= i; j++) {
                cases[k][0] = nums[i];
                cases[k][1] = nums[j];
                cases[k][2] = expected[j][i];
                k++;

                if(i != j) {
                    cases[k][0] = nums[j];
                    cases[k][1] = nums[i];
                    cases[k][2] = expected[j][i];
                    k++;
                }
            }
        }

        return cases;
    }

    @Test(dataProvider = "addData")
    public void testAdd(Apfloat a, Apfloat b, Apfloat c) throws Exception {
        assertEquals(a.add(b), c);
    }

    @DataProvider
    private Object[][] subData() {
        Apfloat[][] expected = new Apfloat[][] {
                {
                        Apint.ZERO,
                        new Apfloat(".2"),
                        new Apfloat(".1")
                },
                {
                        null,
                        Apint.ZERO,
                        new Aprational(-1, 12)
                },
                {
                        null,
                        null,
                        Apint.ZERO
                }
        };

        int numOfCases = nums.length * nums.length;
        int k = 0;
        Object[][] cases = new Object[numOfCases][3];

        for(int i = 0; i < nums.length; i++) {
            for(int j = 0; j <= i; j++) {
                cases[k][0] = nums[i];
                cases[k][1] = nums[j];
                cases[k][2] = expected[j][i].negate();
                k++;

                if(i != j) {
                    cases[k][0] = nums[j];
                    cases[k][1] = nums[i];
                    cases[k][2] = expected[j][i];
                    k++;
                }
            }
        }

        return cases;
    }

    @Test(dataProvider = "subData")
    public void testSubtract(Apfloat a, Apfloat b, Apfloat c) throws Exception {
        assertEquals(a.subtract(b), c);
    }

    @DataProvider
    private Object[][] mulData() {
        Apfloat[][] expected = new Apfloat[][] {
                {
                        new Apfloat(".2"),
                        new Apfloat(".1"),
                        new Apfloat(".1")
                },
                {
                        null,
                        new Aprational(1, 16),
                        new Aprational(1, 12)
                },
                {
                        null,
                        null,
                        new Aprational(1, 9)
                }
        };

        int numOfCases = nums.length * nums.length;
        int k = 0;
        Object[][] cases = new Object[numOfCases][3];

        for(int i = 0; i < nums.length; i++) {
            for(int j = 0; j <= i; j++) {
                cases[k][0] = nums[i];
                cases[k][1] = nums[j];
                cases[k][2] = expected[j][i];
                k++;

                if(i != j) {
                    cases[k][0] = nums[j];
                    cases[k][1] = nums[i];
                    cases[k][2] = expected[j][i];
                    k++;
                }
            }
        }

        return cases;
    }

    @Test(dataProvider = "mulData")
    public void testMultiply(Apfloat a, Apfloat b, Apfloat c) throws Exception {
        assertEquals(a.multiply(b), c);
    }

    @DataProvider
    private Object[][] divData() {
        Apfloat[][] expected = new Apfloat[][] {
                {
                        Apint.ONE,
                        new Apfloat("2"),
                        new Apfloat("1")
                },
                {
                        null,
                        Apint.ONE,
                        new Aprational(3, 4)
                },
                {
                        new Apfloat(".6"),
                        null,
                        Apint.ONE
                }
        };

        int numOfCases = nums.length * nums.length;
        int k = 0;
        Object[][] cases = new Object[numOfCases][3];

        for(int i = 0; i < nums.length; i++) {
            for(int j = 0; j <= i; j++) {
                cases[k][0] = nums[i];
                cases[k][1] = nums[j];
                if(expected[i][j] == null)
                    cases[k][2] = expected[j][i].reciprocal();
                else
                    cases[k][2] = expected[i][j];
                k++;

                if(i != j) {
                    cases[k][0] = nums[j];
                    cases[k][1] = nums[i];
                    cases[k][2] = expected[j][i];
                    k++;
                }
            }
        }

        return cases;
    }

    @Test(dataProvider = "divData")
    public void testDivide(Apfloat a, Apfloat b, Apfloat c) throws Exception {
        assertEquals(a.divide(b), c);
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