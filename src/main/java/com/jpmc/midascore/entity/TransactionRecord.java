package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_records")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(name = "amount", nullable = false)
    private float amount;

    @Column(name = "incentive", nullable = false)
    private float incentive = 0.0f;

    @Column(name = "status", nullable = false)
    private String status = "COMPLETED";

    protected TransactionRecord() {}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.status = "COMPLETED";
        this.incentive = 0.0f;
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
        this.status = "COMPLETED";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserRecord getSender() { return sender; }
    public void setSender(UserRecord sender) { this.sender = sender; }

    public UserRecord getRecipient() { return recipient; }
    public void setRecipient(UserRecord recipient) { this.recipient = recipient; }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }

    public float getIncentive() { return incentive; }
    public void setIncentive(float incentive) { this.incentive = incentive; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("TransactionRecord[id=%d, senderId=%d, recipientId=%d, amount=%f, incentive=%f, status=%s]",
                id, sender.getId(), recipient.getId(), amount, incentive, status);
    }
}