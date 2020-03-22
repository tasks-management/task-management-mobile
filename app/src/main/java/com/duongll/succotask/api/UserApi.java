package com.duongll.succotask.api;

import java.util.List;

import com.duongll.succotask.dto.ManagerDto;
import com.duongll.succotask.dto.UserDto;
import com.duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @POST("api/v1/user/login")
    Call<User> logInToApp(@Body User user);

    @PUT("api/v1/user/{id}/notification/refresh-token")
    Call<User> refreshedNewToken(@Path("id") Long userId,
                                 @Query("firebaseToken") String token);

    @POST("api/v1/user/createManager")
    Call<User> createNewManager(@Body ManagerDto user);

    @POST("api/v1/user/createUser")
    Call<User> createNewUser(@Body UserDto user);

    @GET("api/v1/user/list/users")
    Call<List<User>> getAllUserForAdmin();

    @GET("api/v1/user/{id}/users")
    Call<List<User>> getAllUserInTeam(@Path("id") Long userId);

    @GET("api/v1/user/{id}")
    Call<User> getUserInformation(@Path("id") Long userId);
}
