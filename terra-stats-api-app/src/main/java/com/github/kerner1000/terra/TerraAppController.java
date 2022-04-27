package com.github.kerner1000.terra;

import com.github.kerner1000.terra.json.data.DataHubRequestBody;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.feign.DataHubClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class TerraAppController {

    private final DataHubClient dataHubClient;

    private final WeightedMeanCalculatorService callbackService;

    public TerraAppController(DataHubClient dataHubClient, WeightedMeanCalculatorService callbackService) {
        this.dataHubClient = dataHubClient;
        this.callbackService = callbackService;
    }

    @GetMapping(value = "/averageBuyLuna/{terraAddress}", produces = "application/json")
    WeightedMeanSwapPrices averageBuyLuna(@PathVariable(required = true) String terraAddress) {
        log.info("Collecting all transactions");
        int limit = 100;
        int startOffset = 0;
        int stepOffset = 100;
        int stopOffset = 10000;
        List<Transaction> result = new ArrayList<>();
        log.info("Starting to query transactions for Terra {}", terraAddress);
        for(int i = startOffset; i <= stopOffset; i += stepOffset){
            List<Transaction> transactions = paginate(terraAddress, limit, i);
            if(transactions == null) {
                // no more transactions
                break;
            }
            callbackService.visit(transactions);
            result.addAll(transactions);
            log.info("Collected {} transactions", result.size());
        }
        WeightedMeanSwapPrices result2 = Transactions.getWeightedMean(callbackService.getMeanMap());
        log.info("Collected {} transactions, average swap price is {}", result.size(), result2);

        return result2;
    }

    List<Transaction> paginate(String terraAddress, int txLimit, int txOffset) {

        DataHubRequestBody body = new DataHubRequestBody();
        body.setAccount(Collections.singletonList(terraAddress));
        body.setNetwork("terra");
        body.setChainIds(Arrays.asList("columbus-3", "columbus-4", "columbus-5"));
        body.setType(Collections.singletonList("execute_contract"));
        body.setLimit(txLimit);
        body.setOffset(txOffset);

        return dataHubClient.getFuu(body);
    }
}
