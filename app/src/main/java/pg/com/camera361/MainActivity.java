package pg.com.camera361;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("Camera361", "Error creating media file, check storage permissions!");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
//                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("Camera361", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Camera361", "Error accessing file: " + e.getMessage());
            }
        }
    };
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i("camera361", "myShutterCallback:onShutter...");

        }
    };
    private Camera mCamera;
    private CameraPreview mPreview;
    private int numberOfCameras;
    private int cameraCurrentlyLocked;
    // The first rear facing camera
    private int defaultCameraId;
    private Uri fileUri;
    private boolean isAutoFousSet = false;
    private Bitmap mBitmap;
    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            // TODO Auto-generated method stub
            if (success) {
                Log.i("camera361", "myAutoFocusCallback: success...");
                //myCamera.setOneShotPreviewCallback(null);
            } else {
                Log.i("camera361", "myAutoFocusCallback: fail...");
            }
        }
    };

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Camera361", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = this.getWindow();
        myWindow.setFlags(flag, flag);

        setContentView(R.layout.camera_preview);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.autoFocus(mAutoFocusCallback);
                        if (mCamera != null) {
                            mCamera.takePicture(null, null, mPicture);
                        }
                    }
                }
        );

        // Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }

    }

    private void startCameraAndAutoFocus(){
        if (!isAutoFousSet) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            isAutoFousSet = true;
        }
        mPreview.switchCamera(mCamera);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Open the default i.e. the first rear facing camera.
        // Create an instance of Camera
        mCamera = getCameraInstance();
        cameraCurrentlyLocked = defaultCameraId;
        startCameraAndAutoFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.switchCamera(null);
            mCamera.release();
            mCamera = null;
            isAutoFousSet = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate our menu which can gather user input for switching camera
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camera_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.switch_cam:
                // check for availability of multiple cameras
                if (numberOfCameras == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(this.getString(R.string.camera_alert))
                            .setNeutralButton("Close", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }

                // OK, we have multiple cameras.
                // Release this camera -> cameraCurrentlyLocked
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mPreview.switchCamera(null);
                    mCamera.release();
                    mCamera = null;
                }

                // Acquire the next camera and request Preview to reconfigure
                // parameters.
                mCamera = Camera
                        .open((cameraCurrentlyLocked + 1) % numberOfCameras);
                cameraCurrentlyLocked = (cameraCurrentlyLocked + 1)
                        % numberOfCameras;
//                isAutoFousSet = false;
                startCameraAndAutoFocus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    public class MyOnTouchListener implements View.OnTouchListener {

        public final float[] BT_SELECTED = new float[]
                {2, 0, 0, 0, 2,
                        0, 2, 0, 0, 2,
                        0, 0, 2, 0, 2,
                        0, 0, 0, 1, 0};

        public final float[] BT_NOT_SELECTED = new float[]
                {1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0};

        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
                v.setBackgroundDrawable(v.getBackground());

            }
            return false;
        }

    }
}
