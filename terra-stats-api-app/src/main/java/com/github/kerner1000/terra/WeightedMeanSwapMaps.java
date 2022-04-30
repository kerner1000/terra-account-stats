package com.github.kerner1000.terra;

import lombok.Data;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Data
public class WeightedMeanSwapMaps {

    // offer coin is UST
    private final Map<Integer, Integer> buyMap;
    // receive coin is UST
    private final Map<Integer, Integer> sellMap;
    public WeightedMeanSwapMaps(Map<Integer, Integer> buyMap, Map<Integer, Integer> sellMap) {
        this.buyMap = buyMap;
        this.sellMap = sellMap;
    }

    public WeightedMeanSwapMaps() {
        this.buyMap = new TreeMap<>(Collections.reverseOrder());
        this.sellMap = new TreeMap<>(Collections.reverseOrder());
    }

    public void add(WeightedMeanSwapMaps weightedMeanSwapMaps) {
        this.buyMap.putAll(weightedMeanSwapMaps.buyMap);
        this.sellMap.putAll(weightedMeanSwapMaps.sellMap);
    }

    @Override
    public String toString() {
        return
                "buy: price, amount:\n" + buyMap.entrySet().stream().map(this::map).collect(Collectors.joining("\n")) +
                "\nsell: price,amount:\n" + sellMap.entrySet().stream().map(this::map).collect(Collectors.joining("\n"))
                ;
    }

    private String map(Map.Entry<Integer, Integer> doubleDoubleEntry) {
        String price = String.format("%,10d",doubleDoubleEntry.getKey());
        String amount = String.format("%,7d",doubleDoubleEntry.getValue());
        return price + ": " + amount;
    }
}
