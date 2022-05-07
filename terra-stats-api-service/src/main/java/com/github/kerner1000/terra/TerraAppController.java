package com.github.kerner1000.terra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.commons.BinnedBuySellMaps;
import com.github.kerner1000.terra.commons.BuySellMaps;
import com.github.kerner1000.terra.commons.Coin;
import com.github.kerner1000.terra.commons.SwapPrices;
import com.github.kerner1000.terra.feign.LcdClient;
import com.github.kerner1000.terra.feign.LcdTransactionsPagination;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.Transactions;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.SwapsApi;
import org.openapitools.model.BuySellSwaps;
import org.openapitools.model.BuySellSwapsPerCoin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestController
public class TerraAppController implements SwapsApi {

    private final LcdClient lcdClient;

    private final ObjectMapper objectMapper;

    private final TerraConfig terraConfig;

    public TerraAppController(LcdClient lcdClient, ObjectMapper objectMapper, TerraConfig terraConfig) {
        this.lcdClient = lcdClient;
        this.objectMapper = objectMapper;
        this.terraConfig = terraConfig;
    }

    @Override
    public ResponseEntity<BuySellSwapsPerCoin> getSwaps(String terraAddress, List<String> hide) {
        log.info("Calculating average buy/sell for address {}", terraAddress);
        long transactionsCount = 0;
        long offset = 0;
        Map<Coin,BuySellMaps> coinToCollectedSwaps = new TreeMap<>();
        do {
            try {
                LcdTransactionsPagination lcdTransactionsPagination = lcdClient.searchTransactions(terraAddress, 100, offset);
                List<Transaction> transactions = new ArrayList<>(lcdTransactionsPagination.getTxs());
                var buySellMapsForCoin = new LunaWeightedMeanCalculatorService().visit(lcdTransactionsPagination.getTxs());
                var buySellMaps = coinToCollectedSwaps.get(buySellMapsForCoin.coin());
                if(buySellMaps != null){
                    buySellMaps.add(buySellMapsForCoin.buySellMaps());
                } else {
                    buySellMaps = buySellMapsForCoin.buySellMaps();
                }
                coinToCollectedSwaps.put(buySellMapsForCoin.coin(), buySellMaps);
                transactionsCount += transactions.size();
                log.info("Collected {} transactions, current offset: {}", transactionsCount, offset);
                if(terraConfig.isWriteTransactions()) {
                    try {
                        File directory = new File("transactions/"+terraAddress);
                        File file = new File(directory,"transaction-" + offset + ".json");
                        if(directory.exists() || directory.mkdirs()){
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

        BuySellSwapsPerCoin result = new BuySellSwapsPerCoin();
        for(Map.Entry<Coin, BuySellMaps> entry : coinToCollectedSwaps.entrySet()){
            result.addEntriesItem(Transformer.transform(entry.getKey(), entry.getValue()));
        }
        return ResponseEntity.ok(result);
    }
}
