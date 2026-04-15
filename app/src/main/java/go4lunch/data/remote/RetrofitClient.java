package go4lunch.data.remote;

import com.example.Go4Lunch.BuildConfig;

import java.io.IOException;

import go4lunch.data.api.OpenTripMapApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://opentripmap-places-v1.p.rapidapi.com/";

    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("X-RapidAPI-Key", BuildConfig.API_KEY)
                                .header("X-RapidAPI-Host", "opentripmap-places-v1.p.rapidapi.com")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OpenTripMapApi getOpenTripMapApi() {
        return getInstance().create(OpenTripMapApi.class);
    }
    }


