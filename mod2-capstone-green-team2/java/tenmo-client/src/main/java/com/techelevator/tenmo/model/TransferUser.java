package com.techelevator.tenmo.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class TransferUser {
private int senderId;
private int receiverId;
public BigDecimal amountTo;

@Autowired

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(BigDecimal amountTo) {
        this.amountTo = amountTo;
    }

//    public TransferUser(int senderId, int receiverId, BigDecimal amountTo) {
//        this.senderId = senderId;
//        this.receiverId = receiverId;
//        this.amountTo = amountTo;
//    }
}
