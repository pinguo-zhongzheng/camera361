package pg.com.camera361;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private CameraController mCameraController;

    public CameraPreview(Context context) {
        super(context);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraController = CameraController.getInstance();
    }

    public void setDisplayOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        mCameraController.setCameraDisplayOrientation(degrees);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        Log.d(CameraConstants.TAG, "cameraPreview surfaceCreated");
        mCameraController.initCamera(getContext());
        mCameraController.initSupportedPreviewSizes();
        mCameraController.bindSurface(mHolder);
        mCameraController.startCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        Log.d(CameraConstants.TAG, "cameraPreview surfaceChanged");
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        try {
            mCameraController.stopCamera();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        mCameraController.setSupportCameraSize(w, h);

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCameraController.bindSurface(mHolder);
            mCameraController.startCamera();

        } catch (Exception e) {
            Log.d(CameraConstants.TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        Log.d(CameraConstants.TAG, "cameraPreview onMeasure");

        if (mCameraController.getSupportedPreviewSizes() != null) {
            mCameraController.stopCamera();
            mCameraController.setSupportCameraSize(width, height);
            mCameraController.startCamera();
        }
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        Log.d(CameraConstants.TAG,"cameraPreview onLayout");
////        if (changed) {
////            final View child = getChildAt(0);
////
////            final int width = r - l;
////            final int height = b - t;
////
////            int previewWidth = width;
////            int previewHeight = height;
////            Camera.Size size = mCameraController.getCurrentPreviewSize();
////            if (size != null) {
////                previewWidth = size.width;
////                previewHeight = size.height;
////            }
////
////            // Center the child SurfaceView within the parent.
////            if (width * previewHeight > height * previewWidth) {
////                final int scaledChildWidth = previewWidth * height / previewHeight;
////                child.layout((width - scaledChildWidth) / 2, 0,
////                        (width + scaledChildWidth) / 2, height);
////            } else {
////                final int scaledChildHeight = previewHeight * width / previewWidth;
////                child.layout(0, (height - scaledChildHeight) / 2,
////                        width, (height + scaledChildHeight) / 2);
////            }
////        }
//    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        Log.d(CameraConstants.TAG, "cameraPreview surfaceDestroy");
        mCameraController.releaseCamera();
    }
}