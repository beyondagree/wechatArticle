package com.robl.wechatarticle.service;

public interface WechatService {

    boolean sendTextMsgTo(String user, String content);

    void autoSendLatelyArticles();

    String wechatApplicationConfigure(String msgSignature, String timeStamp
            , String nonce, String echoStr);

    void articalController(String msgSignature, String timeStamp
            , String nonce, String sReqData);
}
