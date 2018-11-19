package com.adrian.ng;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import yahoofinance.histquotes.Interval;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class VaR extends Utils {
    public static String[] strSymbols;
    public static HashMap<String, String> hashParam;
    public static String[] riskMeasures;
    public static HashMap<String, Stock> stockHashMap;
    public static HashMap<String, Integer> hashStockDeltas = new HashMap<>();
    public static HashMap<String, Integer> hashOptionDeltas = new HashMap<>();

    static {
        strSymbols = readTxt("symbol.txt");
        hashParam = readParameters();
        riskMeasures = readTxt("RiskMeasures.txt");
        stockHashMap = getStock();
        readDeltas();
    }

    private static HashMap<String, String> readParameters() {
        HashMap<String, String> hashParam = new HashMap<>();
        String[] strParam = readTxt("parameters.txt");

        for (String str : strParam)
            hashParam.put(str.split(",")[0], str.split(",")[1]);
        return hashParam;
    }

    private static void readDeltas() {
        for (String sym : strSymbols) {
            StringBuilder stringBuilder = new StringBuilder();
            String filename = stringBuilder
                    .append("Deltas")
                    .append(File.separator)
                    .append(sym)
                    .append(".txt")
                    .toString();

            String[] strDeltas = readTxt(filename);

            try {
                hashStockDeltas.put(sym,    Integer.parseInt(strDeltas[0]));
                hashOptionDeltas.put(sym,   Integer.parseInt(strDeltas[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static HashMap<String, Stock> getStock() {
        int years = Integer.parseInt(hashParam.get("HistoricalYears"));

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -years); // from 5 years ago
        HashMap<String, Stock> stockArrayList = new HashMap<>();

        try {
            for (String sym : strSymbols) {
                Stock stock = YahooFinance
                        .get(sym, from, to, Interval.DAILY);
                stockArrayList.put(sym, stock);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockArrayList;
    }


    public static void main(String[] args) {
        /*Iterator it = hashParam.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }*/
        MeasureFactory measureFactory = new MeasureFactory();
        try {
            for (String str : riskMeasures) {
                System.out.printf("\t%s\n", str);
                RiskMeasure riskMeasure = measureFactory.getMeasureType(str);

                BigDecimal VaR = riskMeasure.getVar();
                System.out.printf("\t\tVaR: %f\n", VaR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
