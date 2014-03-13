package main.java.Graph.Builder;

import main.java.CRF.CRFFileReaderWithPOSTags;
import main.java.Utility.Logger;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class GraphBuilderStandardCRFWordClassWithPOSImpl extends GraphBuilderStandardCRFWithPOS {
    public GraphBuilderStandardCRFWordClassWithPOSImpl(Logger logger){
        super(logger);
    }

    protected String getSentence(CRFFileReaderWithPOSTags crfFileReader){
        return crfFileReader.getWordClassSentence();
    }
}
