package com.github.kerner1000.terra;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.terra")
@Configuration
@Data
public class TerraConfig {

    private long sleepBetweenCalls;

    private boolean writeTransactions;
}
