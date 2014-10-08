package pg.com.camera361;

import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by zhongzheng on 14-10-8.
 */
public class BottomViewManager {
    private Fragment mFragment;

    private ImageButton mStart;

    public BottomViewManager(Fragment fragment) {
        mFragment = fragment;
        registerEventListenler();
    }

    private void registerEventListenler() {
        mStart = (ImageButton) mFragment.getView().findViewById(R.id.start);
        mStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    CameraController.getInstance().takePicture();
                }
                return false;
            }
        });
    }
}
