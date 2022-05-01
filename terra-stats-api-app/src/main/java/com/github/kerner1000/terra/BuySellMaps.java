package com.github.kerner1000.terra;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BuySellMaps {

    // offer coin is UST
    private final Map<Integer, Integer> buyMap;
    // receive coin is UST
    private final Map<Integer, Integer> sellMap;

    public BuySellMaps(Map<Integer, Integer> buyMap, Map<Integer, Integer> sellMap) {
        this();
        addBuys(buyMap);
        addSells(sellMap);
    }

    public BuySellMaps(BuySellMaps buySellMaps) {
        this();
        add(buySellMaps);
    }

    public BuySellMaps() {
        this.buyMap = new TreeMap<>(Collections.reverseOrder());
        this.sellMap = new TreeMap<>(Collections.reverseOrder());
    }

    public Map<Integer, Integer> getBuyMap() {
        return Collections.unmodifiableMap(buyMap);
    }

    public Map<Integer, Integer> getSellMap() {
        return Collections.unmodifiableMap(sellMap);
    }

    public void add(BuySellMaps buySellMaps) {
        buySellMaps.buyMap.entrySet().forEach(e -> add(e, buyMap));
        buySellMaps.sellMap.entrySet().forEach(e-> add(e, sellMap));
    }

    public void addBuys(Map<? extends Number, ? extends Number> priceMaps) {
        priceMaps.entrySet().forEach(e -> add(e, buyMap));
    }

    public void addSells(Map<? extends Number, ? extends Number> priceMaps) {
        priceMaps.entrySet().forEach(e -> add(e, sellMap));
    }

    public void addBuy(Number buyPrice, Number buyAmount){
        add(buyPrice, buyAmount, buyMap);
    }

    public void addSell(Number sellPrice, Number sellAmount){
        add(sellPrice, sellAmount, sellMap);
    }

    private static void add(Number price, Number amount, Map<Integer, Integer> map){
        Integer key = Math.toIntExact(Math.round(price.doubleValue()));
        Integer value = map.getOrDefault(key, 0);
        value = Math.addExact(value, Math.toIntExact(Math.round(amount.doubleValue())));
        map.put(key, value);
    }

    private static void add(Map.Entry<? extends Number, ? extends Number> priceAmountMapEntry, Map<Integer, Integer> map){
        add(priceAmountMapEntry.getKey(), priceAmountMapEntry.getValue(), map);
    }

    @Override
    public String toString() {
        return
                "buy: price, amount:\n" + buyMap.entrySet().stream().map(this::map).collect(Collectors.joining("\n")) +
                "\nsell: price,amount:\n" + sellMap.entrySet().stream().map(this::map).collect(Collectors.joining("\n"))
                ;
    }

    private String map(Map.Entry<Integer, Integer> mapEntry) {
        String price = String.format("%,10d",mapEntry.getKey());
        String amount = String.format("%,7d",mapEntry.getValue());
        return price + ": " + amount;
    }
}
