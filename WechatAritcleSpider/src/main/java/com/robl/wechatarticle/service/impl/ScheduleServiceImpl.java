package com.robl.wechatarticle.service.impl;

import com.robl.wechatarticle.dao.ArticalItemDao;
import com.robl.wechatarticle.service.ScheduleService;
import com.robl.wechatarticle.util.PythonUtil;
import com.robl.wechatarticle.vo.ArticleItemVo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private static final String DAILY_ARTICAL_SPIDER_METHOD = "WechatArticalSpider.getDailyArticle";

    @Autowired
    ArticalItemDao articalItemDao;

    @Override
    public void getDailyArticle() {
        try {
            boolean flag = PythonUtil.execPythonFunc(DAILY_ARTICAL_SPIDER_METHOD);
            if (flag) {//读取爬取到的所有记录，将新的内容入库
                BufferedReader reader = new BufferedReader(new FileReader(new File("pyexport/daily_articles.txt")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    JSONObject jsonObject = JSONObject.fromObject(line);
                    if (articalItemDao.ifMd5Exist((String) jsonObject.get("md5")) == 0) {
                        ArticleItemVo vo = new ArticleItemVo();
                        vo.setMd5((String) jsonObject.get("md5"));
                        vo.setUrl((String) jsonObject.get("url"));
                        vo.setPicurl((String) jsonObject.get("picurl"));
                        vo.setDescription((String) jsonObject.get("description"));
                        vo.setTitle((String) jsonObject.get("title"));
                        vo.setCreateDate(new Date());
                        articalItemDao.save(vo);
                        System.out.println("...添加一笔新的记录...");
                    }
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("getDailyArticle failed!");
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/pyexport/daily_articles.txt")));
        String line = "";
        while ((line = reader.readLine()) != null) {
            JSONObject jsonObject = JSONObject.fromObject(line);
            System.out.println(jsonObject.get("md5"));
        }
    }
}
