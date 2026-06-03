package go4lunch.data.remote;

import com.lucas.Go4Lunch.BuildConfig;

import java.io.IOException;

import go4lunch.data.api.OpenTripMapApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://opentripmap-places-v1.p.rapidapi.com/";

    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .addHeader("x-rapidapi-key", "de0fb668c5msh2fdac4816a9f28ap126568jsn11054226573a")
                                .addHeader("x-rapidapi-host", "opentripmap-places-v1.p.rapidapi.com")
                                .addHeader("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    })
                    .addInterceptor(interceptor)
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


