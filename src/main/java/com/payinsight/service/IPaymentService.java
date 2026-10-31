package com.payinsight.service;

import com.payinsight.data.Transaction;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService // declares the SOAP service contract.
public interface IPaymentService {

    @WebMethod // exposes an operation to SOAP clients.
    Transaction getTransaction( // accepts a transaction ID and returns the corresponding transaction.
            @WebParam // defines the name of the input parameter.
                    (name = "transactionId") String transactionId // defines the data returned to the client.
    );

    @WebMethod
    Transaction[] getApprovedTransactions();

    @WebMethod
    Transaction[] getTransactionsAboveAmount(
            @WebParam(name = "minimumAmount") double minimumAmount
    );

    @WebMethod
    int getTransactionCount();
}