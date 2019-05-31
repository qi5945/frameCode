package com.xx.anypay.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xx.anypay.XxAnyPay;

public class WxPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == 0 && XxAnyPay.getIntance().getCallBack() != null) {
            //支付成功
            XxAnyPay.getIntance().getCallBack().onPaySuccess();
        } else if (resp.errCode == -1 && XxAnyPay.getIntance().getCallBack() != null) {
            XxAnyPay.getIntance().getCallBack().onPayFiale("支付失败");
        } else if (resp.errCode == -2 && XxAnyPay.getIntance().getCallBack() != null) {
            XxAnyPay.getIntance().getCallBack().onPayFiale("支付取消");
        }
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, XxAnyPay.getIntance().getWxAppIDProvider().getWeChatAppID());
        api.handleIntent(getIntent(), this);
    }

}