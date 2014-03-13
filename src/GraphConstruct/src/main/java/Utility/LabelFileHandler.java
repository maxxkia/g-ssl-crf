package main.java.Utility;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class LabelFileHandler {
    public static int countLabels(String fileAddress){
        int count = 0;
        String line;
        TextFileInput fileInput = new TextFileInput(fileAddress);
        while ((line =fileInput.readLine()) != null){
            if(line.trim().isEmpty())
                continue;
            ++count;
        }

        fileInput.close();
        return count;
    }
}
