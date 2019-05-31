package com.micropole.librarybase.api;

import java.io.Serializable;

/**
 * @ClassName LoginEntity
 * @Model todo
 * @Description 登录
 * @Author chen qi hao
 * @Sign 沉迷学习不能自拔
 * @Date 2018/10/11 9:50
 * @Email 371232886@qq.com
 * @Copyright Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
public class LoginEntity implements Serializable {
    /**
     * long_token : ..
     * short_token : ..J--
     */

    private String long_token;
    private String short_token;

    public String getLong_token() {
        return long_token;
    }

    public void setLong_token(String long_token) {
        this.long_token = long_token;
    }

    public String getShort_token() {
        return short_token;
    }

    public void setShort_token(String short_token) {
        this.short_token = short_token;
    }
}
