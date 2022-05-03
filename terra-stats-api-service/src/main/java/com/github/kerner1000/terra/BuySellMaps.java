package com.github.kerner1000.terra;

import java.util.*;
import java.util.stream.Collectors;

public class BuySellMaps {

    public record Key(String id, Number price) implements Comparable<Key> {

        @Override
            public int compareTo(Key o) {
                return Double.compare(this.price.doubleValue(), o.price.doubleValue());
            }

        @Override
            public String toString() {
                return "[" +
                        "id=" + id + ", " +
                        "price=" + price + ']';
            }
        }

    // offer coin is UST
    private final Map<Key, Number> buyMap;
    // receive coin is UST
    private final Map<Key, Number> sellMap;

    public BuySellMaps(Map<Key, Number> buyMap, Map<Key, Number> sellMap) {
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

    public Map<Key, Number> getBuyMap() {
        return Collections.unmodifiableMap(buyMap);
    }

    public Map<Key, Number> getSellMap() {
        return Collections.unmodifiableMap(sellMap);
    }

    public void add(BuySellMaps buySellMaps) {
        buySellMaps.buyMap.entrySet().forEach(e -> add(e, buyMap));
        buySellMaps.sellMap.entrySet().forEach(e-> add(e, sellMap));
    }

    public void addBuys(Map<Key, ? extends Number> priceMaps) {
        priceMaps.entrySet().forEach(e -> add(e, buyMap));
    }

    public void addSells(Map<Key, ? extends Number> priceMaps) {
        priceMaps.entrySet().forEach(e -> add(e, sellMap));
    }

    public void addBuy(Key buyPrice, Number buyAmount){
        add(buyPrice, buyAmount, buyMap);
    }

    public void addBuy(Number buyPrice, Number buyAmount){
        add("dummy-id-"+new Random().nextInt(), buyPrice, buyAmount, buyMap);
    }

    public void addBuy(String id, Number buyPrice, Number buyAmount){
        add(new Key(id, buyPrice), buyAmount, buyMap);
    }

    public void addSell(Key sellPrice, Number sellAmount){
        add(sellPrice, sellAmount, sellMap);
    }

    public void addSell(String id, Number sellPrice, Number sellAmount){
        add(new Key(id, sellPrice), sellAmount, sellMap);
    }

    public void addSell(Number sellPrice, Number sellAmount){
        add(new Key("dummy-id-"+ new Random().nextInt(), sellPrice), sellAmount, sellMap);
    }

    private static void add(Key price, Number amount, Map<Key, Number> map){
        map.put(price, amount);
    }

    private static void add(String txHash, Number price, Number amount, Map<Key, Number> map){
        map.put(new Key(txHash, price), amount);
    }

    private static void add(Map.Entry<Key, ? extends Number> priceAmountMapEntry, Map<Key, Number> map){
        add(priceAmountMapEntry.getKey(), priceAmountMapEntry.getValue(), map);
    }

    @Override
    public String toString() {
        return
                "buy: id, price, amount:\n" + buyMap.entrySet().stream().map(this::entryToString).collect(Collectors.joining("\n")) +
                "\nsell: id, price, amount:\n" + sellMap.entrySet().stream().map(this::entryToString).collect(Collectors.joining("\n"))
                ;
    }

    private String entryToString(Map.Entry<Key, Number> mapEntry) {
        return mapEntry.getKey().id + ": " + mapEntry.getKey().price + ": " + mapEntry.getValue();
    }
}
