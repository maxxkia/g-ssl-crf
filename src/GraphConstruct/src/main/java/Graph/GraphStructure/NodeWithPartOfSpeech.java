package main.java.Graph.GraphStructure;

import main.java.TextToNgram.NgramContainer;
import main.java.Utility.Defaults;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class NodeWithPartOfSpeech extends Node<LocationWithPOSTags> {

    public NodeWithPartOfSpeech(NgramContainer value){
        super(0, value, 0);
    }

    public NodeWithPartOfSpeech(NgramContainer value, int labelCount){
        super(0, value, labelCount);
    }

    public NodeWithPartOfSpeech(int nodeId ,NgramContainer value, int labelCount) {
        super(nodeId, value, labelCount);
    }

    public void addLocation(LocationWithPOSTags location){
        if (location!=null)
            locationArrayList.add(location);
    }

    public NgramContainer getContextPOSTags(int index){
        this.throwExceptionForInvalidLocationArrayListIndex(index);

        NgramContainer context = new NgramContainer(5);
        LocationWithPOSTags currentLocation = this.locationArrayList.get(index);

        context.setMemberValue(0, currentLocation.getLeftContextPOSTags().getMemberValue(0));
        context.setMemberValue(1, currentLocation.getNgramPOSTags().getMemberValue(0));
        context.setMemberValue(2, currentLocation.getNgramPOSTags().getMemberValue(1));
        context.setMemberValue(3, currentLocation.getNgramPOSTags().getMemberValue(2));
        context.setMemberValue(4, currentLocation.getRightContextPOSTags().getMemberValue(1));
        return context;
    }
}
