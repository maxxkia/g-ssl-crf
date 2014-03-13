package main.java.Graph.Builder;

import main.java.Utility.Logger;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class GraphBuilderFactory {
    public enum GraphNgramType{
        WordClass, Word
    }

    /**
     * default builder class
     * @param logger a logger object used for logging purposes inside graphBuilder object
     * @param ngramType graph builder will extract sentences based on type of ngram
     * @return a generic purpose instance of graphbuilder
     */
    public static IGraphBuilder getGraphBuilder(Logger logger, GraphNgramType ngramType){
        switch (ngramType){
            case WordClass:
                return new GraphBuilderStandardCRFWordClassImpl(logger);

            case Word:
                return new GraphBuilderStandardCRFWordsImpl(logger);
        }

        return null;
    }
}
