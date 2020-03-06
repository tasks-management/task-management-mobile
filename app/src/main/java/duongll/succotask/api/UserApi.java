package duongll.succotask.api;

import duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {



    @GET("api/v1/user/test")
    Call<User> getUserTestApi();

    @POST("api/v1/user/log-in")
    Call<User> logInToApp(@Body User user);
}
