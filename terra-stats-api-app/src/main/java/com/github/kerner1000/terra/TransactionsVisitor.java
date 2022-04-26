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

import static com.github.kerner1000.terra.Coin.LUNA;
import static com.github.kerner1000.terra.Coin.UST;

@Slf4j
public class TransactionsVisitor {

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

    static class TerraSwapAstroportSwapHandler {

        static ExtractedSwap handle(ExecuteMessage executeMessage, Map<Double, Double> buyMap, Map<Double, Double> sellMap){
            Swap swap = executeMessage.getSwap();
            double price = swap.getBeliefPrice().doubleValue();
            double simpleAmount = Constants.simpleAmount(swap.getOfferAsset().getAmount().doubleValue());
            SwapType swapType;
            if (BUY_WITH_UST.test(swap)) {
                buyMap.put(price, simpleAmount);
                swapType = new SwapType(UST, LUNA);
            } else if(BUY_WITH_LUNA.test(swap)){
                price = 1/price;
                swapType = new SwapType(LUNA, UST);
                sellMap.put(price, simpleAmount);
            } else
                throw new IllegalStateException();

            var result = new ExtractedSwap(swapType, price, simpleAmount);
            return result;
        }
    }

    static class MarketSwapHandler {
        static ExtractedSwap handle(ExecuteMessage executeMessage, Map<Double, Double> buyMap, Map<Double, Double> sellMap){
            AssertLimitOrder assertLimitOrder = executeMessage.getAssertLimitOrder();
            double receiveAmount = assertLimitOrder.getMinimumReceive().doubleValue();
            double nativeAmount = assertLimitOrder.getOfferCoin().getAmount().doubleValue();
            double simpleAmount = Constants.simpleAmount(nativeAmount);
            double price;
            SwapType swapType;
            if("uluna".equals(assertLimitOrder.getAskDenom())){
                price = nativeAmount / receiveAmount;
                buyMap.put(price, simpleAmount);
                swapType = new SwapType(UST, LUNA);
            } else if ("uusd".equals(assertLimitOrder.getAskDenom())){
                price = receiveAmount / nativeAmount;
                sellMap.put(price, simpleAmount);
                swapType = new SwapType(LUNA, UST);
            }
            else
                throw new IllegalStateException();

            var result = new ExtractedSwap(swapType, price, simpleAmount);
            return result;
        }
    }

    static WeightedMeanSwapMaps getSwapAverageMap(Collection<? extends Transaction> transactionsList) {
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

        static WeightedMeanSwapResult getWeightedMean(Collection<? extends Transaction> transactionsList) {
            WeightedMeanSwapMaps result = getSwapAverageMap(transactionsList);
            return getWeightedMean(result);
        }

        static WeightedMeanSwapResult getWeightedMean(WeightedMeanSwapMaps swapResult) {
            return new WeightedMeanSwapResult(swapResult.getBuyMap().entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue)), swapResult.getSellMap().entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue)));
        }

}
