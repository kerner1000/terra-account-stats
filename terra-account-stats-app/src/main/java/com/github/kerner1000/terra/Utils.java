package com.github.kerner1000.terra;

import com.github.kerner1000.terra.data.*;
import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;

@Slf4j
public class Utils {

    static final Predicate<Swap> BUY_WITH_UST = s -> s != null && "uusd".equals(s.getOfferAsset().getInfo().getNativeToken().getDenom());

    static final Predicate<Swap> BUY_WITH_LUNA = s -> s != null && "uluna".equals(s.getOfferAsset().getInfo().getNativeToken().getDenom());

    static final Predicate<Additional> TERRASWAP_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.TerraSwap.LUNA_UST));

    static final Predicate<Additional> MARKET_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.Market.LUNA_UST));

    static final Predicate<Additional> ASTROPORT_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.Astroport.LUNA_UST));

    static <T> Collector<T, ?, Double> averagingWeighted(ToDoubleFunction<T> valueFunction, ToDoubleFunction<T> weightFunction) {
        class Box {
            double num = 0;
            double denom = 0;
        }
        return Collector.of(
                Box::new,
                (b, e) -> {
                    b.num += valueFunction.applyAsDouble(e) * weightFunction.applyAsDouble(e);
                    b.denom += weightFunction.applyAsDouble(e);
                },
                (b1, b2) -> {
                    b1.num += b2.num;
                    b1.denom += b2.denom;
                    return b1;
                },
                b -> b.num / b.denom
        );
    }

    static Map<Double, Double> getSwapAverageMap(Collection<? extends Transaction> transactionsList) {
        Map<Double, Double> map = new TreeMap<>(Collections.reverseOrder());
        for (Transaction t : transactionsList) {
            for (Event e : t.getEvents()) {
                for (Sub s : e.getSub()) {
                    Additional additional = s.getAdditional();
                    if (TERRASWAP_FILTER.test(additional)) {
                        for (ExecuteMessage executeMessage : s.getAdditional().getExecuteMessages()) {
                            Swap swap = executeMessage.getSwap();
                            if (BUY_WITH_UST.test(swap)) {
                                double price = swap.getBeliefPrice().doubleValue();
                                double nativeAmount = swap.getOfferAsset().getAmount().doubleValue();
                                double simpleAmount = Constants.simpleAmount(nativeAmount);
                                log.info("Found TerraSwap buy, price: {}, amount: {}", String.format("%.2f", price), String.format("%.2f", simpleAmount));
                                map.put(price, simpleAmount);
                            }
                        }
                    } else if (MARKET_FILTER.test(additional)) {
                        for (ExecuteMessage executeMessage : s.getAdditional().getExecuteMessages()) {
                            AssertLimitOrder assertLimitOrder = executeMessage.getAssertLimitOrder();
                            double receiveAmount = assertLimitOrder.getMinimumReceive().doubleValue();
                            double nativeAmount = assertLimitOrder.getOfferCoin().getAmount().doubleValue();
                            double simpleAmount = Constants.simpleAmount(nativeAmount);
                            double price = nativeAmount / receiveAmount;
                            log.info("Found Market buy, price: {}, amount: {}", String.format("%.2f", price), String.format("%.2f", simpleAmount));
                            map.put(price, simpleAmount);
                        }
                    } else if (ASTROPORT_FILTER.test(additional)) {
                        for (ExecuteMessage executeMessage : s.getAdditional().getExecuteMessages()) {
                            Swap swap = executeMessage.getSwap();
                            double price = swap.getBeliefPrice().doubleValue();
                            double nativeAmount = swap.getOfferAsset().getAmount().doubleValue();
                            double simpleAmount = Constants.simpleAmount(nativeAmount);
                            log.info("Found Astroport buy, price: {}, amount: {}", String.format("%.2f", price), String.format("%.2f", simpleAmount));
                            map.put(price, simpleAmount);
                        }
                    } else {
//                        log.debug("Unkown contract for {}", t);
                    }
                }
            }
        }
        return map;
    }

    static double getWeightedMean(Collection<? extends Transaction> transactionsList) {
        return getWeightedMean(getSwapAverageMap(transactionsList));
    }
    static double getWeightedMean(Map<Double, Double> map) {
        return map.entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue));
    }
}
