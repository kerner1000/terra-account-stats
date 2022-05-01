package com.github.kerner1000.terra;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BuySellMapsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Tests for immutable return map.
     */
    @Test
    void getBuyMap() {
        var priceMaps = new BuySellMaps();
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            priceMaps.getBuyMap().put(1,1);
        });
    }

    /**
     * Tests for immutable return map.
     */
    @Test
    void getSellMap() {
        var priceMaps = new BuySellMaps();
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            priceMaps.getSellMap().put(1,1);
        });
    }

    @Test
    void add01() {
        var priceMaps1 = new BuySellMaps();
        var priceMaps2 = new BuySellMaps();
        priceMaps2.addBuy(1,2);
        priceMaps1.add(priceMaps2);
        assertEquals(1, priceMaps1.getBuyMap().size());
        assertEquals(0, priceMaps1.getSellMap().size());
    }

    @Test
    void add02() {
        var priceMaps1 = new BuySellMaps();
        var priceMaps2 = new BuySellMaps();
        priceMaps2.addSell(1,2);
        priceMaps1.add(priceMaps2);
        assertEquals(1, priceMaps1.getSellMap().size());
        assertEquals(0, priceMaps1.getBuyMap().size());
    }

    @Test
    void addBuys() {
        var priceMaps1 = new BuySellMaps();
        Map<Double, Double> map = new LinkedHashMap<>();
        map.put(5.2, 7.7);
        priceMaps1.addBuys(map);
        assertEquals(1, priceMaps1.getBuyMap().size());
        assertEquals(5, priceMaps1.getBuyMap().keySet().iterator().next());
        assertEquals(8, priceMaps1.getBuyMap().values().iterator().next());
    }

    @Test
    void addSells() {
        var priceMaps1 = new BuySellMaps();
        Map<Double, Double> map = new LinkedHashMap<>();
        map.put(5.2, 7.7);
        priceMaps1.addSells(map);
        assertEquals(1, priceMaps1.getSellMap().size());
        assertEquals(5, priceMaps1.getSellMap().keySet().iterator().next());
        assertEquals(8, priceMaps1.getSellMap().values().iterator().next());
    }

    @Test
    void addBuy() {
        var priceMaps1 = new BuySellMaps();
        priceMaps1.addBuy(1.8, 2.1);
        assertEquals(1, priceMaps1.getBuyMap().size());
        assertEquals(0, priceMaps1.getSellMap().size());
        assertEquals(2, priceMaps1.getBuyMap().keySet().iterator().next());
        assertEquals(2, priceMaps1.getBuyMap().values().iterator().next());
    }

    @Test
    void addSell() {
        var priceMaps1 = new BuySellMaps();
        priceMaps1.addBuy(1.8, 2.1);
        assertEquals(1, priceMaps1.getBuyMap().size());
        assertEquals(0, priceMaps1.getSellMap().size());
        assertEquals(2, priceMaps1.getBuyMap().keySet().iterator().next());
        assertEquals(2, priceMaps1.getBuyMap().values().iterator().next());
    }
}