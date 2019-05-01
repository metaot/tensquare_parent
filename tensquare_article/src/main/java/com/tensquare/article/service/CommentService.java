package com.tensquare.article.service;

import com.tensquare.article.dao.CommentDao;
import com.tensquare.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private IdWorker idWorker;

    public void save(Comment comment) {
        String id = idWorker.nextId() + "";
        comment.set_id(id);
        comment.setPublishdate(new Date());

        commentDao.save(comment);
    }

    public List<Comment> findByArticleId(String articleId) {
        List<Comment> list = commentDao.findByArticleid(articleId);
        return list;
    }

    public void deleteBycommentId(String commentId) {
        commentDao.deleteById(commentId);
    }
}
