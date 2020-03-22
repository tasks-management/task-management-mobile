package com.duongll.succotask.api;

import com.duongll.succotask.dto.NotificationBackgroundDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationApi {

    @POST(value = "api/v1/notification/background")
    Call<NotificationBackgroundDto> pushBackgroundNotification(@Query("firebaseToken") String token,
                                                               @Body NotificationBackgroundDto dto);

}
