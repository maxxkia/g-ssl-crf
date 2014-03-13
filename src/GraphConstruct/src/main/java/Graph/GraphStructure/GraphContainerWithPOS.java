package main.java.Graph.GraphStructure;

import main.java.PMI.FeatureHandler;
import main.java.Text.WordDictionary;
import main.java.TextToNgram.NgramContainer;
import main.java.Utility.LocationToLabelFileHandler;

import java.util.ArrayList;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class GraphContainerWithPOS extends GraphContainerAbstract<LocationWithPOSTags>{
    protected NgramStatMap ngramStatMapForPOS;
    protected NgramPairStatMap ngramPairStatMapForPOS;

    protected GraphContainerWithPOS[] ngramGraph;

    public GraphContainerWithPOS(){
        super();
    }

    public GraphContainerWithPOS(WordDictionary dictionaryOfClasses){
        super(dictionaryOfClasses);
    }

    public GraphContainerWithPOS(WordDictionary dictionaryOfClasses, WordDictionary dictionaryOfPrepositions){
        super(dictionaryOfClasses, dictionaryOfPrepositions);
    }

    protected void initialize(){
        super.initialize();

        this.ngramStatMapForPOS = new NgramStatMap();
        this.ngramPairStatMapForPOS = new NgramPairStatMap();
    }

    @Override
    protected void initializeNgramGraphsArray() {
        this.ngramGraph = new GraphContainerWithPOS[5];
    }

    @Override
    protected void initializeNodeList() {
        this.nodeList = new ArrayList<Node<LocationWithPOSTags>>();
    }

    @Override
    protected LocationWithPOSTags newLocationObject(int sequence, int position) {
        return null;
    }

    @Override
    protected void storeSelfInGraphOfNgrams() {
        this.setGraphOfNgram(GraphContainerAbstract.defaultNgramSize, this); //set the tri-gram graph to self
    }


    /**
     * sets the reference to graph for a specified ngram size
     * @param ngramSize size of ngram
     * @param graph a given graph object
     */
    public void setGraphOfNgram(int ngramSize, GraphContainerWithPOS graph){
        this.ngramGraph[this.getIndexOfGraph(ngramSize)] = graph;
    }

    /**
     * gets the graph assigned to a given ngram size
     * @param ngramSize size of ngram
     * @return a GraphContainer object containing information on ngrams of a specified size
     */
    public GraphContainerWithPOS getGraphOfNgram(int ngramSize){
        return this.ngramGraph[this.getIndexOfGraph(ngramSize)];
    }

    public int getCountOfNgram(NgramContainer ngram){
        return this.getGraphOfNgram(ngram.getSize()).getCountOfNgramInSelf(ngram);
    }

    public void removeRedundantData() {
        for (int i=0; i<ngramGraph.length ; ++i)
            this.ngramGraph[i] = null;
    }


    public void addNgramsToGraph(NgramContainer[] ngramSet, NgramContainer[] POSSet, int sequence) {
        LocationWithPOSTags currentLocation, previousLocation = null;
        int position = 0; //position of the word in current sentence (sequence)
        NgramContainer previousNgram = null;

        for (int i=1; i<ngramSet.length-1 ; ++i) {
            //todo: this was changed
            //for (NgramContainer ngram : ngramSet) {
            currentLocation = new LocationWithPOSTags(sequence, position, POSSet[i]);

            currentLocation.setPreviousLocation(previousLocation, previousNgram, ngramSet[i]);

            this.addNode(new Node<LocationWithPOSTags>(ngramSet[i]), currentLocation);//add node to graph or else update node frequency

            ++position;
            previousLocation = currentLocation;
            previousNgram = ngramSet[i];
        }
    }

    public void addNgramsToGraph(NgramContainer[] ngramSet, NgramContainer[] POSSet, int sequence, int labelCount, LocationToLabelFileHandler fileInputLocationToLabelMapping){
        int position = 0;
        LocationWithPOSTags currentLocation, previousLocation = null;
        NgramContainer previousNgram = null;
        float[] labelProbabilitiesArray;
        Node<LocationWithPOSTags> tempNode;

        for (int i=1; i<ngramSet.length-1 ; ++i) {
            currentLocation = new LocationWithPOSTags(sequence, position, POSSet[i]);

            currentLocation.setPreviousLocation(previousLocation, previousNgram, ngramSet[i]);
            labelProbabilitiesArray = fileInputLocationToLabelMapping.getLabelProbabilitiesOf(sequence, position, labelCount);
            tempNode = new Node<LocationWithPOSTags>(ngramSet[i], labelCount);

            this.addNode(tempNode, currentLocation, labelProbabilitiesArray);//add node to graph or else update node frequency

            ++position;
            previousLocation = currentLocation;
            previousNgram = ngramSet[i];
        }
    }


    public NgramStatMap getNgramStatMapForPOS(){
        return this.ngramStatMapForPOS;
    }

    public NgramPairStatMap getNgramPairStatMapForPOS(){
        return this.ngramPairStatMapForPOS;
    }

    public void computeFeatureStats(){
        for(Node<LocationWithPOSTags> node:nodeList)
            this.computeFeatureStats(node);
    }

    //todo: call this method
    protected void computeFeatureStats(Node<LocationWithPOSTags> node){
        NgramContainer[] featureArray;
        NgramContainer nodeContext;

        for (int i=0; i<node.getLocationArrayList().size() ; ++i){
            nodeContext = node.getContext(i);
            featureArray = FeatureHandler.extractFeaturesOfContext(nodeContext);
            for (NgramContainer aFeature : featureArray) {
                if (FeatureHandler.isPOSFeature(aFeature)) {
                    ngramStatMapForPOS.add(FeatureHandler.getMainPartOfNonSimpleFeature(aFeature));
                    ngramPairStatMapForPOS.add(node.getNgram(), FeatureHandler.getMainPartOfNonSimpleFeature(aFeature));
                }
            }
        }
    }
}
