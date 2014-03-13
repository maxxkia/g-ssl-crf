package main.java.Utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class Logger {
    private BufferedWriter output;
    private String fileAddress;
    private Calendar calendar;
    private SimpleDateFormat dateTimeFormatter;
    private RuntimeAnalyzer runtimeAnalyzer;

    public Logger(String logFileAddress){
        fileAddress = logFileAddress;
        try {
            output = new BufferedWriter(new FileWriter(fileAddress, true));
            this.writeLine();
        } catch (IOException e) {
            System.out.println("Error: an error occurred while creating the log file");
            System.out.println("output file: " + fileAddress);
            e.printStackTrace();
        }

        calendar = Calendar.getInstance();
        dateTimeFormatter = new SimpleDateFormat("YYYY/MM/DD HH:mm:ss");
        runtimeAnalyzer = new RuntimeAnalyzer();
    }

    public void writeLine(String message){
        calendar = Calendar.getInstance();
        this.write(dateTimeFormatter.format(calendar.getTime()) + " - " + message + Config.outputNewLineCharacter);
    }

    public void writeLine(){
        this.write(Config.outputNewLineCharacter);
    }

    private void write(String message){
        try{
            output.write(message);
        }catch(IOException ex){
            System.out.println("Error: there was an error in writing to output file");
            System.out.println("output file: " + fileAddress);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public RuntimeAnalyzer taskStarted(String taskName){
        RuntimeAnalyzer ra = new RuntimeAnalyzer();
        this.writeLine(ra.start(taskName));
        return ra;
    }

    public void taskFinished(RuntimeAnalyzer ra, String taskName){
        this.writeLine(ra.finish(taskName));
    }

    public void close(){
        try{
            output.flush();
            //bw.close();
        }catch(IOException ex){
            System.out.println("Error: there was an error when closing the output file");
            System.out.println("output file: " + fileAddress);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void finalize(){
        this.close();
        try{
            super.finalize();
        } catch (Throwable ex){
            System.out.println("Error: unexpected error when finalizing an object of class Logger");
            ex.printStackTrace();
        }

    }
}
