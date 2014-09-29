package pg.com.camera361;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhongzheng on 14-9-29.
 */
class EventListener implements View.OnClickListener, View.OnTouchListener {
    private static EventListener mEventListener = new EventListener();

    private EventListener() {
    }

    public static EventListener getInstance() {
        return mEventListener;
    }

    public void setTouchAndClickListener(View view) {
        view.setOnTouchListener(mEventListener);
        view.setOnClickListener(mEventListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.camera_switching:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(CameraConstants.TAG, "camera_switching onTouch down");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                }
                break;
            case R.id.resolution:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(CameraConstants.TAG, "resolution onTouch down");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                }
                break;
            case R.id.start:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(CameraConstants.TAG, "start onTouch down");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.camera_switching:
                break;
            case R.id.resolution:
                break;
            case R.id.start:
                break;
        }
    }

}
