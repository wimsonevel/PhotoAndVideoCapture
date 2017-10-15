package id.co.blogspot.wimsonevel.photoandvideocapture;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.co.blogspot.wimsonevel.photoandvideocapture.network.BaseResponse;
import id.co.blogspot.wimsonevel.photoandvideocapture.network.NotificationUtil;
import id.co.blogspot.wimsonevel.photoandvideocapture.network.UploadService;
import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button capturePhoto;
    private Button recordVideo;

    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final int REQUEST_VIDEO_PERMISSIONS = 932;
    private static final int CAPTURE_PHOTO = 368;
    private static final int RECORD_VIDEO = 369;

    private static final String UPLOAD_PHOTO_ACT = "photo";
    private static final String UPLOAD_VIDEO_ACT = "video";

    private UploadService uploadService;
    private NotificationUtil notificationUtil;

    String[] permissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capturePhoto = (Button) findViewById(R.id.btn_photo);
        recordVideo = (Button) findViewById(R.id.btn_video);

        capturePhoto.setOnClickListener(this);
        recordVideo.setOnClickListener(this);


        notificationUtil = new NotificationUtil(this);
    }

    @Override
    public void onClick(View view) {
        if (view == capturePhoto) {
            List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }

            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            }else {
                AnncaConfiguration.Builder photo = new AnncaConfiguration.Builder(this, CAPTURE_PHOTO);
                photo.setMediaAction(AnncaConfiguration.MEDIA_ACTION_PHOTO);
                photo.setMediaResultBehaviour(AnncaConfiguration.PREVIEW);

                new Annca(photo.build()).launchCamera();
            }
        } else if (view == recordVideo) {
            List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            }else {
                AnncaConfiguration.Builder videoLimited = new AnncaConfiguration.Builder(this, RECORD_VIDEO);
                videoLimited.setMediaAction(AnncaConfiguration.MEDIA_ACTION_VIDEO);
                videoLimited.setMediaQuality(AnncaConfiguration.MEDIA_QUALITY_LOW);
                videoLimited.setVideoDuration(2 * 1000); // video max duration
                videoLimited.setMediaResultBehaviour(AnncaConfiguration.PREVIEW);

                new Annca(videoLimited.build()).launchCamera();
            }
        }
    }

    private void uploadPhoto(File file) {
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("file",
                file.getName(), photoBody);

        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), UPLOAD_PHOTO_ACT);

        notificationUtil.showNotification("Upload Foto", "Mengirim...");
        notificationUtil.showNotificationProgress();

        uploadService = new UploadService();
        uploadService.upload(action, photoPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                if(baseResponse != null) {
                    notificationUtil.showCompleteNotification(baseResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                notificationUtil.showCompleteNotification("Upload Failed");
            }
        });
    }

    private void uploadVideo(File file) {
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("file",
                file.getName(), videoBody);

        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), UPLOAD_VIDEO_ACT);

        notificationUtil.showNotification("Upload Video", "Mengirim...");
        notificationUtil.showNotificationProgress();

        uploadService = new UploadService();
        uploadService.upload(action, videoPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                if(baseResponse != null) {
                    notificationUtil.showCompleteNotification(baseResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                notificationUtil.showCompleteNotification("Upload Failed");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) return;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_PHOTO && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH);
            File file = new File(filePath);

            uploadPhoto(file);
        }else if(requestCode == RECORD_VIDEO && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(AnncaConfiguration.Arguments.FILE_PATH);
            File file = new File(filePath);

            uploadVideo(file);
        }
    }
}
