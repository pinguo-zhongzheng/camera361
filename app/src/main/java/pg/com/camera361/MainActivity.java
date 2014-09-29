package pg.com.camera361;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends FragmentActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i("camera361", "myShutterCallback:onShutter...");

        }
    };
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = this.getWindow();
        myWindow.setFlags(flag, flag);
        setContentView(R.layout.main_layout);
    }

    @Override
    protected void onStart() {
        super.onResume();
        Log.d(CameraConstants.TAG, "mainActivity onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(CameraConstants.TAG, "mainActivity onResume");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(CameraConstants.camera_alert)
//                .setNeutralButton("Close", null);
//        AlertDialog alert = builder.create();
//        alert.show();
        // Open the default i.e. the first rear facing camera.
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(CameraConstants.TAG, "mainActivity onPause");
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
    }

    @Override
    protected void onStop() {
        super.onPause();
        Log.d(CameraConstants.TAG, "mainActivity onStop");
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
    }

//    public class MyOnTouchListener implements View.OnTouchListener {
//
//        public final float[] BT_SELECTED = new float[]
//                {2, 0, 0, 0, 2,
//                        0, 2, 0, 0, 2,
//                        0, 0, 2, 0, 2,
//                        0, 0, 0, 1, 0};
//
//        public final float[] BT_NOT_SELECTED = new float[]
//                {1, 0, 0, 0, 0,
//                        0, 1, 0, 0, 0,
//                        0, 0, 1, 0, 0,
//                        0, 0, 0, 1, 0};
//
//        public boolean onTouch(View v, MotionEvent event) {
//            // TODO Auto-generated method stub
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
//                v.setBackgroundDrawable(v.getBackground());
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
//                v.setBackgroundDrawable(v.getBackground());
//
//            }
//            return false;
//        }
//
//    }
}
