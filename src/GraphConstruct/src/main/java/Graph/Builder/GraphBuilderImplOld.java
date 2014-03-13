package main.java.Graph.Builder;

import main.java.Graph.GraphStructure.GraphContainer;
import main.java.Graph.GraphStructure.Node;
import main.java.Graph.GraphStructure.Location;
import main.java.Graph.Concurrency.GraphThreadHandler;
import main.java.Text.WordDictionary;
import main.java.TextToNgram.NgramContainer;
import main.java.TextToNgram.NgramUtility;
import main.java.Utility.*;

/**
 * Use this class to create a weighted graph from a file containing n-grams of a text and export the resulting graph
 * in a desired way.
 */
public class GraphBuilderImplOld {
    private static final int knnDefaultSize = 5;
    private Logger logHandler;

    public GraphBuilderImplOld(Logger logger){
        this.logHandler = logger;
    }

    public GraphContainer createGraphFromNgramFile(String ngramFileAddress){
        String line;
        NgramContainer ngram;
        NgramUtility ngramUtil = new NgramUtility();
        GraphContainer graph = new GraphContainer();
        Node tempNode;
        int nodeId = 1;

        int seq, pos;
        seq = -1;
        pos = 0;
        Location currentLocation;

        TextFileInput fileInput = new TextFileInput(ngramFileAddress);

        while ((line = fileInput.readLine()) != null) {
            ngram = ngramUtil.sentenceToNgram(line);
            if(ngram == null)
                continue;//invalid line, ignore

            currentLocation = new Location(seq,pos);
            if(ngram.isBeginningOfLine()){
                ++seq;
                pos = 0;
                currentLocation.setSeqAndPos(seq,pos);
            }

            tempNode = new Node(nodeId, ngram, 0);

            graph.addNode(tempNode,currentLocation, null);//add node to graph or else update node frequency


            ++pos;
        }

        fileInput.close();

        return graph;
    }

    public GraphContainer createGraphFromNgramFile(String ngramFileAddress, String labelsFileAddress,
                                                   String wordLocationLabelProbabilityFileAddress){
        String line;
        NgramContainer ngram, previousNgram = null;
        NgramUtility ngramUtil = new NgramUtility();
        GraphContainer graph = new GraphContainer();
        Node tempNode;
        int nodeId = 1;
        int labelCount = LabelFileHandler.countLabels(labelsFileAddress);
        float[] labelProbabilitiesArray;

        int sequence, position;
        sequence = -1;
        position = 0;
        Location currentLocation, previousLocation = null;

        TextFileInput fileInput = new TextFileInput(ngramFileAddress);
        LocationToLabelFileHandler fileInputLocationToLabelMapping =
                new LocationToLabelFileHandler(wordLocationLabelProbabilityFileAddress);

        while ((line = fileInput.readLine()) != null) {
            ngram = ngramUtil.sentenceToNgram(line);
            if(ngram == null)
                continue;//invalid line, ignore

            currentLocation = new Location(sequence,position);
            if(ngram.isBeginningOfLine()){
                ++sequence;
                position = 0;
                currentLocation.setSeqAndPos(sequence,position);
                previousLocation = null;
            }

            currentLocation.setPreviousLocation(previousLocation, previousNgram, ngram);
            labelProbabilitiesArray = fileInputLocationToLabelMapping.getLabelProbabilitiesOf(sequence, position, labelCount);
            tempNode = new Node(nodeId, ngram, labelCount);

            graph.addNode(tempNode, currentLocation, labelProbabilitiesArray);//add node to graph or else update node frequency


            ++position;
            previousLocation = currentLocation;
            previousNgram = ngram;
        }

        fileInput.close();

        return graph;
    }

    public GraphContainer createGraphFromFileBase(String corpusFileAddress,
                                                  String labelsFileAddress,
                                                  String wordLocationLabelProbabilityFileAddress){
        String line;
        NgramContainer[] ngramSet;
        TextFileInput fileInput = new TextFileInput(corpusFileAddress);
        int labelCount = LabelFileHandler.countLabels(labelsFileAddress);
        LocationToLabelFileHandler fileInputLocationToLabelMapping =
                new LocationToLabelFileHandler(wordLocationLabelProbabilityFileAddress);
        NgramUtility ngramUtility = new NgramUtility();

        int ngramSize = 3;
        Node tempNode;
        int nodeId = 1;

        int sequence = -1, position = 0;
        Location currentLocation, previousLocation = null;
        NgramContainer previousNgram = null;
        float[] labelProbabilitiesArray;

        GraphContainer graph = new GraphContainer();

        while ((line = fileInput.readLine()) != null) {
            //ignore first line
            //todo: if input file is corrected next line of code should be removed
            fileInput.readLine(); //ignore one line because of duplicate sentence
            line = fileInput.readLine();
            ngramSet = ngramUtility.extractNgramsFromSentenceDefaultWithEscapeCharacters(line, ngramSize);

            for(int i=0; i<ngramSet.length ; ++i){
                currentLocation = new Location(sequence,position);
                if(ngramSet[i].isBeginningOfLine()){
                    ++sequence;
                    position = 0;
                    currentLocation.setSeqAndPos(sequence,position);
                    previousLocation = null;
                }

                currentLocation.setPreviousLocation(previousLocation, previousNgram, ngramSet[i]);
                labelProbabilitiesArray = fileInputLocationToLabelMapping.getLabelProbabilitiesOf(sequence, position, labelCount);
                tempNode = new Node(nodeId, ngramSet[i], labelCount);

                graph.addNode(tempNode, currentLocation, labelProbabilitiesArray);//add node to graph or else update node frequency


                ++position;
                previousLocation = currentLocation;
                previousNgram = ngramSet[i];
            }
        }

        return graph;
    }


    public GraphContainer createGraphFromFileBase(String corpusFileAddress, int ngramSize){
        String line;
        NgramContainer[] ngramSet;
        TextFileInput fileInput = new TextFileInput(corpusFileAddress);
        NgramUtility ngramUtility = new NgramUtility();

        Node tempNode;
        int nodeId = 1;

        GraphContainer graph = new GraphContainer();

        while ((line = fileInput.readLine()) != null) {
            //ignore first line
            //todo: if input file is corrected next line of code should be removed
            fileInput.readLine(); //ignore one line because of duplicate sentence
            line = fileInput.readLine();
            ngramSet = ngramUtility.extractNgramsFromSentenceDefaultWithEscapeCharacters(line, ngramSize);

            for(int i=0; i<ngramSet.length ; ++i){
                tempNode = new Node(ngramSet[i]);

                graph.addNode(tempNode);//add node to graph or else update node frequency

            }
        }

        return graph;
    }

    /**
     * Use this method to create the graph of tri-grams for a given corpus
     * @param corpusFileAddress address of input text.
     * @param labelsFileAddress address of labels file
     * @param wordLocationLabelProbabilityFileAddress address of the file containing location to
     *                                                label probability mappings.
     *                                                each line of this file is formatted as below: </br>
     *                                                #sequence #position #labelIndex (Real number)probability
     * @return a graph of tri-grams of the given corpus
     */
    public GraphContainer createGraphFromFileMultiThread(String corpusFileAddress,
                                                         String labelsFileAddress,
                                                         String wordLocationLabelProbabilityFileAddress){
        RuntimeAnalyzer ra;
        ra = logHandler.taskStarted("[GraphBuilderImplOld]- creating nodes of ngram graphs");

        GraphContainer baseGraph = this.createGraphFromFileBase(corpusFileAddress,labelsFileAddress,wordLocationLabelProbabilityFileAddress);
        GraphContainer unigramGraph = this.createGraphFromFileBase(corpusFileAddress, 1);
        GraphContainer bigramGraph = this.createGraphFromFileBase(corpusFileAddress, 2);
        GraphContainer fourgramGraph = this.createGraphFromFileBase(corpusFileAddress, 4);
        GraphContainer fivegramGraph = this.createGraphFromFileBase(corpusFileAddress, 5);

        logHandler.taskFinished(ra, "[GraphBuilderImplOld]- creating nodes of ngram graphs");
        ra = logHandler.taskStarted("[GraphBuilderImplOld]- calculating pmi values and assigning edge weights for nodes");

        baseGraph.setGraphOfNgram(1 ,unigramGraph);
        baseGraph.setGraphOfNgram(2, bigramGraph);
        baseGraph.setGraphOfNgram(4, fourgramGraph);
        baseGraph.setGraphOfNgram(5, fivegramGraph);

        unigramGraph = null;
        bigramGraph = null;
        fourgramGraph = null;
        fivegramGraph = null;

        try{
            int threadCount = 8;
            //build feature score map in first run
            GraphThreadHandler[] threads = new GraphThreadHandler[threadCount];
            for (int i=0; i<threadCount ; ++i){
                threads[i] = new GraphThreadHandler(i, threadCount, baseGraph, true);
                threads[i].start();
            }

            for (int i=0; i<threadCount ; ++i){
                threads[i].join();
            }

            //measure similarity values of nodes and assign edge values
            for (int i=0; i<threadCount ; ++i){
                threads[i] = new GraphThreadHandler(i, threadCount, baseGraph);
                threads[i].start();
            }

            for (int i=0; i<threadCount ; ++i){
                threads[i].join();
            }

        }catch (InterruptedException ex){
            ex.printStackTrace();
        }

        logHandler.taskFinished(ra, "[GraphBuilderImplOld]- assigning edge values");
        ra = logHandler.taskStarted("[GraphBuilderImplOld]- converting graph to KNN form");
        baseGraph.convertToKNN(GraphBuilderImplOld.knnDefaultSize);
        logHandler.taskFinished(ra, "[GraphBuilderImplOld]- converting graph to KNN form");

        baseGraph.removeRedundantData();

        return baseGraph;
    }

    /**
     * Use this method to create the graph of tri-grams for a given corpus.
     * @deprecated This is the sequential version of GraphBuilderImplOld.createGraphFromFileMultiThread method. Be aware that,
     * Running this implementation will require considerable amount of time compared to multi-thread version.
     * @param corpusFileAddress address of input text.
     * @param labelsFileAddress address of labels file
     * @param wordLocationLabelProbabilityFileAddress address of the file containing location to
     *                                                label probability mappings.
     *                                                each line of this file is formatted as below: </br>
     *                                                #sequence #position #labelIndex (Real number)probability
     * @return a graph of tri-grams of the given corpus
     */

    public GraphContainer createGraphFromFile(String corpusFileAddress,
                                              String labelsFileAddress,
                                              String wordLocationLabelProbabilityFileAddress){
        GraphContainer baseGraph = this.createGraphFromFileBase(corpusFileAddress,labelsFileAddress,wordLocationLabelProbabilityFileAddress);
        GraphContainer unigramGraph = this.createGraphFromFileBase(corpusFileAddress, 1);
        GraphContainer bigramGraph = this.createGraphFromFileBase(corpusFileAddress, 2);
        GraphContainer fourgramGraph = this.createGraphFromFileBase(corpusFileAddress, 4);
        GraphContainer fivegramGraph = this.createGraphFromFileBase(corpusFileAddress, 5);

        baseGraph.setGraphOfNgram(1 ,unigramGraph);
        baseGraph.setGraphOfNgram(2, bigramGraph);
        baseGraph.setGraphOfNgram(4, fourgramGraph);
        baseGraph.setGraphOfNgram(5, fivegramGraph);

        unigramGraph = null;
        bigramGraph = null;
        fourgramGraph = null;
        fivegramGraph = null;

        baseGraph.populateEdgeValuesOld();
        baseGraph.convertToKNN(GraphBuilderImplOld.knnDefaultSize);

        baseGraph.removeRedundantData();

        return baseGraph;
    }

    /**
     * Use this method to export graph nodes as node id to ngram mapping. Output format is as described below: </br>
     * #nodeId [space separated ngram members]
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */

    public void saveFileAsNodeIdToNgramMapping(GraphContainer graph, String outputFileAddress){
        graph.exportToFileAsIdMapping(outputFileAddress);
    }

    /**
     * Use this method to export graph data to file. Output format is as described below: </br>
     * #source-nodeId #destination-nodeId (Real number)edge-weight
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */

    public void saveGraphToFile(GraphContainer graph, String outputFileAddress){
        graph.exportGraphToFile(outputFileAddress);
    }

    /**
     * Use this method to export graph data to file. Output format is as described below: </br>
     * [source-node word set] [destination-node word set] (Real number)edge-weight
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     * @param dictionary a dictionary object containing <word-index to word>
     */

    public void saveGraphToFileAsWordSets(GraphContainer graph, String outputFileAddress, WordDictionary dictionary){
        graph.exportGraphToFileAsWordSetsSimilarity(outputFileAddress, dictionary);
    }

    /**
     * Use this method to export graph nodes' data to file. Output format is as described below: </br>
     * #nodeId #sequence #position
     * </br>
     * sequence number and position number match to sentence number and position of the n-gram center word in sentence.
     * Both of these indexes are zero-based.
     * @deprecated this method is only used for debugging purposes.
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */

    public void saveFileAsNodeIdToLocationMapping(GraphContainer graph, String outputFileAddress){
        graph.exportToFileAsIdToLocationMapping(outputFileAddress);
    }

    /**
     * Use this method to export type probability information contained in the graph.
     * Output format is as described below: </br>
     * #nodeId #labelId (Real number)probability
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */

    public void saveFileAsNodeIdToTypeLevelProbabilities(GraphContainer graph, String outputFileAddress){
        graph.exportToFileAsIdToTypeLevelProbabilities(outputFileAddress);
    }
}
