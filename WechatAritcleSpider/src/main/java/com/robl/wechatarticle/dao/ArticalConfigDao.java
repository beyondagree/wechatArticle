package com.robl.wechatarticle.dao;

import com.robl.wechatarticle.vo.ArticleConfigVo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticalConfigDao extends CrudRepository<ArticleConfigVo, Integer> {

    public List<ArticleConfigVo> findAll();

    @Query(value = "select user from user_wechat_article_config where auto_send = 1", nativeQuery = true)
    public List<String> findAutoSendList();

    @Query(value = "update user_wechat_article_config set auto_send=:autoSend where user=:userName", nativeQuery = true)
    @Modifying
    void updateAutoSendFlagByUserName(@Param("autoSend") Integer autoSend, @Param("userName") String userName);

    @Query(value = "update user_wechat_article_config set keyword_filter=:keywordFilter where user=:userName", nativeQuery = true)
    @Modifying
    void updatekeywordFilterFlagByUserName(@Param("keywordFilter") Integer keywordFilter, @Param("userName") String userName);
}
