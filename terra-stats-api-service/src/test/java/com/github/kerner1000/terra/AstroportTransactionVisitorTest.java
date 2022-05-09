package com.github.kerner1000.terra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kerner1000.terra.json.data.Transaction;
import com.github.kerner1000.terra.transactions.AstroportTransactionVisitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AstroportTransactionVisitorTest {

    private AstroportTransactionVisitor transactionVisitor;

    @BeforeEach
    void setUp() {
        transactionVisitor = new AstroportTransactionVisitor();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testAstroportUstToLuna() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Transaction transaction = objectMapper.readValue(new File("src/test/resources/example-transaction-astroport-UST-to-LUNA.json"), Transaction.class);
        var result = transactionVisitor.visit(transaction);
        assertNotNull(result);
        assertEquals(1, result.getBuyMap().getMap().size());
        assertEquals(0, result.getSellMap().getMap().size());
        assertEquals(10d, result.getBuyMap().getMap().values().iterator().next());

    }


}