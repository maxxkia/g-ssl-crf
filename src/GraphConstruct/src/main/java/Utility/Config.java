package main.java.Utility;

import main.java.Graph.Builder.GraphBuilderFactory;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */

public class Config {
    public static final int graphBuilderThreadCount = 8;

    public static final String outputNewLineCharacter = "\r\n";
    public static final String outputDelimiter = " ";
    public static final String defaultLogFileAddress = "graphConstruct.log";
    public static final Boolean pmiSmoothing = true;
    public static final float pmiSmoothingEpsilon = (float) 0.0000001;
    public static final String packageOutputDummyValue = "0";
    private static int knnDefaultSize = 5;
    public static boolean POSstyleInput = false;

    public static RunModeType runMode = RunModeType.Graph;
    public static GraphBuilderFactory.GraphNgramType graphNgramType = GraphBuilderFactory.GraphNgramType.WordClass;
    public static float edgeWeightThreshold = 0;

    public static int getKnnDefaultSize() {
        return knnDefaultSize;
    }

    public static void setKnnDefaultSize(int knnDefaultSize) {
        if (knnDefaultSize <= 0)
            MessagePrinter.PrintAndDie("K value (as in KNN-graph) must be a positive integer! K=" + knnDefaultSize);
        else
            Config.knnDefaultSize = knnDefaultSize;
    }

    public enum RunModeType{
        Graph, TypeProbability, EmpiricalTypeProbability
    }
}
