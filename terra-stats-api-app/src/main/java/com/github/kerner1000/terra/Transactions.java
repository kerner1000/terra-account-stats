package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;

@Slf4j
public class Transactions {

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

    static final Predicate<Swap> BUY_WITH_UST = s -> s != null && "uusd".equals(s.getOfferAsset().getInfo().getNativeToken().getDenom());

    static final Predicate<Swap> BUY_WITH_LUNA = s -> s != null && "uluna".equals(s.getOfferAsset().getInfo().getNativeToken().getDenom());

    static final Predicate<Additional> TERRASWAP_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.TerraSwap.LUNA_UST));

    static final Predicate<Additional> MARKET_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.Market.LUNA_UST));

    static final Predicate<Additional> ASTROPORT_FILTER = a -> a != null && a.getContract() != null && a.getContract().stream().anyMatch(c -> c.contains(SwapPairs.Astroport.LUNA_UST));

    static WeightedMeanSwapMaps getWeightedMeanSwapMaps(Collection<? extends Transaction> transactionsList) {
            // offer coin is UST
            Map<Double, Double> buyMap = new TreeMap<>(Collections.reverseOrder());
            // receive coin is UST
            Map<Double, Double> sellMap = new TreeMap<>(Collections.reverseOrder());

            for (Transaction t : transactionsList) {
                for (Event e : t.getEvents()) {
                    for (Sub s : e.getSub()) {
                        Additional additional = s.getAdditional();
                        if (TERRASWAP_FILTER.test(additional)) {
                            for (ExecuteMessage executeMessage : s.getAdditional().getExecuteMessages()) {
                                if(executeMessage.getSwap() != null) {
                                    ExtractedSwap extractedSwap = TerraSwapAstroportSwapHandler.handle(executeMessage, buyMap, sellMap);
                                    log.info("Found TerraSwap swap, {}", extractedSwap);
                                } else {
                                    log.debug("Terraswap transaction without swap, Tx: {}", t.getHash());
                                }
                            }
                        } else if (ASTROPORT_FILTER.test(additional)) {
                            for (ExecuteMessage executeMessage : s.getAdditional().getExecuteMessages()) {
                                if(executeMessage.getSwap() != null) {
                                    ExtractedSwap extractedSwap = TerraSwapAstroportSwapHandler.handle(executeMessage, buyMap, sellMap);
                                    log.info("Found Astroport swap, {}", extractedSwap);
                                } else {
                                    log.debug("Terraswap transaction without swap, Tx: {}", t.getHash());
                                }

                            }
                        } else if (MARKET_FILTER.test(additional)) {
                            for (ExecuteMessage executeMessage : s.getAdditional().getExecuteMessages()) {
                                ExtractedSwap extractedSwap = MarketSwapHandler.handle(executeMessage, buyMap, sellMap);
                                log.info("Found Market swap {}", extractedSwap);
                            }

                        } else {
//                        log.debug("Unkown contract for {}", t);
                        }
                    }
                }
            }
            return new WeightedMeanSwapMaps(buyMap, sellMap);
        }

        static WeightedMeanSwapPrices getWeightedMean(Collection<? extends Transaction> transactionsList) {
            WeightedMeanSwapMaps result = getWeightedMeanSwapMaps(transactionsList);
            return getWeightedMean(result);
        }

        static WeightedMeanSwapPrices getWeightedMean(WeightedMeanSwapMaps swapResult) {
            return new WeightedMeanSwapPrices(swapResult.getBuyMap().entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue)), swapResult.getSellMap().entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue)));
        }

}
