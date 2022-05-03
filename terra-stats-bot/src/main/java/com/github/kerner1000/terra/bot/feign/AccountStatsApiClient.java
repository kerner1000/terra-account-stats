package com.github.kerner1000.terra.bot.feign;

import org.openapitools.api.SwapsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "account-stats-api", url = "${terra-stats-api-url}", configuration = FeignConfiguration.class)
public interface AccountStatsApiClient extends SwapsApi {

}
