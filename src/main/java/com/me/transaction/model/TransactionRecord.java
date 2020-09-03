package com.me.transaction.model;

import java.time.LocalDateTime;

public class TransactionRecord {
    String transactionid;

    public TransactionRecord() {
        super();
    }

    public TransactionRecord(String transactionid, String toAccountid, String fromAccountid, LocalDateTime createAt, Double amount, String transactionType, String relatedTransaction) {
        this.transactionid = transactionid;
        this.toAccountid = toAccountid;
        this.fromAccountid = fromAccountid;
        this.createAt = createAt;
        this.amount = amount;
        this.transactionType = transactionType;
        this.relatedTransaction = relatedTransaction;
    }

    String toAccountid;
    String fromAccountid;
    LocalDateTime createAt;
    Double amount;
    String transactionType;
    String relatedTransaction;


    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getToAccountid() {
        return toAccountid;
    }

    public void setToAccountid(String toAccountid) {
        this.toAccountid = toAccountid;
    }

    public String getFromAccountid() {
        return fromAccountid;
    }

    public void setFromAccountid(String fromAccountid) {
        this.fromAccountid = fromAccountid;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRelatedTransaction() {
        return relatedTransaction;
    }

    public void setRelatedTransaction(String relatedTransaction) {
        this.relatedTransaction = relatedTransaction;
    }
}
