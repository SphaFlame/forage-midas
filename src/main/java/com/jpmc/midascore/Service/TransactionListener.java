package com.jpmc.midascore.Service;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    private static int count = 0;

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        count++;

        logger.info("=== TRANSACTION #{} ===", count);
        logger.info("Sender ID: {}", transaction.getSenderId());
        logger.info("Recipient ID: {}", transaction.getRecipientId());
        logger.info("Amount: {}", transaction.getAmount());
        logger.info("========================");

        // Process the transaction with validation and database updates
        boolean success = transactionService.processTransaction(transaction);

        if (success) {
            logger.info("Transaction #{} processed successfully", count);
        } else {
            logger.warn("Transaction #{} was discarded (validation failed)", count);
        }

        // SET BREAKPOINT HERE to inspect transaction processing
        float amount = transaction.getAmount();

        if (count <= 4) {
            System.out.println("*** TRANSACTION " + count + " AMOUNT: " + amount + " ***");
        }
    }
}