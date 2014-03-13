package main.java.TextToNgram;

import main.java.Utility.Config;
import main.java.Utility.TextFileInput;
import main.java.Utility.TextFileOutput;

import java.util.StringTokenizer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class NgramUtility {
    private int ngramSize = Utils.packageDefaultNgramSize;

    public void CreateNgramFileFromTextFile(String inputFileAddress, String outputFileAddress, int ingramSize){
        String line;
        NgramContainer[] ngramSet;
        TextFileInput fileInput = new TextFileInput(inputFileAddress);
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        ngramSize = ingramSize;

        while ((line = fileInput.readLine()) != null) {
            //ignore first line
            //todo: if input file is corrected next line of code should be removed
            fileInput.readLine(); //ignore one line because of duplicate sentence
            line = fileInput.readLine();
            ngramSet = extractNgramsFromSentenceDefaultWithEscapeCharacters(line, ngramSize);

            for(int i=0; i<ngramSet.length ; i++){
                for(int j=0; j<ngramSize ; j++){
                    fileOutput.write(ngramSet[i].getMemberValue(j));
                    fileOutput.write(Utils.packageOutputDelimiter);
                }
                fileOutput.write(Utils.packageOutputNewLineCharacter);
            }
        }

        fileOutput.close();
        fileInput.close();
    }

    /**
     * given sentence (as in a line of input file) this method creates the n-grams associated with the given sentence
     * and returns them as an array of type NgramContainer </br>
     * note: this method currently can only be used for uni-grams and tri-grams. This implementation considers first and
     * last words of a sentence as Begin_Of_Sentence and _End_Of_Sentence words.
     * @param line the sentence to process
     * @param sizeOfNgram size of each n-gram, e.g. sizeOfNgram=3 means 3-grams are intended
     * @return an array containing all the n-grams existing in the given sentence
     */
    public NgramContainer[] extractNgramsFromSentenceDefaultWithEscapeCharacters(String line, int sizeOfNgram){
/*
        if (sizeOfNgram != 1 && sizeOfNgram != 3){
            throw new IllegalArgumentException(Utils.packageExceptionPrefix
                    + "[invalid use of method: extractNgramsFromSentenceDefaultWithEscapeCharacters] " +
                    "value of ngramSize can only be 1 or 3");
        }
*/
        int offset = (sizeOfNgram / 2) + ((sizeOfNgram+1) % 2);
        if (offset<0)
            offset = 0;

        StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
        int wordCount = stringTokenizer.countTokens();
        //wordCount-2 means first and last characters of a sentence are BOS and EOS
        wordCount = wordCount - 2;
        NgramContainer wholeSentenceContainer = new NgramContainer(wordCount + (offset*2));
        int i,j;
        for(i=0; i<offset ; ++i)
            wholeSentenceContainer.setMemberValue(i, Config.packageOutputDummyValue);
        stringTokenizer.nextToken();//ignore first word as BOS character
        for(j=0; j<wordCount ; ++j)
            wholeSentenceContainer.setMemberValue(i + j, stringTokenizer.nextToken());
        j = i+j;
        for(i=0; i<offset ; ++i)
            wholeSentenceContainer.setMemberValue(i + j, Config.packageOutputDummyValue);

        int resultSetSize;
        if (sizeOfNgram % 2 == 1)
            resultSetSize = wordCount;
        else
            resultSetSize = wholeSentenceContainer.getSize() - sizeOfNgram + 1;

        NgramContainer[] resultSet = new NgramContainer[resultSetSize];
        for(i=0; i<resultSetSize ; ++i){
            resultSet[i] = new NgramContainer(sizeOfNgram);
            for(j=0; j< sizeOfNgram; ++j)
                resultSet[i].setMemberValue(j, wholeSentenceContainer.getMemberValue(i + j));
        }

        return resultSet;
    }

    /**
     * given sentence (as in a line of input file) this method creates the n-grams associated with the given sentence
     * and returns them as an array of type NgramContainer </br>
     * @param line the sentence to process
     * @param sizeOfNgram size of each n-gram, e.g. sizeOfNgram=3 means 3-grams are intended
     * @return an array containing all the n-grams existing in the given sentence
     */
    public NgramContainer[] extractNgramsFromSentence(String line, int sizeOfNgram){
        if (line.trim().equals(""))
            return null;
        //todo: created this new method to handle standard CRF input
        int offset;

        if (sizeOfNgram == 1)
            offset = 1;
        else {
            offset = (sizeOfNgram / 2) + ((sizeOfNgram+1) % 2);
            if (offset<0)
                offset = 0;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
        int wordCount = stringTokenizer.countTokens();

        NgramContainer wholeSentenceContainer = new NgramContainer(wordCount + (offset*2));
        int i,j;
        for(i=0; i<offset ; ++i)
            wholeSentenceContainer.setMemberValue(i, Config.packageOutputDummyValue);
        for(j=0; j<wordCount ; ++j)
            wholeSentenceContainer.setMemberValue(i + j, stringTokenizer.nextToken());
        j = i+j;
        for(i=0; i<offset ; ++i)
            wholeSentenceContainer.setMemberValue(i + j, Config.packageOutputDummyValue);

        int resultSetSize;
        if (sizeOfNgram == 1){
            resultSetSize = wordCount + (offset*2);
        } else{
            if (sizeOfNgram % 2 == 1)
                resultSetSize = wordCount;
            else
                resultSetSize = wholeSentenceContainer.getSize() - sizeOfNgram + 1;
        }

        NgramContainer[] resultSet = new NgramContainer[resultSetSize];
        for(i=0; i<resultSetSize ; ++i){
            resultSet[i] = new NgramContainer(sizeOfNgram);
            for(j=0; j< sizeOfNgram; ++j)
                resultSet[i].setMemberValue(j, wholeSentenceContainer.getMemberValue(i + j));
        }

        return resultSet;
    }

    /**
     * given sentence (as in a line of input file) this method creates the n-grams associated with the given sentence
     * and returns them as an array of type NgramContainer </br>
     * note: this method currently can only be used for uni-grams and tri-grams
     * @param line the sentence to process
     * @param sizeOfNgram size of each n-gram, e.g. sizeOfNgram=3 means 3-grams are intended
     * @return an array containing all the n-grams existing in the given sentence
     */
    private NgramContainer[] extractNgramsFromSentenceDefaultOld(String line, int sizeOfNgram){
        if (sizeOfNgram != 1 && sizeOfNgram != 3){
            throw new IllegalArgumentException(Utils.packageExceptionPrefix
                    + "[invalid use of method: extractNgramsFromSentenceDefaultWithEscapeCharacters] " +
                    "value of ngramSize can only be 1 or 3");
        }
        int offset = (sizeOfNgram - 2);
        if (offset<0)
            offset = 0;

        StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
        int wordCount = stringTokenizer.countTokens();
        NgramContainer wholeSentenceContainer = new NgramContainer(wordCount + (offset*2));
        int i,j;
        for(i=0; i<offset ; ++i)
            wholeSentenceContainer.setMemberValue(i, Config.packageOutputDummyValue);
        for(j=0; j<wordCount ; ++j)
            wholeSentenceContainer.setMemberValue(i + j, stringTokenizer.nextToken());
        j = i+j;
        for(i=0; i<offset ; ++i)
            wholeSentenceContainer.setMemberValue(i + j, Config.packageOutputDummyValue);

        NgramContainer[] resultSet = new NgramContainer[wordCount];
        for(i=0; i<wordCount ; i++){
            resultSet[i] = new NgramContainer(sizeOfNgram);
            for(j=0; j< sizeOfNgram; j++)
                resultSet[i].setMemberValue(j, wholeSentenceContainer.getMemberValue(i + j));
        }

        return resultSet;
    }

    public NgramContainer sentenceToNgram(String ngramSentence){
        StringTokenizer stringTokenizer = new StringTokenizer(ngramSentence, " ");
        int wordCount = stringTokenizer.countTokens();
        NgramContainer result = new NgramContainer(wordCount);

        if(wordCount==0){
            result = null;
        }else{
            for(int j=0; j<wordCount ; ++j)
                result.setMemberValue(j, stringTokenizer.nextToken());
        }
        return result;
    }
}
