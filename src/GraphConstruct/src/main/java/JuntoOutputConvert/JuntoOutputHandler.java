package main.java.JuntoOutputConvert;

import main.java.Utility.Config;
import main.java.Utility.LabelFileHandler;
import main.java.Utility.TextFileInput;
import main.java.Utility.TextFileOutput;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class JuntoOutputHandler {
    public void convertJuntoOutputToViterbiFormat(String juntoFileAddress,
                                                  String labelsFileAddress,
                                                  String outputFileAddress){
        TextFileInput fileInput = new TextFileInput(juntoFileAddress);
        TextFileOutput fileOutput = new TextFileOutput(outputFileAddress);

        String line;
        String nodeId, labelsStream, outputStream;
        String labelId, labelValue;

        int labelCount = LabelFileHandler.countLabels(labelsFileAddress);
        float[] labels = new float[labelCount];
        String[] tokens;

        while ((line = fileInput.readLine()) != null){
            tokens = line.split("\\t");
            if (tokens.length != 6)
                continue;
            //1 & 4 are important
            labels = this.initializeLabelsArray(labels);

            nodeId = tokens[0];
            labelsStream = tokens[3];

            tokens = labelsStream.split("\\s");
            int index = 0;
            while (index < tokens.length){
                labelId = tokens[index++];
                labelValue = tokens[index++];
                if (!labelId.equalsIgnoreCase("__DUMMY__")){
                    labels[Integer.parseInt(labelId)] = Float.parseFloat(labelValue);
                }
            }

            outputStream = "";
            for (float label : labels)
                outputStream += nodeId + "\t" + label + Config.outputNewLineCharacter;
            fileOutput.write(outputStream);
        }

        fileOutput.close();
        fileInput.close();
    }

    private float[] initializeLabelsArray(float[] labels) {
        for (int i=0; i<labels.length ; ++i)
            labels[i] = 0;
        return labels;
    }
}
