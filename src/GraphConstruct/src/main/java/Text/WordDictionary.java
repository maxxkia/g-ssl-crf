package main.java.Text;

import main.java.Utility.Config;
import main.java.Utility.TextFileInput;

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class WordDictionary {
    private Hashtable<Integer, String> wordLookUpTable;
    private static final float tableLoadFactor = (float)0.8;
    private static final int tableInitialCapacity = 400;

    public WordDictionary(){
        this.wordLookUpTable =
                new Hashtable<Integer, String>(WordDictionary.tableInitialCapacity, WordDictionary.tableLoadFactor);

        //insert 0 as "null_word"
        this.addEntry(Config.packageOutputDummyValue, Config.packageOutputDummyValue);
    }

    public String getEntry(int index){
        return this.wordLookUpTable.get(index);
    }

    public String getEntry(String indexInStringFormat){
        return this.getEntry(Integer.parseInt(indexInStringFormat));
    }

    public void addEntry(int index, String value){
        this.wordLookUpTable.put(index, value);
    }

    public void addEntry(String index, String value){
        this.addEntry(Integer.parseInt(index), value);
    }

    public boolean containsValue(String value){
        return wordLookUpTable.containsValue(value);
    }

    public void buildDictionaryFromFile(String fileName){
        TextFileInput inputFile = new TextFileInput(fileName);
        String line;
        StringTokenizer tokenizer;
        String wordIndex,word;

        while ((line = inputFile.readLine()) != null){
            tokenizer = new StringTokenizer(line, " ");
            if (tokenizer.countTokens() < 2)
                continue;

            wordIndex = tokenizer.nextToken();
            word = tokenizer.nextToken();
            this.addEntry(wordIndex, word);
        }

        inputFile.close();
    }

    public boolean containsKey(String key) {
        return wordLookUpTable.containsKey(Integer.parseInt(key));
    }
}
