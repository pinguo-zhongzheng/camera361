package pg.com.camera361.Camera.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pg.com.camera361.Camera.CameraConstants;
import pg.com.camera361.R;

public class TopControl extends Fragment {
    boolean mDualPane;
    int mCurCheckPosition = 0;

    private TopViewManager mViewManager;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("LeftFragment onCreateView");
        // 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
        if (getTag().contains(CameraConstants.Album_Type)) {
            return inflater.inflate(R.layout.album_top_layout, container, true);
        } else {
            return inflater.inflate(R.layout.top_control_layout, container, true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewManager = new TopViewManager(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
}
