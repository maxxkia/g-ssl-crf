package main.java.Graph.GraphStructure;

import main.java.TextToNgram.NgramContainer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class NgramStatMapCell {
    private NgramContainer ngram;
    private int value;

    public NgramStatMapCell(NgramContainer ngram) {
        this.ngram = ngram;
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public NgramContainer getNgram() {
        return ngram;
    }

    public void increaseValue(){
        ++value;
    }
}
