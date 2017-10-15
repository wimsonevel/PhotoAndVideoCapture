package id.co.blogspot.wimsonevel.photoandvideocapture.network;

import java.util.concurrent.TimeUnit;

import id.co.blogspot.wimsonevel.photoandvideocapture.BuildConfig;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wim on 10/15/17.
 */

public class UploadService {
    private UploadInterface uploadInterface;

    public UploadService() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(180, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(180, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(180, TimeUnit.SECONDS);
        okhttpBuilder.retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpBuilder.addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(okhttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        uploadInterface = retrofit.create(UploadInterface.class);
    }

    public void upload(RequestBody action, MultipartBody.Part media, Callback callback) {
        uploadInterface.upload(action, media).enqueue(callback);
    }

}
