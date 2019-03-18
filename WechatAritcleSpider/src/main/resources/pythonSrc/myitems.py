# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class MyscrapyspiderItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    title = scrapy.Field()
    thumbnail = scrapy.Field()
    link = scrapy.Field()
    pass

class ArticalItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    md5 = scrapy.Field()
    index = scrapy.Field()
    title = scrapy.Field()
    thumbnail = scrapy.Field()
    link = scrapy.Field()
    pass
