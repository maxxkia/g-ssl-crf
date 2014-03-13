package main.java.Graph.GraphStructure;

import main.java.TextToNgram.NgramContainer;
import main.java.Utility.LocationToLabelFileHandler;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class LocationWithPOSTags extends Location {
    protected NgramContainer leftContextPOSTags, rightContextPOSTags;
    protected NgramContainer ngramPOSTags;

    protected void initialize(){
        super.initialize();
        this.leftContextPOSTags = new NgramContainer(2);
        this.rightContextPOSTags = new NgramContainer(2);
    }

    public LocationWithPOSTags(int seq, int pos, NgramContainer POSTags){
        super(seq, pos);
        this.leftContext = new NgramContainer(2);
        this.rightContext = new NgramContainer(2);
        this.ngramPOSTags = POSTags;
    }

    public LocationWithPOSTags(LocationWithPOSTags oldCopy){
        super(oldCopy);
        this.leftContext = oldCopy.getLeftContext();
        this.rightContext = oldCopy.getRightContext();
        this.ngramPOSTags = oldCopy.getNgramPOSTags();
    }

    public NgramContainer getLeftContextPOSTags(){
        return this.leftContextPOSTags;
    }

    public NgramContainer getRightContextPOSTags(){
        return this.rightContextPOSTags;
    }

    public NgramContainer getNgramPOSTags(){
        return this.ngramPOSTags;
    }

    public void setPreviousLocation(LocationWithPOSTags previousLocation,
                                    NgramContainer previousNgram, NgramContainer currentNgram,
                                    NgramContainer previousPOSTag, NgramContainer currentPOSTag){

        if (previousLocation != null){
            super.setPreviousLocation(previousLocation, previousNgram, currentNgram);

            previousLocation.getRightContextPOSTags().setMemberValue(0, currentPOSTag.getMemberValue(1));
            previousLocation.getRightContextPOSTags().setMemberValue(1, currentPOSTag.getMemberValue(2));

            this.getLeftContextPOSTags().setMemberValue(0, previousPOSTag.getMemberValue(0));
            this.getLeftContextPOSTags().setMemberValue(1, previousPOSTag.getMemberValue(1));
        }
    }
}
