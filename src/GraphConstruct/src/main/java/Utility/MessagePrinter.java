package main.java.Utility;

public class MessagePrinter {

    public static void Print (String msg) {
        System.out.println(msg);
    }

    public static void PrintAndDie(String msg) {
        Print(msg);
        printHelpMessage();
        System.exit(1);
    }

    private static void printHelpMessage() {
        Print("");
        Print("Input arguments format:");
        Print("");
        Print("Specify the run mode using -graph or -typeprobability or -marginals . In graph mode the complete graph is created " +
                "and .graph file is produced. In typeprobability mode only graph nodes are loaded and only the .type2probability file is " +
                "produced. In marginals mode only graph nodes are loaded and .seed file is produced.");
        Print("");
        Print("\"-text [fileAddress]\" specifies the address of input text file");
        Print("");
        Print("\"-textU [fileAddress]\" specifies the address of unlabeled input text file. this command should " +
                "only be specified when graph mode is selected.");
        Print("");
        Print("\"-output [fileAddress]\" output file name format, information on graph is stored in files" +
                " starting with this name.");
        Print("");
        Print("\"-labels [fileAddress]\" labels input file. Should be specified when typeprobability run mode is selected.");
        Print("");
        Print("\"-marginals [fileAddress]\" locationToLabelProbability input file. Should be specified when typeprobability run mode is selected.");
        Print("");
        Print("\"-dictionary [fileAddress]\" optional dictionary input file of labeled words.");
        Print("");
        Print("\"-features [fileAddress]\" optional features input file. Features specified in this file will" +
                " be used to extract features from each ngram when calculating main.java.PMI values. If this file is not specified " +
                "default features will be used.");
        Print("");
        Print("\"-node [word | wordclass]\" optional graph node type input. If word is specified, nodes of graph are created based on words and " +
                "if wordclass is specified nodes are created based on word classes. Default option is: " + Config.graphNgramType);
        Print("");
        Print("\"-k [positive integer]\" optional K value input.");
        Print("");
        Print("\"-threshold [positive float]\" optional threshold value for edge weight. Edges having weight of less than " +
                "the specified value, will be filtered out (i.e. will not be added to graph)");
        Print("");
        Print("\"-classdic [fileAddress]\" optional dictionary input file. This string specifies address of the classes" +
                " dictionary file which must be given when isClass feature is defined in the features file.");
        Print("");
        Print("\"-prepositiondic [fileAddress]\" optional dictionary input file. This string specifies address of the prepositions" +
                " dictionary file which must be given when isPreposition feature is defined in the features file.");
        Print("");
    }
}
