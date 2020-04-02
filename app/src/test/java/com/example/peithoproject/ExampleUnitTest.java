package com.example.peithoproject;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testFindMinimum() {
        double timestamp = Peitho.findMinimum("minimumTest");

        double expectedTime = 1;

        assertEquals(expectedTime, timestamp);
    }
}