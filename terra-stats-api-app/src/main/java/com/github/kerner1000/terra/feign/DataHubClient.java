package com.github.kerner1000.terra.feign;

import com.github.kerner1000.terra.json.data.DataHubRequestBody;
import com.github.kerner1000.terra.json.data.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "datahubclient", url = "https://terra--search.datahub.figment.io/apikey", configuration = FeignConfiguration.class)
public interface DataHubClient {

    @RequestMapping(method = RequestMethod.POST, value = "/${api-key}/transactions_search", produces = "application/json")
    List<Transaction> getFuu(@RequestBody DataHubRequestBody requestBody);
}
