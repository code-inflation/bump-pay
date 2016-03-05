package me.sixhackathon.bumppay.restlayer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PayMitapiInterface {

    @GET("balance/")
    Call<Balance> getBalance(@Header("usertoken") String usertoken);

}
