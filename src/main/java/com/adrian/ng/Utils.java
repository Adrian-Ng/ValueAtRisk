package com.adrian.ng;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Utils {

    public static String[] readTxt(String filename) {
        try {
            Scanner inFile = new Scanner(new FileReader(filename));
            ArrayList<String> stringArrayList = new ArrayList<>();
            while (inFile.hasNextLine()) {
                String strLine = inFile.nextLine();
                stringArrayList.add(strLine);
            }
            String[] stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
            return stringArray;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, Double> hashMapInPutValues(String[] inputValues){
        int length = inputValues.length;
        HashMap<String, Double> hashMap = new HashMap<>();
        for(int i = 0; i< length; i++){
            hashMap.put(    inputValues[i].split(",")[0]                        // key
                    ,   Double.parseDouble(inputValues[i].split(",")[1]));  // value
        }
        return hashMap;
    }

}
