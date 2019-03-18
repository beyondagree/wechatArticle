package com.robl.wechatarticle.dao;

import com.robl.wechatarticle.vo.ArticleItemVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticalItemDao extends CrudRepository<ArticleItemVo, Integer> {

    public List<ArticleItemVo> findAll();

    @Query(value = "select count(md5) from artical_spider_log where md5=:md5", nativeQuery = true)
    public Integer ifMd5Exist(@Param("md5") String md5);


    @Query(value = "SELECT (select count(1) - 1 from artical_spider_log where id>=asl.id) as index_,id,title, url,md5,picurl,description,create_date From artical_spider_log asl WHERE create_date >= date_sub(now(), interval 7 day) order by id desc", nativeQuery = true)
    public List<ArticleItemVo> find7daysArticleslately();
}
