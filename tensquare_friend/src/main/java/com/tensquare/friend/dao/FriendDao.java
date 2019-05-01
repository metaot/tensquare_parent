package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String> {

    //根据自己的id和好友id查询好友数据
    @Query(value = "SELECT * FROM tb_friend WHERE userid=? AND friendid=?", nativeQuery = true)
    Friend findFriend(String userid, String friendid);


    //根据自己的id和好友id修改islike
    @Query(value = "UPDATE tb_friend SET islike=? WHERE userid=? AND friendid=?", nativeQuery = true)
    @Modifying
    void updateIslike(String islike, String usreid, String friendid);

    //根据自己的id和好友id,删除
    @Query(value = "DELETE FROM tb_friend WHERE userid=? AND friendid=?", nativeQuery = true)
    @Modifying
    void deleteFriend(String userid, String friendid);


}
