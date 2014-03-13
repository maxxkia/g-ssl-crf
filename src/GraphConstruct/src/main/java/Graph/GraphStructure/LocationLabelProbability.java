package main.java.Graph.GraphStructure;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class LocationLabelProbability {
    private int sequence;
    private int position;
    private int labelId;
    private float labelProbability;

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

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public float getLabelProbability() {
        return labelProbability;
    }

    public void setLabelProbability(float labelProbability) {
        this.labelProbability = labelProbability;
    }
}
