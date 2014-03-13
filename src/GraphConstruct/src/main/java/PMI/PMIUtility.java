package main.java.PMI;

/**
 * Copyright: Masoud Kiaeeha, Mohammad Aliannejadi
 * This work is licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-nc/4.0/.
 */
public class PMIUtility {
    public static double calculatePMI(int N, int countPair, int countFirstMember, int countSecondMember){
        return (Math.log(N * countPair)) -
                (Math.log(countFirstMember * countSecondMember));
    }
}