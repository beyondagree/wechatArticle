package com.robl.wechatarticle.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ARTICAL_SPIDER_LOG")
public class ArticleItemVo implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    @NonNull
    @Column(name = "MD5")
    private String md5;
    @Getter
    @Setter
    @Column(name = "INDEX_")
    private String index_;
    @Getter
    @Setter
    @Column(name = "TITLE")
    private String title;
    @Getter
    @Setter
    @Column(name = "URL")
    private String url;
    @Getter
    @Setter
    @Column(name = "PICURL")
    private String picurl;
    @Getter
    @Setter
    @Column(name = "DESCRIPTION")
    private String description;
    @Getter
    @Setter
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    public ArticleItemVo clone() {
        try {
            return (ArticleItemVo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
