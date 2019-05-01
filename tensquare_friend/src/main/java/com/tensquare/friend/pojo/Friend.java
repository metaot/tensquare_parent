package com.tensquare.friend.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_friend")
//一个pojo有俩主键(多个主键),联合主键
//使用联合主键,声明注解
@IdClass(Friend.class)
public class Friend implements Serializable {

    @Id
    private String userid;
    @Id
    private String friendid;

    //是否互相喜欢,0单项喜欢,1表示相互喜欢
    private String islike;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userid='" + userid + '\'' +
                ", friendid='" + friendid + '\'' +
                ", islike='" + islike + '\'' +
                '}';
    }
}
