package main.java.Utility;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class DataTypeManipulator {
    public static float[] initializeFloatArray(float[] inputArray){
        for(int index=0; index<inputArray.length ; ++index)
            inputArray[index] = 0;
        return inputArray;
    }

    public static float[] newInitializedFloatArray(int sizeOfArray){
        float[] result = new float[sizeOfArray];
        result = DataTypeManipulator.initializeFloatArray(result);
        return result;
    }

    public static boolean[] newInitializedBooleanArray(int sizeOfArray){
        boolean[] result = new boolean[sizeOfArray];
        result = DataTypeManipulator.initializeBooleanArray(result);
        return result;
    }

    private static boolean[] initializeBooleanArray(boolean[] inputArray) {
        for(int index=0; index<inputArray.length ; ++index)
            inputArray[index] = false;
        return inputArray;
    }
}
