package me.sixhackathon.bumppay.restlayer;


public class BalanceManager {

    private static double balance = 83.5; // just for testing purposes

    /**
     * get the vault balance of the current user
     * @return current vault amount
     */
    public  static  double getBalance(){
        return balance;
    }

    public static void addToBalance(double amount){
        balance += amount;
    }
}
