package main.java.Graph.Builder;

import main.java.Graph.GraphStructure.GraphContainer;
import main.java.Graph.GraphStructure.GraphContainerAbstract;
import main.java.Text.WordDictionary;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public interface IGraphBuilder {

    /**
     * Use this method to create a graph of ngrams extracted from sentences of a given text file.
     * @param corpusFileAddress address of the text file to read from
     * @param ngramSize size of ngrams to extract from each sentence
     * @return a graph of ngrams
     */
    GraphContainer createGraphFromFileBase(String corpusFileAddress, int ngramSize);

    /**
     * Use this method to create a graph of ngrams extracted from sentences of a given text file and add this data to
     * a previously built graph
     * @param graph a given graph to add new data to
     * @param corpusFileAddress address of the text file to read from
     * @param ngramSize size of ngrams to extract from each sentence
     * @return a graph of ngrams
     */
    GraphContainer createGraphFromFileBase(GraphContainer graph, String corpusFileAddress, int ngramSize);

    //todo: reform the following javadoc comments
    /**
     * Use this method to calculate label probabilities for each tri-gram.
     * @param corpusFileAddress address of the text file to read from
     * @param labelsFileAddress address of labels dictionary file
     * @param wordLocationLabelProbabilityFileAddress address of the file containing label probability for each location.
     * @return a graph of nodes containing label probability data for each node
     */
    GraphContainer createGraphFromFileBaseForTypeProbabilityCalculation(String corpusFileAddress,
                                                                        String labelsFileAddress,
                                                                        String wordLocationLabelProbabilityFileAddress);

    /**
     * Use this method to calculate label probabilities for each tri-gram, and add new data to a previously built graph
     * @param graph a given graph to add new data to
     * @param corpusFileAddress address of the text file to read from
     * @param labelsFileAddress address of labels dictionary file
     * @param wordLocationLabelProbabilityFileAddress address of the file containing label probability for each location.
     * @return a graph of nodes containing label probability data for each node
     */
    GraphContainer createGraphFromFileBaseForTypeProbabilityCalculation(GraphContainer graph,
                                                                        String corpusFileAddress,
                                                                        String labelsFileAddress,
                                                                        String wordLocationLabelProbabilityFileAddress);

    /**
     * Use this method to calculate marginal probability for each tri-gram, and add new data to a previously built graph
     * @param corpusFileAddress address of the text file to read from
     * @return a graph of nodes containing marginal probability data for each node
     */
    GraphContainer createGraphFromFileBaseForMarginalsCalculation(String corpusFileAddress);

    /**
     * Use this method to calculate marginal probability for each tri-gram. And add new data to a previously built graph
     * @param graph a given graph to add new data to
     * @param corpusFileAddress address of the text file to read from
     * @return a graph of nodes containing marginal probability data for each node
     */
    GraphContainer createGraphFromFileBaseForMarginalsCalculation(GraphContainer graph, String corpusFileAddress);

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
    GraphContainer createGraphFromFile(String corpusFileAddress,
                                       String labelsFileAddress,
                                       String wordLocationLabelProbabilityFileAddress);

    /**
     * Use this method to create the graph of tri-grams for a given corpus
     * @param corpusFileAddress address of input text.
     * @return a graph of tri-grams of the given corpus
     */
    GraphContainer createGraphFromFileMultiThread(String corpusFileAddress);

    /**
     * Use this method to create the graph of tri-grams for a given corpus
     * @param corpusFileAddress address of input text.
     * @return a graph of tri-grams of the given corpus
     */
    GraphContainer createGraphFromFileMultiThread(String corpusFileAddress,
                                                         String corpusUnlabeledFileAddress);

    public GraphContainer createGraphFromFileMultiThread(GraphContainer graph, String corpusFileAddress,
                                                         String corpusUnlabeledFileAddress);

    /**
     * Use this method to export graph nodes as node id to ngram mapping. Output format is as described below: </br>
     * #nodeId [space separated ngram members]
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    void saveFileAsNodeIdToNgramMapping(GraphContainer graph, String outputFileAddress);

    /**
     * Use this method to export graph data to file. Output format is as described below: </br>
     * #source-nodeId #destination-nodeId (Real number)edge-weight
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    void saveGraphToFile(GraphContainer graph, String outputFileAddress);

    /**
     * Use this method to export graph data to file. Output format is as described below: </br>
     * [source-node word set] [destination-node word set] (Real number)edge-weight
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     * @param dictionary a dictionary object containing <word-index to word>
     */
    void saveGraphToFileAsWordSets(GraphContainer graph, String outputFileAddress, WordDictionary dictionary);

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
    void saveFileAsNodeIdToLocationMapping(GraphContainer graph, String outputFileAddress);

    /**
     * Use this method to export type probability information contained in the graph.
     * Output format is as described below: </br>
     * nodeIdInSerializedForm [TAB] #labelId [TAB] (Real number)probability
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    void saveFileAsNodeIdToTypeLevelProbabilities(GraphContainer graph, String outputFileAddress);

    /**
     * Use this method to export type marginal probabilities to a file.
     * Output format is as described below: </br>
     * nodeIdInSerializedForm [TAB] #labelId [TAB] (Real number)probability
     * @param graph the input graph
     * @param outputFileAddress name of the file to save output
     */
    void saveFileAsTypeLevelEmpiricalLabelProbabilities(GraphContainer graph, String outputFileAddress);
}
