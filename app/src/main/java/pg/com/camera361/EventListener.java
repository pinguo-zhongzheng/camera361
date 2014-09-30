package pg.com.camera361;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhongzheng on 14-9-29.
 */
class EventListener implements View.OnClickListener, View.OnTouchListener {
    private static EventListener mEventListener = new EventListener();
    private Activity mActivity;

    private EventListener() {
    }

    public static EventListener getInstance() {
        return mEventListener;
    }

    public void init(Activity activity){
        mActivity = activity;
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
                    CameraController.getInstance().changeCamera();
                }
                return false;
            case R.id.resolution:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(CameraConstants.TAG, "resolution onTouch down");
                    Intent intent = new Intent( "topControl.eventManager" );
                    intent.putExtra("resolution", "down");
                    mActivity.sendBroadcast(intent);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                }
                return false;
            case R.id.start:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(CameraConstants.TAG, "start onTouch down");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    CameraController.getInstance().takePicture();
                }
                return false;
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
