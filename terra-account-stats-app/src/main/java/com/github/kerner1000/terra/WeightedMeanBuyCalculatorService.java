package com.github.kerner1000.terra;

import com.github.kerner1000.terra.data.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WeightedMeanBuyCalculatorService {

    private final Map<Double, Double> meanMap = new LinkedHashMap<>();

    public void visit(List<Transaction> transactions) {
        Map<Double, Double> wm = Utils.getSwapAverageMap(transactions);
        meanMap.putAll(wm);
        if(wm.size() > 1) {
            log.debug("Weighted mean map:\n{}\n======\nweighted mean: {}", meanMap.entrySet().stream().map(Object::toString).collect(Collectors.joining("\n")), Utils.getWeightedMean(meanMap));
        }
        return;
    }

    public Map<Double, Double> getMeanMap() {
        return meanMap;
    }
}
