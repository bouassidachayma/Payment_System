package com.payinsight.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class PaymentDataService {

    private static final String XML_PATH = "data/payments.xml";
    private static final String XSD_PATH = "data/payments.xsd";

    /**
     * Retrieves a transaction from payments.xml using its ID.
     *
     * Returns the Transaction object if found.
     * Returns null if the ID does not exist or the XML cannot be read.
     */
    public Transaction getTransaction(String transactionId) {

        try {
            // Validate the XML before reading payment data.
            if (!com.payinsight.Main.validateXML(XML_PATH, XSD_PATH)) {
                throw new IllegalStateException(
                        "Payment data failed XML/XSD validation."
                );
            }

            // Load the XML document.
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            // Prevent external XML entities from being processed.
            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true
            );
            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false
            );
            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false
            );
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(XML_PATH));

            // Find transactions with the requested ID.
            NodeList transactions = document.getElementsByTagName(
                    "transaction"
            );

            for (int i = 0; i < transactions.getLength(); i++) {

                Element element = (Element) transactions.item(i);

                if (transactionId.equals(element.getAttribute("id"))) {

                    Transaction transaction = new Transaction();

                    transaction.setId(element.getAttribute("id"));

                    transaction.setCustomerId(
                            getText(element, "customerId")
                    );

                    transaction.setMerchantId(
                            getText(element, "merchantId")
                    );

                    transaction.setTerminalId(
                            getText(element, "terminalId")
                    );

                    Element amountElement =
                            (Element) element
                                    .getElementsByTagName("amount")
                                    .item(0);

                    transaction.setAmount(
                            Double.parseDouble(
                                    amountElement.getTextContent().trim()
                            )
                    );

                    transaction.setCurrency(
                            amountElement.getAttribute("currency")
                    );

                    transaction.setDate(
                            getText(element, "date")
                    );

                    transaction.setStatus(
                            getText(element, "status")
                    );

                    return transaction;
                }
            }

            // The requested transaction was not found.
            return null;

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to retrieve transaction: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Retrieves the text of a child element.
     */
    private String getText(Element parent, String tagName) {

        NodeList nodes = parent.getElementsByTagName(tagName);

        if (nodes.getLength() == 0) {
            return "";
        }

        return nodes.item(0).getTextContent().trim();
    }

    //getApprovedTransactions return all transactions whose status is approved
    //1.Validates payments.xml against payments.xsd.
    //2.Reads the transactions from the XML file.
    //3.Selects transactions with the status approved.
    //4.Returns them as an array of Transaction objects.
    //It reuses getTransaction() method to construct each result.

    public Transaction[] getApprovedTransactions() {
        try {
            if (!com.payinsight.Main.validateXML(XML_PATH, XSD_PATH)) {
                throw new IllegalStateException(
                        "Payment data failed XML/XSD validation."
                );
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true
            );
            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false
            );
            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false
            );
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(XML_PATH));

            NodeList transactions =
                    document.getElementsByTagName("transaction");

            java.util.List<Transaction> approvedTransactions =
                    new java.util.ArrayList<>();

            for (int i = 0; i < transactions.getLength(); i++) {
                Element element = (Element) transactions.item(i);

                String status = getText(element, "status");

                if ("approved".equals(status)) {
                    String id = element.getAttribute("id");

                    Transaction transaction = getTransaction(id);

                    if (transaction != null) {
                        approvedTransactions.add(transaction);
                    }
                }
            }

            return approvedTransactions.toArray(
                    new Transaction[0]
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to retrieve approved transactions: "
                            + e.getMessage(),
                    e
            );
        }
    }
    //getTransactionsAboveAmount retrieve transactions whose amount is greater than a value supplied by the SOAP client
    //1.Reads the payment XML file.
    //2.Checks each transaction's amount.
    //3.Compares it with the minimum amount.
    //4.Returns transactions greater than that amount.
    public Transaction[] getTransactionsAboveAmount(double minimumAmount) {

        try {
            if (!com.payinsight.Main.validateXML(XML_PATH, XSD_PATH)) {
                throw new IllegalStateException(
                        "Payment data failed XML/XSD validation."
                );
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false
            );

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(XML_PATH));

            NodeList transactions =
                    document.getElementsByTagName("transaction");

            java.util.List<Transaction> results =
                    new java.util.ArrayList<>();

            for (int i = 0; i < transactions.getLength(); i++) {

                Element element = (Element) transactions.item(i);

                Element amountElement =
                        (Element) element.getElementsByTagName("amount").item(0);

                double amount = Double.parseDouble(
                        amountElement.getTextContent().trim()
                );

                if (amount > minimumAmount) {

                    String id = element.getAttribute("id");

                    Transaction transaction = getTransaction(id);

                    if (transaction != null) {
                        results.add(transaction);
                    }
                }
            }

            return results.toArray(new Transaction[0]);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to retrieve transactions: " + e.getMessage(),
                    e
            );
        }
    }
    //getTransactionCount returns the total number of transactions stored in payments.xml.
    public int getTransactionCount() {

        try {
            if (!com.payinsight.Main.validateXML(XML_PATH, XSD_PATH)) {
                throw new IllegalStateException(
                        "Payment data failed XML/XSD validation."
                );
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false
            );

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(XML_PATH));

            NodeList transactions =
                    document.getElementsByTagName("transaction"); //finds all transaction elements.

            return transactions.getLength(); //counts how many elements were found and returns that number.

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to count transactions: " + e.getMessage(),
                    e
            );
        }
    }
}