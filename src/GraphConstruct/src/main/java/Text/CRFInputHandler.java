package main.java.Text;

import main.java.Utility.TextFileInput;
import main.java.Utility.TextFileOutput;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class CRFInputHandler {

    private static final int wordClassStartingIndex = 3;
    private static final int labelStartingIndex = 0;

    public void convertSimpleCRFInputToIndexedFormat(String fileAddress, String outputFileAddress,
                                                     String outputWordClassDictionaryFile,
                                                     String outputLabelDictionaryFile){
        TextFileInput fileInput = new TextFileInput(fileAddress);
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        Hashtable<Integer, String> indexToWordClassMap = new Hashtable<Integer, String>(300, (float)0.8);
        Hashtable<String, Integer> wordClassToIndexMap = new Hashtable<String, Integer>(300, (float)0.8);
        Hashtable<Integer, String> indexToLabelMap = new Hashtable<Integer, String>(300, (float)0.8);
        Hashtable<String, Integer> labelToIndexMap = new Hashtable<String, Integer>(300, (float)0.8);

        int wordClassIndex = wordClassStartingIndex, labelIndex = labelStartingIndex;

        String line;
        StringTokenizer tokenizer;
        String word, wordClass, label;

        while ((line = fileInput.readLine()) != null){
            tokenizer = new StringTokenizer(line, " ");
            if (tokenizer.countTokens() == 3){
                word = tokenizer.nextToken();
                wordClass = tokenizer.nextToken();
                if (!indexToWordClassMap.contains(wordClass)){
                    indexToWordClassMap.put(wordClassIndex, wordClass);
                    wordClassToIndexMap.put(wordClass, wordClassIndex);
                    ++wordClassIndex;
                }
                label = tokenizer.nextToken();

                if (!indexToLabelMap.contains(label)){
                    indexToLabelMap.put(labelIndex, label);
                    labelToIndexMap.put(label, labelIndex);
                    ++labelIndex;
                }
                fileOutput.writeLine(word + " " + wordClassToIndexMap.get(wordClass) + " " + labelToIndexMap.get(label));

            } else{
                fileOutput.writeLine(line);
            }
        }

        TextFileOutput wordClassDictionary = new TextFileOutput(outputWordClassDictionaryFile);
        for (int i=wordClassStartingIndex; i<wordClassIndex ; ++i){
            wordClassDictionary.writeLine(i + " " + indexToWordClassMap.get(i));
        }
        wordClassDictionary.close();

        TextFileOutput labelDictionary = new TextFileOutput(outputLabelDictionaryFile);
        for (int i=labelStartingIndex; i<labelIndex ; ++i){
            labelDictionary.writeLine(i + " " + indexToLabelMap.get(i));
        }
        labelDictionary.close();

        fileOutput.close();
        fileInput.close();
    }
}
