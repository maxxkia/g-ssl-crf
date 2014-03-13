package main.java.Graph.GraphStructure;

import main.java.PMI.Struct.NodePairFeatureSetContainer;
import main.java.Text.WordDictionary;
import main.java.TextToNgram.NgramContainer;
import main.java.Utility.Config;
import main.java.Utility.DataTypeManipulator;
import main.java.Utility.Defaults;

import java.util.ArrayList;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class Node<LocationType extends Location> {
    protected int nodeId;
    protected NgramContainer ngram;
    protected ArrayList<LocationType> locationArrayList;
    protected ArrayList<Edge<LocationType>> edgeArrayList;
    protected NodePairFeatureSetContainer featureSetContainer;
    protected LabelCountMap labelCountMap;

    /**
     * number of occurrences of the associated ngram in analyzed text
     */
    protected int frequency = 0;

    /**
     * member at index i of this array stores the sum of probability of label[i] in all
     * occurrences of the ngram associated with this node.
     */
    protected float[] totalLabelProbability;

    public int getNodeId(){
        return nodeId;
    }

    public void setNodeId(int id){
        this.nodeId = id;
    }

    public NgramContainer getNgram(){
        return ngram;
    }

    public void addLocation(LocationType location){
        if (location!=null)
            locationArrayList.add(location);
    }

    public ArrayList<LocationType> getLocationArrayList(){
        return locationArrayList;
    }

    public int getFrequency(){
        return frequency;
    }

    public Node(NgramContainer value){
        Constructor(0, value, 0);
    }

    public Node(NgramContainer value, int labelCount){
        Constructor(0, value, labelCount);
    }

    public Node(int nodeId ,NgramContainer value, int labelCount) {
        Constructor(nodeId, value, labelCount);
    }

    protected void Constructor(int nodeId ,NgramContainer value, int labelCount){
        this.edgeArrayList = new ArrayList<Edge<LocationType>>();
        this.locationArrayList = new ArrayList<LocationType>();
        this.nodeId = nodeId;
        this.ngram = value;
        this.totalLabelProbability = DataTypeManipulator.newInitializedFloatArray(labelCount);
        this.increaseFrequency();
        this.labelCountMap = new LabelCountMap();
    }

    /**
     * use this method to calculate Q(y) for each ngram, y stands for label
     * @return an array of float typeLabel probabilities of current node
     */
    public float[] getTypeLabelProbabilities(){
        float[] Q = new float[totalLabelProbability.length];
        for (int i=0; i<Q.length ; ++i)
            Q[i] = totalLabelProbability[i]/frequency;
        return Q;
    }

    public void increaseFrequency(){
        ++frequency;
    }

    public void addLabelProbability(float[] labelProbabilityArray){
        if (labelProbabilityArray == null)
            return;

        if (totalLabelProbability.length != labelProbabilityArray.length)
            throw new IllegalArgumentException(Defaults.packageExceptionPrefix
                    + "[invalid use of method: addLabelProbability] " +
                    "size of input array does not match with totalLabelProbability array");

        for (int i=0; i<totalLabelProbability.length ; ++i){
            totalLabelProbability[i] += labelProbabilityArray[i];
        }
    }

    public boolean equals(Node<LocationType> matchingNode){
        return this.ngram.equals(matchingNode.ngram);
    }

    /**
     * get context of this trigram occurring in the index location
     * @param index index of the trigram occurrence
     * @return context of the given trigram
     */
    public NgramContainer getContext(int index){
        throwExceptionForInvalidLocationArrayListIndex(index);

        NgramContainer context = new NgramContainer(5);
        context.setMemberValue(0, this.locationArrayList.get(index).getLeftContext().getMemberValue(0));
        context.setMemberValue(1, this.ngram.getMemberValue(0));
        context.setMemberValue(2, this.ngram.getMemberValue(1));
        context.setMemberValue(3, this.ngram.getMemberValue(2));
        context.setMemberValue(4, this.locationArrayList.get(index).getRightContext().getMemberValue(1));
        return context;
    }

    protected void throwExceptionForInvalidLocationArrayListIndex(int index) {
        if (index < 0 || index >= locationArrayList.size())
            throw new IllegalArgumentException(Defaults.packageExceptionPrefix
                    + "[invalid use of method: Node.getContext] "
                    + "index must be a non-negative integer less than the size of location array list. index="
                    + index + ", size=" + locationArrayList.size());
    }

    public ArrayList<Edge<LocationType>> getEdgeArrayList(){
        return this.edgeArrayList;
    }

    public void addEdge(Node<LocationType> destination ,float weight){
        this.addEdgeWithSort(destination, weight);
    }

    protected void addEdgeWithSort(Node<LocationType> destination, float weight){
        int index;
        synchronized (this.edgeArrayList){
            for (index=0; index<this.edgeArrayList.size() ; ++index){
                if (weight > this.edgeArrayList.get(index).getWeight())
                    break;
            }

            this.edgeArrayList.add(index, new Edge<LocationType>(weight,destination));
        }
    }

    public void convertEdgesToKNN(int kValue){
        for (int index=this.edgeArrayList.size() - 1; index>=kValue ; --index)
            this.edgeArrayList.remove(index);
        //also edgeArrayList.subList(fromIndex, toIndex) method can be used
    }

    public String serialize(){
        String result = "";

        for (Edge<LocationType> edge : edgeArrayList)
            result += this.ngram.serialize()
                    + "\t" + edge.getDestination().getNgram().serialize()
                    + "\t" + edge.getWeight()
                    + Config.outputNewLineCharacter;
        return result;
    }

    /**
     * note: this method is only used for debugging purposes
     * @param dictionary a WordDictionary object used to map each wordId to its string representation
     * @return [source node in serialized form] [destination node in serialized form] Real(weight of edge connecting these nodes)
     */
    public String serializeAsWordSets(WordDictionary dictionary){
        String result = "";
        String myWordSet = this.ngram.getWordSet(dictionary);

        for (Edge<LocationType> edge : edgeArrayList)
            result += myWordSet
                    + "\t" + edge.getDestination().getNgram().getWordSet(dictionary)
                    + "\t" + edge.getWeight()
                    + Config.outputNewLineCharacter;
        return result;
    }

    public String serializeTypeLabelProbabilities(){
        float[] typeProbabilitiesArray = this.getTypeLabelProbabilities();
        String result = "";

        for(int index=0; index<typeProbabilitiesArray.length ; ++index)
            result += this.ngram.serialize()
                    + "\t" + index
                    + "\t" + typeProbabilitiesArray[index]
                    + Config.outputNewLineCharacter;
        return result;
    }

    public NodePairFeatureSetContainer getFeatureSetContainer() {
        return featureSetContainer;
    }

    public void setFeatureSetContainer(NodePairFeatureSetContainer featureSetContainer) {
        this.featureSetContainer = featureSetContainer;
    }

    public void incrementLabelCount(int labelIndex){
        this.labelCountMap.increaseFrequency(labelIndex);
    }

    public void updateLabelsEmpiricalProbabilities(){
        this.labelCountMap.updateEmpiricalProbabilities();
    }

    public String serializeAsEmpiricalProbabilities() {
        return this.labelCountMap.serializeEmpiricalProbabilities(this.ngram.serialize());
    }

    public boolean isMemberOfDictionary(WordDictionary wordDictionary) {
        return this.getNgram().isMemberOfDictionary(wordDictionary);
    }
}
