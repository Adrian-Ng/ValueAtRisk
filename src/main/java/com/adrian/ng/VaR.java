package com.adrian.ng;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import yahoofinance.histquotes.Interval;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class VaR extends Utils {
    public static String[] strSymbols;
    public static HashMap<String, String> hashParam;
    public static String[] riskMeasures;
    public static HashMap<String, Stock> stockHashMap;
    public static HashMap<String, Integer> hashStockDeltas = new HashMap<>();
    public static HashMap<String, Integer> hashOptionDeltas = new HashMap<>();
    public static double currentPortfolio;
    public static int countAsset;
    public static int size;
    static {
        strSymbols = readTxt("symbol.txt");
        countAsset = strSymbols.length;
        hashParam = readParameters();
        riskMeasures = readTxt("RiskMeasures.txt");
        stockHashMap = getStock();
        readDeltas();
        currentPortfolio = valuePortfolio();
        size = getSize();
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
                hashStockDeltas.put(sym, Integer.parseInt(strDeltas[0]));
                hashOptionDeltas.put(sym, Integer.parseInt(strDeltas[1]));
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

    private static double valuePortfolio() {
        double currentPortfolio = 0.0;
        try {
            for (String sym : strSymbols) {
                Stock stock = stockHashMap.get(sym);
                double currentPrice = stock.getQuote()
                        .getPreviousClose()
                        .doubleValue();
                // add to current portfolio
                currentPortfolio += currentPrice * hashStockDeltas.get(sym);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentPortfolio;
    }

    public static int getSize() {
        Stock stck = stockHashMap.get(strSymbols[0]);
        int size = 0;
        try {
            size = stck.getHistory().size() - 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }


    public static void main(String[] args) {
        HashMap<String, Double> varEstimates = new HashMap<>();

        MeasureFactory measureFactory = new MeasureFactory();
        try {
            for (String str : riskMeasures) {
                System.out.printf("\t%s\n", str);
                RiskMeasure riskMeasure = measureFactory.getMeasureType(str);

                Double VaR = riskMeasure.getVar();
                System.out.printf("\t\tVaR: %f\n", VaR);

                varEstimates.put(str, VaR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
