package main.java.Graph.GraphStructure;

import main.java.TextToNgram.NgramContainer;
import main.java.TextToNgram.Utils;
import main.java.Utility.Config;
import main.java.Utility.DataTypeManipulator;

import java.util.StringTokenizer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class Location {
    protected NgramContainer leftContext, rightContext;
    protected int sequence;
    protected int position;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSeqAndPos(int seq, int pos){
        setSequence(seq);
        setPosition(pos);
    }

    protected void initialize(){
        this.leftContext = new NgramContainer(2);
        this.rightContext = new NgramContainer(2);
        this.sequence = 0;
        this.position = 0;
    }

    public Location(int seq, int pos){
        this.initialize();
        this.sequence = seq;
        this.position = pos;
    }

    public Location(Location oldCopy){
        this.initialize();
        this.sequence = oldCopy.sequence;
        this.position = oldCopy.position;
    }

    public static LocationLabelProbability extractLocationFromString(String lineOfData){
        LocationLabelProbability loc;
        StringTokenizer stringTokenizer = new StringTokenizer(lineOfData, " \t");
        int countTokens = stringTokenizer.countTokens();
        if(countTokens == 4){
            loc  = new LocationLabelProbability();

            int seq = Integer.parseInt(stringTokenizer.nextToken());
            int pos = Integer.parseInt(stringTokenizer.nextToken());
            int labelId = Integer.parseInt(stringTokenizer.nextToken());
            float probability = Float.parseFloat(stringTokenizer.nextToken());

            loc.setSequence(seq);
            loc.setPosition(pos);
            loc.setLabelId(labelId);
            loc.setLabelProbability(probability);
        }else {
            loc = null;
        }
        return loc;
    }

    public void setPreviousLocation(Location previousLocation,
                                    NgramContainer previousNgram, NgramContainer currentNgram){
        if (previousLocation != null){
            previousLocation.getRightContext().setMemberValue(0, currentNgram.getMemberValue(1));
            previousLocation.getRightContext().setMemberValue(1, currentNgram.getMemberValue(2));

            this.getLeftContext().setMemberValue(0, previousNgram.getMemberValue(0));
            this.getLeftContext().setMemberValue(1, previousNgram.getMemberValue(1));
        }
    }

    public NgramContainer getLeftContext() {
        return leftContext;
    }

    public void setLeftContext(NgramContainer leftContext) {
        this.leftContext = leftContext;
    }

    public NgramContainer getRightContext() {
        return rightContext;
    }

    public void setRightContext(NgramContainer rightContext) {
        this.rightContext = rightContext;
    }

    /**
     * for debugging purposes
     * @return
     */
    public String serializeLeftAndRightContext(){
        String result = "";
        if (this.getLeftContext() != null)
            result += "leftContext(" + this.getLeftContext().serialize() + ")";
        if (this.getRightContext() != null)
            result += Config.outputDelimiter + "rightContext(" + this.getRightContext().serialize() + ")";
        return result;
    }
}
