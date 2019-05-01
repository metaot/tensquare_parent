package com.tensquare.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tb_nofriend")
//一个pojo有俩主键(多个主键),联合主键
//使用联合主键,声明注解
@IdClass(NoFriend.class)
public class NoFriend implements Serializable{

    @Id
    private String userid;
    @Id
    private String friendid;


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

    @Override
    public String toString() {
        return "Friend{" +
                "userid='" + userid + '\'' +
                ", friendid='" + friendid + '\'' +
                '}';
    }
}
