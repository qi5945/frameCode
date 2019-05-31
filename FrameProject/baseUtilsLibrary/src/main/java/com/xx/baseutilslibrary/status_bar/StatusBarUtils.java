package com.xx.baseutilslibrary.status_bar;

import android.app.Activity;
import android.content.Context;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import com.xx.baseutilslibrary.status_bar.bar.MeizuStatusBar;
import com.xx.baseutilslibrary.status_bar.bar.MiuiStatusbar;
import com.xx.baseutilslibrary.status_bar.bar.OSMStatusBar;


/**
 * StatusBarUtils
 * (๑• . •๑)
 * 类描述:状态栏字体颜色修改工具
 * Created by 雷小星🍀 on 2017/6/21 09:52
 */

public class StatusBarUtils {
    /**
     * 对Activity应用字体颜色
     *
     * @param activity   要修改状态栏字体颜色的Activity
     * @param isDarkFont 是否黑色字体
     */
    public static boolean apply(Activity activity, boolean isDarkFont) {
        if (new MeizuStatusBar().setStatusBarLightMode(activity, isDarkFont)) {
            return true;
        } else if (new OSMStatusBar().setStatusBarLightMode(activity, isDarkFont)) {
            return true;
        }
        return false;
    }

    /**
     * 状态栏颜色
     *
     * @param activity
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    /**
     * 状态栏颜色
     * 用这个方法设置，状态栏字体会变黑色
     *
     * @param activity
     */
    public static void setStatusBarColor2(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
