# coding=utf-8
import hashlib

import requests
from scrapy.selector import Selector
import os
import time

from selenium import webdriver

from myitems import ArticalItem
import uniout


class StaticResourceSpider():
    name = 'StaticResourceSpider'

    def __init__(self):
        return

    @staticmethod
    def get_artical_html(initSize,tableIndex,rangeMax):
        # print '%s %s %s' % (initSize,tableIndex,rangeMax)
        print('%%%%%%%%%%%% getarticalhtml start %%%%%%%%%%%%%%%%')
        # 请求的首部信息
        headers = {
            'content-type': 'text/html;charset=GBK',
            'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.146 Safari/537.36'
        }

        items = []
        index = initSize
        # 资源url
        for num in range(1, rangeMax):
            url = 'https://weixin.sogou.com/pcindex/pc/pc_%d/%d.html' % (tableIndex, num)  # 科技咖链接
            # 利用requests对象的get方法，对指定的url发起请求
            # 该方法会返回一个Response对象
            content = requests.get(url, headers=headers).content
            if not any(Selector(text=content).xpath('/html/body/li')):
                break
            for li in Selector(text=content).xpath('/html/body/li'):
                item = ArticalItem()
                rawstr = "".join(li.xpath('div[2]/h3/a').extract())
                rawtitle = rawstr[rawstr.find('\">') + 2:rawstr.find('</a>')]
                hash_md5 = hashlib.md5(rawtitle.encode("utf-8"))
                item['md5'] = hash_md5.hexdigest()
                item['index'] = index
                item['title'] = rawtitle
                item['thumbnail'] = 'https:%s' % "".join(li.xpath('div[1]/a/img/@src').extract())
                item['link'] = "".join(li.xpath('div[2]/h3/a/@href').extract())
                index += 1
                items.append(item)
        print('%%%%%%%%%%%% getarticalhtml end %%%%%%%%%%%%%%%%')
        print items.__len__()
        return items

    @staticmethod
    def get_10jkqa_ggsyl_html(date):
        print('%%%%%%%%%%%% get_10jkqa_html start %%%%%%%%%%%%%%%%')
        # 请求的首部信息
        chromedriver = "/home/robl/tools/chromedriver"
        os.environ["webdriver.chrome.driver"] = chromedriver
        driver = webdriver.Chrome(chromedriver)  # 模拟打开浏览器
        driver.minimize_window()
        file_object = open('export/data_10jkqa_ggsyl_%s.txt' % date, 'w')
        start = time.time()

        import sys
        reload(sys)  # Python2.5 初始化后会删除 sys.setdefaultencoding 这个方法，我们需要重新载入
        sys.setdefaultencoding('utf-8')
        index = 1
        for seq in range(1, 2):
            url = 'http://data.10jqka.com.cn/market/ggsyl/field/syl/order/asc/page/%d/ajax/1/' % seq
            driver.get(url)  # 打开网址
            if driver.find_elements_by_xpath('/html/body/table/tbody/tr') == []:
                break
            for tr in driver.find_elements_by_xpath('/html/body/table/tbody/tr'):
                code = "".join(tr.find_element_by_xpath('td[2]/a').text)
                name = "".join(tr.find_element_by_xpath('td[3]/a').text)
                peratio = "".join(tr.find_element_by_xpath('td[4]').text)
                peratio_d = "".join(tr.find_element_by_xpath('td[5]').text)
                price = "".join(tr.find_element_by_xpath('td[6]').text)
                change = "".join(tr.find_element_by_xpath('td[7]').text)
                turnover = "".join(tr.find_element_by_xpath('td[8]').text)
                file_object.write(
                    '%s|%s|%s|%s|%s|%s|%s|%s\n' % (index, code, name, peratio, peratio_d, price, change, turnover))
                index += 1
            end = time.time()
            print '第【%s】页数据写入完成，累计耗时： %s 秒' % (seq,end - start)
            if seq % 5 == 0:
                driver.quit()
                driver = webdriver.Chrome(chromedriver)  # 模拟打开浏览器
        driver.quit()
        file_object.close()
        print ('%%%%%%%%%%%% export list data success %%%%%%%%%%%%%%%%')
        # for item in items:
        #    print '%s %s %s %s %s %s %s %s' % (item['index'],item['code'],item['name'],item['peratio'],item['peratio_d'],item['price'],item['change'],item['turnover'])
        print('%%%%%%%%%%%% get_10jkqa_ggsyl_html end %%%%%%%%%%%%%%%%')