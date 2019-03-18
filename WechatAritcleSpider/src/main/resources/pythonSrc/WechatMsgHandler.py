# coding=utf-8
import requests
import json


class WechatMsgHandler:
    @staticmethod
    def send_text_msg_to(user, content):
        url_token = 'https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s' % (
            'yourcorpid', 'yourcorpsecret')
        ret = requests.get(url_token)
        url_send = 'https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s' % \
                   json.loads(ret.content, 'utf-8')[
                       'access_token']

        headers = {'content-type': 'application/json'}
        req_data = {"touser": user, "msgtype": "text", "agentid": youragentid, "text": {"content": content}, "safe": 0}
        res = requests.post(url_send, json.dumps(req_data), headers)
        print res.content
        print "******** send text end ***********"

    @staticmethod
    def send_news_msg_to(user, articles):
        url_token = 'https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s' % (
            'yourcorpid', 'yourcorpsecret')
        ret = requests.get(url_token)
        url_send = 'https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s' % \
                   json.loads(ret.content, 'utf-8')[
                       'access_token']

        headers = {'content-type': 'application/json'}
        req_data = {"touser": user, "msgtype": "news", "agentid": youragentid, "news": {"articles": articles}, "safe": 0}
        res = requests.post(url_send, json.dumps(req_data), headers)
        print res.content
        print "******** send news end ***********"
