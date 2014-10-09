/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package pg.com.camera361.Album.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import pg.com.camera361.Album.ImageManager;
import pg.com.camera361.R;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class BaseFragment extends Fragment {

    protected DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ImageManager.getInstance().init(this);
        ImageManager.getInstance().initDefaultImageLoaderConfig();

        //使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片

//        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//        getAlbum.setType(AlbumConstants.IMAGE_TYPE);
//        startActivityForResult(getAlbum, AlbumConstants.IMAGE_CODE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        if (resultCode == AlbumConstants.IMAGE_CODE) {
//            Bitmap bm = null;
//            //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
//            ContentResolver resolver = getActivity().getContentResolver();
//            //此处的用于判断接收的Activity是不是你想要的那个
//            try {
//                Uri originalUri = data.getData();        //获得图片的uri
//                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片
//                //这里开始的第二部分，获取图片的路径：
//                String[] proj = {MediaStore.Images.Media.DATA};
//                //好像是android多媒体数据库的封装接口，具体的看Android文档
//                Cursor cursor = getActivity().managedQuery(originalUri, proj, null, null, null);
//                //按我个人理解 这个是获得用户选择的图片的索引值
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                //将光标移至开头 ，这个很重要，不小心很容易引起越界
//                cursor.moveToFirst();
//                //最后根据索引值获取图片路径
//                String path = cursor.getString(column_index);
//            }catch (IOException e) {
//                Log.e(AlbumConstants.TAG,e.toString());
//            }
//            return;
//        }else {
//
//        }
//    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_clear_memory_cache:
                ImageLoader.getInstance().clearMemoryCache();
                return true;
            case R.id.item_clear_disc_cache:
                ImageLoader.getInstance().clearDiskCache();
                return true;
            default:
                return false;
        }
    }
}
