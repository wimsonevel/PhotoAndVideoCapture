package id.co.blogspot.wimsonevel.photoandvideocapture.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Wim on 10/15/17.
 */

public interface UploadInterface {

    @Multipart
    @POST(Config.API_UPLOAD)
    Call<BaseResponse> upload(
            @Part("action") RequestBody action,
            @Part MultipartBody.Part media);
}
