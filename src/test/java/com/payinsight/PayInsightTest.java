package com.payinsight;

import com.payinsight.data.PaymentDataService;
import com.payinsight.data.Transaction;

import org.junit.Test;

import static org.junit.Assert.*;

import com.payinsight.service.PaymentService;

public class PayInsightTest {

    private final PaymentDataService service =
            new PaymentDataService();

    // TEST 1: XML/XSD validation
    @Test
    public void testXMLValidation() {

        boolean result = Main.validateXML(
                "data/payments.xml",
                "data/payments.xsd"
        );

        assertTrue("XML validation should succeed", result);
    }

    // TEST 2: Retrieve a transaction by ID
    @Test
    public void testGetTransaction() {

        Transaction transaction =
                service.getTransaction("TX001");

        assertNotNull(transaction);

        assertEquals("TX001", transaction.getId());
        assertEquals("C001", transaction.getCustomerId());
        assertEquals(250.00, transaction.getAmount(), 0.001);
        assertEquals("TND", transaction.getCurrency());
        assertEquals("approved", transaction.getStatus());
    }

    // TEST 3: Retrieve approved transactions
    @Test
    public void testGetApprovedTransactions() {

        Transaction[] transactions =
                service.getApprovedTransactions();

        assertEquals(2, transactions.length);

        for (Transaction transaction : transactions) {
            assertEquals("approved", transaction.getStatus());
        }
    }

    // TEST 4: Retrieve transactions above 100 TND
    @Test
    public void testTransactionsAboveAmount() {

        Transaction[] transactions =
                service.getTransactionsAboveAmount(100);

        assertEquals(2, transactions.length);

        for (Transaction transaction : transactions) {
            assertTrue(transaction.getAmount() > 100);
        }
    }

    // TEST 5: Count all transactions
    @Test
    public void testTransactionCount() {

        int count = service.getTransactionCount();

        assertEquals(3, count);
    }

    // TEST 6: Invalid transaction ID
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTransactionID() {

        PaymentService paymentService = new PaymentService();

        paymentService.getTransaction("TX999");
    }

    // TEST 7: Negative transaction amount
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAmount() {

        PaymentService paymentService = new PaymentService();

        paymentService.getTransactionsAboveAmount(-100);
    }
}