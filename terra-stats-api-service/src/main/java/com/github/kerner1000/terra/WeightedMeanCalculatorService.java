package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.Transactions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Slf4j
@RequestScope
@Service
public class WeightedMeanCalculatorService {

    private final BuySellMaps result = new BuySellMaps();

    public void visit(List<Transaction> transactions) throws InterruptedException {
        BuySellMaps wm = new Transactions().getWeightedMeanSwapMaps(transactions);
        result.add(wm);
        if(wm.getBuyMap().size() > 1 || wm.getSellMap().size() > 1) {
            log.debug("swap map:\n{}\n======\nweighted mean: {}", result, new Transactions().getWeightedMean(result));
        }
        return;
    }

    public BuySellMaps getMeanMap() {
        return result;
    }
}
