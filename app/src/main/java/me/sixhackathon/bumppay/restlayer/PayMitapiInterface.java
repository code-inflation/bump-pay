package me.sixhackathon.bumppay.restlayer;

import me.sixhackathon.bumppay.paymitObjects.Balance;
import me.sixhackathon.bumppay.paymitObjects.LogInBody;
import me.sixhackathon.bumppay.paymitObjects.SendMoney;
import me.sixhackathon.bumppay.paymitObjects.SignInBody;
import me.sixhackathon.bumppay.paymitObjects.Transaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PaymitAPIInterface {

    @GET("balance/")
    Call<Balance> getBalance(@Header("usertoken") String usertoken);

    @PUT("balance/transaction/send/")
    Call<Transaction> putPayment(@Header("usertoken") String usertoken, @Body SendMoney paymentBody);

    @GET("signin/{number}/")
    Call<SignInBody> signIn(@Path("number") String number);

    @GET("signin/login/{smscode}/")
    Call<LogInBody> logIn(@Path("smscode") String smscode,@Header("sessiontoken") String sessiontoken);


}
