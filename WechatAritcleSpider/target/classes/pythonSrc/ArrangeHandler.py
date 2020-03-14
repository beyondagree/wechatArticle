# encoding:utf-8
import json
import re

class ArrangeHandler:

    @staticmethod
    def rearrange(items,weightlist):
        print "************ rearrange start ******************"
        print 'weightlist=%s' % weightlist
        weightlist = weightlist.split(',')
        print type(weightlist)
        res = {}
        for item in items:
            title = item['title']
            for weightitem in weightlist:
                keyword = weightitem.decode('utf-8')
                if re.match(r'^[\s\S]*%s[\s\S]*$' % keyword,title):
                    if not res.__contains__(keyword):
                        res[keyword] = [item['index']]
                    else:
                        res[keyword].append(item['index'])
                    break
        #按照res交换原有list
        index = 0
        dict_ = {}
        for weightitem in weightlist:
            keyword = weightitem.decode('utf-8')
            if res.__contains__(keyword):
                for originIndex in res[keyword]:
                    originIndex_str = '%s' % originIndex
                    if dict_.__contains__(originIndex_str):
                        origin = items[dict_['%s' % originIndex]]
                        items[dict_['%s' % originIndex]] = items[index]
                    else:
                        origin = items[originIndex]
                        items[originIndex] = items[index]
                    items[index] = origin
                    dict_['%s' % originIndex] = index
                    index += 1
        print "************ rearrange end ******************"
        return items
