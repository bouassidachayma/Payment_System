package com.payinsight.server;

import javax.xml.ws.Endpoint;
import com.payinsight.service.PaymentService;

public class PayInsightServer {

    public static void main(String[] args) {

        String url = "http://localhost:8086/PayInsight/PaymentService";

        Endpoint.publish(url, new PaymentService());
        /*
        url specifies where clients can access the service.
        new PaymentService() provides the implementation of the payment operations.
        Endpoint.publish() starts exposing the service at that address.
         */

        System.out.println("==================================");
        System.out.println("PayInsight SOAP Service is running!");
        System.out.println("Service URL: " + url);
        System.out.println("WSDL URL: " + url + "?wsdl");
        System.out.println("==================================");
    }
}