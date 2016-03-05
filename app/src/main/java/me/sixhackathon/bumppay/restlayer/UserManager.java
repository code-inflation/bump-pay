package me.sixhackathon.bumppay.restlayer;


public class UserManager {

    private static String phoneNumber = "+41796550243";
    private static String userToken =  "krXxrgOc83BWCmif2BMax0zAwqam8ZQc";

    protected static String getUserToken(){
        return userToken;
    }

    public static String getUserPhoneNumber(){
        return phoneNumber;
    }
}
