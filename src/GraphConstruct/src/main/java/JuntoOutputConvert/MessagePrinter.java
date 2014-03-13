package main.java.JuntoOutputConvert;

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
        Print("\"-text [fileAddress]\" specifies the address of input junto file.");
        Print("\"-labels [fileAddress]\" specifies the address of labels dictionary file.");
        Print("\"-output [fileAddress]\" output file name.");
    }
}
