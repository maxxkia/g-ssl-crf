package main.java.Graph.GraphStructure;

import main.java.TextToNgram.NgramContainer;

import java.util.Hashtable;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class NgramPairStatMap{
    protected Hashtable<String, Integer> mapData;

    public NgramPairStatMap(){
        mapData = new Hashtable<String, Integer>(20);
    }

    public void add(NgramContainer ngram1, NgramContainer ngram2){
        String key = this.getStringFormOf(ngram1, ngram2);
        if (mapData.containsKey(key)){
            Integer value = mapData.get(key);
            ++value;
        }else {
            Integer value = 1;
            mapData.put(key, value);
        }
    }

    protected String getStringFormOf(NgramContainer ngram1, NgramContainer ngram2) {
        return ngram1.serialize() + "#" + ngram2.serialize();
    }

    public int getValueOf(NgramContainer ngram1, NgramContainer ngram2) {
        int result = 0;
        String key = this.getStringFormOf(ngram1, ngram2);
        if (mapData.containsKey(key))
            result = mapData.get(key);
        return result;
    }
}
