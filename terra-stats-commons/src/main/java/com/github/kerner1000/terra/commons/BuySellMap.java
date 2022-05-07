package com.github.kerner1000.terra.commons;

import org.openapitools.model.SwapEntry;
import org.openapitools.model.Swaps;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BuySellMap {

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

    private final Map<Key, Number> map;

    public BuySellMap(Swaps swaps) {
        this.map = new TreeMap<>(Collections.reverseOrder());
        for(SwapEntry s : swaps.getSwaps()){
            add(s.getId(), s.getPrice(), s.getAmount());
        }
    }

    public BuySellMap() {
        this.map = new TreeMap<>(Collections.reverseOrder());
    }

    public void add(Key key, Number value) {
        map.put(key, value);
    }

    public void add(String txHash, Number price, Number amount){
        add(new Key(txHash, price), amount);
    }

    public void add(Number price, Number amount){
        add("dummy-id-"+ new Random().nextInt(), price, amount);
    }

    public Map<Key, Number> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return map.entrySet().stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}
