package com.tensquare.article.dao;

import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentDao extends MongoRepository<Comment, String> {

    //方法名明明的方式,根据文章id查询评论
    List<Comment> findByArticleid(String articleId);
}
