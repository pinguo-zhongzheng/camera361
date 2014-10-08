package pg.com.camera361;

import android.app.Activity;
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

    public void init(Activity activity) {
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
            case R.id.camera_preview:
                break;
        }
    }

}
