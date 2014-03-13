package main.java.TextToNgram;

import main.java.PMI.FeatureHandler;
import main.java.Text.WordDictionary;
import main.java.Utility.Config;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: masouD
 * Date: 1/22/14
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class NgramContainerTest {
    private NgramContainer ngram;

    @Before
    public void setUp() throws Exception {
        ngram = new NgramContainer(new String[] {"first", "second", "third"});
    }

    @Test
    public void testGetSize() throws Exception {
        assertEquals(ngram.getSize(), 3);
    }

    @Test
    public void testSetMemberValue() throws Exception {
        ngram.setMemberValue(0, "bar");
        assertEquals(ngram.getMemberValue(0), "bar");
    }

    @Test
    public void testGetMemberValue() throws Exception {
        try{
            ngram.getMemberValue(-1);
            assertFalse(true);
        } catch (Exception x){

        }

        try{
            ngram.getMemberValue(100);
            assertFalse(true);
        } catch (Exception x){

        }
    }

    @Test
    public void testGetCenterIndex() throws Exception {

    }

    @Test
    public void testGetCenterValue() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {
        //todo:write more tests here
        NgramContainer secondNgram = new NgramContainer(new String[] {"first", "second", "third"});
        assertTrue(ngram.equals(secondNgram));

        secondNgram.setMemberValue(0, "bar");
        assertFalse(ngram.equals(secondNgram));

        secondNgram = new NgramContainer(new String[] {"first", "second"});
        assertFalse(ngram.equals(secondNgram));
    }

    @Test
    public void testEqualsWithUnequalLengths() throws Exception {
        NgramContainer secondNgram = new NgramContainer(new String[] {"first", "second"});
        assertFalse(ngram.equals(secondNgram));
    }

    @Test
    public void testEqualsWithTemplate() throws Exception {
        NgramContainer secondNgram = new NgramContainer(new String[] {"first",
                FeatureHandler.nullTokenIdentifier, FeatureHandler.nullTokenIdentifier});
        assertTrue(ngram.equalsWithTemplate(secondNgram));

        secondNgram.setMemberValue(2, "first");
        assertFalse(ngram.equalsWithTemplate(secondNgram));

        secondNgram.setMemberValue(2, "third");
        assertTrue(ngram.equalsWithTemplate(secondNgram));
    }

    @Test
    public void testEqualsWithTemplateWithUnequalLengths() throws Exception {
        NgramContainer secondNgram = new NgramContainer(new String[] {"first", "second"});
        assertFalse(ngram.equalsWithTemplate(secondNgram));
    }

    @Test
    public void testHasMember() throws Exception {
        assertTrue(ngram.hasMember("second"));
        assertTrue(ngram.hasMember("ThIRd"));
        assertFalse(ngram.hasMember("malmal"));
        assertFalse(ngram.hasMember(""));
    }

    @Test
    public void testSerialize() throws Exception {
        assertEquals(ngram.serialize(), "first,second,third");
    }

    @Test
    public void testIsBeginningOfLine() throws Exception {
        assertFalse(ngram.isBeginningOfLine());
        ngram.setMemberValue(0, Config.packageOutputDummyValue);
        assertTrue(ngram.isBeginningOfLine());
    }

    @Test
    public void testGetWordSet() throws Exception {
        NgramContainer ngram = new NgramContainer(new String[] {"1", "2", "4"});
        WordDictionary dictionary = getSampleWordDictionary();

        assertEquals(ngram.getWordSet(dictionary), "( first second fourth )");
    }

    private WordDictionary getSampleWordDictionary() {
        WordDictionary dictionary = new WordDictionary();
        dictionary.addEntry("1", "first");
        dictionary.addEntry("2", "second");
        dictionary.addEntry("4", "fourth");
        return dictionary;
    }

    @Test
    public void testIsMemberOfDictionary() throws Exception {
        NgramContainer ngram = new NgramContainer(new String[] {"1", "2", "4"});
        WordDictionary dictionary = getSampleWordDictionary();

        assertTrue(ngram.isMemberOfDictionary(dictionary));

        ngram.setMemberValue(1, "54");
        assertFalse(ngram.isMemberOfDictionary(dictionary));
    }

    @Test
    public void testGetRightPart() throws Exception {
        NgramContainer actualRightPart = new NgramContainer(new String[] {"second", "third"});
        assertTrue(ngram.getRightPart().equals(actualRightPart));
    }

    @Test
    public void testGetLeftPart() throws Exception {
        NgramContainer actualLeftPart = new NgramContainer(new String[] {"first", "second"});
        assertTrue(ngram.getLeftPart().equals(actualLeftPart));
    }
}
