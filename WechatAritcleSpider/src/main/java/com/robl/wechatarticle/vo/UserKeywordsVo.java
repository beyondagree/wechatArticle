package com.robl.wechatarticle.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_KEYWORDS")
public class UserKeywordsVo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    @NonNull
    @Column(name = "USER_NAME")
    private String userName;
    @Getter
    @Setter
    @Column(name = "KEYWORDS")
    private String keywords;

    public UserKeywordsVo(String userName, String keywords) {
        this.userName = userName;
        this.keywords = keywords;
    }
}
