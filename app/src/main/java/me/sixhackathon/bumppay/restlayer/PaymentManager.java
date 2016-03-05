package me.sixhackathon.bumppay.restlayer;

import me.sixhackathon.bumppay.paymitObjects.TransactionInfo;

public class PaymentManager {
    /**
     * Make a transaction to a specific phone number
     * @param recieverPhoneNr
     * @param amount
     * @return TransactionNumber
     */
    public static String pay(String recieverPhoneNr, Integer amount){
        BalanceManager.addToBalance(amount);
        return "12341234";
    }

    public static TransactionInfo getTransactionInfo(String TransactionID){
        TransactionInfo info = new TransactionInfo();
        return info;
    }
}
