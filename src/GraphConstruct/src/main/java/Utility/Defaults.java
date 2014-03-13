package main.java.Utility;

import java.util.Hashtable;

public class Defaults {

    public static final String packageExceptionPrefix = "[Graph]-";
    public static final String packageOutputDelimiter = " ";
    public static final String exportFileType = "";
    public static final String exportIndexToNgramPostfix = ".index2ngram" + exportFileType;
    public static final String exportTypeLevelEmpiricalLabelProbabilitiesPostfix = ".seed" + exportFileType;
    public static final String exportGraphPostfix = ".graph" + exportFileType;
    public static final String exportTypeLevelProbabilitiesPostfix = ".type2probability" + exportFileType;
    public static final String exportIndexToLocationPostfix = ".index2location" + exportFileType;
    /*************/

    public static String GetValueOrDie(Hashtable config, String key) {
    if (!config.containsKey(key)) {
      MessagePrinter.PrintAndDie("Must specify " + key + "");
    }
    return ((String) config.get(key));
  }

    public static String GetValueOrDefault(Hashtable config, String key, String defaultVal) {
        String result;
        if (!config.containsKey(key)) {
            result = defaultVal;
        } else {
            result = ((String) config.get(key));
        }
        return result;
    }

  public static String GetValueOrDefault(String valStr, String defaultVal) {
    String res = defaultVal;
    if (valStr != null) {
      res = valStr;
    }
    return (res);
  }

  public static double GetValueOrDefault(String valStr, double defaultVal) {
    double res = defaultVal;
    if (valStr != null) {
      res = Double.parseDouble(valStr);
    }
    return (res);
  }

  public static boolean GetValueOrDefault(String valStr, boolean defaultVal) {
    boolean res = defaultVal;
    if (valStr != null) {
      res = Boolean.parseBoolean(valStr);
    }
    return (res);
  }

  public static int GetValueOrDefault(String valStr, int defaultVal) {
    int res = defaultVal;
    if (valStr != null) {
      res = Integer.parseInt(valStr);
    }
    return (res);
  }

}
