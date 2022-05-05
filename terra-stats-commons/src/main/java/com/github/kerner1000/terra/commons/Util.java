package com.github.kerner1000.terra.commons;

import org.openapitools.model.SwapEntry;

import java.util.Collection;
import java.util.Map;

public class Util {

    public static double weightedMean(Collection<? extends SwapEntry> swapEntries){
        double num = 0;
        double denom = 0;
        for (SwapEntry entry : swapEntries) {
            num += entry.getPrice().doubleValue() * entry.getAmount().doubleValue();
            denom += entry.getAmount().doubleValue();
        }

        return num / denom;
    }

    public static double weightedMean(Map<Number, Number> map) {

        double num = 0;
        double denom = 0;
        for (Map.Entry<Number, Number> entry : map.entrySet()) {
            num += entry.getKey().doubleValue() * entry.getValue().doubleValue();
            denom += entry.getValue().doubleValue();
        }

        return num / denom;

    }

    public static boolean doubleEquals(Number n1, Number n2){
        double epsilon = 0.000001d;
        return Math.abs(n1.doubleValue() - n2.doubleValue()) < epsilon;
    }
}
