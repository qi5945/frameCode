package com.xx.anypay;

/**
 * XxAnyPayResultCallBack
 * (。・∀・)ノ
 * Describe：支付结果信息同步回调
 * Created by 雷小星🍀 on 2017/7/18 10:17.
 */

public interface XxAnyPayResultCallBack {

    /**
     * 支付成功回调
     */
    void onPaySuccess();

    /**
     * 支付失败回调
     *
     * @param error 错误描述
     */
    void onPayFiale(String error);
}
