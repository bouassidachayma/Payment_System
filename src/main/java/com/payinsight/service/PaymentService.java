package com.payinsight.service;

import com.payinsight.data.PaymentDataService;
import com.payinsight.data.Transaction;

import javax.jws.WebService;

@WebService(endpointInterface = "com.payinsight.service.IPaymentService")
public class PaymentService implements IPaymentService {

    private final PaymentDataService paymentDataService =
            new PaymentDataService();

    @Override
    public Transaction getTransaction(String transactionId) {

        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Transaction ID must not be empty."
            );
        }

        Transaction transaction =
                paymentDataService.getTransaction(transactionId.trim());

        if (transaction == null) {
            throw new IllegalArgumentException(
                    "Transaction not found: " + transactionId
            );
        }

        return transaction;
    }
    @Override
    public Transaction[] getApprovedTransactions() {
        return paymentDataService.getApprovedTransactions();
    }
    @Override
    public Transaction[] getTransactionsAboveAmount(double minimumAmount) {
        if (minimumAmount < 0) {
            throw new IllegalArgumentException(
                    "Minimum amount cannot be negative."
            );
        }

        return paymentDataService.getTransactionsAboveAmount(minimumAmount);
    }
    @Override
    public int getTransactionCount() {
        return paymentDataService.getTransactionCount();
    }
}