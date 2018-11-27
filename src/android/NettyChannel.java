package com.dyz.android;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

public class NettyChannel extends CordovaPlugin {
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
}
