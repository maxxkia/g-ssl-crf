package main.java.Graph.Builder;

import main.java.CRF.CRFFileReader;
import main.java.Utility.*;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class GraphBuilderStandardCRFWordClassImpl extends GraphBuilderStandardCRF {

    public GraphBuilderStandardCRFWordClassImpl(Logger logger){
        super(logger);
    }

    protected String getSentence(CRFFileReader crfFileReader){
        return crfFileReader.getWordClassSentence();
    }
}
