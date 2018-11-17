package com.adrian.ng;

import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PercentageChange {



    public static ArrayList<BigDecimal> percentageChange(List<HistoricalQuote> historicalQuotes) {

        Iterator<HistoricalQuote> iterator = historicalQuotes.iterator();

        ArrayList<BigDecimal> percentageChange = new ArrayList<>();
        BigDecimal a = iterator.next().getClose();
        do{
            //System.out.println(a);

            BigDecimal b = iterator.next().getClose();
            //System.out.println(b);
            BigDecimal PriceDiff = a
                                    .subtract(b)
                                    .divide(b, RoundingMode.HALF_UP)
            ;
            //System.out.println(PriceDiff);
            percentageChange.add(PriceDiff);
            a = b;
        }
        while (iterator.hasNext());

        return percentageChange;
    }


}
