# coding=utf-8
import json

from ArrangeHandler import ArrangeHandler
from StaticResourceSpider import StaticResourceSpider
from WechatMsgHandler import WechatMsgHandler


class WechatArticalSpider():
    name = 'WechatArticalSpider'

    @staticmethod
    def startSpider(touser, tableindex, keywords):
        items = []
        if (tableindex == '1'):
            items = StaticResourceSpider.get_artical_html(items.__len__(), 0, 2)
        elif (tableindex == '2'):
            items = StaticResourceSpider.get_artical_html(items.__len__(), 1, 2)
        elif (tableindex == '3'):
            items = StaticResourceSpider.get_artical_html(items.__len__(), 5, 2)
        elif (tableindex == '4'):
            items = StaticResourceSpider.get_artical_html(items.__len__(), 6, 2)
        elif (tableindex == '5'):
            items += StaticResourceSpider.get_artical_html(items.__len__(), 2, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 3, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 4, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 7, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 8, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 9, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 10, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 11, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 12, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 13, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 14, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 15, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 16, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 17, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 18, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 19, 2)
            items += StaticResourceSpider.get_artical_html(items.__len__(), 20, 2)

        if keywords != '':
            items = ArrangeHandler.rearrange(items, keywords)

        print items.__len__()
        articals = []
        for item in items:
            jdata = {"title": item['title'], "description": '', "url": item['link'], "picurl": item['thumbnail']}
            articals.append(jdata)
            if articals.__len__() == 8:
                break
        print articals
        WechatMsgHandler.send_news_msg_to(touser, articals)

    @staticmethod
    def getDailyArticle():
        items = []
        items += StaticResourceSpider.get_artical_html(items.__len__(), 0, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 1, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 2, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 3, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 4, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 5, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 6, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 7, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 8, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 9, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 10, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 11, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 12, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 13, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 14, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 15, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 16, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 17, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 18, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 19, 2)
        items += StaticResourceSpider.get_artical_html(items.__len__(), 20, 2)

        print 'items.size()=%s' % items.__len__()
        file_object = open('pyexport/daily_articles.txt', 'w')
        for item in items:
            jdata = {"md5": item['md5'], "title": item['title'], "description": '', "url": item['link'],
                     "picurl": item['thumbnail']}
            file_object.write(json.dumps(jdata))
            file_object.write('\n')
        file_object.close()
        print 'daily_articles exported success!'
