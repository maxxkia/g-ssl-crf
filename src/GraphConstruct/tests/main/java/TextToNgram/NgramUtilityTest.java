package main.java.TextToNgram;

import main.java.Utility.Config;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: masouD
 * Date: 1/30/14
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class NgramUtilityTest {
    NgramUtility ngramUtility;

    @Before
    public void setUp() throws Exception{
        ngramUtility = new NgramUtility();
    }

    @Test
    public void testExtractNgramsFromSentenceForUnigramsNullString() throws Exception {
        //for unigrams
        NgramContainer[] ngramSet = ngramUtility.extractNgramsFromSentence("", 1);
        assertEquals(ngramSet, null);
    }

    @Test
    public void testExtractNgramsFromSentenceForUnigrams1Words() throws Exception {
        //for unigrams
        NgramContainer[] ngramSet = ngramUtility.extractNgramsFromSentence("19", 1);
        assertEquals(ngramSet.length, 3);
        assertEquals(ngramSet[0].getMemberValue(0), Config.packageOutputDummyValue);
        assertEquals(ngramSet[2].getMemberValue(0), Config.packageOutputDummyValue);
        assertEquals(ngramSet[1].getMemberValue(0), "19");
    }

    @Test
    public void testExtractNgramsFromSentenceForUnigrams2Words() throws Exception {
        //for unigrams
        NgramContainer[] ngramSet = ngramUtility.extractNgramsFromSentence("6 19", 1);
        assertEquals(ngramSet.length, 4);
        assertEquals(ngramSet[0].getMemberValue(0), Config.packageOutputDummyValue);
        assertEquals(ngramSet[3].getMemberValue(0), Config.packageOutputDummyValue);
        assertEquals(ngramSet[1].getMemberValue(0), "6");
        assertEquals(ngramSet[2].getMemberValue(0), "19");
    }

    @Test
    public void testExtractNgramsFromSentenceForBigramsNullString() throws Exception {
        //for unigrams
        NgramContainer[] ngramSet = ngramUtility.extractNgramsFromSentence("", 2);
        assertEquals(ngramSet, null);
    }

    @Test
    public void testExtractNgramsFromSentenceForBigram1Words() throws Exception {
        //for bigrams
        NgramContainer[] ngramSet = ngramUtility.extractNgramsFromSentence("19", 2);
        assertEquals(ngramSet.length, 4);
        assertTrue(ngramSet[0].equals(new NgramContainer(new String[]{"0", "0"})));
        assertTrue(ngramSet[1].equals(new NgramContainer(new String[]{"0", "19"})));
        assertTrue(ngramSet[2].equals(new NgramContainer(new String[]{"19", "0"})));
        assertTrue(ngramSet[3].equals(new NgramContainer(new String[]{"0", "0"})));
    }

    @Test
    public void testExtractNgramsFromSentenceForBigram2Words() throws Exception {
        //for bigrams
        NgramContainer[] ngramSet = ngramUtility.extractNgramsFromSentence("6 19", 2);
        assertEquals(ngramSet.length, 5);
        assertTrue(ngramSet[0].equals(new NgramContainer(new String[]{"0", "0"})));
        assertTrue(ngramSet[1].equals(new NgramContainer(new String[]{"0", "6"})));
        assertTrue(ngramSet[2].equals(new NgramContainer(new String[]{"6", "19"})));
        assertTrue(ngramSet[3].equals(new NgramContainer(new String[]{"19", "0"})));
        assertTrue(ngramSet[4].equals(new NgramContainer(new String[]{"0", "0"})));
    }

    @Test
    public void testExtractNgramsFromSentenceForBigram3Words() throws Exception {
        //for bigrams
        NgramContainer[] ngramSet = ngramUtility.extractNgramsFromSentence("6 19 54", 2);
        assertEquals(ngramSet.length, 6);
        assertTrue(ngramSet[0].equals(new NgramContainer(new String[]{"0", "0"})));
        assertTrue(ngramSet[1].equals(new NgramContainer(new String[]{"0", "6"})));
        assertTrue(ngramSet[2].equals(new NgramContainer(new String[]{"6", "19"})));
        assertTrue(ngramSet[3].equals(new NgramContainer(new String[]{"19", "54"})));
        assertTrue(ngramSet[4].equals(new NgramContainer(new String[]{"54", "0"})));
        assertTrue(ngramSet[5].equals(new NgramContainer(new String[]{"0", "0"})));
    }
}
