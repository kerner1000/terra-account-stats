package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WeightedMeanBuyCalculatorService {

    private final WeightedMeanSwapMaps result = new WeightedMeanSwapMaps();

    public void visit(List<Transaction> transactions) {
        WeightedMeanSwapMaps wm = TransactionsVisitor.getSwapAverageMap(transactions);
        result.add(wm);
        if(wm.getBuyMap().size() > 1 || wm.getSellMap().size() > 1) {
            log.debug("Weighted mean map:\n{}\n======\nweighted mean: {}", result, TransactionsVisitor.getWeightedMean(result));
        }
        return;
    }

    public WeightedMeanSwapMaps getMeanMap() {
        return result;
    }
}
