package me.sixhackathon.bumppay.paymitObjects;

import java.security.Timestamp;

public class TransactionInfo {
    private int id;
    private Timestamp timestamp;
    private double amount;
    private String phoneNumber;
    private TransactionStatus status;
}
