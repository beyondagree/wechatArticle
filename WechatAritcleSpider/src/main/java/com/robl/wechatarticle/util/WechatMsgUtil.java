package com.robl.wechatarticle.util;

import org.apache.commons.lang.StringUtils;

public class WechatMsgUtil {

    private static final String ARTICAL_SPIDER_METHOD = "WechatArticalSpider.startSpider";

//    public static void sendTextMsg(String toUser, String content) {
//        System.out.println("收到发送消息请求...");
//        try {
//            PythonUtil.execPythonFunc(msgHandler, toUser, content);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("sendTextMsg failed!");
//        }
//    }

    public static void getSelectedItems(String toUser, String index, String keywords) {
        System.out.println("收到获取对应文章请求请求...");
        if (StringUtils.isEmpty(keywords))
            keywords = "\"\"";
        try {
            PythonUtil.execPythonFunc(ARTICAL_SPIDER_METHOD, toUser, index, keywords);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("getSelectedItems failed!");
        }
    }

    public static void main(String[] args) {
        WechatMsgUtil.getSelectedItems("all", "5", null);
        System.out.println("...end...");
    }
}
