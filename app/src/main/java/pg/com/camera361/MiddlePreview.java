package pg.com.camera361;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by zhongzheng on 14-9-29.
 */
public class MiddlePreview extends Fragment {
    boolean mDualPane;
    int mCurCheckPosition = 0;
    private CameraPreview mPreview;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.middle_preview);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("LeftFragment onCreateView");
        // Create our Preview view and set it as the content of our activity.
        // 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
        return inflater.inflate(R.layout.middle_preview_layout, container, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPreview = new CameraPreview(getActivity().getBaseContext());
        FrameLayout preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
}