package main.java.Graph.GraphStructure;

import main.java.PMI.FeatureHandler;
import main.java.PMI.Struct.NodePairFeatureSetContainer;
import main.java.Text.WordDictionary;
import main.java.TextToNgram.NgramContainer;
import main.java.Utility.*;

import java.util.ArrayList;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public abstract class GraphContainerAbstract<LocationType extends Location> {
    public static final int defaultNgramSize = 3;
    /**
     * nodes of the graph
     */

    protected ArrayList<Node<LocationType>> nodeList;

    /**
     * total number of n-grams seen in input
     */
    protected int totalFrequency;

    /**
     * Use this array to store references to all graphs of the analyzed corpus.
     * Each member of this array having index=i represents the graph for (i+1)-grams
     */

    protected static final int nodeIdStartingIndex = 0;

    protected WordDictionary dictionaryOfClasses;
    protected WordDictionary dictionaryOfPrepositions;
    protected float edgeWeightThreshold;

    public GraphContainerAbstract(){
        initialize();
    }

    public GraphContainerAbstract(WordDictionary dictionaryOfClasses){
        this.initialize();
        this.dictionaryOfClasses = dictionaryOfClasses;
    }

    public GraphContainerAbstract(WordDictionary dictionaryOfClasses, WordDictionary dictionaryOfPrepositions){
        this.initialize();
        this.dictionaryOfClasses = dictionaryOfClasses;
        this.dictionaryOfPrepositions = dictionaryOfPrepositions;
    }

    protected void initialize() {
        initializeNodeList();
        totalFrequency = 0;
        this.edgeWeightThreshold = Config.edgeWeightThreshold;
        initializeNgramGraphsArray();

        storeSelfInGraphOfNgrams();
    }

    protected abstract void initializeNgramGraphsArray();

    protected abstract void initializeNodeList();

    protected abstract LocationType newLocationObject(int sequence, int position);

    protected abstract void storeSelfInGraphOfNgrams();
    /**
     * Finds a specified node using its ngram
     * @param iNode a given node
     * @return a non-negative integer represtenting the index of the node in the graph.
     * If the specified node does not exist in the graph -1 is returned.
     */
    public int indexOf(Node<LocationType> iNode){
        int result = -1;
        Node<LocationType> currentNode;

        for(int i=0; i<nodeList.size() ; ++i){
            currentNode = nodeList.get(i);
            if (currentNode.equals(iNode)){
                result = i;
                break;
            }
        }

        return result;
    }

    /**
     * Adds a specified node to current graph. If node already exists adds a new location for the specified node.
     * @param iNode the node to add to graph
     * @param location location of occurrence of node in text
     * @param labelProbabilityArray associated label probability for current occurrence of the node
     * @return true if node already not existed in the graph.
     */
    public int addNode(Node<LocationType> iNode, LocationType location, float[] labelProbabilityArray){
        int index = this.indexOf(iNode);
        int id;
        if(index < 0){
            id = nodeList.size() + GraphContainerAbstract.nodeIdStartingIndex;
            iNode.setNodeId(id);

            index = nodeList.size();
            nodeList.add(iNode);
        }else
            nodeList.get(index).increaseFrequency();

        nodeList.get(index).addLocation(location);
        nodeList.get(index).addLabelProbability(labelProbabilityArray);

        ++this.totalFrequency;
        return index;
    }

    /**
     * Adds a specified node to current graph. If node already exists adds a new location for the specified node.
     * @param iNode the node to add to graph
     * @param location location of occurrence of node in text
     * @return true if node already not existed in the graph.
     */
    public int addNode(Node<LocationType> iNode, LocationType location){
        return this.addNode(iNode, location, null);
    }

    /**
     * Adds a specified node to current graph. If node already exists adds a new location for the specified node.
     * @param iNode the node to add to graph
     * @return true if node already not existed in the graph.
     */
    public int addNode(Node<LocationType> iNode){
        return this.addNode(iNode, null, null);
    }

    /**
     * exports graph information to file as node and edge data
     * @param outputFileAddress address of the file to save graph information
     */
    public void exportGraphToFile(String outputFileAddress){
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        for (Node<LocationType> node : nodeList)
            fileOutput.write(node.serialize());

        fileOutput.close();
    }

    /**
     * Use this method to export graph data to file. Output format is as described below: </br>
     * [source-node word set] [destination-node word set] (Real number)edge-weight </br>
     * note: this method is only used for debug purposes
     * @param outputFileAddress address of the file to save graph information
     * @param dictionary a word dictionary which has wordId -> word mappings
     */
    public void exportGraphToFileAsWordSetsSimilarity(String outputFileAddress, WordDictionary dictionary){
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        for (Node<LocationType> node : nodeList)
            fileOutput.write(node.serializeAsWordSets(dictionary));

        fileOutput.close();
    }

    public void exportToFileAsIdMapping(String outputFileAddress){
        //todo: append a header section to the beginning of output file
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        String bufferData;
        for (Node<LocationType> node : nodeList) {
            bufferData = node.getNodeId() + Defaults.packageOutputDelimiter
                    + node.getNgram().serialize() + Defaults.packageOutputDelimiter
                    + node.getFrequency();
            fileOutput.writeLine(bufferData);
        }

        fileOutput.close();
    }

    public void exportToFileAsIdToLocationMapping(String outputFileAddress){
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        String bufferData;
        ArrayList<LocationType> locations;
        int currentNodeId;

        for (Node<LocationType> node : nodeList) {
            currentNodeId = node.getNodeId();
            locations = node.getLocationArrayList();
            for (Location location : locations) {
                bufferData = currentNodeId + Defaults.packageOutputDelimiter
                        + location.getSequence() + Defaults.packageOutputDelimiter
                        + location.getPosition();
                fileOutput.writeLine(bufferData);
            }
        }

        fileOutput.close();
    }

    public void exportToFileAsIdToTypeLevelProbabilities(String outputFileAddress){
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        for (Node<LocationType> node : nodeList) {
            fileOutput.write(node.serializeTypeLabelProbabilities());
        }

        fileOutput.close();
    }

    /**
     * gets the index of the graph which stores information on ngrams of a specified size
     * @param ngramSize size of ngrams
     * @return zero-based index of graph in ngramGraph
     */
    protected int getIndexOfGraph(int ngramSize){
        return ngramSize-1;
    }

    /**
     * convert graph to a KNN-graph. In a KNN-graph some edges are removed so that each node only has a maximum
     * of K edges heading out of it. This is done in a way that K most valuable edges are preserved (e.g. only
     * K edges having the highest weights are preserved).</br>
     * note: Be careful, using this method will modify original graph data. Another version which modifies a clone
     * of the original graph can be implemented.
     * @param kValue value of K to use for pruning
     */
    public void convertToKNN(int kValue){
        for (Node<LocationType> node:nodeList)
            node.convertEdgesToKNN(kValue);
    }

    public void populateEdgeValuesOld() {
        NodePairFeatureSetContainer featureScoreMap;
        Node<LocationType> node1,node2;
        RuntimeAnalyzer raTotal;
        raTotal = new RuntimeAnalyzer();
        raTotal.start("populateEdgeValues started");

        for (int i=0; i<nodeList.size() ; ++i){
            node1 = nodeList.get(i);
            featureScoreMap = new NodePairFeatureSetContainer();
            buildFeatureScoreMap(featureScoreMap, node1);

            for (int j=i+1; j<nodeList.size() ; ++j){
                node2 = nodeList.get(j);
                populateEdgeValue(featureScoreMap.makeCopy(), node1, node2);
            }
        }

        raTotal.finish("populateEdgeValues finished");
    }

    public void populateEdgeValuesOld(int seed, int step) {
        NodePairFeatureSetContainer featureScoreMap;
        Node<LocationType> node1,node2;

        for (int i=seed; i<nodeList.size() ; i+=step){
            node1 = nodeList.get(i);
            featureScoreMap = new NodePairFeatureSetContainer();
            buildFeatureScoreMap(featureScoreMap, node1);

            for (int j=i+1; j<nodeList.size() ; ++j){
                node2 = nodeList.get(j);

                populateEdgeValue(featureScoreMap.makeCopy(), node1, node2);
            }
        }
    }

    public void buildFeatureScoreMapForNodes(){
        for (Node<LocationType> node:nodeList){
            node.setFeatureSetContainer(this.buildFeatureScoreMap(node));
        }
    }

    public void buildFeatureScoreMapForNodes(int seed, int step){
        Node<LocationType> node;
        for (int i=seed; i<nodeList.size() ; i+=step){
            node = nodeList.get(i);
            node.setFeatureSetContainer(this.buildFeatureScoreMap(node));
        }
    }

    public void populateEdgeValues() {
        Node<LocationType> node1,node2;
        RuntimeAnalyzer raTotal;
        raTotal = new RuntimeAnalyzer();
        raTotal.start("populateEdgeValues started");

        for (int i=0; i<nodeList.size() ; ++i){
            node1 = nodeList.get(i);

            for (int j=i+1; j<nodeList.size() ; ++j){
                node2 = nodeList.get(j);
                this.populateEdgeValue(node1, node2);
            }
        }

        raTotal.finish("populateEdgeValues finished");
    }

    public void populateEdgeValues(int seed, int step) {
        Node<LocationType> node1,node2;

        for (int i=seed; i<nodeList.size() ; i+=step){
            node1 = nodeList.get(i);

            for (int j=i+1; j<nodeList.size() ; ++j){
                node2 = nodeList.get(j);
                populateEdgeValue(node1, node2);
            }
        }
    }

    protected NodePairFeatureSetContainer buildFeatureScoreMap(NodePairFeatureSetContainer featureScoreMap, Node<LocationType> node){
        int nodeIndex;
        NgramContainer[] featureArray, featureCombinedFormArray;
        double pmi;
        NgramContainer nodeContext;

        for (int i=0; i<node.getLocationArrayList().size() ; ++i){
            nodeContext = node.getContext(i);
            featureArray = FeatureHandler.extractFeaturesOfContext(nodeContext);
            featureCombinedFormArray = FeatureHandler.extractFeaturesInCombinedFormOfContext(nodeContext);
            for (int j=0; j<featureArray.length ; ++j){
                pmi = calculatePMIForPair(node.getNgram(), featureArray[j], featureCombinedFormArray[j]);

                nodeIndex = featureScoreMap.add(featureArray[j]);
                featureScoreMap.setScore(nodeIndex, 0, pmi);
            }
        }

        return featureScoreMap;
    }

    protected NodePairFeatureSetContainer buildFeatureScoreMap(Node<LocationType> node){
        int nodeIndex;
        NgramContainer[] featureArray, featureCombinedFormArray;
        double pmi;
        NgramContainer nodeContext;
        NodePairFeatureSetContainer featureScoreMap = new NodePairFeatureSetContainer();

        for (int i=0; i<node.getLocationArrayList().size() ; ++i){
            nodeContext = node.getContext(i);
            featureArray = FeatureHandler.extractFeaturesOfContext(nodeContext);
            featureCombinedFormArray = FeatureHandler.extractFeaturesInCombinedFormOfContext(nodeContext);
            for (int j=0; j<featureArray.length ; ++j){
                if (featureScoreMap.indexOf(featureArray[j]) < 0){
                    pmi = calculatePMIForPair(node.getNgram(), featureArray[j], featureCombinedFormArray[j]);

                    nodeIndex = featureScoreMap.add(featureArray[j]);
                    featureScoreMap.setScore(nodeIndex, 0, pmi);
                }
            }
        }

        return featureScoreMap;
    }

    protected void populateEdgeValue(Node<LocationType> node1, Node<LocationType> node2) {
        float similarity = (float)node1.getFeatureSetContainer()
                .makeCopy()
                .measureSimilarity(node2.getFeatureSetContainer());

        if (! (similarity < edgeWeightThreshold) ){
            node1.addEdge(node2, similarity);
            node2.addEdge(node1, similarity);
        }
    }

    protected void populateEdgeValue(NodePairFeatureSetContainer featureScoreMap, Node<LocationType> node1, Node<LocationType> node2) {
        int nodeIndex;
        NgramContainer[] featureArray, featureCombinedFormArray;
        NgramContainer nodeContext;
        float pmi;

        for (int i=0; i<node2.getLocationArrayList().size() ; ++i){
            nodeContext = node2.getContext(i);
            featureArray = FeatureHandler.extractFeaturesOfContext(nodeContext);
            featureCombinedFormArray = FeatureHandler.extractFeaturesInCombinedFormOfContext(nodeContext);
            for (int j=0; j<featureArray.length ; ++j){
                pmi = (float)calculatePMIForPair(node2.getNgram(), featureArray[j], featureCombinedFormArray[j]);

                nodeIndex = featureScoreMap.add(featureArray[j]);
                featureScoreMap.setScore(nodeIndex, 1, pmi);
            }
        }

        float similarity = (float)featureScoreMap.measureSimilarity();
        if (! (similarity < edgeWeightThreshold) ){
            node1.addEdge(node2, similarity);
            node2.addEdge(node1, similarity);
        }
    }

    protected void populateEdgeValueDeprecated(Node<LocationType> node1, Node<LocationType> node2) {
        NodePairFeatureSetContainer featureScoreMap = new NodePairFeatureSetContainer();
        int nodeIndex;
        NgramContainer[] featureArray, featureCombinedFormArray;
        float pmi;

        for (int i=0; i<node1.getLocationArrayList().size() ; ++i){
            featureArray = FeatureHandler.extractFeaturesOfContext(node1.getContext(i));
            featureCombinedFormArray = FeatureHandler.extractFeaturesInCombinedFormOfContext(node1.getContext(i));
            for (int j=0; j<featureArray.length ; ++j){
                pmi = (float)calculatePMIForPair(node1.getNgram(), featureArray[j], featureCombinedFormArray[j]);

                nodeIndex = featureScoreMap.add(featureArray[j]);
                featureScoreMap.setScore(nodeIndex, 0, pmi);
            }
        }

        for (int i=0; i<node2.getLocationArrayList().size() ; ++i){
            featureArray = FeatureHandler.extractFeaturesOfContext(node2.getContext(i));
            featureCombinedFormArray = FeatureHandler.extractFeaturesInCombinedFormOfContext(node2.getContext(i));
            for (int j=0; j<featureArray.length ; ++j){
                pmi = (float)calculatePMIForPair(node2.getNgram(), featureArray[j], featureCombinedFormArray[j]);

                nodeIndex = featureScoreMap.add(featureArray[j]);
                featureScoreMap.setScore(nodeIndex, 1, pmi);
            }
        }

        float similarity = (float)featureScoreMap.measureSimilarity();
        node1.addEdge(node2, similarity);
        node2.addEdge(node1, similarity);
    }

    protected double calculatePMIForPair(NgramContainer ngram1, NgramContainer ngram2, NgramContainer combinedForm){
        return FeatureHandler.computePMIForPair(this.totalFrequency, ngram1, ngram2,combinedForm, this);
    }

    public abstract int getCountOfNgram(NgramContainer ngram);

    protected int getCountOfNgramInSelf(NgramContainer ngram){
        int result = 0;

        if (FeatureHandler.isTemplate(ngram)){
            for (Node<LocationType> node : nodeList) {
                if (node.getNgram().equalsWithTemplate(ngram)) {
                    result += node.getFrequency();
                }
            }
        }else {
            for (Node<LocationType> node : nodeList) {
                if (node.getNgram().equals(ngram)) {
                    result = node.getFrequency();
                    break;
                }
            }
        }

        return result;
    }

    public abstract void removeRedundantData();

    public Node<LocationType> getNodeAt(int nodeIndex){
        if (nodeIndex < nodeList.size())
            return this.nodeList.get(nodeIndex);
        else
            throw new IllegalArgumentException("nodeIndex out of ArrayList bounds in GraphContainerAbstract.getNodeAt method");
    }

    public void updateNodesEmpiricalLabelProbabilities() {
        for (Node<LocationType> node:nodeList)
            node.updateLabelsEmpiricalProbabilities();
    }

    public void exportToFileAsEmpiricalProbabilities(String outputFileAddress) {
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        for (Node<LocationType> node: nodeList)
            fileOutput.write(node.serializeAsEmpiricalProbabilities());

        fileOutput.close();
    }

    public void getGraphAnalytics(String labeledNodesDictionaryFileAddress){
        WordDictionary labeledWordsDictionary = new WordDictionary();
        labeledWordsDictionary.buildDictionaryFromFile(labeledNodesDictionaryFileAddress);

        boolean[] isMemberOfLabeledData = DataTypeManipulator.newInitializedBooleanArray(nodeList.size());
        ArrayList<Edge<LocationType>> edges;
        Node<LocationType> currentNode;

        for (int index=0; index<nodeList.size() ;++index){
            currentNode = nodeList.get(index);
            if (!isMemberOfLabeledData[index] && currentNode.isMemberOfDictionary(labeledWordsDictionary))
                isMemberOfLabeledData[index] = true;

            edges = currentNode.getEdgeArrayList();
            if (isMemberOfLabeledData[index]) {
                for (Edge edge:edges)
                    isMemberOfLabeledData[edge.getDestination().getNodeId()] = true;
            } else {
                for (Edge edge:edges)
                    if (isMemberOfLabeledData[edge.getDestination().getNodeId()] ||
                            edge.getDestination().isMemberOfDictionary(labeledWordsDictionary)){
                        isMemberOfLabeledData[index] = true;
                        //this code can be injected for optimization isMemberOfLabeledData[edge.getDestination().getNodeId()] = true;
                        break;
                    }
            }
        }

        //output information
        int countOfNodesNotConnectedWithLabeledData = 0;
        for (boolean isLabeled:isMemberOfLabeledData)
            if (!isLabeled)
                ++countOfNodesNotConnectedWithLabeledData;
        float percentOfNotConnectedNodes = (float)countOfNodesNotConnectedWithLabeledData / (float)isMemberOfLabeledData.length;

        System.out.println(Defaults.packageExceptionPrefix + "[Info]: " + percentOfNotConnectedNodes
                + "% of nodes (" + countOfNodesNotConnectedWithLabeledData + " out of " + isMemberOfLabeledData.length
                + " nodes) are not connected to any labeled node.");
    }

    public void addNgramsToGraph(NgramContainer[] ngramSet, int sequence) {
        LocationType currentLocation, previousLocation = null;
        int position = 0; //position of the word in current sentence (sequence)
        NgramContainer previousNgram = null;

        for (int i=1; i<ngramSet.length-1 ; ++i) {
            //todo: this was changed
            //for (NgramContainer ngram : ngramSet) {
            currentLocation = this.newLocationObject(sequence, position);

            currentLocation.setPreviousLocation(previousLocation, previousNgram, ngramSet[i]);

            this.addNode(new Node<LocationType>(ngramSet[i]), currentLocation);//add node to graph or else update node frequency

            ++position;
            previousLocation = currentLocation;
            previousNgram = ngramSet[i];
        }
    }

    public void addNgramsToGraph(NgramContainer[] ngramSet, int sequence, int labelCount, LocationToLabelFileHandler fileInputLocationToLabelMapping){
        int position = 0;
        LocationType currentLocation, previousLocation = null;
        NgramContainer previousNgram = null;
        float[] labelProbabilitiesArray;
        Node<LocationType> tempNode;

        for (NgramContainer ngram : ngramSet) {
            currentLocation = this.newLocationObject(sequence, position);

            currentLocation.setPreviousLocation(previousLocation, previousNgram, ngram);
            labelProbabilitiesArray = fileInputLocationToLabelMapping.getLabelProbabilitiesOf(sequence, position, labelCount);
            tempNode = new Node<LocationType>(ngram, labelCount);

            this.addNode(tempNode, currentLocation, labelProbabilitiesArray);//add node to graph or else update node frequency

            ++position;
            previousLocation = currentLocation;
            previousNgram = ngram;
        }
    }

    public WordDictionary getDictionaryOfClasses() {
        return dictionaryOfClasses;
    }

    public WordDictionary getDictionaryOfPrepositions() {
        return dictionaryOfPrepositions;
    }

    public NgramStatMap getNgramStatMapForPOS(){
        return null;
    }

    public NgramPairStatMap getNgramPairStatMapForPOS(){
        return null;
    }
}
