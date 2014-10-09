package pg.com.camera361.Camera.Fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import pg.com.camera361.Camera.CameraConstants;
import pg.com.camera361.Camera.CameraController;
import pg.com.camera361.R;

/**
 * Created by zhongzheng on 14-10-8.
 */
public class TopViewManager {
    private Fragment mFragment;

    private PopupWindow mPopupwindow;
    private PopupMenu mPopMenu;
    private boolean popupWindowHasInit = false;

    private List<Camera.Size> mPreviewSizes;

    private ImageButton mResolution;
    private ImageButton mCameraSwitching;

    private ImageButton mBackToCamera;

    public TopViewManager(Fragment fragment) {
        mFragment = fragment;
        if (mFragment.getTag().contains(CameraConstants.Camera_Type)) {
            registerCameraEventListenler();
        } else if (mFragment.getTag().contains(CameraConstants.Album_Type)) {
            registerAlbumEventListenler();
        }
    }

    private void registerCameraEventListenler() {
        mResolution = (ImageButton) mFragment.getView().findViewById(R.id.resolution);
        mResolution.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!popupWindowHasInit) {
                        initPopupWindow();
                        popupWindowHasInit = true;
                    }
                    if (mPopupwindow != null && !mPopupwindow.isShowing()) {
                        mPopupwindow.showAsDropDown(mResolution);
                    } else {
                        mPopupwindow.dismiss();
                    }
                }
                return false;
            }
        });

        mCameraSwitching = (ImageButton) mFragment.getView().findViewById(R.id.camera_switching);
        mCameraSwitching.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    CameraController.getInstance().changeCamera();
                }
                return false;
            }
        });
    }

    private void initPopupWindow() {
        LinearLayout layout = new LinearLayout(mFragment.getActivity());
        layout.setBackgroundColor(Color.GRAY);
        layout.setOrientation(LinearLayout.VERTICAL);

        RadioGroup group = new RadioGroup(mFragment.getActivity());
        group.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
        group.setOrientation(LinearLayout.VERTICAL);

        mPreviewSizes = CameraController.getInstance().getSupportedPreviewSizes();
        for (Camera.Size size : mPreviewSizes) {
            StringBuilder text = new StringBuilder();
            text.append(size.width);
            text.append(" * ");
            text.append(size.height);
            addResolutionItem(group, text.toString());
        }
        group.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int numOfRadio = group.getChildCount();
                for (int i = 0; i < numOfRadio; i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        Camera.Size size = mPreviewSizes.get(i);
                        if (mPopupwindow != null && mPopupwindow.isShowing()) {
                            mPopupwindow.dismiss();
                        }
                        CameraController controller = CameraController.getInstance();
                        controller.stopCamera();
                        controller.setSupportCameraSize(size.width, size.height);
                        controller.startCamera();
                    }
                }
            }
        });

        TextView tv = new TextView(mFragment.getActivity());
        tv.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
        tv.setText("resolution: ");
        tv.setTextColor(Color.WHITE);

        layout.addView(tv);
        layout.addView(group);

        mPopupwindow = new PopupWindow(layout, 280,
                WindowManager.LayoutParams.WRAP_CONTENT, false);
        mPopupwindow.setFocusable(true);
        mPopupwindow.setOutsideTouchable(true);
        mPopupwindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupwindow.getBackground().setVisible(false, false);
        mPopupwindow.getBackground().setAlpha(50);
    }

    private void addResolutionItem(ViewGroup parent, String text) {
        RadioButton button = new RadioButton(mFragment.getActivity());
        button.setText(text);
        button.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                50));
        parent.addView(button);
    }

    private void registerAlbumEventListenler() {
        mBackToCamera = (ImageButton) mFragment.getView().findViewById(R.id.back_to_camera);
        mBackToCamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mFragment.getActivity().finish();
                }
                return false;
            }
        });
    }
}
