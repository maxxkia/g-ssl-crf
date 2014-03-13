package main.java.Utility;

import main.java.Graph.GraphStructure.Location;
import main.java.Graph.GraphStructure.LocationLabelProbability;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class LocationToLabelFileHandler {
    TextFileInput fileInput;
    int currentSequence, currentPosition;
    LocationLabelProbability lastLocation;

    /**
     * Create a new instance of LocationToLabel file reader based on a given input file
     * @param fileAddress input file to work on
     */
    public LocationToLabelFileHandler(String fileAddress){
        fileInput = new TextFileInput(fileAddress);
        currentSequence = currentPosition = 0;
        lastLocation = null;
    }

    /**
     * Use this method to read all label probabilities associated with a specific location.</br>
     * note:currently this method can only be used to read probabilities in forward reading order. This means if you're
     * trying to read probabilities for location Y while you have previously read information for location X (and Y was
     * seen before X), you are not able to read information for location Y.
     * @param sequence identifier of the specified sequence (zero-based index)
     * @param position identifier of the specified position occurring in a sequence (zero-based index)
     * @param labelCount number of labels available
     * @return an array of float[labelCount] containing all label probabilities associated with the specified location
     */
    public float[] getLabelProbabilitiesOf(int sequence, int position, int labelCount){
        if(isLocationPassed(sequence, position))
            return null;

        String line;
        float[] probabilityArray = DataTypeManipulator.newInitializedFloatArray(labelCount);
        boolean locationReached = false;
        if(currentSequence == sequence && currentPosition == position){
            locationReached = true;
            //use last data read in previous call of the method
            if(this.lastLocation != null){
                probabilityArray[this.lastLocation.getLabelId()] = this.lastLocation.getLabelProbability();
            }
        }

        while ((line = fileInput.readLine()) != null){
            lastLocation = Location.extractLocationFromString(line);
            if(lastLocation != null){
                if(!locationReached && isLocationMatch(sequence, position)){
                    locationReached = true;
                }

                if(locationReached){
                    if(!isLocationMatch(sequence,position)){
                        currentSequence = this.lastLocation.getSequence();
                        currentPosition = this.lastLocation.getPosition();
                        break;
                    }
                    probabilityArray[lastLocation.getLabelId()] = lastLocation.getLabelProbability();
                }
            }
        }

        return probabilityArray;
    }

    /**
     * given a location determines if the given location was passed during the previous iteration
     * @param sequence identifier of the specified sequence (zero-based index)
     * @param position identifier of the specified position in a given sequence (zero-based index)
     * @return true if the given location was passed in previous iterations
     */
    private boolean isLocationPassed(int sequence, int position) {
        return sequence < this.currentSequence || (sequence==this.currentSequence && position<this.currentPosition);
    }

    /**
     * given a location determines if the given location matches the current location or not
     * @param sequence identifier of the specified sequence (zero-based index)
     * @param position identifier of the specified position in a given sequence (zero-based index)
     * @return true if the current location is the same as given location
     */
    private boolean isLocationMatch(int sequence, int position) {
        return lastLocation.getSequence() == sequence && lastLocation.getPosition() == position;
    }

    /**
     * use this method to close the input file after work is finished
     */
    public void closeFile(){
        fileInput.close();
    }

    protected void finalize(){
        this.closeFile();
    }
}
