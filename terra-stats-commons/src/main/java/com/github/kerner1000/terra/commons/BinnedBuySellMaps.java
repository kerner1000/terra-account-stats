package com.github.kerner1000.terra.commons;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class BinnedBuySellMaps<T extends Comparable<T>> {

    public static class BinFactory {
        public static List<Bin<Double>> buildWithFixBinCount(double min, double max, int binCnt) {

            List<Bin<Double>> result = new ArrayList<>(binCnt);
            var binSize = (max - min) / binCnt;
            var lower = min;
            double upper = lower + binSize;
            upper = Util.round(upper, 4);
            Bin<Double> newBin = null;
            while (upper <= max) {
                newBin = new Bin<Double>(lower, upper);
                result.add(newBin);
//                System.out.println("Now: lower: " + lower + ", upper: " + upper + ", rounded: " + Math.round(upper*1000)/1000d);
//                System.out.println(newBin);
                lower = lower + binSize;
                lower = Util.round(lower, 4);
                upper = lower + binSize;
                upper = Util.round(upper, 4);
            }
            if(result.size() < binCnt){
                newBin = new Bin<Double>(upper, upper + binSize);
                result.add(newBin);
            }
            if(newBin != null){
                if(Util.doubleEquals(newBin.upperBound, max, 0.0001)){
                    if(!result.remove(newBin)){
                        throw new RuntimeException();
                    }
                    newBin = new Bin<Double>(newBin.lowerBound, max);
                    result.add(newBin);
                }
            }
            return result;
        }

        public static List<Bin<Double>> buildWithFixBinSize(double min, double max, int binSize) {

            List<Bin<Double>> result = new ArrayList<>();
            var lower = min;
            double upper = lower + binSize;
            while (upper <= max) {
                var newBin = new Bin<Double>(lower, upper);
                result.add(newBin);
                lower = lower + binSize;
                upper = lower + binSize;
            }

            // one last bin to catch elements equal to max
            var newBin = new Bin<Double>(max, max + binSize);
            result.add(newBin);

            return result;
        }
    }

    public static class BinnedBuySellMapsFactory {

        public static BinnedBuySellMaps<Double> buildWithFixBinSize(BuySellMap map, int binSize) {
            double max = map.getMap().keySet().stream().mapToDouble(k -> k.price().doubleValue()).max().orElse(0);
            double min = 0;
            var bins = BinFactory.buildWithFixBinSize(min, max, binSize);
            BinnedBuySellMaps<Double> binnedBuySellMaps = new BinnedBuySellMaps<>(bins);
            binnedBuySellMaps.add(map);
            return binnedBuySellMaps;
        }

        public static BinnedBuySellMaps<Double> buildWithFixBinSize(BuySellMap map) {
            double max = map.getMap().keySet().stream().mapToDouble(k -> k.price().doubleValue()).max().orElse(0);
            int binSize = (int)Math.round (max / 100d * 5);
            binSize = Math.max(binSize, 1);
            return buildWithFixBinSize(map, binSize);
        }

        public static BinnedBuySellMaps<Double> buildWithFixBinCount(BuySellMap map, int binCnt) {
            double max = map.getMap().keySet().stream().mapToDouble(k -> k.price().doubleValue()).max().orElse(0);
            double min = 0;
            var bins = BinFactory.buildWithFixBinCount(min, max, binCnt);
            BinnedBuySellMaps<Double> binnedBuySellMaps = new BinnedBuySellMaps<>(bins);
            binnedBuySellMaps.add(map);
            return binnedBuySellMaps;
        }
    }

    public record Bin<T extends Comparable<T>>(T lowerBound, T upperBound) implements Comparable<Bin<T>> {

        @Override
        public int compareTo(Bin<T> o) {
            return this.lowerBound.compareTo(o.lowerBound);
        }

        /**
         * Tests if given value matches this {@code Bin}s bounds, lower inclusive, upper exclusive.
         *
         * @param value value to test
         * @return {@code true} if given value matches ranges; {@code false} otherwise
         */
        public boolean matches(T value) {
            var eins = this.lowerBound.compareTo(value) <= 0;
            var zwei = this.upperBound.compareTo(value) > 0;

            return eins && zwei;
        }
    }

    private final Map<Bin<Double>, BuySellMap> binToBuySellMaps = new TreeMap<>(Comparator.reverseOrder());

    public BinnedBuySellMaps(Collection<? extends Bin<Double>> bins) {
        for (Bin<Double> b : bins) {
            binToBuySellMaps.put(b, new BuySellMap());
        }
    }

    public void add(BuySellMap buySellMap) {

        for (Map.Entry<BuySellMap.Key, Number> e1 : buySellMap.getMap().entrySet()) {
            boolean matched = false;
            for (Map.Entry<Bin<Double>, BuySellMap> e2 : binToBuySellMaps.entrySet()) {
                if (e2.getKey().matches(e1.getKey().price().doubleValue())) {
                    e2.getValue().add(e1.getKey(), e1.getValue());
                    matched = true;
                }
            }
//            if(!matched){
//                Bin<Double> highestBin = getHighestBin();
//                if(highestBin == null){
//                    throw new IllegalStateException("No bins defined");
//                }
//                if(Util.doubleEquals(e1.getKey().price(), highestBin.upperBound)) {
//                    log.debug("No bin found for {}, but highest upper bound matches, putting to highest bin", e1);
//                    binToBuySellMaps.get(highestBin).add(e1.getKey(), e1.getValue());
//                }
//            }
        }
    }

    public Bin<Double> getHighestBin() {
        return binToBuySellMaps.keySet().stream().findFirst().orElse(null);
    }

    public long getValuesCount() {
        return binToBuySellMaps.values().stream().flatMap(e -> e.getMap().values().stream()).count();
    }

    public int getBinCnt() {
        return binToBuySellMaps.keySet().size();
    }

    private static double normalize(double value, double min, double max) {
        if (value == min)
            return 0;
        return ((value - min) / (max - min));
    }

    public String toAsciiHistogram() {
        return toAsciiHistogram(true);
    }

    public String toAsciiHistogram(boolean printEmpty) {
        StringBuilder sb = new StringBuilder();
        double allMax = 0;
        for (Map.Entry<Bin<Double>, BuySellMap> entry : binToBuySellMaps.entrySet()) {
            allMax += entry.getValue().getMap().values().stream().mapToDouble(n -> n.doubleValue()).sum();
        }
        for (Map.Entry<Bin<Double>, BuySellMap> entry : binToBuySellMaps.entrySet()) {
            var value = entry.getValue().getMap().values().stream().mapToDouble(n -> n.doubleValue()).sum();
            if (printEmpty || value > 0.1) {
                sb.append(String.format(Locale.US, "%6.2f - %6.2f", entry.getKey().lowerBound, entry.getKey().upperBound));
                sb.append(": ");
                var normalized = normalize(value, 0, allMax) * 100;
                sb.append(String.format("%6.2f%%", normalized));
                sb.append("\n");
            }
        }
        sb.append("\n");
//        sb.append(buildLabelRow());
        return sb.toString();
    }

    String buildLabelRow() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Bin<Double>, BuySellMap> entry : binToBuySellMaps.entrySet()) {
            sb.append(String.format(Locale.US, "%5.2f-%5.2f", entry.getKey().lowerBound, entry.getKey().upperBound));
            sb.append("  ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return binToBuySellMaps.entrySet().stream().map(this::elementToString).collect(Collectors.joining("\n"));
    }

    private String elementToString(Map.Entry<Bin<Double>, BuySellMap> binBuySellMapEntry) {
        return binBuySellMapEntry.getKey() + ":\n" + binBuySellMapEntry.getValue();
    }


}
