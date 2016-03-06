package me.sixhackathon.bumppay.restlayer;


import me.sixhackathon.bumppay.paymitObjects.Balance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceManager {

    private static int balance = 0;

    public static int  getBalance(){
        return balance;
    }
    /**
     * get the vault balance of the current user
     * @return current vault amount
     */
    public  static void loadBalance(){

        PaymitAPIInterface client = ServiceGenerator.createService(PaymitAPIInterface.class);


        Call<Balance> call = client.getBalance(UserManager.getUserToken());
        call.enqueue(new Callback<Balance>() {
            @Override
            public void onResponse(Call<Balance> call, Response<Balance> response) {
                int statusCode = response.code();
                Balance balance = response.body();
                BalanceManager.balance = balance.getBalance();
            }

            @Override
            public void onFailure(Call<Balance> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }
}
