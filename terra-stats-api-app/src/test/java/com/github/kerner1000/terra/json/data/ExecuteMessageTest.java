package com.github.kerner1000.terra.json.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExecuteMessageTest {

    @Test
    void testTerraswap() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ExecuteMessage messages = objectMapper.readValue(new File("src/test/resources/example-execute-message-Luna-UST-terraswap.json"), new TypeReference<>() {
        });
        assertNotNull(messages);
        assertNotNull(messages.getSwap());
    }

    @Test
    void testMarket() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ExecuteMessage messages = objectMapper.readValue(new File("src/test/resources/example-execute-message-UST-Luna-market.json"), new TypeReference<>() {
        });
        assertNotNull(messages);
        AssertLimitOrder assertLimitOrder = messages.getAssertLimitOrder();
        assertNotNull(assertLimitOrder);
    }
}
