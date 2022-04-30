package com.github.kerner1000.terra.feign;

import com.github.kerner1000.terra.json.data.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "lcd", url = "https://fcd.terra.dev", configuration = LcdConfiguration.class)
public interface LcdClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/tx/{txHash}", produces = "application/json")
    Transaction getTransactionByHash(@PathVariable String txHash);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/txs", produces = "application/json")
    LcdTransactionsPagination searchTransactions(@RequestParam String account, @RequestParam int limit, @RequestParam long offset);
}
