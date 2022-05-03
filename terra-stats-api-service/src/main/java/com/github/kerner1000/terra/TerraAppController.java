package com.github.kerner1000.terra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.commons.SwapPrices;
import com.github.kerner1000.terra.feign.DataHubClient;
import com.github.kerner1000.terra.feign.LcdClient;
import com.github.kerner1000.terra.feign.LcdTransactionsPagination;
import com.github.kerner1000.terra.feign.TerraConfig;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.Transactions;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.SwapsApi;
import org.openapitools.model.BuySellSwaps;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class TerraAppController implements SwapsApi {

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

    @Override
    public ResponseEntity<BuySellSwaps> getSwaps(String token, String terraAddress) {
        log.info("Calculating average buy/sell for {} and address {}", token, terraAddress);
        List<Transaction> allTransactions = new ArrayList<>();
        long offset = 0;
        do {
            try {
                LcdTransactionsPagination lcdTransactionsPagination = lcdClient.searchTransactions(terraAddress, 100, offset);
                List<Transaction> transactions = new ArrayList<>(lcdTransactionsPagination.getTxs());
                callbackService.visit(lcdTransactionsPagination.getTxs());
                allTransactions.addAll(transactions);
                log.info("Collected {} transactions, current offset: {}", allTransactions.size(), String.format("%,d", offset));
                try {
                    objectMapper.writeValue(new File("transaction-"+offset+".json"), allTransactions);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                offset = lcdTransactionsPagination.getNext();
                Thread.sleep(terraConfig.getSleepBetweenCalls());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        } while (offset != 0);

        SwapPrices result2 = new Transactions().getWeightedMean(callbackService.getMeanMap());
        log.info("Collected {} transactions, average swap price is {}", allTransactions.size(), result2);

        org.openapitools.model.BuySellSwaps result = Transformer.transform(callbackService.getMeanMap());

        return ResponseEntity.ok(result);
    }
}
