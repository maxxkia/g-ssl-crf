package main.java.Graph;

import main.java.Graph.Builder.*;
import main.java.Graph.GraphStructure.GraphContainer;
import main.java.PMI.FeatureHandler;
import main.java.Text.WordDictionary;
import main.java.Utility.*;

import java.util.Hashtable;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */

//todo: read graph file concurrently

public class GraphConstructRunner {
    private static String inputFileAddress,
            inputUnlabeledFileAddress,
            outputFileAddress,
            labelsInputFile,
            locationToLabelProbFile,
            dictionaryFile = null,
            featuresFile = null,
            dictionaryOfClassesFile = null,
            dictionaryOfPrepositionsFile = null;

    public static void main(String[] args) {
        processArguments(args);

        Logger logger = new Logger(Config.defaultLogFileAddress);

        RuntimeAnalyzer totalRunAnalyzer;
        totalRunAnalyzer = logger.taskStarted("[GraphConstructRunner]- ");

        switch (Config.runMode){
            case Graph:
                runInGraphMode(logger);
                break;
            case EmpiricalTypeProbability:
                runInEmpiricalTypeLabelProbabilityMode(logger);
                break;
            case TypeProbability:
                runInTypeProbabilityMode(logger);
                break;
        }

        logger.taskFinished(totalRunAnalyzer, "[GraphConstructRunner]- ");
        logger.close();
    }

    /**
     * program is run in typeprobability mode so only .type2probability file will be created.
     * @param logger logger object to use
     */
    private static void runInTypeProbabilityMode(Logger logger) {
        IGraphBuilder graphBuilder = GraphBuilderFactory.getGraphBuilder(logger, Config.graphNgramType);
        GraphContainer graph;

        graph = graphBuilder.createGraphFromFileBaseForTypeProbabilityCalculation(inputFileAddress,
                labelsInputFile, locationToLabelProbFile);

        graphBuilder.saveFileAsNodeIdToTypeLevelProbabilities(graph,
                outputFileAddress + Defaults.exportTypeLevelProbabilitiesPostfix);
    }

    /**
     * program is run in type empirical probability mode so only .seed file will be created.
     * @param logger logger object to use
     */
    private static void runInEmpiricalTypeLabelProbabilityMode(Logger logger) {
        IGraphBuilder graphBuilder = GraphBuilderFactory.getGraphBuilder(logger, Config.graphNgramType);
        GraphContainer graph;

        graph = graphBuilder.createGraphFromFileBaseForMarginalsCalculation(inputFileAddress);

        graphBuilder.saveFileAsTypeLevelEmpiricalLabelProbabilities(graph,
                outputFileAddress + Defaults.exportTypeLevelEmpiricalLabelProbabilitiesPostfix);
    }

    /**
     * program is run in graph mode so .graph and .type2probability files will be created.
     * @param logger logger object to use
     */
    private static void runInGraphMode(Logger logger) {
        //todo: add pos graphbuilder here
        if (featuresFile != null)
            FeatureHandler.readFeaturesFromFile(featuresFile);

        IGraphBuilder graphBuilder = GraphBuilderFactory.getGraphBuilder(logger, Config.graphNgramType);
        GraphContainer graph = null;
        //todo: correct here, these should be run independently
        if (dictionaryOfClassesFile != null){
            WordDictionary dictionaryOfClasses = new WordDictionary();
            dictionaryOfClasses.buildDictionaryFromFile(dictionaryOfClassesFile);
            if (dictionaryOfPrepositionsFile != null){
                WordDictionary dictionaryOfPrepositions = new WordDictionary();
                dictionaryOfPrepositions.buildDictionaryFromFile(dictionaryOfPrepositionsFile);
                graph = new GraphContainer(dictionaryOfClasses, dictionaryOfPrepositions);
            } else {
                graph = new GraphContainer(dictionaryOfClasses);
            }
        }
        graph = graphBuilder.createGraphFromFileMultiThread(graph, inputFileAddress, inputUnlabeledFileAddress);

        graphBuilder.saveGraphToFile(graph, outputFileAddress + Defaults.exportGraphPostfix);

        if (dictionaryFile != null){
            graph.getGraphAnalytics(dictionaryFile);
        }
    }

    private static void processArguments(String[] args) {
        Hashtable<String, String> configTable = new Hashtable<String, String>(10, (float) 0.9);
        for (int i=0; i<args.length ; ++i){
            if (args[i].startsWith("-")){
                if (args[i].toLowerCase().equals("-typeprobability") || args[i].toLowerCase().equals("-graph") ||
                        args[i].toLowerCase().equals("-empirical") || args[i].toLowerCase().equals("-pos"))
                    configTable.put(getCommandFromArg(args[i]), "true");
                else if (i+1 < args.length){
                    configTable.put(getCommandFromArg(args[i]), args[++i]);
                }
            }
        }

        if (configTable.containsKey("h") || configTable.containsKey("help"))
            MessagePrinter.PrintAndDie("help->");

        if (configTable.containsKey("graph"))
            Config.runMode = Config.RunModeType.Graph;
        else if (configTable.containsKey("typeprobability"))
            Config.runMode = Config.RunModeType.TypeProbability;
        else if (configTable.containsKey("empirical"))
            Config.runMode = Config.RunModeType.EmpiricalTypeProbability;
        else
            MessagePrinter.PrintAndDie("run mode must be specified using input argument -graph or -typeprobability or -empirical");

        inputFileAddress = Defaults.GetValueOrDie(configTable, "text");
        outputFileAddress = Defaults.GetValueOrDie(configTable, "output");

        switch (Config.runMode){
            case Graph:
                inputUnlabeledFileAddress = Defaults.GetValueOrDie(configTable, "textu");
                break;

            case EmpiricalTypeProbability:
                break;

            case TypeProbability:
                labelsInputFile = Defaults.GetValueOrDie(configTable, "labels");
                locationToLabelProbFile = Defaults.GetValueOrDie(configTable, "marginals");
                break;
        }

        if (configTable.containsKey("pos"))
            Config.POSstyleInput = true;

        dictionaryFile = Defaults.GetValueOrDefault(configTable, "dictionary", null);
        featuresFile = Defaults.GetValueOrDefault(configTable, "features", null);
        dictionaryOfClassesFile = Defaults.GetValueOrDefault(configTable, "classdic", null);
        dictionaryOfPrepositionsFile = Defaults.GetValueOrDefault(configTable, "prepositiondic", null);
        String kValue = Defaults.GetValueOrDefault(configTable, "k", null);
        if (kValue != null)
            Config.setKnnDefaultSize(Integer.parseInt(kValue));
        String edgeWeightThreshold = Defaults.GetValueOrDefault(configTable, "threshold", null);
        if (edgeWeightThreshold != null)
            Config.edgeWeightThreshold = Float.parseFloat(edgeWeightThreshold);

        String nodeType = Defaults.GetValueOrDefault(configTable, "node", null);
        if (nodeType!=null){
            if (nodeType.equalsIgnoreCase("word"))
                Config.graphNgramType = GraphBuilderFactory.GraphNgramType.Word;
            else if (nodeType.equalsIgnoreCase("wordclass"))
                Config.graphNgramType = GraphBuilderFactory.GraphNgramType.WordClass;
        }
    }

    /**
     * returns the command without the first character e.g. given "-command" output would be "command"
     * @param arg the command string
     * @return the command without the first "-" character
     */
    private static String getCommandFromArg(String arg) {
        return arg.substring(1, arg.length()).toLowerCase();
    }
}
