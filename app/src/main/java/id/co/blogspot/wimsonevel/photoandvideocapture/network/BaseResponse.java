package id.co.blogspot.wimsonevel.photoandvideocapture.network;

/**
 * Created by Wim on 10/15/17.
 */

public class BaseResponse {

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
