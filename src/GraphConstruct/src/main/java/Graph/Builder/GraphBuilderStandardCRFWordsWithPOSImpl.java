package main.java.Graph.Builder;

import main.java.CRF.CRFFileReader;
import main.java.CRF.CRFFileReaderWithPOSTags;
import main.java.Graph.GraphStructure.GraphContainer;
import main.java.Graph.GraphStructure.GraphContainerWithPOS;
import main.java.Graph.GraphStructure.NodeWithPartOfSpeech;
import main.java.TextToNgram.NgramContainer;
import main.java.TextToNgram.NgramUtility;
import main.java.Utility.Logger;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class GraphBuilderStandardCRFWordsWithPOSImpl extends GraphBuilderStandardCRFWithPOS {
    public GraphBuilderStandardCRFWordsWithPOSImpl(Logger logger){
        super(logger);
    }

    protected String getSentence(CRFFileReaderWithPOSTags crfFileReader){
        return crfFileReader.getWordSentence();
    }
}
