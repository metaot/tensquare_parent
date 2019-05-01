package com.tensquare.friend.service;

//好友和非好友的操作,都在这个service处理

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Transactional
    public Boolean addFriend(String userId, String friendid) {
        //查询是否有好友关系
        Friend friend = friendDao.findFriend(userId, friendid);

        // 如果有好友关系,返回true,不能添加好友
        if (friend != null) {
            return true;
        }

        //保存当前的好友关系,islike设置为0,表示单项喜欢
        friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendid);
        //初始值为单项喜欢
        friend.setIslike("0");

        friendDao.save(friend);

        //查询对方是否加自己为好友
        if (friendDao.findFriend(friendid, userId) != null) {
            //如果对方加自己为好友,现在是相互喜欢
            //修改自己和对方是好友的islike为1,相互喜欢
            friendDao.updateIslike("1", userId, friendid);
            //还要修改别人和自己是好友的islike也为1,相互喜欢
            friendDao.updateIslike("1", friendid, userId);
        }


        //如果没有好友关系,返回false,能够添加好友
        return false;
    }

    public void addNoFriend(String userId, String friendid) {

        NoFriend noFriend = new NoFriend();

        noFriend.setUserid(userId);
        noFriend.setFriendid(friendid);

        //既然已经不是好友了,应该把好友关系删除,学员自己补齐


        noFriendDao.save(noFriend);
    }

    @Transactional
    public void deleteFriend(String userid, String friendid) {
        //根据自己的id和好友id,删除好友数据
        friendDao.deleteFriend(userid, friendid);

        //判断对方是否加自己为好友
        if (friendDao.findFriend(friendid, userid) != null) {
            //如果对方加自己为好友,把对方的islike修改为0
            friendDao.updateIslike("0", friendid, userid);
        }


        //如果删除好友,添加到非好友列表中(业务要求),学员自己补齐
    }
}
