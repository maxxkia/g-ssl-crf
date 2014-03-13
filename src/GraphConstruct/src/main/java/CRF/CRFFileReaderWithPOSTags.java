package main.java.CRF;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class CRFFileReaderWithPOSTags extends CRFFileReader {
    protected String POSTagSentence, nextPOSTagSentence;

    public CRFFileReaderWithPOSTags(String fileAddress){
        super(fileAddress);
    }

    public void getNext(){
        this.POSTagSentence = this.nextPOSTagSentence;
        super.getNext();
    }

    protected void readOneSentence(String line){
        this.initializeState();

        StringTokenizer tokenizer;
        while (line != null && !isEmptyString(line)){
            tokenizer = new StringTokenizer(line, " ");
            this.nextWordSentence += tokenizer.nextToken() + " ";
            this.nextWordClassSentence += tokenizer.nextToken() + " ";
            this.nextLabels.add(Integer.parseInt(tokenizer.nextToken()));
            this.nextPOSTagSentence += tokenizer.nextToken() + " ";

            line = this.fileInput.readLine();
        }

        this.nextWordSentence = this.nextWordSentence.substring(0, this.nextWordSentence.length() - 1);
        this.nextWordClassSentence = this.nextWordClassSentence.substring(0, this.nextWordClassSentence.length() - 1);//remove the extra space (" ") added to the end of sentence
        this.nextPOSTagSentence = this.nextPOSTagSentence.substring(0, this.nextPOSTagSentence.length() - 1);
    }

    protected void initializeState() {
        super.initializeState();
        this.nextPOSTagSentence = "";
    }

    protected void setStateToNull(){
        super.setStateToNull();
        this.nextPOSTagSentence = null;
    }

    public String getPOSTagSentence(){
        return this.POSTagSentence;
    }
}
