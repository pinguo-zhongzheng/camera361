package pg.com.camera361;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
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

public class TopControl extends Fragment {
    boolean mDualPane;
    int mCurCheckPosition = 0;

    private PopupWindow mPopupwindow;
    private PopupMenu mPopMenu;
    private boolean popupWindowHasInit = false;

    List<Camera.Size> mPreviewSizes;

    private BroadcastReceiver mEventManager = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 相关处理，如收短信，监听电量变化信息
            if (!intent.getStringExtra("resolution").isEmpty()){
                if(!popupWindowHasInit){
                    initPopupWindow();
                    popupWindowHasInit = true;
                }
                if (mPopupwindow != null&&!mPopupwindow.isShowing()) {

                    ImageButton resolution = (ImageButton)getActivity().findViewById(R.id.resolution);
                    mPopupwindow.showAsDropDown(resolution);
                    return;
                } else {
                    mPopupwindow.dismiss();
                }
            }
        }
    };

    public void initmPopupWindowView() {

        // // 获取自定义布局文件pop.xml的视图
        // 创建PopupWindow实例,200,150分别是宽度和高度
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        //mPopupwindow.setAnimationStyle(R.style.resolution_font);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.top_control);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        EventListener.getInstance().init(getActivity());
        IntentFilter intentFilter = new IntentFilter( "topControl.eventManager" );
        getActivity().registerReceiver(mEventManager, intentFilter);
    }

    private void initPopupWindow(){
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setBackgroundColor(Color.GRAY);
        layout.setOrientation(LinearLayout.VERTICAL);

        RadioGroup group = new RadioGroup(getActivity());
        group.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
        group.setOrientation(LinearLayout.VERTICAL);

        mPreviewSizes = CameraController.getInstance().getSupportedPreviewSizes();
        for(Camera.Size size : mPreviewSizes){
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
                for(int i = 0; i < numOfRadio; i++){
                    if(group.getChildAt(i).getId() == checkedId){
                        Camera.Size size = mPreviewSizes.get(i);
                        if (mPopupwindow != null && mPopupwindow.isShowing()){
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

        TextView tv = new TextView(getActivity());
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

    private void addResolutionItem(ViewGroup parent, String text){
        RadioButton button = new RadioButton(getActivity());
        button.setText(text);
        button.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                50));
        parent.addView(button);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("LeftFragment onCreateView");
        // 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
        return inflater.inflate(R.layout.top_control_layout, container, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageButton resolution = (ImageButton) getView().findViewById(R.id.resolution);
        EventListener.getInstance().setTouchAndClickListener(resolution);

        ImageButton cameraSwitching = (ImageButton) getView().findViewById(R.id.camera_switching);
        EventListener.getInstance().setTouchAndClickListener(cameraSwitching);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
}
