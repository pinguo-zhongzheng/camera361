package pg.com.camera361;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhongzheng on 14-10-8.
 */
public class MiddleViewManager {
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CameraConstants.SEEKBAR_TIMER:
                    mSeekBar.setVisibility(View.GONE);
                    stopTimer();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Fragment mFragment;
    private Timer mTimer;
    private CameraPreview mPreview;
    private SeekBar mSeekBar;
    private CameraController mController;
    private boolean isInitSeekBar;
    private boolean enableSeekBar;
    private TimerTask mTimerTask;

    public MiddleViewManager(Fragment fragment) {
        mFragment = fragment;
        mController = CameraController.getInstance();
        initView();
        registerEventListenler();
    }

    private void initView() {
        mPreview = new CameraPreview(mFragment.getActivity().getBaseContext());
        FrameLayout preview = (FrameLayout) mFragment.getView().findViewById(R.id.camera_preview);
        preview.addView(mPreview, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSeekBar = (SeekBar) mFragment.getView().findViewById(R.id.seek);
        mSeekBar.setVisibility(View.GONE);
    }

    private void initSeekBar() {
        int maxZoom = mController.getMaxZoom();
        if (maxZoom > 0) {
            enableSeekBar = true;
            mSeekBar.setMax(maxZoom);
            mSeekBar.setProgress(0);
            mSeekBar.setVisibility(View.VISIBLE);
        } else {
            enableSeekBar = false;
        }
        isInitSeekBar = true;
    }

    private void registerEventListenler() {
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(CameraConstants.TAG, "preview onClick!!");
                if (!isInitSeekBar) {
                    initSeekBar();
                }
                stopTimer();
                startTimer();

                if (mSeekBar.getVisibility() == View.GONE) {
                    mSeekBar.setVisibility(View.VISIBLE);
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                stopTimer();
                startTimer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopTimer();
                startTimer();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                stopTimer();
                startTimer();
                mController.setZoom(seekBar.getProgress());
            }
        });
    }

    private void startTimer() {
        if (enableSeekBar) {
            if (mTimer == null) {
                mTimer = new Timer();
            }

            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Log.i(CameraConstants.TAG, "send message!!!");
                        sendMessage(CameraConstants.SEEKBAR_TIMER);
                    }
                };
            }

            if (mTimer != null && mTimerTask != null)
                mTimer.schedule(mTimerTask, 2000);
        }
    }

    private void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }

    public void sendMessage(int id) {
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }
}
