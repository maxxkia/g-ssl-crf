package main.java.Graph.GraphStructure;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class Edge<LocationType extends Location> {
    private float weight;
    private Node<LocationType> destination;

    public float getWeight(){
        return weight;
    }

    public Node getDestination(){
        return destination;
    }

    public Edge(float iWeight, Node iDestination){
        this.weight = iWeight;
        this.destination = iDestination;
    }
}
