package com.xx.anypay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xx.anypay.entity.AliPayResultEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;


/**
 * XxAnyPay
 * (。・∀・)ノ
 * Describe：支付集成SDK{支付包，微信}
 * Created by 雷小星🍀 on 2017/7/18 9:44.
 */

public class XxAnyPay {

    public static final int XXPAY_ALI = 0x00000000;//支付宝支付
    public static final int XXPAY_WX = 0x00000001;//微信支付
    @SuppressLint("StaticFieldLeak")
    private static XxAnyPay paySDK;
    private XxAnyPayResultCallBack callBack;
    private IWXAPI msgApi;
    private Context context;
    private WxAppIDProvider wxAppIDProvider;

    private XxAnyPay() {
    }

    /**
     * 单例获取SDK实例
     */
    public static XxAnyPay getIntance() {
        if (paySDK == null) {
            synchronized (XxAnyPay.class) {
                if (paySDK == null) {
                    paySDK = new XxAnyPay();
                }
            }
        }
        return paySDK;
    }

    /**
     * 检查支付宝是否安装
     */
    private boolean checkAliPayInstalled() {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 是否安装并且支持微信API
     *
     * @return 是否支持
     */
    private boolean isWXAppInstalledAndSupported() {
        return msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();
    }

    /**
     * 获取支付结果回调
     *
     * @return 支付结果回调
     */
    public XxAnyPayResultCallBack getCallBack() {
        return callBack != null ? callBack : new XxAnyPayResultCallBack() {
            @Override
            public void onPaySuccess() {
                Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayFiale(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * 步骤一
     * 步骤二{@link #setWxAppIDProvider(WxAppIDProvider)}
     * 初始化SDK
     *
     * @param context 内容提供者，建议放在应用入口处
     */
    public void init(Context context) {
        this.context = context;
    }

    /**
     * 获取微信信息提供者
     *
     * @return 微信信息提供者
     */
    public WxAppIDProvider getWxAppIDProvider() {
        return wxAppIDProvider;
    }

    /**
     * 步骤二
     * 设置支付信息提供者
     *
     * @param wxAppIDProvider 支付信息提供者
     */
    public void setWxAppIDProvider(WxAppIDProvider wxAppIDProvider) {
        if (wxAppIDProvider == null || TextUtils.isEmpty(wxAppIDProvider.getWeChatAppID())) {

            throw new IllegalArgumentException("微信信息提供者未设置或设置内容为空");
        }
        this.wxAppIDProvider = wxAppIDProvider;
        msgApi = WXAPIFactory.createWXAPI(context, wxAppIDProvider.getWeChatAppID());
        msgApi.registerApp(wxAppIDProvider.getWeChatAppID());
    }

    /**
     * 支付宝支付
     * 使用支付宝支付前,需使用Activity对SDK初始化一次
     *
     * @param aliPayInfo 参数信息
     */
    public void openAliPay(String aliPayInfo, XxAnyPayResultCallBack callBack) {
        this.callBack = callBack;
        if (checkAliPayInstalled()) {
            if (context instanceof Activity) {
                new AliPayTask().execute(aliPayInfo);
            } else {
                if (callBack != null) {
                    callBack.onPayFiale("请使用Activity对SDK初始化一次");
                }
            }
        } else {
            //没有安装
            if (callBack != null) {
                callBack.onPayFiale("请先安装支付宝");
            }
        }
    }

    /**
     * 打开指定类型支付
     *
     * @param xxPayType SDK支付类型{@value XXPAY_ALI}{@value XXPAY_WX}
     * @param payInfo   支付信息,微信支付时需要使用GSON序列化字符串
     * @param callBack  支付结果回调
     */
    public void openAnyPay(@XxPayType int xxPayType, String payInfo, XxAnyPayResultCallBack callBack) {
        if (xxPayType == XXPAY_ALI) {
            openAliPay(payInfo, callBack);
        } else if (xxPayType == XXPAY_WX) {
            openWxPay(payInfo, callBack);
        }
    }

    /**
     * 微信字符串解析为PayReq实体
     * 适配两种解析方式
     *
     * @param wxJsonStr 封装字符串
     */
    private PayReq jsonStrToPayReq(String wxJsonStr) throws JSONException {
        //解析Java后台返回数据
        JSONObject wxJsonObj = new JSONObject(wxJsonStr);
        String appId = wxJsonObj.optString("appId");
        String partnerId = wxJsonObj.optString("partnerId");
        String prepayId = wxJsonObj.optString("prepayId");
        String nonceStr = wxJsonObj.optString("nonceStr");
        String timeStamp = wxJsonObj.optString("timeStamp",System.currentTimeMillis()+"");
        String packageValue = wxJsonObj.optString("packageValue", "Sign=WXPay");
        String sign = wxJsonObj.optString("sign");

        //驼峰字段解析为空时,尝试使用-连接符字段进行解析
        if (TextUtils.isEmpty(appId)) {
            //解析Php后台返回数据,字段以_连接
            appId = wxJsonObj.optString("appid");
            partnerId = wxJsonObj.optString("mch_id");
            prepayId = wxJsonObj.optString("prepay_id");
            nonceStr = wxJsonObj.optString("nonce_str");
            timeStamp = wxJsonObj.optString("time",System.currentTimeMillis()+"");
            packageValue = wxJsonObj.optString("package", "Sign=WXPay");
            sign = wxJsonObj.optString("sign");
            //如果以上内容解析还为空,交给PayReq内部检查去判断了
        }

        //创建PayReq实体对象并赋值
        PayReq payReq = new PayReq();
        payReq.appId = appId;
        payReq.partnerId = partnerId;
        payReq.prepayId = prepayId;
        payReq.nonceStr = nonceStr;
        payReq.timeStamp = timeStamp;
        payReq.packageValue = packageValue;
        payReq.sign = sign;
        return payReq;
    }

    /**
     * 开始微信支付
     *
     * @param wxPayInfo 微信支付信息
     */
    public void openWxPay(String wxPayInfo, XxAnyPayResultCallBack callBack) {
        this.callBack = callBack;
        //检查手机上是否安装了微信
        if (!isWXAppInstalledAndSupported() && this.callBack != null) {
            this.callBack.onPayFiale("请先安装微信");
            return;
        }
        PayReq request = null;
        try {
            request = jsonStrToPayReq(wxPayInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onPayFiale("json格式错误");
            }
            return;
        }
        if (msgApi == null && callBack != null) {
            callBack.onPayFiale("请先设置微信信息提供者");
            return;
        }

        if (request != null) {
            //前置步骤正常,调起微信支付
            msgApi.sendReq(request);
        }
    }

    /**
     * 支付类型限制
     */
    @IntDef({XXPAY_ALI, XXPAY_WX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface XxPayType {
    }

    /**
     * 支付宝支付异步任务
     */
    @SuppressLint("StaticFieldLeak")
    private class AliPayTask extends AsyncTask<String, Void, Map<String, String>> {

        private PayTask alipay;

        AliPayTask() {
            alipay = new PayTask((Activity) context);
        }

        @Override
        protected Map<String, String> doInBackground(String... strings) {
            return alipay.payV2(strings[0], true);
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            String resultStatus = new AliPayResultEntity(stringStringMap).getResultStatus();
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                if (callBack != null) {
                    callBack.onPaySuccess();
                }
            } else {
                if (callBack != null) {
                    callBack.onPayFiale("支付失败");
                }
            }
        }
    }
}
