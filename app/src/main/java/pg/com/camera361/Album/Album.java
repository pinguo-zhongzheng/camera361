package pg.com.camera361.Album;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import pg.com.camera361.Camera.CameraConstants;
import pg.com.camera361.R;


/**
 * Created by zhongzheng on 14-10-8.
 */
public class Album extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.album_layout);
    }


    @Override
    protected void onStart() {
        super.onResume();
        Log.d(CameraConstants.TAG, "Album onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(CameraConstants.TAG, "Album onResume");
        // Open the default i.e. the first rear facing camera.
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(CameraConstants.TAG, "Album onPause");
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(CameraConstants.TAG, "Album onDestroy");
    }
}
