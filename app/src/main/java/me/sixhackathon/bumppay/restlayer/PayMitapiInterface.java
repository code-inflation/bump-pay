package me.sixhackathon.bumppay.restlayer;

import me.sixhackathon.bumppay.paymitObjects.Balance;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PaymitAPIInterface {

    @GET("balance/")
    Call<Balance> getBalance(@Header("usertoken") String usertoken);

}
