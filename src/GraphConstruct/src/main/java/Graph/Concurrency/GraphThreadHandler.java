package main.java.Graph.Concurrency;

import main.java.Graph.GraphStructure.GraphContainer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class GraphThreadHandler extends Thread {
    private int seed,step;
    GraphContainer graph;
    private boolean calculatePMI;

    public GraphThreadHandler(int seed, int step, GraphContainer graph, boolean calculatePMI){
        this.seed = seed;
        this.step = step;
        this.graph = graph;
        this.calculatePMI = calculatePMI;
    }

    public GraphThreadHandler(int seed, int step, GraphContainer graph){
        this.seed = seed;
        this.step = step;
        this.graph = graph;
        this.calculatePMI = false;
    }

    public void run(){
        if (calculatePMI){
            graph.buildFeatureScoreMapForNodes(seed, step);
            calculatePMI = false;
        } else
            graph.populateEdgeValues(seed, step);
    }
}
