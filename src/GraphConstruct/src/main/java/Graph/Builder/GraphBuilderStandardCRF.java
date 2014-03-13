package main.java.Graph.Builder;

import main.java.CRF.CRFFileReader;
import main.java.Graph.Concurrency.GraphThreadHandler;
import main.java.Graph.GraphStructure.GraphContainer;
import main.java.Graph.GraphStructure.Location;
import main.java.Graph.GraphStructure.Node;
import main.java.Text.WordDictionary;
import main.java.TextToNgram.NgramContainer;
import main.java.TextToNgram.NgramUtility;
import main.java.Utility.*;

import java.util.ArrayList;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public abstract class GraphBuilderStandardCRF implements IGraphBuilder {
    private static final int defaultNgramSize = 3;
    private Logger logHandler;

    public GraphBuilderStandardCRF(Logger logger){
        this.logHandler = logger;
    }

    protected abstract String getSentence(CRFFileReader crfFileReader);

    public GraphContainer createGraphFromFileBase(String corpusFileAddress, int ngramSize){
        return this.createGraphFromFileBase(null, corpusFileAddress, ngramSize);
    }

    public GraphContainer createGraphFromFileBase(GraphContainer graph, String corpusFileAddress, int ngramSize){
        NgramContainer[] ngramSet;
        NgramUtility ngramUtility = new NgramUtility();

        if (graph == null)
            graph = new GraphContainer();

        String sentence;

        CRFFileReader crfFileReader = new CRFFileReader(corpusFileAddress);

        while (crfFileReader.hasNext()) {
            crfFileReader.getNext();

            sentence = getSentence(crfFileReader);

            ngramSet = ngramUtility.extractNgramsFromSentence(sentence, ngramSize);

            for (NgramContainer ngram : ngramSet) {
                graph.addNode(new Node<Location>(ngram));//add node to graph or else update node frequency
            }
        }
        crfFileReader.close();

        return graph;
    }

    public GraphContainer createGraphFromFileBaseForMainGraph(String corpusFileAddress, int ngramSize){
        return createGraphFromFileBaseForMainGraph(null, corpusFileAddress, ngramSize);
    }

    public GraphContainer createGraphFromFileBaseForMainGraph(GraphContainer graph, String corpusFileAddress, int ngramSize){
        NgramContainer[] ngramSet;
        NgramUtility ngramUtility = new NgramUtility();

        int sequence = 0;//todo: this variable can be declared as a field

        if (graph == null)
            graph = new GraphContainer();

        String sentence;

        CRFFileReader crfFileReader = new CRFFileReader(corpusFileAddress);

        while (crfFileReader.hasNext()) {
            crfFileReader.getNext();

            sentence = getSentence(crfFileReader);

            ngramSet = ngramUtility.extractNgramsFromSentence(sentence, ngramSize);

            graph.addNgramsToGraph(ngramSet, sequence);
            ++sequence;
        }

        return graph;
    }

    public GraphContainer createGraphFromFileBaseForTypeProbabilityCalculation(String corpusFileAddress,
                                                                               String labelsFileAddress,
                                                                               String wordLocationLabelProbabilityFileAddress){
        return this.createGraphFromFileBaseForTypeProbabilityCalculation(null, corpusFileAddress, labelsFileAddress, wordLocationLabelProbabilityFileAddress);
    }

    public GraphContainer createGraphFromFileBaseForTypeProbabilityCalculation(GraphContainer graph,
                                                                               String corpusFileAddress,
                                                                               String labelsFileAddress,
                                                                               String wordLocationLabelProbabilityFileAddress){
        if (graph == null)
            graph = new GraphContainer();

        int ngramSize = defaultNgramSize;

        NgramContainer[] ngramSet;
        NgramUtility ngramUtility = new NgramUtility();

        int sequence = 0;

        String sentence;

        int labelCount = LabelFileHandler.countLabels(labelsFileAddress);
        LocationToLabelFileHandler fileInputLocationToLabelMapping =
                new LocationToLabelFileHandler(wordLocationLabelProbabilityFileAddress);
        CRFFileReader crfFileReader = new CRFFileReader(corpusFileAddress);

        while (crfFileReader.hasNext()) {
            crfFileReader.getNext();

            sentence = getSentence(crfFileReader);

            ngramSet = ngramUtility.extractNgramsFromSentence(sentence, ngramSize);

            graph.addNgramsToGraph(ngramSet, sequence, labelCount, fileInputLocationToLabelMapping);
            ++sequence;
        }

        return graph;
    }

    public GraphContainer createGraphFromFileBaseForMarginalsCalculation(String corpusFileAddress){
        return this.createGraphFromFileBaseForMarginalsCalculation(null, corpusFileAddress);
    }

    public GraphContainer createGraphFromFileBaseForMarginalsCalculation(GraphContainer graph, String corpusFileAddress){
        if (graph == null)
            graph = new GraphContainer();

        int ngramSize = 3;
        NgramContainer[] ngramSet;
        NgramUtility ngramUtility = new NgramUtility();

        int nodeIndex;

        String sentence;
        ArrayList<Integer> labels;

        CRFFileReader crfFileReader = new CRFFileReader(corpusFileAddress);

        while (crfFileReader.hasNext()) {
            crfFileReader.getNext();

            sentence = getSentence(crfFileReader);
            labels = crfFileReader.getLabels();

            ngramSet = ngramUtility.extractNgramsFromSentence(sentence, ngramSize);

            for(int i=0; i<ngramSet.length ; ++i){
                nodeIndex = graph.addNode(new Node<Location>(ngramSet[i]));//add node to graph or else update node frequency
                graph.getNodeAt(nodeIndex).incrementLabelCount(labels.get(i)); //add label data to node
            }
        }
        graph.updateNodesEmpiricalLabelProbabilities();

        return graph;
    }

    /**
     * Use this method to create the graph of tri-grams for a given corpus.
     * @deprecated This is the sequential version of GraphBuilder.createGraphFromFileMultiThread method. Be aware that,
     * Running this implementation will require considerable amount of time compared to multi-thread version.
     * @param corpusFileAddress address of input text.
     * @param labelsFileAddress address of labels file
     * @param wordLocationLabelProbabilityFileAddress address of the file containing location to
     *                                                label probability mappings.
     *                                                each line of this file is formatted as below: </br>
     *                                                #sequence #position #labelIndex (Real number)probability
     * @return a graph of tri-grams of the given corpus
     */
    @Override
    public GraphContainer createGraphFromFile(String corpusFileAddress,
                                              String labelsFileAddress,
                                              String wordLocationLabelProbabilityFileAddress){
        GraphContainer baseGraph = this.createGraphFromFileBase(corpusFileAddress, 3);
        GraphContainer unigramGraph = this.createGraphFromFileBase(corpusFileAddress, 1);
        GraphContainer bigramGraph = this.createGraphFromFileBase(corpusFileAddress, 2);
        GraphContainer fourgramGraph = this.createGraphFromFileBase(corpusFileAddress, 4);
        GraphContainer fivegramGraph = this.createGraphFromFileBase(corpusFileAddress, 5);

        baseGraph.setGraphOfNgram(1 ,unigramGraph);
        baseGraph.setGraphOfNgram(2, bigramGraph);
        baseGraph.setGraphOfNgram(4, fourgramGraph);
        baseGraph.setGraphOfNgram(5, fivegramGraph);

        baseGraph.buildFeatureScoreMapForNodes();
        baseGraph.populateEdgeValues();
        baseGraph.convertToKNN(Config.getKnnDefaultSize());

        baseGraph.removeRedundantData();

        return baseGraph;
    }

    /**
     * Use this method to create the graph of tri-grams for a given corpus
     * @param corpusFileAddress address of input text.
     * @return a graph of tri-grams of the given corpus
     */
    @Override
    public GraphContainer createGraphFromFileMultiThread(String corpusFileAddress){
        RuntimeAnalyzer ra;
        ra = logHandler.taskStarted("[GraphBuilder]- creating nodes of ngram graphs");

        GraphContainer baseGraph = this.createGraphFromFileBase(corpusFileAddress, 3);

        GraphContainer unigramGraph = this.createGraphFromFileBase(corpusFileAddress, 1);
        GraphContainer bigramGraph = this.createGraphFromFileBase(corpusFileAddress, 2);
        GraphContainer fourgramGraph = this.createGraphFromFileBase(corpusFileAddress, 4);
        GraphContainer fivegramGraph = this.createGraphFromFileBase(corpusFileAddress, 5);

        logHandler.taskFinished(ra, "[GraphBuilder]- creating nodes of ngram graphs");

        baseGraph.setGraphOfNgram(1 ,unigramGraph);
        baseGraph.setGraphOfNgram(2, bigramGraph);
        baseGraph.setGraphOfNgram(4, fourgramGraph);
        baseGraph.setGraphOfNgram(5, fivegramGraph);

        try{
            int threadCount = Config.graphBuilderThreadCount;
            //build feature score map in first run
            ra = logHandler.taskStarted("[GraphBuilder]- calculating pmi values");
            GraphThreadHandler[] threads = new GraphThreadHandler[threadCount];
            for (int i=0; i<threadCount ; ++i){
                threads[i] = new GraphThreadHandler(i, threadCount, baseGraph, true);
                threads[i].start();
            }

            for (int i=0; i<threadCount ; ++i){
                threads[i].join();
            }
            logHandler.taskFinished(ra, "[GraphBuilder]- calculating pmi values");

            //measure similarity values of nodes and assign edge values
            ra = logHandler.taskStarted("[GraphBuilder]- assigning edge weights for nodes");
            for (int i=0; i<threadCount ; ++i){
                threads[i] = new GraphThreadHandler(i, threadCount, baseGraph);
                threads[i].start();
            }

            for (int i=0; i<threadCount ; ++i){
                threads[i].join();
            }
            logHandler.taskFinished(ra, "[GraphBuilder]- assigning edge weights for nodes");

        }catch (InterruptedException ex){
            ex.printStackTrace();
        }

        ra = logHandler.taskStarted("[GraphBuilder]- converting graph to KNN form");
        baseGraph.convertToKNN(Config.getKnnDefaultSize());
        logHandler.taskFinished(ra, "[GraphBuilder]- converting graph to KNN form");

        baseGraph.removeRedundantData();

        return baseGraph;
    }

    /**
     * Use this method to create the graph of tri-grams for a given corpus
     * @param corpusFileAddress address of input text.
     * @param corpusUnlabeledFileAddress address of second input file which is supposed to be the unlabeled set
     * @return a graph of tri-grams of the given corpus
     */
    public GraphContainer createGraphFromFileMultiThread(String corpusFileAddress,
                                                         String corpusUnlabeledFileAddress){
        return createGraphFromFileMultiThread(null, corpusFileAddress, corpusUnlabeledFileAddress);
    }

    /**
     * Use this method to create the graph of tri-grams for a given corpus
     * @param graph a graph object to add graph data to
     * @param corpusFileAddress address of input text.
     * @param corpusUnlabeledFileAddress address of second input file which is supposed to be the unlabeled set
     * @return a graph of tri-grams of the given corpus
     */
    @Override
    public GraphContainer createGraphFromFileMultiThread(GraphContainer graph, String corpusFileAddress, String corpusUnlabeledFileAddress) {
        RuntimeAnalyzer ra;
        ra = logHandler.taskStarted("[GraphBuilder]- creating nodes of ngram graphs");

        GraphContainer baseGraph = this.createGraphFromFileBaseForMainGraph(graph, corpusFileAddress, 3);
        baseGraph = this.createGraphFromFileBaseForMainGraph(baseGraph, corpusUnlabeledFileAddress, 3);

        GraphContainer unigramGraph = this.createGraphFromFileBase(corpusFileAddress, 1);
        unigramGraph = this.createGraphFromFileBase(unigramGraph, corpusUnlabeledFileAddress, 1);

        GraphContainer bigramGraph = this.createGraphFromFileBase(corpusFileAddress, 2);
        bigramGraph = this.createGraphFromFileBase(bigramGraph, corpusUnlabeledFileAddress, 2);

        GraphContainer fourgramGraph = this.createGraphFromFileBase(corpusFileAddress, 4);
        fourgramGraph = this.createGraphFromFileBase(fourgramGraph, corpusUnlabeledFileAddress, 4);

        GraphContainer fivegramGraph = this.createGraphFromFileBase(corpusFileAddress, 5);
        fivegramGraph = this.createGraphFromFileBase(fivegramGraph, corpusUnlabeledFileAddress, 5);

        logHandler.taskFinished(ra, "[GraphBuilder]- creating nodes of ngram graphs");

        baseGraph.setGraphOfNgram(1 ,unigramGraph);
        baseGraph.setGraphOfNgram(2, bigramGraph);
        baseGraph.setGraphOfNgram(4, fourgramGraph);
        baseGraph.setGraphOfNgram(5, fivegramGraph);

        try{
            int threadCount = Config.graphBuilderThreadCount;
            //build feature score map in first run
            ra = logHandler.taskStarted("[GraphBuilder]- calculating pmi values");
            GraphThreadHandler[] threads = new GraphThreadHandler[threadCount];
            for (int i=0; i<threadCount ; ++i){
                threads[i] = new GraphThreadHandler(i, threadCount, baseGraph, true);
                threads[i].start();
            }

            for (int i=0; i<threadCount ; ++i){
                threads[i].join();
            }
            logHandler.taskFinished(ra, "[GraphBuilder]- calculating pmi values");

            //measure similarity values of nodes and assign edge values
            ra = logHandler.taskStarted("[GraphBuilder]- assigning edge weights for nodes");
            for (int i=0; i<threadCount ; ++i){
                threads[i] = new GraphThreadHandler(i, threadCount, baseGraph);
                threads[i].start();
            }

            for (int i=0; i<threadCount ; ++i){
                threads[i].join();
            }
            logHandler.taskFinished(ra, "[GraphBuilder]- assigning edge weights for nodes");
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }

        ra = logHandler.taskStarted("[GraphBuilder]- converting graph to KNN form");
        baseGraph.convertToKNN(Config.getKnnDefaultSize());
        logHandler.taskFinished(ra, "[GraphBuilder]- converting graph to KNN form");

        baseGraph.removeRedundantData();

        return baseGraph;
    }

    /**
     * Use this method to export graph nodes as node id to ngram mapping. Output format is as described below: </br>
     * #nodeId [space separated ngram members]
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    @Override
    public void saveFileAsNodeIdToNgramMapping(GraphContainer graph, String outputFileAddress){
        RuntimeAnalyzer sectionRunAnalyzer = logHandler.taskStarted("[GraphBuilder]- exporting graph data");
        graph.exportToFileAsIdMapping(outputFileAddress);
        logHandler.taskFinished(sectionRunAnalyzer, "[GraphBuilder]- exporting graph data");
    }

    /**
     * Use this method to export graph data to file. Output format is as described below: </br>
     * #source-nodeId #destination-nodeId (Real number)edge-weight
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    @Override
    public void saveGraphToFile(GraphContainer graph, String outputFileAddress){
        RuntimeAnalyzer sectionRunAnalyzer = logHandler.taskStarted("[GraphBuilder]- exporting graph data");
        graph.exportGraphToFile(outputFileAddress);
        logHandler.taskFinished(sectionRunAnalyzer, "[GraphBuilder]- exporting graph data");
    }

    /**
     * Use this method to export graph data to file. Output format is as described below: </br>
     * [source-node word set] [destination-node word set] (Real number)edge-weight
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     * @param dictionary a dictionary object containing <word-index to word>
     */
    @Override
    public void saveGraphToFileAsWordSets(GraphContainer graph, String outputFileAddress, WordDictionary dictionary){
        RuntimeAnalyzer sectionRunAnalyzer = logHandler.taskStarted("[GraphBuilder]- exporting graph data");
        graph.exportGraphToFileAsWordSetsSimilarity(outputFileAddress, dictionary);
        logHandler.taskFinished(sectionRunAnalyzer, "[GraphBuilder]- exporting graph data");
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
    @Override
    public void saveFileAsNodeIdToLocationMapping(GraphContainer graph, String outputFileAddress){
        RuntimeAnalyzer sectionRunAnalyzer = logHandler.taskStarted("[GraphBuilder]- exporting graph data");
        graph.exportToFileAsIdToLocationMapping(outputFileAddress);
        logHandler.taskFinished(sectionRunAnalyzer, "[GraphBuilder]- exporting graph data");
    }

    /**
     * Use this method to export type probability information contained in the graph.
     * Output format is as described below: </br>
     * #nodeId #labelId (Real number)probability
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    @Override
    public void saveFileAsNodeIdToTypeLevelProbabilities(GraphContainer graph, String outputFileAddress){
        RuntimeAnalyzer sectionRunAnalyzer = logHandler.taskStarted("[GraphBuilder]- exporting graph data");
        graph.exportToFileAsIdToTypeLevelProbabilities(outputFileAddress);
        logHandler.taskFinished(sectionRunAnalyzer, "[GraphBuilder]- exporting graph data");
    }

    /**
     * Use this method to export type marginal probabilities to a file.
     * Output format is as described below: </br>
     * nodeIdInSerializedForm [TAB] #labelId [TAB] (Real number)probability
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    @Override
    public void saveFileAsTypeLevelEmpiricalLabelProbabilities(GraphContainer graph, String outputFileAddress){
        RuntimeAnalyzer sectionRunAnalyzer = logHandler.taskStarted("[GraphBuilder]- exporting graph data");
        graph.exportToFileAsEmpiricalProbabilities(outputFileAddress);
        logHandler.taskFinished(sectionRunAnalyzer, "[GraphBuilder]- exporting graph data");
    }
}
