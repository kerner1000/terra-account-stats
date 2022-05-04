package com.github.kerner1000.terra.commons;

import java.util.*;

public class BuySellMaps {

    // offer coin is UST
    private final BuySellMap buyMap;
    // receive coin is UST
    private final BuySellMap sellMap;

    public BuySellMaps(BuySellMap buyMap, BuySellMap sellMap) {
        this();
        addBuys(buyMap);
        addSells(sellMap);
    }

    public BuySellMaps(BuySellMaps buySellMaps) {
        this();
        add(buySellMaps);
    }

    public BuySellMaps() {
        this.buyMap = new BuySellMap();
        this.sellMap = new BuySellMap();
    }

    public BuySellMap getBuyMap() {
        return buyMap;
    }

    public BuySellMap getSellMap() {
        return sellMap;
    }

    public void add(BuySellMaps buySellMaps) {
        buySellMaps.buyMap.getMap().entrySet().forEach(e -> add(e, buyMap));
        buySellMaps.sellMap.getMap().entrySet().forEach(e-> add(e, sellMap));
    }

    public void addBuys(BuySellMap priceMaps) {
        priceMaps.getMap().entrySet().forEach(e -> add(e, buyMap));
    }

    public void addSells(BuySellMap priceMaps) {
        priceMaps.getMap().entrySet().forEach(e -> add(e, sellMap));
    }

    public void addBuy(BuySellMap.Key buyPrice, Number buyAmount){
        add(buyPrice, buyAmount, buyMap);
    }

    public void addBuy(Number buyPrice, Number buyAmount){
        add("dummy-id-"+new Random().nextInt(), buyPrice, buyAmount, buyMap);
    }

    public void addBuy(String id, Number buyPrice, Number buyAmount){
        add(new BuySellMap.Key(id, buyPrice), buyAmount, buyMap);
    }

    public void addSell(BuySellMap.Key sellPrice, Number sellAmount){
        add(sellPrice, sellAmount, sellMap);
    }

    public void addSell(String id, Number sellPrice, Number sellAmount){
        add(new BuySellMap.Key(id, sellPrice), sellAmount, sellMap);
    }

    public void addSell(Number sellPrice, Number sellAmount){
        add(new BuySellMap.Key("dummy-id-"+ new Random().nextInt(), sellPrice), sellAmount, sellMap);
    }

    private static void add(BuySellMap.Key price, Number amount, BuySellMap map){
        map.getMap().put(price, amount);
    }

    private static void add(String txHash, Number price, Number amount, BuySellMap map){
        map.getMap().put(new BuySellMap.Key(txHash, price), amount);
    }

    private static void add(Map.Entry<BuySellMap.Key, ? extends Number> priceAmountMapEntry, BuySellMap map){
        add(priceAmountMapEntry.getKey(), priceAmountMapEntry.getValue(), map);
    }

    @Override
    public String toString() {
        return
                "buy: \n" + buyMap +
                "\nsell:\n" + sellMap;
    }

    private String entryToString(Map.Entry<BuySellMap.Key, Number> mapEntry) {
        return mapEntry.getKey().id() + ": " + mapEntry.getKey().price() + ": " + mapEntry.getValue();
    }
}
