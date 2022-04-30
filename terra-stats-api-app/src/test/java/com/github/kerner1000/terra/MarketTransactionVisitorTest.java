package com.github.kerner1000.terra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.MarketTransactionVisitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MarketTransactionVisitorTest {

    private MarketTransactionVisitor transactionVisitor;

    @BeforeEach
    void setUp() {
        transactionVisitor = new MarketTransactionVisitor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testTerraswapUstToLuna() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Transaction transaction = objectMapper.readValue(new File("src/test/resources/example-transaction-market-UST-to-LUNA.json"), Transaction.class);
        var result = transactionVisitor.visit(transaction);
        assertNotNull(result);
       assertEquals(1,result.getBuyMap().size());
       assertEquals(0,result.getSellMap().size());

    }


}