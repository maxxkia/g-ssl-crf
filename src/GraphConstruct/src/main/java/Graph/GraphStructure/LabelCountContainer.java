package main.java.Graph.GraphStructure;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class LabelCountContainer {
    private int labelIndex;
    private int count;
    private float empiricalProbability;

    public LabelCountContainer(){
        labelIndex = 0;
        count = 0;
        empiricalProbability = 0;
    }

    public LabelCountContainer(int labelIndex) {
        this.labelIndex = labelIndex;
        count = 0;
        empiricalProbability = 0;
    }

    public int getLabelIndex() {
        return labelIndex;
    }

    public void setLabelIndex(int labelIndex) {
        this.labelIndex = labelIndex;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount() {
        ++count;
    }

    public void setEmpiricalProbability(int totalLabelCount){
        this.empiricalProbability = ((float) this.count) / ((float)totalLabelCount);
    }

    public float getEmpiricalProbability(){
        return this.empiricalProbability;
    }
}
