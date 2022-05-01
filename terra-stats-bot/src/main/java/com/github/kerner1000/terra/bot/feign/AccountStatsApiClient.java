package com.github.kerner1000.terra.bot.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "account-stats-api", url = "${terra-stats-api-url}"+"/averageBuyLuna", configuration = FeignConfiguration.class)
public interface AccountStatsApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{terra-address}", produces = "text/plain")
    String getFuu(@PathVariable(name = "terra-address", required = true) String terraAddress);
}
