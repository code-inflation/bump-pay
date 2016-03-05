package me.sixhackathon.bumppay.restlayer;


import me.sixhackathon.bumppay.paymitObjects.LogInBody;
import me.sixhackathon.bumppay.paymitObjects.SignInBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManager {

    private static String phoneNumber;
    private static String userToken;


    public static void signIn(String number) {

        phoneNumber = number;
        PaymitAPIInterface client = ServiceGenerator.createService(PaymitAPIInterface.class);

        Call<SignInBody> call = client.signIn(number);

        call.enqueue(new Callback<SignInBody>() {
            @Override
            public void onResponse(Call<SignInBody> call, Response<SignInBody> response) {
                int statusCode = response.code();
                String smsCode = response.body().getSmscode();
                String sessionToken = response.headers().values("Sessiontoken").get(0);
                logIn(smsCode, sessionToken);
            }

            @Override
            public void onFailure(Call<SignInBody> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    private static void logIn(String smsCode, String sessionToken) {
        PaymitAPIInterface client = ServiceGenerator.createService(PaymitAPIInterface.class);
        Call<LogInBody> call = client.logIn(smsCode, sessionToken);

        call.enqueue(new Callback<LogInBody>() {
            @Override
            public void onResponse(Call<LogInBody> call, Response<LogInBody> response) {
                int statusCode = response.code();
                userToken = response.headers().values("Usertoken").get(0);
            }

            @Override
            public void onFailure(Call<LogInBody> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    public static boolean isLoggedIn() {
        if (userToken == null) {
            return false;
        } else {
            return true;
        }
    }

    protected static String getUserToken() {
        return userToken;
    }

    public static String getUserPhoneNumber() {
        return phoneNumber;
    }


}
