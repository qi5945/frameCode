package com.micropole.librarybase.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.micropole.librarybase.R;

import java.io.ByteArrayOutputStream;


/**
 * @创建者 mingyan.su
 * @创建时间 2017/8/16 14:09
 * @类描述 ${TODO}基于glide的图片加载工具类
 */
public class ImageLoader {
    //
    private static final int DEFAULT_IMAGE = R.drawable.mis_default_error;
    private static final int ERROR_IMAGE = R.drawable.mis_default_error;

    private static final int IMAGE_AVATAR = R.drawable.ic_avatar;

    public static void loadToUrl(Context context, final ImageView imageView, String url) {
        Glide.with(context).
                load(url)//图片的url
                .error(ERROR_IMAGE)//加载失败的图片
                .placeholder(DEFAULT_IMAGE) //默认的占位图片
                .centerCrop()//图片的显示方式。这里在是居中裁剪
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存策略
                .into(imageView);
    }

    /**
     * 去掉居中裁剪和动画
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadToUrl2(Context context, final ImageView imageView, String url) {
        Glide.with(context).
                load(url)//图片的url
                .error(ERROR_IMAGE)//加载失败的图片
                .placeholder(DEFAULT_IMAGE) //默认的占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadAvatar(Context context, final ImageView imageView, String url) {
        Glide.with(context).
                load(url)//图片的url
                .error(IMAGE_AVATAR)//加载失败的图片
                .placeholder(IMAGE_AVATAR) //默认的占位图片
                .centerCrop()//图片的显示方式。这里在是居中裁剪
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存策略
                .into(imageView);
    }

    /**
     * 把图片压缩成Base64传给后台
     *
     * @param path
     * @return
     */
    public static String Bitmap2StrByBase64(String path) {
        Bitmap bit = changeRotateImgFile(path);
        int options = 100;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, bos);//参数100表示不压缩
        int bosLength = bos.toByteArray().length;
        while (bosLength / 1024 / 1024 >= 1) {//循环判断如果压缩后图片是否大于maxMemmorrySize,大于继续压缩
            bos.reset();//重置baos即让下一次的写入覆盖之前的内容
            options -= 10; //图片质量每次减少10
            bit.compress(Bitmap.CompressFormat.JPEG, options, bos);//将压缩后的图片保存到baos中
            bosLength = bos.toByteArray().length;
            if (options == 0)//如果图片的质量已降到最低则，不再进行压缩
                break;
        }
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 修改有些手机旋转图片的bug
     *
     * @return 图片文件
     */
    public static Bitmap changeRotateImgFile(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) return null;
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imgPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }

            Bitmap returnBm;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            // 根据旋转角度，生成旋转矩阵
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (returnBm == null) {
                returnBm = bitmap;
            }
            if (bitmap != returnBm) {
                bitmap.recycle();
            }
            return returnBm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
