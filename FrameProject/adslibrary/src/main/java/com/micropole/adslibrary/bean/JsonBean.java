package com.micropole.adslibrary.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName JsonBean
 * @Model todo
 * @Description 城市数据
 * @Author chen qi hao
 * @Sign 沉迷学习不能自拔
 * @Date 2019/1/11 16:23
 * @Email 371232886@qq.com
 * @Copyright Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
public class JsonBean implements IPickerViewData {

    private String areaId;
    private String areaName;
    private List<CityBean> cities;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<CityBean> getCities() {
        return cities;
    }

    public void setCities(List<CityBean> cities) {
        this.cities = cities;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.areaName;
    }


    public static class CityBean {

        private String areaId;
        private String areaName;
        private List<DisBean> counties;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public List<DisBean> getCounties() {
            return counties;
        }

        public void setCounties(List<DisBean> counties) {
            this.counties = counties;
        }

        public static class DisBean implements Serializable {

            private String areaId;
            private String areaName;

            public String getAreaId() {
                return areaId;
            }

            public void setAreaId(String areaId) {
                this.areaId = areaId;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }
        }
    }
}

