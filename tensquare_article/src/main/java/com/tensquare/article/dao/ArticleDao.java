package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    //文章审核,根据文章id修改状态为1
    @Query(value = "UPDATE tb_article SET state = ? WHERE id = ?", nativeQuery = true)
    //进行数据库写操作需要添加注解@Modifying
    @Modifying
    void examine(String state, String articleId);

    //文章点赞,根据文章id修改文章点赞数据+1
    @Query(value = "UPDATE tb_article SET thumbup = thumbup+1 WHERE id = ?",nativeQuery = true)
    @Modifying
    void thumbup(String articleId);
}
