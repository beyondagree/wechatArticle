package com.robl.wechatarticle.service.impl;

import com.robl.wechatarticle.aes.WXBizMsgCrypt;
import com.robl.wechatarticle.dao.ArticalConfigDao;
import com.robl.wechatarticle.dao.ArticalItemDao;
import com.robl.wechatarticle.dao.UserKeywordsDao;
import com.robl.wechatarticle.service.WechatService;
import com.robl.wechatarticle.util.ArrangeHandler;
import com.robl.wechatarticle.util.HttpRequest;
import com.robl.wechatarticle.util.WechatMsgUtil;
import com.robl.wechatarticle.vo.ArticleConfigVo;
import com.robl.wechatarticle.vo.ArticleItemVo;
import com.robl.wechatarticle.vo.UserKeywordsVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.List;

@Service
@Transactional
public class WechatServiceImpl implements WechatService {

    @Value("wechat.sToken")
    private String sToken;
    @Value("wechat.sCorpID")
    private String sCorpID;
    @Value("wechat.sEncodingAESKey")
    private String sEncodingAESKey;
    @Value("wechat.corpsecret")
    private String corpsecret;
    @Value("wechat.agentid")
    private String agentid;


    private static String HINT_SUBSCRIPT_KEYWORDS = "您还没有订阅的关键字哦，按如下格式回复可添加:\n\uD83D\uDC49\uD83C\uDFFB关键字：您想要的关键字(多个关键字用,分隔)\n也可回复2，开启每日最热门文章自动推动";
    private static String HINT_CONFIG_SUCCESS = "\uD83D\uDC4D\uD83C\uDFFB恭喜您开启自动推送成功(每早08:10推送)，推荐您按如下格式回复添加关键字:\n\uD83D\uDC49\uD83C\uDFFB关键字：您想要的关键字(多个关键字用,分隔)";
    private static String HINT_CONFIG2_SUCCESS = "\uD83D\uDC4D\uD83C\uDFFB恭喜您开启带关键字筛选自动推送功能(每早08:10推送)，推荐您按如下格式回复添加关键字:\n\uD83D\uDC49\uD83C\uDFFB关键字：您想要的关键字(多个关键字用,分隔)";
    private static String HINT_FOR_HELP = "\uD83D\uDD0E欢迎来到帮助咨询：\n回复 1 :查看已关注关键字\n回复 2:打开我的订阅自动推送\n回复 3 :取消每日自动推送\n回复 4 :取消关键字过滤搜索结果\n回复 5 :重开关键字过滤搜索结果";
    private static String HINT_FOR_HELP_WHEN_BEYOND = "\uD83D\uDE02您这命令超纲了哦\n回复 1 :查看已关注关键字\n回复 2 :打开我的订阅自动推送\n回复 3 :取消每日自动推送\n回复 4 :取消关键字过滤搜索结果\n回复 5 :重开关键字过滤搜索结果";
    private static String KEYWORDS_ADD_SUCCESS = "\uD83D\uDC4D\uD83C\uDFFB恭喜您！您订阅的关键字已经添加成功，重复添加可以修改。\n⏰系统每日将自动向您推送满足您关键字的文章8篇，回复2可取消";
    private static String KEYWORDS_MODIFY_SUCCESS = "\uD83D\uDC4D\uD83C\uDFFB恭喜您！您订阅的关键字已经修改成功。";
    private static String HINT_OPERATE_SUCCESS = "\uD83D\uDC4D\uD83C\uDFFB操作成功！";
    private static String HINT_DONOTHING = "您无需此操作哦！";

    @Autowired
    UserKeywordsDao userKeywordsDao;

    @Autowired
    ArticalItemDao articalItemDao;

    @Autowired
    ArticalConfigDao articalConfigDao;

    private boolean sendWechatMsg(JSONObject jsonData) {
        String url_token = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
        String param = "corpid=" + sCorpID + "&corpsecret=" + corpsecret;

        String ret = HttpRequest.sendGet(url_token, param);

        String url_send = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + JSONObject.fromObject(ret).get("access_token");

        String res = HttpRequest.sendPost(url_send, jsonData.toString());
        if ((int) JSONObject.fromObject(res).get("errcode") == 0)
            return true;
        System.out.println("res=" + res);
        return false;
    }

    @Override
    public boolean sendTextMsgTo(String user, String content) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonContent = new JSONObject();
        jsonObject.put("touser", user);
        jsonObject.put("msgtype", "text");
        jsonObject.put("agentid", agentid);
        jsonContent.put("content", content);
        jsonObject.put("text", jsonContent);
        jsonObject.put("safe", 0);
        return sendWechatMsg(jsonObject);
    }

    @Override
    public void autoSendLatelyArticles() {
        List<String> userList = articalConfigDao.findAutoSendList();
        List<ArticleItemVo> artiles7DaysLately = articalItemDao.find7daysArticleslately();
        if (artiles7DaysLately != null) {
            for (String user : userList
            ) {
                sendLatelyArticlesToSpecificUser(artiles7DaysLately, user);
            }
            System.out.println("auto send success!");
        }
    }

    private void sendLatelyArticlesToSpecificUser(List<ArticleItemVo> artiles7DaysLately, String user) {
        String keyword = userKeywordsDao.findKeywordsByUserName(user);
        if (StringUtils.isNotBlank(keyword)) {
            ArrangeHandler.reArrange(artiles7DaysLately, keyword);
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonContent = new JSONObject();
        JSONArray articals = new JSONArray();
        jsonObject.put("touser", user);
        jsonObject.put("msgtype", "news");
        jsonObject.put("agentid", agentid);

        for (ArticleItemVo vo : artiles7DaysLately
        ) {
            JSONObject jObj = new JSONObject();
            jObj.put("title", vo.getTitle());
            jObj.put("description", vo.getDescription());
            jObj.put("url", vo.getUrl());
            jObj.put("picurl", vo.getPicurl());
            articals.add(jObj);
            if (articals.size() == 8)
                break;
        }
        jsonContent.put("articles", articals);
        jsonObject.put("news", jsonContent);
        jsonObject.put("safe", 0);
        sendWechatMsg(jsonObject);
    }

    @Override
    public String wechatApplicationConfigure(String msgSignature, String timeStamp, String nonce, String echoStr) {
        try {
            // 解析出url上的参数值如下：
            String sVerifyMsgSig = URLDecoder.decode(msgSignature, "utf-8");
            String sVerifyTimeStamp = URLDecoder.decode(timeStamp, "utf-8");
            String sVerifyNonce = URLDecoder.decode(nonce, "utf-8");
            String sVerifyEchoStr = URLDecoder.decode(echoStr, "utf-8");
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
            //需要返回的明文
            String sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
                    sVerifyNonce, sVerifyEchoStr);
            System.out.println("verifyurl echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            return sEchoStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void articalController(String msgSignature, String timeStamp, String nonce, String sReqData) {
        try {
            // 解析出url上的参数值如下：
            String sReqMsgSig = URLDecoder.decode(msgSignature, "utf-8");
            String sReqTimeStamp = URLDecoder.decode(timeStamp, "utf-8");
            String sReqNonce = URLDecoder.decode(nonce, "utf-8");

            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
            String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
            System.out.println("after decrypt msg: " + sMsg);
            // TODO: 解析出明文xml标签的内容进行处理
            // For example:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(sMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Event");
            String fromUser = root.getElementsByTagName("FromUserName").item(0).getTextContent();
            if (nodelist1 != null && nodelist1.item(0) != null) {//事件
                String eventKey = root.getElementsByTagName("EventKey").item(0).getTextContent();
                if ("0".equals(eventKey)) {//我的订阅
                    if (userKeywordsDao.ifUserHasConfig(fromUser) == 0) {
                        //还没有关键字
                        addUserToConfig(fromUser);
                        return;
                    } else {
                        List<ArticleItemVo> artiles7DaysLately = articalItemDao.find7daysArticleslately();
                        if (artiles7DaysLately != null) {
                            sendLatelyArticlesToSpecificUser(artiles7DaysLately, fromUser);
                            System.out.println("send to specific user success!");
                        }
                        return;
                    }
                } else {
                    WechatMsgUtil.getSelectedItems(fromUser, eventKey, userKeywordsDao.findKeywordsByUserName(fromUser));
                    return;
                }
            } else {//回复消息
                nodelist1 = root.getElementsByTagName("Content");
                String content = nodelist1.item(0).getTextContent();
                if (content.trim().replace("：", ":").startsWith("关键字:")) {
                    if (userKeywordsDao.ifUserHasKeywords(fromUser) != 0) {
                        userKeywordsDao.updateKeywordsByUserName(content.replace("关键字:", ""), fromUser);
                        sendTextMsgTo(fromUser, KEYWORDS_MODIFY_SUCCESS);
                    } else {
                        UserKeywordsVo user = new UserKeywordsVo(fromUser, content.replace("关键字:", ""));
                        userKeywordsDao.save(user);
                        sendTextMsgTo(fromUser, KEYWORDS_ADD_SUCCESS);
                    }
                    return;
                } else {
                    if (askForHelp(content)) {
                        sendTextMsgTo(fromUser, HINT_FOR_HELP);
                    } else if ("1".equals(content.trim())) {
                        if (userKeywordsDao.ifUserHasKeywords(fromUser) == 0) {
                            //还没有关键字
                            sendTextMsgTo(fromUser, HINT_SUBSCRIPT_KEYWORDS);
                            return;
                        } else {
                            String keywords = userKeywordsDao.findKeywordsByUserName(fromUser);
                            sendTextMsgTo(fromUser, keywords);
                            return;
                        }
                    } else if ("2".equals(content.trim())) {
                        if (userKeywordsDao.ifUserHasConfig(fromUser) == 0) {
                            addUserToConfig(fromUser);
                        } else {
                            articalConfigDao.updateAutoSendFlagByUserName(1, fromUser);
                            sendTextMsgTo(fromUser, HINT_CONFIG_SUCCESS);
                        }
                    } else if ("3".equals(content.trim())) {
                        if (userKeywordsDao.ifUserHasConfig(fromUser) == 0) {
                            sendTextMsgTo(fromUser, HINT_DONOTHING);
                        } else {
                            articalConfigDao.updateAutoSendFlagByUserName(0, fromUser);
                            sendTextMsgTo(fromUser, HINT_OPERATE_SUCCESS);
                        }
                        return;
                    } else if ("4".equals(content.trim())) {
                        if (userKeywordsDao.ifUserHasConfig(fromUser) == 0) {
                            sendTextMsgTo(fromUser, HINT_DONOTHING);
                        } else {
                            articalConfigDao.updatekeywordFilterFlagByUserName(0, fromUser);
                            sendTextMsgTo(fromUser, HINT_OPERATE_SUCCESS);
                        }
                        return;
                    } else if ("5".equals(content.trim())) {
                        if (userKeywordsDao.ifUserHasConfig(fromUser) == 0) {
                            ArticleConfigVo vo = new ArticleConfigVo();
                            vo.setUser(fromUser);
                            vo.setAutoSend(1);
                            vo.setKeywordFilter(1);
                            articalConfigDao.save(vo);
                            sendTextMsgTo(fromUser, HINT_CONFIG2_SUCCESS);
                        } else {
                            articalConfigDao.updatekeywordFilterFlagByUserName(1, fromUser);
                            sendTextMsgTo(fromUser, HINT_OPERATE_SUCCESS);
                        }
                        return;
                    } else {
                        sendTextMsgTo(fromUser, HINT_FOR_HELP_WHEN_BEYOND);
                    }
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUserToConfig(String fromUser) {
        ArticleConfigVo vo = new ArticleConfigVo();
        vo.setUser(fromUser);
        vo.setAutoSend(1);
        articalConfigDao.save(vo);
        sendTextMsgTo(fromUser, HINT_CONFIG_SUCCESS);
        return;
    }

    private boolean askForHelp(String msg) {
        String regx = "^(\\?|？|帮助|help|bangzhu|bz)$";
        if (msg.trim().toLowerCase().matches(regx))
            return true;
        return false;
    }

    public static void main(String[] args) {
        System.out.println(new WechatServiceImpl().sendTextMsgTo("all", "\uD83D\uDE02您这命令超纲了哦\n回复 1 :查看已关注关键字\n回复 2 :取消每日自动推送\n回复 3 :重新打开每日推送\n回复 4 :取消关键字过滤搜索结果\n回复 5 :重开关键字过滤搜索结果"));
    }
}