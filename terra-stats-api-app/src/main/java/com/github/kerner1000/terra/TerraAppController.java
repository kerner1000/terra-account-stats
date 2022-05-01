package com.github.kerner1000.terra;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.feign.DataHubClient;
import com.github.kerner1000.terra.feign.LcdClient;
import com.github.kerner1000.terra.feign.LcdTransactionsPagination;
import com.github.kerner1000.terra.feign.TerraConfig;
import com.github.kerner1000.terra.json.data.DataHubRequestBody;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.Transactions;
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

    private final LcdClient lcdClient;

    private final WeightedMeanCalculatorService callbackService;

    private final ObjectMapper objectMapper;

    private final TerraConfig terraConfig;

    public TerraAppController(DataHubClient dataHubClient, LcdClient lcdClient, WeightedMeanCalculatorService callbackService, ObjectMapper objectMapper, TerraConfig terraConfig) {
        this.dataHubClient = dataHubClient;
        this.lcdClient = lcdClient;
        this.callbackService = callbackService;
        this.objectMapper = objectMapper;
        this.terraConfig = terraConfig;


    }

    @GetMapping(value = "/averageBuyLuna/{terraAddress}", produces = "text/plain")
    String averageBuyLuna(@PathVariable(required = true) String terraAddress) throws InterruptedException, JsonMappingException {
        log.info("Collecting all transactions");
        int limit = 100;
        int startOffset = 0;
        int stopOffset = 10000;
        List<Transaction> result = new ArrayList<>();
        log.info("Starting to query transactions for Terra {}", terraAddress);
        long offset = 0;
        do {
            LcdTransactionsPagination lcdTransactionsPagination = lcdClient.searchTransactions(terraAddress, limit, offset);
            List<Transaction> transactions = new ArrayList<>(lcdTransactionsPagination.getTxs());
            callbackService.visit(transactions);
            result.addAll(transactions);
            log.info("Collected {} transactions, current offset: {}", result.size(), String.format("%,d", offset));
            offset = lcdTransactionsPagination.getNext();
            Thread.sleep(terraConfig.getSleepBetweenCalls());
        } while (offset != 0);
        SwapPrices result2 = Transactions.getWeightedMean(callbackService.getMeanMap());
        log.info("Collected {} transactions, average swap price is {}", result.size(), result2);
        Stats stats = new Stats(callbackService.getMeanMap());
        return stats.toString();
    }

    List<Transaction> paginate(String terraAddress, int txLimit, int txOffset) {

        DataHubRequestBody body = new DataHubRequestBody();
        body.setAccount(Collections.singletonList(terraAddress));
        body.setNetwork("terra");
        body.setChainIds(Arrays.asList("columbus-3", "columbus-4", "columbus-5"));
        body.setType(Collections.singletonList("execute_contract"));
        body.setLimit(txLimit);
        body.setOffset(txOffset);

        LcdTransactionsPagination result = lcdClient.searchTransactions(terraAddress, txLimit, txOffset);
        List<Transaction> result2 = new ArrayList<>();

        result2.addAll(result.getTxs());

        return result2;
    }
}
