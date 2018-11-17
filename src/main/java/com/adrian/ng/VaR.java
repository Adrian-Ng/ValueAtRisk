package com.adrian.ng;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.*;

public class VaR extends Utils {


    public static HashMap<String, String> hashParam;
    public static String[] riskMeasures;
    public static ArrayList<Stock> stockArrayList;

    static {
        hashParam = readParameters();
        riskMeasures = readTxt("RiskMeasures.txt");
        stockArrayList = getStock();
    }

    private static HashMap<String, String> readParameters() {
        HashMap<String, String> hashParam = new HashMap<>();
        String[] strParam = readTxt("parameters.txt");

        for (String str : strParam) {
            //System.out.println(str.split(",")[0]);

            hashParam.put(str.split(",")[0], str.split(",")[1]);

        }
        return hashParam;
    }

    public static ArrayList<Stock> getStock() {

        int years = Integer.parseInt(hashParam.get("HistoricalYears"));

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -years); // from 5 years ago

        String[] symbol = readTxt("symbol.txt");
        ArrayList<Stock> stockArrayList = new ArrayList<>();

        try {
            for (String sym : symbol) {
                Stock stock = YahooFinance
                        .get(sym, from, to, Interval.DAILY);
                stockArrayList.add(stock);
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




        System.out.println(hashParam.get("HistoricalYears"));
        MeasureFactory measureFactory = new MeasureFactory();

        try {
            for (String str : riskMeasures) {
                System.out.printf("\t%s\n", str);
                RiskMeasure riskMeasure = measureFactory.getMeasureType(str);

                riskMeasure.getVar();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //String[] symbol = Utils.readTxt("symbol.txt") ;





        /*for (String sym : symbol){
           stockList.add(getStock(sym)) ;
        }*/


    }

}
