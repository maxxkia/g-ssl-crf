package main.java.CRF;

import main.java.Utility.TextFileInput;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class CRFFileReader {
    protected TextFileInput fileInput;
    protected String wordClassSentence, nextWordClassSentence,
            wordSentence, nextWordSentence;
    protected ArrayList<Integer> labels, nextLabels;
    protected boolean hasMoreSentences;

    public CRFFileReader(String fileAddress){
        this.hasMoreSentences = true;
        fileInput = new TextFileInput(fileAddress);

        this.readNext();
    }

    public boolean hasNext(){
        return this.hasMoreSentences;
    }

    public void getNext(){
        this.wordClassSentence = this.nextWordClassSentence;
        this.wordSentence = this.nextWordSentence;
        this.labels = this.nextLabels;

        this.readNext();
    }

    protected void readNext(){
        if (!this.hasMoreSentences)
            return;

        String line = this.fileInput.readLine();
        while (line != null && isEmptyString(line)){
            line = fileInput.readLine();
        }
        if (line == null){
            this.hasMoreSentences = false;

            this.setStateToNull();
        }else {
            this.hasMoreSentences = true;

            this.readOneSentence(line);
        }
    }
    
    protected void readOneSentence(String line){
        initializeState();

        StringTokenizer tokenizer;
        while (line != null && !isEmptyString(line)){
            tokenizer = new StringTokenizer(line, " ");
            this.nextWordSentence += tokenizer.nextToken() + " ";
            this.nextWordClassSentence += tokenizer.nextToken() + " ";
            this.nextLabels.add(Integer.parseInt(tokenizer.nextToken()));

            line = this.fileInput.readLine();
        }

        this.nextWordSentence = this.nextWordSentence.substring(0, this.nextWordSentence.length() - 1);
        this.nextWordClassSentence = this.nextWordClassSentence.substring(0, this.nextWordClassSentence.length() - 1);//remove the extra space (" ") added to the end of sentence
    }

    protected void initializeState() {
        this.nextWordSentence = "";
        this.nextWordClassSentence = "";
        this.nextLabels = new ArrayList<Integer>();
    }

    protected void setStateToNull(){
        this.nextWordClassSentence = null;
        this.nextWordSentence = null;
        this.nextLabels = null;
    }

    protected boolean isEmptyString(String line) {
        return line.trim().equals("");
    }

    public void close(){
        fileInput.close();
    }

    public String getWordClassSentence() {
        return this.wordClassSentence;
    }

    public String getWordSentence() {
        return this.wordSentence;
    }

    public ArrayList<Integer> getLabels(){
        return this.labels;
    }
}
