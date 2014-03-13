package main.java.Graph.GraphStructure;

import main.java.Text.WordDictionary;
import main.java.TextToNgram.NgramContainer;

import java.util.ArrayList;

//todo: ngram search should be modified after adding POS feature capability
/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */

//todo: add <Location extends Location> for GraphContainer
public class GraphContainer extends GraphContainerAbstract<Location>{
    protected GraphContainer[] ngramGraph;

    public GraphContainer(){
        super();
    }

    public GraphContainer(WordDictionary dictionaryOfClasses){
        super(dictionaryOfClasses);
    }

    public GraphContainer(WordDictionary dictionaryOfClasses, WordDictionary dictionaryOfPrepositions){
        super(dictionaryOfClasses, dictionaryOfPrepositions);
    }

    @Override
    protected void initializeNgramGraphsArray() {
        this.ngramGraph = new GraphContainer[5];
    }

    @Override
    protected void initializeNodeList() {
        this.nodeList = new ArrayList<Node<Location>>();
    }

    @Override
    protected Location newLocationObject(int sequence, int position) {
        return new Location(sequence, position);
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
    public void setGraphOfNgram(int ngramSize, GraphContainer graph){
        this.ngramGraph[this.getIndexOfGraph(ngramSize)] = graph;
    }

    /**
     * gets the graph assigned to a given ngram size
     * @param ngramSize size of ngram
     * @return a GraphContainer object containing information on ngrams of a specified size
     */
    public GraphContainer getGraphOfNgram(int ngramSize){
        return this.ngramGraph[this.getIndexOfGraph(ngramSize)];
    }

    public int getCountOfNgram(NgramContainer ngram){
        return this.getGraphOfNgram(ngram.getSize()).getCountOfNgramInSelf(ngram);
    }

    public void removeRedundantData() {
        for (int i=0; i<ngramGraph.length ; ++i)
            this.ngramGraph[i] = null;
    }
}
