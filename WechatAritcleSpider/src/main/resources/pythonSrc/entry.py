# encoding:utf-8
from self import self

import sys

from StaticResourceSpider import StaticResourceSpider
from WechatArticalSpider import WechatArticalSpider


#
# 需要执行的Class文件，需要先import
#

class entry():

    def callFunc(self, args):
        func = args[1]
        #print ('?call %s()' % (func))
        # print ('args=%s' % (args[2:]))
        args = args[2:]
        # print 'args=%s' % args
        s = ''
        for str in args:
            s = '%s,\'%s\'' % (s, str)
        # print '%s(%s)' % (func, s[1:])
        # WechatArticalSpider.getDailyArticle()
        eval('%s(%s)' % (func, s[1:]))

    callFunc(self, sys.argv)
