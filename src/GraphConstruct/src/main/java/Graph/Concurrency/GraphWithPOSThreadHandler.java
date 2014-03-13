package main.java.Graph.Concurrency;

import main.java.Graph.GraphStructure.GraphContainerWithPOS;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class GraphWithPOSThreadHandler extends Thread{
    private int seed,step;
    GraphContainerWithPOS graph;
    private boolean calculatePMI;

    public GraphWithPOSThreadHandler(int seed, int step, GraphContainerWithPOS graph, boolean calculatePMI){
        this.seed = seed;
        this.step = step;
        this.graph = graph;
        this.calculatePMI = calculatePMI;
    }

    public GraphWithPOSThreadHandler(int seed, int step, GraphContainerWithPOS graph){
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
