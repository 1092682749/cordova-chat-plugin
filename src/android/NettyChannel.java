package com.dyz.android;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class NettyChannel extends CordovaPlugin implements Observer {
    private Activity activity;
    private CordovaWebView webView;
    private ChatMsgRecord chatMsgRecord;
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        this.activity = cordova.getActivity();
        this.webView = webView;
        CacheMessage.observerMap.put("123", this);
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if ("write".equals(action)) {
            ChatMsgRecord chatMsgRecord = new ChatMsgRecord();
            chatMsgRecord.setContent(args.getString(0));
            chatMsgRecord.setReceivename("123");
            chatMsgRecord.setSendname("123");
            try {
                NettyChatClient.getInstance().write(chatMsgRecord);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    @JavascriptInterface
    public void callback() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chatMsgRecord == null) {
                    return;
                }
               webView.loadUrl("javascript:receiveMessage("+JSON.toJSONString(chatMsgRecord)+")");
            }
        });
    }

//    @Override
//    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
//        if ()
//    }

    @Override
    public void setMessage(Object o) {
        chatMsgRecord = (ChatMsgRecord) o;
//        try {
//            this.execute("receive", o.toString(),null);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        callback();
    }
}
