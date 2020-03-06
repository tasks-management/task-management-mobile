package duongll.succotask.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIConfig {

    final static String BASE_URL = "http://192.168.1.110:8080/";

    public static Retrofit createRetrofitForAPI(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static <T> T getAPIFromClass(Retrofit retrofit, final Class<T> service) {
        if (retrofit == null) {
            return null;
        }
        return retrofit.create(service);
    }
}
