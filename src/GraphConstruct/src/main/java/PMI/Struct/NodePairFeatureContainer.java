package main.java.PMI.Struct;

import main.java.TextToNgram.NgramContainer;

/**
 * Created with IntelliJ IDEA.
 * User: masouD
 * Date: 12/31/13
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodePairFeatureContainer {
    private NgramContainer ngram;
    private double[] nodeScores;
    private boolean[] valueSet;

    public NodePairFeatureContainer(NgramContainer ngram){
        this.ngram = ngram;

        nodeScores = new double[2];
        nodeScores[0] = 0;
        nodeScores[1] = 0;

        valueSet = new boolean[2];
        valueSet[0] = false;
        valueSet[1] = false;
    }

    public NgramContainer getNgram() {
        return ngram;
    }

    public void setNgram(NgramContainer ngram) {
        this.ngram = ngram;
    }

    public double getNodeScore(int index) {
        return nodeScores[index];
    }

    public void setNodeScore(int memberIndex, double memberScore) {
        this.nodeScores[memberIndex] = memberScore;
        this.valueSet[memberIndex] = true;
    }

    public boolean isSet(int index){
        return valueSet[index];
    }

    public NodePairFeatureContainer makeCopy() {
        NodePairFeatureContainer copy = new NodePairFeatureContainer(this.ngram);
        copy.nodeScores[0] = this.nodeScores[0];
        copy.nodeScores[1] = this.nodeScores[1];
        copy.valueSet[0] = this.valueSet[0];
        copy.valueSet[1] = this.valueSet[1];
        return copy;
    }
}
