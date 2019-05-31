package com.micropole.adslibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.View;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.micropole.adslibrary.bean.JsonBean;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @ClassName PickerViewUtil
 * @Model todo
 * @Description todo
 * @Author chen qi hao
 * @Sign 沉迷学习不能自拔
 * @Date 2019/1/11 17:01
 * @Email 371232886@qq.com
 * @Copyright Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
public class PickerViewUtil {
    private static OnResultSelectListener mListener;

    private static ArrayList<JsonBean> options1Items = null;//省
    private static ArrayList<ArrayList<String>> options2Items = null;//市
    private static ArrayList<ArrayList<ArrayList<String>>> options3Items = null;//市

    private PickerViewUtil() {

    }

    public static void showPickerView(Context context, boolean[] type, OnResultSelectListener listener) {// 弹出选择器（省市区三级联动）
        if (options1Items == null)
            initData(context);
        mListener = listener;
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            if (mListener != null)
                mListener.onOptionsSelect(options1, options2, options3, v);
        })
                .setTitleText("地区选择")
                .setCancelColor(Color.BLUE)
                .setSubmitColor(Color.BLUE)
                .setDividerColor(Color.WHITE)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options1Items, !type[1] ? null : options2Items, !type[2] ? null : options3Items);//三级选择器
        pvOptions.show();
    }

    private static void initData(Context context) {
        options1Items = new ArrayList<>();
        options2Items = new ArrayList<>();
        options3Items = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(getJson(context));
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                options1Items.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < options1Items.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三级）

            for (int c = 0; c < options1Items.get(i).getCities().size(); c++) {//遍历该省份的所有城市
                String cityName = options1Items.get(i).getCities().get(c).getAreaName();
                cityList.add(cityName);//添加城市
                ArrayList<String> cityAreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (options1Items.get(i).getCities().get(c).getAreaName() == null
                        || options1Items.get(i).getCities().get(c).getCounties().size() == 0) {
                    cityAreaList.add("");
                } else {
                    for (int d = 0; d < options1Items.get(i).getCities().get(c).getCounties().size(); d++)
                        cityAreaList.add(options1Items.get(i).getCities().get(c).getCounties().get(d).getAreaName());
                }
                province_AreaList.add(cityAreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
    }

    private static String getJson(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("city.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public interface OnResultSelectListener {
        void onOptionsSelect(int options1, int options2, int options3, View v);
    }
}
