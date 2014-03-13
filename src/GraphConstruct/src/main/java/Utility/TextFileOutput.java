package main.java.Utility;

import java.io.*;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class TextFileOutput {
    private BufferedWriter bw;
    private String outputFile;

    public TextFileOutput(String outputFileAddress){
        outputFile = outputFileAddress;
        try {
            OutputStream ois = new FileOutputStream(outputFile);
            bw = new BufferedWriter(new OutputStreamWriter(ois));
        }catch(FileNotFoundException ex){
            System.out.println("Error: there was an error in creating output file");
            System.out.println("outputfile: " + outputFile);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void write(String data){
        try{
            bw.write(data);
        }catch(IOException ex){
            System.out.println("Error: there was an error in writing to output file");
            System.out.println("output file: " + outputFile);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void writeLine(String line){
        this.write(line + Config.outputNewLineCharacter);
    }

    public void nextLine(){
        this.writeLine("");
    }

    public void close(){
        try{
            bw.flush();
            //bw.close();
        }catch(IOException ex){
            System.out.println("Error: there was an error when closing the output file");
            System.out.println("output file: " + outputFile);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    protected void finalize(){
        this.close();
        try{
            super.finalize();
        } catch (Throwable ex){
            System.out.println("Error: unexpected error when finalizing an object of class TextFileOutput");
            ex.printStackTrace();
        }
    }
}
