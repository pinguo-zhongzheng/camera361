package pg.com.camera361.Camera.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import pg.com.camera361.Camera.CameraConstants;
import pg.com.camera361.Camera.CameraController;
import pg.com.camera361.R;

/**
 * Created by zhongzheng on 14-10-8.
 */
public class BottomViewManager {
    private Fragment mFragment;

    private ImageButton mStart;
    private ImageButton mShareToAlbum;

    public BottomViewManager(Fragment fragment) {
        mFragment = fragment;
        if (mFragment.getTag().contains(CameraConstants.Camera_Type)) {
            registerCameraEventListenler();
        }
    }

    private void registerCameraEventListenler() {
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

        mShareToAlbum = (ImageButton) mFragment.getView().findViewById(R.id.share_to_album);
        mShareToAlbum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(mFragment.getActivity(), pg.com.camera361.Album.Album.class);
//                    intent.putExtra(CameraConstants.Extra.FRAGMENT_INDEX, ImageGalleryFragment.INDEX);
                    mFragment.getActivity().startActivity(intent);
                }
                return false;
            }
        });
    }
}
