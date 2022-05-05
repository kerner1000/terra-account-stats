package com.github.kerner1000.terra.commons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinnedBuySellMapsTest {

    BinnedBuySellMaps binned;

    List<BinnedBuySellMaps.Bin<Double>> bins;

    @BeforeEach
    void setUp() {
        bins = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    public void testSimpleFiltering01() {
        bins.add(new BinnedBuySellMaps.Bin<Double>(2d, 3.000001));
        binned = new BinnedBuySellMaps(bins);
        var map = new BuySellMap();
        map.add(1, 100);
        map.add(2, 200);
        map.add(3, 300);
        map.add(4, 400);
        binned.add(map);
        assertEquals(2, binned.getValuesCount());
        assertEquals(1, binned.getBinCnt());
    }

    @Test
    public void testSimpleFiltering02() {
        bins.add(new BinnedBuySellMaps.Bin<Double>(2d, 3d));
        binned = new BinnedBuySellMaps(bins);
        var map = new BuySellMap();
        map.add(1, 100);
        map.add(2, 200);
        map.add(3, 300);
        map.add(4, 400);
        binned.add(map);
        System.out.println(binned);
        // upper bound excluding
        assertEquals(1, binned.getValuesCount());
        assertEquals(1, binned.getBinCnt());
    }

    @Test
    public void testTwoBins() {
        bins.add(new BinnedBuySellMaps.Bin<Double>(2d, 3d));
        bins.add(new BinnedBuySellMaps.Bin<Double>(3d, 5d));
        binned = new BinnedBuySellMaps(bins);
        var map = new BuySellMap();
        map.add(1, 100);
        map.add(2, 200);
        map.add(3, 300);
        map.add(4, 400);
        binned.add(map);
        System.out.println(binned);
        assertEquals(3, binned.getValuesCount());
        assertEquals(2, binned.getBinCnt());
    }

    @Test
    public void testBinFactoryFixedBinCnt01() {

        var min = 0;
        var max = 10;
        var binCnt = 2;
        var result = BinnedBuySellMaps.BinFactory.buildWithFixBinCount(min, max, binCnt);
        assertEquals(2, result.size());

    }

    @Test
    public void testBinFactoryFixedBinSize01() {

        var min = 0;
        var max = 10;
        var binSize = 2;
        var result = BinnedBuySellMaps.BinFactory.buildWithFixBinSize(min, max, binSize);
//        System.out.println(result.stream().map(Objects::toString).collect(Collectors.joining("\n")));
        // one extra bin because of excluding upper bound
        assertEquals(6, result.size());

    }

    @Test
    public void testBinFactoryFixedBinCnt02() {

        var min = 2;
        var max = 12;
        var binCnt = 3;
        var result = BinnedBuySellMaps.BinFactory.buildWithFixBinCount(min, max, binCnt);
        assertEquals(3, result.size());

    }

    @Test
    public void testBinFactoryFixedBinSize02() {

        var min = 2;
        var max = 12;
        var binCnt = 3;
        var result = BinnedBuySellMaps.BinFactory.buildWithFixBinSize(min, max, binCnt);
        assertEquals(4, result.size());

    }

    @Test
    public void testToAsciiHistogram01() {

        bins.add(new BinnedBuySellMaps.Bin<Double>(2d, 3d));
        bins.add(new BinnedBuySellMaps.Bin<Double>(3d, 5d));
        binned = new BinnedBuySellMaps(bins);
        var map = new BuySellMap();
        map.add(1, 100);
        map.add(2, 200);
        map.add(3, 300);
        map.add(4, 400);
        binned.add(map);
    }

    @Test
    public void testSingleBin(){
        var buySellMap = new BuySellMap();
        buySellMap.add(1, 100000);
        var maps = BinnedBuySellMaps.BinnedBuySellMapsFactory.buildWithFixBinSize(buySellMap, 1);
        // two bins, one extra bin because of excluding upper bound
        assertEquals(2, maps.getBinCnt());
        assertEquals(1, maps.getValuesCount());
    }

    @Test
    public void testBinnedBuySellMapsFactoryFixedBinSizeSingleBin01(){
        var buySellMap = new BuySellMap();
        buySellMap.add(1, 100000);
        var maps = BinnedBuySellMaps.BinnedBuySellMapsFactory.buildWithFixBinSize(buySellMap, 1);
        // two bins, one extra bin because of excluding upper bound
       assertEquals(2, maps.getBinCnt());
    }

    @Test
    public void testBinnedBuySellMapsFactoryFixedBinSizeSingleBin02(){
        var buySellMap = new BuySellMap();
        buySellMap.add(1, 100000);
        buySellMap.add(2, 100000);
        var maps = BinnedBuySellMaps.BinnedBuySellMapsFactory.buildWithFixBinSize(buySellMap, 3);
        System.out.println(maps);
        assertEquals(1, maps.getBinCnt());
    }

    @Test
    public void testBinnedBuySellMapsFactoryFixedBinSizeTwoBin(){
        var buySellMap = new BuySellMap();
        buySellMap.add(1, 100000);
        buySellMap.add(3, 100000);
        var maps = BinnedBuySellMaps.BinnedBuySellMapsFactory.buildWithFixBinSize(buySellMap, 2);
//        System.out.println(maps);
        assertEquals(2, maps.getBinCnt());
    }
}