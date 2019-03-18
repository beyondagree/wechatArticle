package com.robl.wechatarticle.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_WECHAT_ARTICLE_CONFIG")
public class ArticleConfigVo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    @NonNull
    @Column(name = "USER")
    private String user;
    @Getter
    @Setter
    @Column(name = "AUTO_SEND")
    private Integer autoSend;
    @Getter
    @Setter
    @Column(name = "KEYWORD_FILTER")
    private Integer keywordFilter;
}
