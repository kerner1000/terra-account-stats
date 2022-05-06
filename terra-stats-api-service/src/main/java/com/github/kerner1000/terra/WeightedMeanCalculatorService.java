package com.github.kerner1000.terra;

import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequestScope
@Service
public class WeightedMeanCalculatorService {

    private final List<AbstractTransactionVisitor> visitors = Arrays.asList(new TerraswapTransactionVisitor(), new AstroportTransactionVisitor(), new MarketTransactionVisitor(), new LoopSwapTransactionVisitor());

    public BuySellMaps visit(List<Transaction> transactions) throws InterruptedException {
        
        BuySellMaps result = new BuySellMaps();

        for (Transaction transaction : transactions) {
            if(Thread.currentThread().isInterrupted()){
                break;
            }
            for (AbstractTransactionVisitor visitor : visitors) {
                result.add(visitor.visit(transaction));
            }
        }
        return result;
    }
}
