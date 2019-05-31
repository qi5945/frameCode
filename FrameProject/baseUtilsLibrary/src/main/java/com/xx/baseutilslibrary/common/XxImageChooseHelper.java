package com.xx.baseutilslibrary.common;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 图片选择工具
 * 作者: LeiXiaoXing on 2016/9/30 11:26
 */


public class XxImageChooseHelper {
    /**
     * 相机获取图片
     */
    private final int REQUEST_CODE_PHOTO = 1;
    /**
     * 图库获取图片
     */
    private final int REQUEST_CODE_ALBUM = 2;
    /**
     * 图片裁剪
     */
    private final int REQUEST_CODE_CROP = 3;
    /**
     * 头像文件
     */
    private File file;
    /**
     * 是否开启裁剪
     * 默认开启
     */
    private boolean isCrop = true;
    /**
     * 使用弱引用保存Activity实例
     */
    private WeakReference<Activity> activityWeakReference;
    private WeakReference<FragmentActivity> fragmentActivityWeakReference;
    private WeakReference<Fragment> fragmentWeakReference;
    private OnFinishChooseImageListener listener;
    private OnFinishChooseAndCropImageListener mOnFinishChooseAndCropImageListener;
    private int width, height;
    private int compressQuality = 100;//文件压缩比率
    private String dirPath;//文件存储路径
    private String authority;//FileProvider路径配置
    private Uri mTempUri;//临时图片Uri

    private Activity mActivity;
    private Fragment mFragment;

    private XxImageChooseHelper(Activity activity) {
        mActivity = activity;
        activityWeakReference = new WeakReference<>(activity);
    }

    private XxImageChooseHelper(Fragment fragment) {
        mFragment = fragment;
        fragmentWeakReference = new WeakReference<>(fragment);
        fragmentActivityWeakReference = new WeakReference<>(fragment.getActivity());
    }

    /**
     * 获取真实图片路径
     *
     * @param contentUri
     * @return
     */
    private static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return "";
        }
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public Context getContext() {
        return getActivity();
    }

    /**
     * Android N下获取文件Uri的正确姿势
     *
     * @param file 文件
     * @return
     */
    private Uri getUri(File file) {
        if (file == null)
            throw new NullPointerException("文件不存在");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (TextUtils.isEmpty(authority)) {
                throw new NullPointerException("Provider路径未配置");
            }
            return FileProvider.getUriForFile(getActivity(),
                    authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 获取弱引用中的Activity实例
     *
     * @return activity
     */
    private Activity getActivity() {
        if (fragmentActivityWeakReference != null) {
            if (fragmentActivityWeakReference.get() == null)
                fragmentActivityWeakReference = new WeakReference<FragmentActivity>(mFragment.getActivity());
            return fragmentActivityWeakReference.get();
        }
        if (activityWeakReference.get() == null)
            activityWeakReference = new WeakReference<Activity>(mActivity);
        return activityWeakReference.get();
    }

    /**
     * 初始化文件名
     */
    private File initFile(String fileName) {
        File dir;
        if (TextUtils.isEmpty(dirPath)) {
            //如果没有配置,默认使用外置存储
            dir = Environment.getExternalStorageDirectory();
        } else {
            dir = new File(dirPath);
            if (!dir.exists())
                dir.mkdirs();
        }
        file = new File(dir, fileName);
        return file;
    }

    /**
     * 进入相机
     */
    public void startCamearPic() {
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", false);// 全屏
        intent.putExtra("showActionIcons", false);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        // 指定调用相机拍照后照片的储存路径
        initFile(System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(file));

        if (fragmentWeakReference != null) {
            fragmentWeakReference.get().startActivityForResult(intent, REQUEST_CODE_PHOTO);
        } else {
            if (getActivity() != null) {
                getActivity().startActivityForResult(intent, REQUEST_CODE_PHOTO);
            }
        }

    }

    /**
     * 进入图库选图
     */
    public void startImageChoose() {
        initFile("");
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");

        //进入选图
        if (fragmentWeakReference != null) {
            fragmentWeakReference.get().startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } else {
            getActivity().startActivityForResult(intent, REQUEST_CODE_ALBUM);
        }


    }

    /**
     * 获取图片的ContextUri
     *
     * @param imageFile 图片文件
     * @return ContextUri
     */
    private Uri getImageContentUri(File imageFile) {
        if (imageFile == null || TextUtils.isEmpty(imageFile.getAbsolutePath())) {
            ToastUtils.showShort("文件找不到");
            return null;
        }
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 裁剪照片
     *
     * @param uri 裁剪uri
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        String manufacturer = Build.MANUFACTURER;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        if (manufacturer.equals("HUAWEI")) {
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999 * height / width);
        } else {
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", width);
            intent.putExtra("aspectY", height);
        }


        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        //  intent.putExtra("noFaceDetection", true);

//        if ("Xiaomi".equals(manufacturer) || "HUAWEI".equals(manufacturer)) {
//            //适配小米后
//            mTempUri = Uri.parse("file:///" + dirPath + File.separator + System.currentTimeMillis() + ".jpg");
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri);
//        } else {
//            //适配小米前
//            intent.putExtra("return-data", true);
//        }
        mTempUri = Uri.parse("file:///" + dirPath + File.separator + System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());


        if (fragmentWeakReference != null) {
            fragmentWeakReference.get().startActivityForResult(intent, REQUEST_CODE_CROP);
        } else {
            getActivity().startActivityForResult(intent, REQUEST_CODE_CROP);
        }
    }

    /**
     * 删除临时文件
     *
     * @return
     */
    private void delFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_PHOTO:
                if (isCrop) {
                    startPhotoZoom(getImageContentUri(file));
                } else {
                    if (listener != null) {
                        //相机，不裁剪,直接返回Uri和照片文件
                        listener.onFinish(getImageContentUri(file), file);
                    }
                }
                break;
            case REQUEST_CODE_ALBUM:

                if (data == null) {
                    return;
                }
                if (isCrop) {
                    startPhotoZoom(data.getData());
                } else {
                    if (listener != null) {
                        //不裁剪,直接返回Uri
//                        listener.onFinish(data.getData(), new File(getRealPathFromURI(getActivity(), data.getData())));
                        Uri picUri = geturi(data);
                        listener.onFinish(picUri, new File(getRealPathFromURI(getActivity(), picUri)));
                    }
                }
                break;
            case REQUEST_CODE_CROP:

                try {
                    Bitmap photo = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mTempUri));
                    File file = new File(new URI(mTempUri.toString()));
                    //通知图库有更新
                    getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                    //裁剪过后返回Bitmap,处理生成文件用来上传
                    mOnFinishChooseAndCropImageListener.onFinish(photo, file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

//                String manufacturer = Build.MANUFACTURER;
//                if (!"Xiaomi".equals(manufacturer) && !"HUAWEI".equals(manufacturer)) {
//                    //适配小米前
//                    if (data == null) {
//                        return;
//                    }
//                    if (data.getExtras() != null) {
//                        Bundle bundle = data.getExtras();
//                        Bitmap photo = bundle.getParcelable("data");
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        if (photo != null) {
//                            photo.compress(Bitmap.CompressFormat.PNG, compressQuality, baos);
//                        }
//
//                        FileOutputStream fos = null;
//                        if (mOnFinishChooseAndCropImageListener != null) {
//
//                            try {
//                                if (file != null) {
//                                    file.getParentFile().delete();//删除照片
//                                }
//                                //将裁剪出来的Bitmap转换成本地文件
//                                File file = initFile(System.currentTimeMillis() + ".png");
//                                fos = new FileOutputStream(file);
//                                fos.write(baos.toByteArray());
//                                fos.flush();
//                                //通知图库有更新
//                                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
//
//                                //裁剪过后返回Bitmap,处理生成文件用来上传
//                                mOnFinishChooseAndCropImageListener.onFinish(photo, file);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } finally {
//                                if (fos != null)
//                                    try {
//                                        fos.close();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                if (baos != null)
//                                    try {
//                                        baos.close();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                            }
//                        }
//
//                    }
//                } else {
//                    //适配小米后
//                    try {
//                        Bitmap photo = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mTempUri));
//                        File file = new File(new URI(mTempUri.toString()));
//                        //通知图库有更新
//                        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
//                        //裁剪过后返回Bitmap,处理生成文件用来上传
//                        mOnFinishChooseAndCropImageListener.onFinish(photo, file);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                }
                break;
        }
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    private Uri geturi(Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = getActivity().getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }


    /**
     * 裁剪图片完成回调监听
     */
    public interface OnFinishChooseAndCropImageListener {
        void onFinish(Bitmap bitmap, File file);
    }

    /**
     * 仅选图完成回调监听
     */
    public interface OnFinishChooseImageListener {
        void onFinish(Uri uri, File file);
    }

    /**
     * 构造者
     */
    static public class Builder {

        private XxImageChooseHelper xxImageChooseHelper;

        public Builder setUpFragment(Fragment fragment) {
            xxImageChooseHelper = new XxImageChooseHelper(fragment);
            return this;
        }

        public Builder setUpActivity(Activity activity) {
            xxImageChooseHelper = new XxImageChooseHelper(activity);
            return this;
        }

        public Builder setAuthority(String authority) {
            xxImageChooseHelper.authority = authority;
            return this;
        }

        /**
         * 设置存储路径
         *
         * @param dirPath 存储路径
         * @return
         */
        public Builder setDirPath(String dirPath) {
            xxImageChooseHelper.dirPath = dirPath;
            return this;
        }

        public Builder isCrop(boolean isCrop) {
            xxImageChooseHelper.isCrop = isCrop;
            return this;
        }

        public Builder setSize(int width, int height) {
            xxImageChooseHelper.width = width;
            xxImageChooseHelper.height = height;
            return this;
        }

        public Builder setCompressQuality(int compressQuality) {
            xxImageChooseHelper.compressQuality = compressQuality;
            return this;
        }


        /**
         * 图片裁剪返回监听
         *
         * @param listener
         */
        public Builder setOnFinishChooseImageListener(OnFinishChooseImageListener listener) {
            xxImageChooseHelper.listener = listener;
            return this;
        }

        /**
         * 设置选图裁剪回调监听
         *
         * @param onFinishChooseAndCropImageListener 选图裁剪回调监听
         */
        public Builder setOnFinishChooseAndCropImageListener(OnFinishChooseAndCropImageListener onFinishChooseAndCropImageListener) {
            xxImageChooseHelper.mOnFinishChooseAndCropImageListener = onFinishChooseAndCropImageListener;
            return this;
        }

        public XxImageChooseHelper create() {
            return xxImageChooseHelper;
        }
    }
}
