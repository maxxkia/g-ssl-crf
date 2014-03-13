package main.java.Utility;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class RuntimeAnalyzer {
    private Long startTime;
    public String start(String message){
        startTime = System.nanoTime();
        message += " task started.";
        System.out.println(message);
        return message;
    }

    public String finish(String message){
        Long endTime = System.nanoTime();
        Long duration = Math.abs(endTime - startTime);
        int sec = (int)(duration / 1000000000) % 60;
        int min = ((int)(duration / 1000000000) / 60);
        message += " task finished in " + min + " min, " + sec + " second(s)";
        System.out.println(message);
        return message;
    }

    public void mileStone(String message){
        finish(message);
    }
}
