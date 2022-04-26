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
    private final Map<Double, Double> buyMap;
    // receive coin is UST
    private final Map<Double, Double> sellMap;
    public WeightedMeanSwapMaps(Map<Double, Double> buyMap, Map<Double, Double> sellMap) {
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
        return "WeightedMeanSwapMaps:\n" +
                "buyMap:\n" + buyMap.entrySet().stream().map(Object::toString).collect(Collectors.joining("\n")) +
                "\nsellMap:\n" + sellMap.entrySet().stream().map(Object::toString).collect(Collectors.joining("\n"))
                ;
    }
}
