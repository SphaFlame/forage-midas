package com.jpmc.midascore.Service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        logger.info("Processing transaction: {}", transaction);

        // Validate senderId and recipientId
        if (!isValidTransaction(transaction)) {
            logger.warn("Invalid transaction data: {}", transaction);
            return false;
        }

        // Find sender and recipient
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender == null) {
            logger.warn("Sender not found: {}", transaction.getSenderId());
            return false;
        }

        if (recipient == null) {
            logger.warn("Recipient not found: {}", transaction.getRecipientId());
            return false;
        }

        // Check if sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance. Sender: {}, Balance: {}, Amount: {}",
                    sender.getName(), sender.getBalance(), transaction.getAmount());
            return false;
        }

        // Update balances
        float newSenderBalance = sender.getBalance() - transaction.getAmount();
        float newRecipientBalance = recipient.getBalance() + transaction.getAmount();

        sender.setBalance(newSenderBalance);
        recipient.setBalance(newRecipientBalance);

        // Save updated users
        userRepository.save(sender);
        userRepository.save(recipient);

        // Create and save transaction record
        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
        transactionRecordRepository.save(record);

        logger.info("Transaction completed. Sender: {} (balance: {}), Recipient: {} (balance: {})",
                sender.getName(), newSenderBalance, recipient.getName(), newRecipientBalance);

        return true;
    }

    private boolean isValidTransaction(Transaction transaction) {
        // Check if senderId is valid (positive number)
        if (transaction.getSenderId() <= 0) {
            return false;
        }

        // Check if recipientId is valid (positive number)
        if (transaction.getRecipientId() <= 0) {
            return false;
        }

        // Check if amount is valid (positive)
        if (transaction.getAmount() <= 0) {
            return false;
        }

        return true;
    }

    public UserRecord findUserById(long id) {
        return userRepository.findById(id);
    }
}
