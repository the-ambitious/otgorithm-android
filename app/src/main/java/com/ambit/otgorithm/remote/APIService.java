package com.ambit.otgorithm.remote;

import com.ambit.otgorithm.models.MyResponse;
import com.ambit.otgorithm.models.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAjgOKEa0:APA91bG5h187V6HRSG2ofBnSrUyf3CzWIJk6teZ0I6t9i3mCVxhaCTQCdgsHqD2wUkdUfWx9v2VPE-MCCV51mV0ceQYt6-EKcGyCgxBGAc7gOx-ds9Ivr6lATMaYJZik1XUJz0mnHVVrNuWTYhOq3Olr2_RyBNwx7w"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
