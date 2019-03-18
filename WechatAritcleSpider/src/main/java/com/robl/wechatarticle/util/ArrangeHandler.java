package com.robl.wechatarticle.util;

import com.robl.wechatarticle.vo.ArticleItemVo;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArrangeHandler {

    public static void reArrange(List<ArticleItemVo> items, String keywords) {
        System.out.println("************ rearrange start ******************");
        long t0 = System.currentTimeMillis();
        String[] weightList = keywords.split(",");
        Map<String, List<Integer>> res = new HashedMap();
        for (ArticleItemVo item : items) {
            String title = item.getTitle();
            for (String keyword : weightList) {
                if (title.matches("^[\\s\\S]*" + keyword + "[\\s\\S]*$")) {
                    if (!res.containsKey(keyword)) {
                        List<Integer> list = new ArrayList<>();
                        list.add(Integer.parseInt(item.getIndex_()));
                        res.put(keyword, list);
                    } else {
                        res.get(keyword).add(Integer.parseInt(item.getIndex_()));
                    }
                    break;
                }
            }
        }

        int index = 0;
        Map<Integer, Integer> dict_ = new HashedMap();
        for (String keyword : weightList
        ) {
            if (res.containsKey(keyword)) {
                for (int j = 0; j < res.get(keyword).size(); j++
                ) {
                    int originIndex = res.get(keyword).get(j);
                    if (dict_.containsKey(originIndex)) {
                        ArticleItemVo originJsonClone = items.get(dict_.get(originIndex)).clone();
                        ArticleItemVo indexJsonClone = items.get(index).clone();
                        items.set(dict_.get(originIndex), indexJsonClone);
                        items.set(index, originJsonClone);
                    } else {
                        ArticleItemVo originJsonClone = items.get(originIndex).clone();
                        ArticleItemVo indexJsonClone = items.get(index).clone();
                        items.set(originIndex, indexJsonClone);
                        items.set(index, originJsonClone);
                    }
                    dict_.put(originIndex, index);
                    if (index == 7)
                        return;
                    index += 1;

                }
            }
        }
        long t1 = System.currentTimeMillis();
        System.out.println("************ reArrange耗时：" + (t1 - t0) + "ms ******************");
        System.out.println("************ rearrange end ******************");
    }
}
