package main.java.TextToNgram;

import main.java.PMI.FeatureHandler;
import main.java.Text.WordDictionary;
import main.java.Utility.Config;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class NgramContainer{
    private String[] members;

    public NgramContainer(String[] membersCopy){
        members = membersCopy.clone();
    }

    /**
     * create a new NgramContainer object of a given size and initialize its members
     * @param ngramSize size of the required ngram, e.g. ngramSize=3 produces a tri-gram container
     */
    public NgramContainer(int ngramSize){
        members = new String[ngramSize];
        //initialize members array
        for (int i=0; i<members.length; ++i)
            members[i] = Config.packageOutputDummyValue;
    }

    /**
     * Use this method to get size of ngram e.g. count of words that this ngram can hold
     * @return size of ngram
     */
    public int getSize(){
        return members.length;
    }

    public void setMemberValue(int index, String value){
        members[index] = value;
    }

    public String getMemberValue(int index){
        if(index>= members.length || index<0)
            throw new IllegalArgumentException(Utils.packageExceptionPrefix
                    + "class:NgramContainer, method:getMemberValue, index:" + index);
        return members[index];
    }

    public String getCenterValue(){
        return this.members[getIndexOfCenterMember()];
    }

    private int getIndexOfCenterMember() {
        return members.length / 2;
    }

    public boolean equals(NgramContainer matchingContainer){
        boolean match = true;
        if(this.members.length != matchingContainer.members.length){
            match = false;
        }else{
            for(int i=0; i<members.length ; ++i)
                if(!this.members[i].equalsIgnoreCase(matchingContainer.members[i])){
                    match = false;
                    break;
                }
        }

        return match;
    }

    public boolean equalsWithTemplate(NgramContainer ngramTemplate) {
        boolean match = true;
        if(this.members.length != ngramTemplate.members.length){
            match = false;
        }else{
            for(int i=0; i<members.length ; ++i)
                if(!ngramTemplate.members[i].equals(FeatureHandler.nullTokenIdentifier)
                        && !this.members[i].equalsIgnoreCase(ngramTemplate.members[i])){
                    match = false;
                    break;
                }
        }

        return match;
    }

    public boolean hasMember(String word){
        boolean result = false;
        for (int i=0 ; i<members.length ; ++i)
            if (members[i].equalsIgnoreCase(word)){
                result = true;
                break;
            }
        return result;
    }

    public String serialize(){
        String result = "";
        if (members.length != 0){
            result = members[0];
            for(int i=1; i<members.length ; ++i)
                result += "," + members[i];
        }
        return result;
    }

    public boolean isBeginningOfLine(){
        return members[0].equalsIgnoreCase(Config.packageOutputDummyValue);
    }

    public String getWordSet(WordDictionary dictionary){
        String result = "( ";

        for (String member : this.members)
            result += dictionary.getEntry(member) + " ";
        result += ")";

        return result;
    }

    public boolean isMemberOfDictionary(WordDictionary wordDictionary) {
        boolean result = true;

        for (String word:members)
            if (! wordDictionary.containsKey(word)){
                result = false;
                break;
            }

        return result;
    }

    public NgramContainer getRightPart(){
        NgramContainer result = new NgramContainer(this.getSize()/2 + 1);
        for (int index=result.getSize()-1, j=this.getSize()-1; index>=0 ; --index,--j)
            result.setMemberValue(index, this.getMemberValue(j));
        return result;
    }

    public NgramContainer getLeftPart(){
        NgramContainer result = new NgramContainer(this.getSize()/2 + 1);
        for (int index=0; index < result.getSize() ; ++index)
            result.setMemberValue(index, this.getMemberValue(index));
        return result;
    }

    public NgramContainer getSubNgram(int startIndex){
        int size = this.getSize() - startIndex;
        NgramContainer result = new NgramContainer(size);
        for (int i=startIndex, j=0; i<this.getSize() ; ++i, ++j)
            result.setMemberValue(j, this.members[i]);
        return result;
    }
}
