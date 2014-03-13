package main.java.Utility;

import java.io.*;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class TextFileInput {
    private BufferedReader br;
    private String inputFile;

    public TextFileInput(String inputFileAddress){
        inputFile = inputFileAddress;
        try {
            InputStream ois = new FileInputStream(inputFile);
            br = new BufferedReader(new InputStreamReader(ois));
        }catch(FileNotFoundException ex){
            System.out.println("Error: there was an error in opening input file");
            System.out.println("input file: " + inputFile);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public String readLine(){
        String result = "";
        try{
            result = br.readLine();
        }catch (IOException ex){
            System.out.println("Error: there was an error in reading input file contents");
            System.out.println("input file: " + inputFile);
            ex.printStackTrace();
            System.exit(1);
        }
        return result;
    }

    public void close(){
        try{
            br.close();
        }catch(IOException ex){
            System.out.println("Error: there was an error when closing the input file");
            System.out.println("input file: " + inputFile);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    protected void finalize(){
        this.close();
        try{
            super.finalize();
        } catch (Throwable ex){
            System.out.println("Error: unexpected error when finalizing an object of class TextFileInput");
            ex.printStackTrace();
        }
    }
}
