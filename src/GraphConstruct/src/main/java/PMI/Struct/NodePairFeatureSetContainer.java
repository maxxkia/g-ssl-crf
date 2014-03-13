package main.java.PMI.Struct;

import main.java.TextToNgram.NgramContainer;
import main.java.Utility.Config;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: masouD
 * Date: 12/31/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodePairFeatureSetContainer {
    private ArrayList<NodePairFeatureContainer> featureMap;

    public NodePairFeatureSetContainer(){
        featureMap = new ArrayList<NodePairFeatureContainer>();
    }

    public int add(NgramContainer ngram){
        int nodeIndex = this.indexOf(ngram);
        if (nodeIndex < 0){
            //node does not exist, create a new node
            nodeIndex = featureMap.size();
            featureMap.add(nodeIndex, new NodePairFeatureContainer(ngram));
        }

        return nodeIndex;
    }

    public int indexOf(NgramContainer ngram){
        int result = -1;
        for (int i=0; i<this.featureMap.size() ; ++i)
            if (this.featureMap.get(i).getNgram().equals(ngram)){
                result = i;
                break;
            }
        return result;
    }

    public boolean isSet(int nodeIndex, int memberIndex){
        return nodeIndex >= 0 && this.featureMap.get(nodeIndex).isSet(memberIndex);
    }

    public void setScore(int nodeIndex, int memberIndex, double memberScore){
        this.featureMap.get(nodeIndex).setNodeScore(memberIndex, memberScore);
    }

    public double measureSimilarity(){
        double dotSum = 0;
        double sumOfSquaresNode1 = 0, sumOfSquaresNode2 = 0;
        for (NodePairFeatureContainer feature : this.featureMap) {
            dotSum += feature.getNodeScore(0) * feature.getNodeScore(1);
            sumOfSquaresNode1 += feature.getNodeScore(0) * feature.getNodeScore(0);
            sumOfSquaresNode2 += feature.getNodeScore(1) * feature.getNodeScore(1);
        }
        sumOfSquaresNode1 = Math.sqrt(sumOfSquaresNode1);
        sumOfSquaresNode2 = Math.sqrt(sumOfSquaresNode2);

        if (Config.pmiSmoothing)
            dotSum += Config.pmiSmoothingEpsilon;
        double result = dotSum / (sumOfSquaresNode1 * sumOfSquaresNode2);
        if (Double.isNaN(result))
            result = 0;

        return result;
    }

    public NodePairFeatureSetContainer makeCopy() {
        NodePairFeatureSetContainer copy = new NodePairFeatureSetContainer();
        for (NodePairFeatureContainer aFeatureMap : featureMap) {
            copy.featureMap.add(aFeatureMap.makeCopy());
        }
        return copy;
    }

    public double measureSimilarity(NodePairFeatureSetContainer featureSetContainerNode2) {
        int nodeIndex;
        for (NodePairFeatureContainer featureContainer : featureSetContainerNode2.featureMap){
            nodeIndex = this.add(featureContainer.getNgram());
            this.setScore(nodeIndex, 1, featureContainer.getNodeScore(0));
        }

        return this.measureSimilarity();
    }
}
