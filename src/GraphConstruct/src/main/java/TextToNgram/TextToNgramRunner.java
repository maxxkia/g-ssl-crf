package main.java.TextToNgram;


/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class TextToNgramRunner {

    /**
     * This method takes a text file as input and creates the associated n-gram file
     * @param args first argument declares the address of input file, second argument declares the address of output
     *             file and the third argument (int) indicates size of n-grams. e.g. args[2] = 3 means 3-grams are
     *             needed.
     */
    public static void main(String[] args){
        String inputFileAddress,outputFileAddress;
        int ngramSize = Utils.packageDefaultNgramSize;

        //args[0] input file
        //args[1] output file
        //args[2] n-gram size, default is 3 which means 3-grams are produced
        if(args.length <= 1)
            throw new IllegalArgumentException(Utils.packageExceptionPrefix + "Error: input & output files should be declared as the first two parameters.");
        if(args.length <= 2)
            System.out.println(Utils.packageExceptionPrefix + "Warning: third argument is not declared. "
                    + Utils.packageDefaultNgramSize + "-grams are produced as default");
        else
            ngramSize = Integer.parseInt(args[2]);

        inputFileAddress = args[0];
        outputFileAddress = args[1];

        //create n-gram for each word
        NgramUtility processor = new NgramUtility();
        processor.CreateNgramFileFromTextFile(inputFileAddress, outputFileAddress, ngramSize);
    }
}
