package main.java.Text;

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
        System.out.println();
        System.out.println("Input arguments format:");
        System.out.println("\"-text [fileAddress]\" specifies the address of input text file. Input file should be in" +
                " standard CRF input format");
        System.out.println("\"-output [fileAddress]\" output file name format. Indexed CRF file, word class dictionary" +
                " file and label dictionary file will be save in files named as [fileAddress][.predefined postfix]");
    }
}
