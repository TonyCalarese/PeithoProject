package com.example.peithoproject;

import android.content.Context;

import androidx.core.util.Pair;

import com.example.peithoproject.recyclerassets.Speech;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testFindMinimum() {
        Speech minSpeech = new Speech();

        minSpeech.addData(1.0f);
        minSpeech.addData(0.1f);

        float expected = 0.1F;

        Pair<Long, Float> gottenMinimum = minSpeech.findMinimum();

        assertEquals(expected, gottenMinimum.second, 0.002);
    }

    @Test
    public void testFindMaximum() {
        Speech maxSpeech = new Speech();

        maxSpeech.addData(0.1f);
        maxSpeech.addData(1.0f);

        float expected = 1.0f;

        Pair<Long, Float> gottenMinimum = maxSpeech.findMaximum();

        assertEquals(gottenMinimum.second, expected, 0.002);
    }

    @Test
    public void testWrite() {
        Speech speechSaveTest = new Speech();
        Context context = null;

        speechSaveTest.addData(0.1f);
        speechSaveTest.addData(1.0f);

        boolean output = speechSaveTest.saveSpeech(context, "Test-Saving");

        assertEquals(output, true);
    }

}