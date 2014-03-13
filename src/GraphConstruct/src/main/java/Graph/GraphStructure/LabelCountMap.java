package main.java.Graph.GraphStructure;

import main.java.Utility.Config;

import java.util.ArrayList;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class LabelCountMap {
    private ArrayList<LabelCountContainer> labels;
    private int totalLabelOccurrence;

    public LabelCountMap(){
        this.labels = new ArrayList<LabelCountContainer>();
        this.totalLabelOccurrence = 0;
    }

    public int indexOf(int labelIndex){
        int result = -1;

        for (int i=0; i<this.labels.size() ;++i){
            if (this.labels.get(i).getLabelIndex() == labelIndex){
                result = i;
                break;
            }
        }

        return result;
    }

    public void increaseFrequency(int labelIndex){
        int index = indexOf(labelIndex);
        if (index < 0){
            index = insertLabel(labelIndex);
        }

        this.labels.get(index).incrementCount();
        ++this.totalLabelOccurrence;
    }

    public int insertLabel(int labelIndex) {
        int index = 0;
        for ( ; index<this.labels.size() ; ++index){
            if (this.labels.get(index).getLabelIndex() > labelIndex)
                break;
        }
        LabelCountContainer labelCountContainer = new LabelCountContainer(labelIndex);
        this.labels.add(index, labelCountContainer);

        return index;
    }

    public void updateEmpiricalProbabilities(){
        for (LabelCountContainer labelInfo:labels)
            labelInfo.setEmpiricalProbability(this.totalLabelOccurrence);
    }

    public String serializeEmpiricalProbabilities(String nodeId){
        String result = "";
        for (LabelCountContainer labelInfo:this.labels)
            result += nodeId + "\t" +
                    labelInfo.getLabelIndex() + "\t" +
                    labelInfo.getEmpiricalProbability() +
                    Config.outputNewLineCharacter;
        return result;
    }
}
