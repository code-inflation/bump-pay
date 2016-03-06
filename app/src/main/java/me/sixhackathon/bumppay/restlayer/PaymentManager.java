package me.sixhackathon.bumppay.restlayer;

import me.sixhackathon.bumppay.paymitObjects.SendMoney;
import me.sixhackathon.bumppay.paymitObjects.Transaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentManager {

    /**
     * Make a transaction to a specific phone number
     *
     * @param recieverPhoneNr
     * @param amount
     * @return TransactionNumber
     */
    public static void pay(String recieverPhoneNr, Integer amount) {

        if(recieverPhoneNr == null){
            return;
        }

        SendMoney paymentBody = new SendMoney();
        paymentBody.setAmount(amount);
        paymentBody.setPhoneNumber(recieverPhoneNr);

        PaymitAPIInterface client = ServiceGenerator.createService(PaymitAPIInterface.class);


        Call<Transaction> call = client.putPayment(UserManager.getUserToken(), paymentBody);

        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                int statusCode = response.code();
                //transaction = response.body();

            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    public static Transaction getTransactionInfo(String TransactionID) {

        return null;
    }
}
