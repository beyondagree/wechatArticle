package com.robl.wechatarticle.controller;

import com.robl.wechatarticle.service.WechatService;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MiniController {

    Log logger = LogFactory.getLog(this.getClass().getName());
    @Autowired
    WechatService wechatService;

    /*
     * 配置微信企业号平台时使用
     * */
    @ResponseBody
    @RequestMapping(value = "/mini/hello", method = RequestMethod.GET)
    public JSONObject hello(@RequestBody JSONObject requestJson) {
        logger.debug("...接收到请求消息:" + requestJson.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "调用成功");
        jsonObject.put("data", new JSONObject());
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/api/v1/articalController", method = RequestMethod.POST)
    public void articalController(@RequestParam(value = "msg_signature") String msgSignature, @RequestParam(value = "timestamp") String timeStamp
            , @RequestParam(value = "nonce") String nonce, @RequestBody String sReqData) {
        System.out.println("...接收到客户端消息...");
        wechatService.articalController(msgSignature, timeStamp, nonce, sReqData);
    }
}
