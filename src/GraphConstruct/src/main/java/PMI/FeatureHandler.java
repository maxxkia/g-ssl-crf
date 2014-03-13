package main.java.PMI;

import main.java.Graph.GraphStructure.GraphContainerAbstract;
import main.java.TextToNgram.NgramContainer;
import main.java.Utility.TextFileInput;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class FeatureHandler {
    /**
     * Use of this identifier as a member of a given ngram means the given
     * member position can be replaced with any word from the corpus
     */
    public static final String nullTokenIdentifier = "?";
    public enum FeatureType{Simple, IsClass, IsPreposition, POS}

    /**
     * a set of features to use when calculating main.java.PMI
     */
    private static NgramContainer[] featureList =
            {new NgramContainer(new String[] {"-1", "0", "1", "2", "3"}),
                    new NgramContainer(new String[] {"-1", "0"}),
                    new NgramContainer(new String[] {"2", "3"}),
                    new NgramContainer(new String[] {"1"})};
                    /*new NgramContainer(new String[] {"0", "?", "2"}),
                    new NgramContainer(new String[] {"0", "?", "2", "3"}),
                    new NgramContainer(new String[] {"-1", "0", "?", "2"})};*/

    /**
     * a set of features used as complementary for main features.
     */
    private static NgramContainer[] combinationFeatureList =
            {new NgramContainer(new String[] {"-1", "0", "1", "2", "3"}),
                    new NgramContainer(new String[] {"-1", "0", "1", "2"}),
                    new NgramContainer(new String[] {"0", "1", "2", "3"}),
                    new NgramContainer(new String[] {"0", "1", "2"})};
                    /*new NgramContainer(new String[] {"0", "1", "2"}),
                    new NgramContainer(new String[] {"0", "1", "2", "3"}),
                    new NgramContainer(new String[] {"-1", "0", "1", "2"})};*/

    private static NgramContainer[] classTypeFeatures =
            {new NgramContainer(new String[] {"class", "?"}),
                    new NgramContainer(new String[] {"pos"}),
                    new NgramContainer(new String[] {"preposition", "?"})};

    /**
     * Use this method to extract all the features of a given context (a 5-gram as in here)
     * defined in FeatureHandler.featureList
     * @param context the context to extract features from
     * @return a set of extracted features of the given context
     */
    public static NgramContainer[] extractFeaturesOfContext(NgramContainer context){
        NgramContainer[] result = new NgramContainer[featureList.length];
        int indexInContext;
        int innerBounds;

        for (int index=0; index<featureList.length ; ++index){
            if (isNonSimpleFeature(featureList[index])){
                result[index] = featureList[index];
            } else {
                result[index] = new NgramContainer(featureList[index].getSize());
                innerBounds = result[index].getSize();
                for (int j=0; j<innerBounds ; ++j){
                    if (featureList[index].getMemberValue(j).equals(FeatureHandler.nullTokenIdentifier))
                        result[index].setMemberValue(j, FeatureHandler.nullTokenIdentifier);
                    else{
                        indexInContext = Integer.parseInt(featureList[index].getMemberValue(j));
                        indexInContext += 1;
                        result[index].setMemberValue(j, context.getMemberValue(indexInContext));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Use this method to extract all the features of a given context (a 5-gram as in here)
     * defined in FeatureHandler.featureList
     * @param context the context to extract features from
     * @return a set of extracted features of the given context
     */
    public static NgramContainer[] extractFeaturesOfContext(NgramContainer context, NgramContainer POSTagscontext){
        NgramContainer[] result = new NgramContainer[featureList.length];
        int indexInContext;
        int innerBounds;

        for (int index=0; index<featureList.length ; ++index){
            if (isNonSimpleFeature(featureList[index])){
                result[index] = featureList[index];
            } else {
                result[index] = new NgramContainer(featureList[index].getSize());
                innerBounds = result[index].getSize();
                for (int j=0; j<innerBounds ; ++j){
                    if (featureList[index].getMemberValue(j).equals(FeatureHandler.nullTokenIdentifier))
                        result[index].setMemberValue(j, FeatureHandler.nullTokenIdentifier);
                    else{
                        indexInContext = Integer.parseInt(featureList[index].getMemberValue(j));
                        indexInContext += 1;
                        result[index].setMemberValue(j, context.getMemberValue(indexInContext));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Use this method to extract combined form of  all the features of a given context (a 5-gram as in here)
     * defined in FeatureHandler.combinationFeatureList
     * @param context the context to extract features from
     * @return a set of extracted features in combined form
     */
    public static NgramContainer[] extractFeaturesInCombinedFormOfContext(NgramContainer context){
        NgramContainer[] result = new NgramContainer[combinationFeatureList.length];
        int indexInContext;
        int innerBounds;

        for (int index=0; index<combinationFeatureList.length ; ++index){
                result[index] = new NgramContainer(combinationFeatureList[index].getSize());
                innerBounds = result[index].getSize();
                for (int j=0; j<innerBounds ; ++j){
                    indexInContext = Integer.parseInt(combinationFeatureList[index].getMemberValue(j));
                    indexInContext += 1;
                    result[index].setMemberValue(j, context.getMemberValue(indexInContext));
                }
        }

        return result;
    }

    public static boolean isTemplate(NgramContainer ngramOfFeature){
        return ngramOfFeature.hasMember(FeatureHandler.nullTokenIdentifier);
    }

    /**
     * Use this method to overwrite default features and use features specified in a given file.
     * features should be defined using indexed references to words occurring in a context,
     * after that comes a ';' and combined form of features should be specified in the same manner.
     * @param featureFileAddress address of a given features file
     */
    public static void readFeaturesFromFile(String featureFileAddress){
        TextFileInput fileInput = new TextFileInput(featureFileAddress);

        String line;
        ArrayList<NgramContainer> features = new ArrayList<NgramContainer>();
        ArrayList<NgramContainer> featuresInCombinedForm = new ArrayList<NgramContainer>();
        String currentFeature, currentFeatureInCombinedForm;

        while ((line = fileInput.readLine()) != null){
            if (line.indexOf("#") == 0 || line.trim().equals(""))
                //this is a comment line, ignore this
                continue;

            currentFeature = line.split(";")[0];
            currentFeatureInCombinedForm = line.split(";")[1];

            features.add( new NgramContainer( getTokens(currentFeature) ) );
            featuresInCombinedForm.add( new NgramContainer( getTokens(currentFeatureInCombinedForm) ) );
        }

        featureList = features.toArray(new NgramContainer[features.size()]);
        combinationFeatureList = featuresInCombinedForm.toArray(new NgramContainer[featuresInCombinedForm.size()]);

        fileInput.close();
    }

    private static String[] getTokens(String str){
        StringTokenizer tokenizer = new StringTokenizer(str, " ");
        ArrayList<String> tokensList = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()){
            tokensList.add(tokenizer.nextToken());
        }

        return tokensList.toArray(new String[tokensList.size()]);
    }

    public static boolean isClassFeature(NgramContainer ngram) {
        return ngram.equalsWithTemplate(classTypeFeatures[0]);
    }

    public static boolean isPOSFeature(NgramContainer ngram) {
        return ngram.getMemberValue(0).equals(classTypeFeatures[1].getMemberValue(0));
    }

    public static boolean isPrepositionFeature(NgramContainer ngram) {
        return ngram.equalsWithTemplate(classTypeFeatures[2]);
    }

    public static boolean isNonSimpleFeature(NgramContainer ngram){
        return getTypeOfFeature(ngram) != FeatureType.Simple;
    }

    public static double computePMIForPair(int totalFrequency, NgramContainer ngram1,
                                           NgramContainer ngram2, NgramContainer combinedForm, GraphContainerAbstract graph) {
        double result = 0;
        int countOfNgram1, countOfNgram2, countOfCombination;

        switch (getTypeOfFeature(ngram2)){
            case Simple:
                countOfNgram1 = graph.getCountOfNgram(ngram1);
                countOfNgram2 = graph.getCountOfNgram(ngram2);
                countOfCombination = graph.getCountOfNgram(combinedForm);

                result = main.java.PMI.PMIUtility.calculatePMI(totalFrequency, countOfCombination, countOfNgram1, countOfNgram2);
                break;

            case IsClass:
                if (graph.getDictionaryOfClasses().containsKey(combinedForm.getMemberValue(0)))
                    result = main.java.PMI.PMIUtility.calculatePMI(totalFrequency, 1, graph.getCountOfNgram(ngram1), 1);
                break;

            case IsPreposition:
                if (graph.getDictionaryOfPrepositions().containsKey(combinedForm.getMemberValue(0)))
                    result = main.java.PMI.PMIUtility.calculatePMI(totalFrequency, 1, graph.getCountOfNgram(ngram1), 1);
                break;

            case POS:
                NgramContainer posFeature = getMainPartOfNonSimpleFeature(ngram2);

                countOfNgram1 = graph.getCountOfNgram(ngram1);
                countOfNgram2 = graph.getNgramStatMapForPOS().getValueOf(posFeature);
                countOfCombination = graph.getNgramPairStatMapForPOS().getValueOf(ngram1, posFeature);

                result = main.java.PMI.PMIUtility.calculatePMI(totalFrequency, countOfCombination, countOfNgram1, countOfNgram2);
                break;
        }

        return result;
    }

    public static FeatureType getTypeOfFeature(NgramContainer ngram){
        FeatureType result = FeatureType.Simple;

        if (FeatureHandler.isClassFeature(ngram))
            result = FeatureType.IsClass;
        else if (FeatureHandler.isPOSFeature(ngram))
            result = FeatureType.POS;
        else if (FeatureHandler.isPrepositionFeature(ngram))
            result = FeatureType.IsPreposition;

        return result;
    }

    public static NgramContainer getMainPartOfNonSimpleFeature(NgramContainer nonSimpleFeature){
        return nonSimpleFeature.getSubNgram(1);
    }
}
