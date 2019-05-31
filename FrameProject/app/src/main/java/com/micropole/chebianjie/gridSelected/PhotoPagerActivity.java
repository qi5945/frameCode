package com.micropole.chebianjie.gridSelected;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.micropole.chebianjie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donglua on 15/6/24.
 * 照片预览
 */
public class PhotoPagerActivity extends AppCompatActivity {

    private ImagePagerFragment pagerFragment;
    private ImageView img_back;
    private TextView tv_text;
    private ImageView img_delete;
    private View view_status_bar;

    public final static String EXTRA_CURRENT_ITEM = "current_item";
    public final static String EXTRA_PHOTOS = "photos";
    public final static String HIDE_DELETE_BUTTON = "HIDE_DELETE_BUTTON";
    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";
    private boolean deleteButton = false;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        setContentView(R.layout.activity_photo_pager);
        view_status_bar = (View) findViewById(R.id.view_status_bar);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view_status_bar.getLayoutParams();
        lp.height = getStatusBarHeight(this);
        lp.width = 0;
        view_status_bar.setLayoutParams(lp);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_text = (TextView) findViewById(R.id.tv_text);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_back.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
            setResult(RESULT_OK, intent);
            finish();
        });
        img_delete.setOnClickListener(v -> {
            final int index = pagerFragment.getCurrentItem();

            final String deletedPath = pagerFragment.getPaths().get(index);

            Snackbar snackbar = Snackbar.make(pagerFragment.getView(), "删除了一张照片",
                    Snackbar.LENGTH_LONG);

            if (pagerFragment.getPaths().size() <= 1) {

                // show confirm dialog
                new AlertDialog.Builder(PhotoPagerActivity.this)
                        .setTitle("确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra(KEY_SELECTED_PHOTOS, new ArrayList<>());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            } else {

                snackbar.show();

                pagerFragment.getPaths().remove(index);
                //pagerFragment.getViewPager().removeViewAt(index);
                pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
            }

            snackbar.setAction("撤销", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pagerFragment.getPaths().size() > 0) {
                        pagerFragment.getPaths().add(index, deletedPath);
                    } else {
                        pagerFragment.getPaths().add(deletedPath);
                    }
                    pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
                    pagerFragment.getViewPager().setCurrentItem(index, true);
                }
            });

        });
        deleteButton = getIntent().getBooleanExtra(HIDE_DELETE_BUTTON, false);
        int currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
        List<String> paths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);

        pagerFragment =
                (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPagerFragment);
        pagerFragment.setPhotos(paths, currentItem);

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//
//        actionBar = getSupportActionBar();
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        updateActionBarTitle();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            actionBar.setElevation(25);
//        }
        if (!deleteButton) {//隐藏删除按钮
            img_delete.setVisibility(View.VISIBLE);
        }
        else {
            img_delete.setVisibility(View.GONE);
        }

        pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateActionBarTitle();
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
    public void updateActionBarTitle() {
        tv_text.setText(getString(R.string.image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
                pagerFragment.getPaths().size()));
//        actionBar.setTitle(
//                getString(R.string.image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
//                        pagerFragment.getPaths().size()));
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
