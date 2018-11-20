package com.adrian.ng;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class Utils {

    public static BigDecimal[][] transposeBigDecimal(BigDecimal[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        BigDecimal[][] transposedMatrix = new BigDecimal[n][m];

        for (int x = 0; x < n; x++)
            for (int y = 0; y < m; y++)
                transposedMatrix[x][y] = matrix[y][x];
        return transposedMatrix;
    }

    public static BigDecimal getSquareNumber(BigDecimal squareNumber) {
        ////https://stackoverflow.com/a/19743026/10526321
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(squareNumber.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = squareNumber.divide(x0, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(new BigDecimal(2), ROUND_HALF_UP);
        }
        return x1;
    }

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

    public static HashMap<String, Double> hashMapInPutValues(String[] inputValues) {
        int length = inputValues.length;
        HashMap<String, Double> hashMap = new HashMap<>();
        for (int i = 0; i < length; i++) {
            hashMap.put(inputValues[i].split(",")[0]                        // key
                    , Double.parseDouble(inputValues[i].split(",")[1]));  // value
        }
        return hashMap;
    }

}
