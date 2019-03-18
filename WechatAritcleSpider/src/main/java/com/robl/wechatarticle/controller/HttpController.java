package com.robl.wechatarticle.controller;

import com.robl.wechatarticle.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HttpController {

    @Autowired
    WechatService wechatService;

    /*
     * 配置微信企业号平台时使用
     * */
    @ResponseBody
    @RequestMapping(value = "/api/v1/articalController", method = RequestMethod.GET)
    public String wechatApplicationConfigure(@RequestParam(value = "msg_signature") String msgSignature, @RequestParam(value = "timestamp") String timeStamp
            , @RequestParam(value = "nonce") String nonce, @RequestParam(value = "echostr") String echoStr) {
        System.out.println("...接收到微信端验证消息...");
        return wechatService.wechatApplicationConfigure(msgSignature, timeStamp, nonce, echoStr);
    }

    @ResponseBody
    @RequestMapping(value = "/api/v1/articalController", method = RequestMethod.POST)
    public void articalController(@RequestParam(value = "msg_signature") String msgSignature, @RequestParam(value = "timestamp") String timeStamp
            , @RequestParam(value = "nonce") String nonce, @RequestBody String sReqData) {
        System.out.println("...接收到客户端消息...");
        wechatService.articalController(msgSignature, timeStamp, nonce, sReqData);
    }
}
