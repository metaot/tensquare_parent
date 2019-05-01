package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

        //要删除文章的所有评论,可以现根据文章id查询评论,再删除所有评论
    //DELETE /comment/{articleId} 根据评论id删除评论
    @RequestMapping(value = "{commentId}", method = RequestMethod.DELETE)
    public Result deleteBycommentId(@PathVariable String commentId) {
        commentService.deleteBycommentId(commentId);
        return new Result(true, StatusCode.OK, "删除成功");

    }

    //Get /comment/{articleId} 根据文章id查询评论
    @RequestMapping(value = "{articleId}", method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable String articleId) {
        List<Comment> list = commentService.findByArticleId(articleId);
        return new Result(true, StatusCode.OK, "查询成功", list);

    }


    //POST /comment 新增评论
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment) {
        commentService.save(comment);
        return new Result(true, StatusCode.OK, "新增成功");

    }
}
