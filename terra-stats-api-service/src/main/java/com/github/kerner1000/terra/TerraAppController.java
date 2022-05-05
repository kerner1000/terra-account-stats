package com.github.kerner1000.terra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.commons.BinnedBuySellMaps;
import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.commons.SwapPrices;
import com.github.kerner1000.terra.feign.LcdClient;
import com.github.kerner1000.terra.feign.LcdTransactionsPagination;
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

    private final LcdClient lcdClient;

    private final WeightedMeanCalculatorService callbackService;

    private final ObjectMapper objectMapper;

    private final TerraConfig terraConfig;

    public TerraAppController(LcdClient lcdClient, WeightedMeanCalculatorService callbackService, ObjectMapper objectMapper, TerraConfig terraConfig) {
        this.lcdClient = lcdClient;
        this.callbackService = callbackService;
        this.objectMapper = objectMapper;
        this.terraConfig = terraConfig;
    }

    @Override
    public ResponseEntity<BuySellSwaps> getSwaps(String token, String terraAddress) {
        log.info("Calculating average buy/sell for {} and address {}", token, terraAddress);
        long transactionsCount = 0;
        long offset = 0;
        BuySellMaps collectedSwaps = new BuySellMaps();
        do {
            try {
                LcdTransactionsPagination lcdTransactionsPagination = lcdClient.searchTransactions(terraAddress, 100, offset);
                List<Transaction> transactions = new ArrayList<>(lcdTransactionsPagination.getTxs());
                collectedSwaps.add(callbackService.visit(lcdTransactionsPagination.getTxs()));
                transactionsCount += transactions.size();
                log.info("Collected {} transactions, current offset: {}", transactionsCount, offset);
                if(terraConfig.isWriteTransactions()) {
                    try {
                        File file = new File("transactions/" + terraAddress + "transaction-" + offset + ".json");
                        if(file.mkdirs()){
                            objectMapper.writeValue(file, transactions);
                        }
                    } catch (IOException e) {
                        log.error("Failed to write transactions {}", e.getLocalizedMessage(), e);
                    }
                }
                offset = lcdTransactionsPagination.getNext();
                Thread.sleep(terraConfig.getSleepBetweenCalls());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        } while (offset != 0);

        SwapPrices result2 = new Transactions().getWeightedMean(collectedSwaps);
        log.info("Collected {} transactions, average swap price is {}", transactionsCount, result2);
        BinnedBuySellMaps<Double> binnedBuySellMaps = BinnedBuySellMaps.BinnedBuySellMapsFactory.buildWithFixBinSize(collectedSwaps.getBuyMap(), 5);
        log.debug("Buy price distribution:\n{}", binnedBuySellMaps.toAsciiHistogram(false));
        org.openapitools.model.BuySellSwaps result = Transformer.transform(collectedSwaps);
        return ResponseEntity.ok(result);
    }
}
