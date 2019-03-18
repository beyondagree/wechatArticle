package com.robl.wechatarticle.controller;

import com.robl.wechatarticle.service.ScheduleService;
import com.robl.wechatarticle.service.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;


@Configuration
@EnableScheduling
public class ScheduleConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    ScheduleService scheduleService;

    @Autowired
    WechatService wechatService;

    @Scheduled(cron = "${corn.configuration.auto.get.articles}") // 每天6点10分执行一次
    public void getDailyArticle() {
        logger.info("getDailyArticle定时任务启动");
        scheduleService.getDailyArticle();
        logger.info("getDailyArticle定时任务执行结束");
    }

    @Scheduled(cron = "${corn.configuration.auto.send.articles}") // 每天8点10分执行一次
    public void autoSendDailyArticle() {
        logger.info("autoSendDailyArticle定时任务启动");
        wechatService.autoSendLatelyArticles();
        logger.info("autoSendDailyArticle定时任务执行结束");
    }
}