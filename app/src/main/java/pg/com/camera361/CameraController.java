package pg.com.camera361;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhongzheng on 14-9-25.
 */
public class CameraController {
    private static CameraController mController = new CameraController();
    private Camera mCamera;
    private int numberOfCameras;
    private int cameraCurrentlyLocked;
    // The first rear facing camera
    private int defaultCameraId;
    private Uri fileUri;
    private boolean isAutoFousSet = false;
    private Bitmap mBitmap;
    private Camera.Size mPreviewSize;
    private List<Camera.Size> mSupportedPreviewSizes;
    private SurfaceHolder mHolder;
    private int mMaxZoom = -1;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(CameraConstants.MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d(CameraConstants.TAG, "Error creating media file, check storage permissions!");
                return;
            }

//            try {
//                FileOutputStream fos = new FileOutputStream(pictureFile);
//                fos.write(data);
//                fos.close();
//                Log.d(CameraConstants.TAG, "picture taken success!! ");
//            } catch (FileNotFoundException e) {
//                Log.d(CameraConstants.TAG, "File not found: " + e.getMessage());
//            } catch (IOException e) {
//                Log.d(CameraConstants.TAG, "Error accessing file: " + e.getMessage());
//            }
            Log.d(CameraConstants.TAG, "picture taken success!! ");
            startCamera();
        }
    };

    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            // TODO Auto-generated method stub
            if (success) {
                Log.i(CameraConstants.TAG, "myAutoFocusCallback: success...");
                //myCamera.setOneShotPreviewCallback(null);
            } else {
                Log.i(CameraConstants.TAG, "myAutoFocusCallback: fail...");
            }
        }
    };

    private CameraController(){}

    public static CameraController getInstance(){
        return mController;
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
        if (type == CameraConstants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == CameraConstants.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera getCameraInstance(int cameraInt) {
        Camera c = null;
        try {
            c = Camera.open(cameraInt); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("The target Camera has already in use!")
//                .setNeutralButton("Close", null);
//        AlertDialog alert = builder.create();
//        alert.show();
        }
        return c; // returns null if camera is unavailable
    }

    private void initDefaultCameraIndex() {
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

    public int getDefaultCameraIndex() {
        // Find the total number of cameras available

        // Find the ID of the default camera
        return defaultCameraId;
    }

    public void setAutoFocus() {
        if (!isAutoFousSet) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            isAutoFousSet = true;
        }
    }

    public void delAutoFocus() {
        if (isAutoFousSet) {
            Camera.Parameters parameters = mCamera.getParameters();
            //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            isAutoFousSet = false;
        }
    }

    public void initCamera(Context context) {
        initDefaultCameraIndex();
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
        mCamera.setDisplayOrientation(CameraConstants.preview_orientation);
        cameraCurrentlyLocked = defaultCameraId;
//        OrientationEventListener listener = new OrientationEventListener(context) {
//            @Override
//            public void onOrientationChanged(int orientation) {
//                if (orientation == ORIENTATION_UNKNOWN) return;
//                android.hardware.Camera.CameraInfo info =
//                        new android.hardware.Camera.CameraInfo();
//                android.hardware.Camera.getCameraInfo(cameraCurrentlyLocked, info);
//                orientation = (orientation + 45) / 90 * 90;
//                int rotation = 0;
//                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                    rotation = (info.orientation - orientation + 360) % 360;
//                } else {  // back-facing camera
//                    rotation = (info.orientation + orientation) % 360;
//                }
//                Camera.Parameters parameters = mCamera.getParameters();
//                parameters.setRotation(rotation);
//                mCamera.setParameters(parameters);
//            }
//        };
//        listener.enable();
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters.getSupportedFocusModes().contains("auto")) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        if (parameters.isZoomSupported()) {
            mMaxZoom = parameters.getMaxZoom();
            parameters.setZoom(0);
        }
        mCamera.setParameters(parameters);
    }

    private void initNextCamera(){
        initDefaultCameraIndex();
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
    }

    public void bindSurface(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mHolder = holder;
    }

    public void initSupportedPreviewSizes() {
        Camera.Parameters parameters = mCamera.getParameters();
        mSupportedPreviewSizes = getTargetSizes(parameters.getSupportedPreviewSizes(),
                parameters.getSupportedPictureSizes());
        if (!mSupportedPreviewSizes.isEmpty()) {
            mPreviewSize = mSupportedPreviewSizes.get(0);
        }
    }

    public void setPreviewSize() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        mCamera.setParameters(parameters);
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
        }
        mCamera = null;
        mSupportedPreviewSizes = null;
        mPreviewSize = null;
        isAutoFousSet = false;
    }

    public void startCamera() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    private void updateSupportSize(int w, int h) {
        mPreviewSize = getOptimalSize(w, h, mSupportedPreviewSizes);
    }

    public List<Camera.Size> getSupportedPreviewSizes() {
        return mSupportedPreviewSizes;
    }

    public void setSupportCameraSize(int w, int h) {
        updateSupportSize(w, h);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        mCamera.setParameters(parameters);
    }

    private List<Camera.Size> getTargetSizes(List<Camera.Size> sizes, List<Camera.Size> optSizes) {
        Camera.Size targetSize = null;
        List<Camera.Size> targetSizes = new ArrayList<Camera.Size>();
        for (Camera.Size size : optSizes) {
            targetSize = getOptimalSize(size.width, size.height, sizes);
            if (targetSize != null) {
                targetSizes.add(targetSize);
            }
        }
        return targetSizes;
    }

    private Camera.Size getOptimalSize(int w, int h, List<Camera.Size> sizes) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public Camera.Size getCurrentPreviewSize() {
        return mPreviewSize;
    }

    public int getCameraNums() {
        return Camera.getNumberOfCameras();
    }

    public void setCameraDisplayOrientation(int degrees) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(defaultCameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    public void switchCamera(Camera camera) {
//        Camera.Parameters parameters = camera.getParameters();
//        if (mSupportedPreviewSizes == null) {
//            mSupportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
//        }
//        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//        camera.setParameters(parameters);
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

    public void changeCamera(){
        if (numberOfCameras == 1) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(this.getString(R.string.camera_alert))
//                    .setNeutralButton("Close", null);
//            AlertDialog alert = builder.create();
//            alert.show();
            return ;
        }

        // OK, we have multiple cameras.
        // Release this camera -> cameraCurrentlyLocked
        if (mCamera != null) {
            stopCamera();
            releaseCamera();
        }

        // Acquire the next camera and request Preview to reconfigure
        // parameters.
        mCamera = getCameraInstance((cameraCurrentlyLocked + 1) % numberOfCameras);
        cameraCurrentlyLocked = (cameraCurrentlyLocked + 1)
                % numberOfCameras;
        initSupportedPreviewSizes();
        mCamera.setDisplayOrientation(CameraConstants.preview_orientation);
        bindSurface(mHolder);
        startCamera();
        return;
    }

    public void takePicture(){
        mCamera.autoFocus(mAutoFocusCallback);
        mCamera.takePicture(null,null,mPicture);
    }

    public void setZoom(int index) {
        if (index <= mMaxZoom) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setZoom(index);
            mCamera.setParameters(parameters);
        }
    }

    public int getMaxZoom() {
        return mMaxZoom;
    }
}
