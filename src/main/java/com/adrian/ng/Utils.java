package com.adrian.ng;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static java.math.BigDecimal.ROUND_HALF_UP;

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
