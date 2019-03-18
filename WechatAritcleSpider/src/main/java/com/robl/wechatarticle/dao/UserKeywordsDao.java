package com.robl.wechatarticle.dao;

import com.robl.wechatarticle.vo.UserKeywordsVo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserKeywordsDao extends CrudRepository<UserKeywordsVo, Integer> {

    public List<UserKeywordsVo> findAll();

    @Query(value = "select count(user_name) from user_keywords where user_name=:userName", nativeQuery = true)
    public Integer ifUserHasKeywords(@Param("userName") String userName);

    @Query(value = "select count(user) from user_wechat_article_config where user=:user", nativeQuery = true)
    public Integer ifUserHasConfig(@Param("user") String user);

    @Query(value = "select keywords from user_keywords,user_wechat_article_config where user = user_name and user_name=:userName and keyword_filter = 1", nativeQuery = true)
    String findKeywordsByUserName(@Param("userName") String userName);


    @Query(value = "update user_keywords set keywords=:keywords where user_name=:userName", nativeQuery = true)
    @Modifying
    void updateKeywordsByUserName(@Param("keywords") String keywords, @Param("userName") String userName);


}
