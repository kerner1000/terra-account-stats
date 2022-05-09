package com.github.kerner1000.terra.commons;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class BuySellMapsForCoin {

    private final Map<Coin, BuySellMaps> coinToBuySellMaps = new TreeMap<>();

    public BuySellMapsForCoin(Coin coin, BuySellMaps result) {
        coinToBuySellMaps.put(coin, result);
    }

    public BuySellMapsForCoin() {

    }

    public BuySellMaps get(Coin coin){
        return coinToBuySellMaps.get(coin);
    }

    public void add(BuySellMapsForCoin other){
       for(Map.Entry<Coin, BuySellMaps> entry : other.entrySet()){
            add(entry);
       }
    }

    private void add(Map.Entry<Coin, BuySellMaps> entry) {
        add(entry.getKey(), entry.getValue());
    }

    private void add(Coin coin, BuySellMaps buySellMaps) {
        BuySellMaps value = coinToBuySellMaps.get(coin);
        if(value != null){
            value.add(buySellMaps);
        } else {
            coinToBuySellMaps.put(coin, buySellMaps);
        }
    }

    public Set<Map.Entry<Coin, BuySellMaps>> entrySet() {
        return coinToBuySellMaps.entrySet();
    }
}
