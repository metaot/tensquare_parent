package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    //根据id查询数据
    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }

    //查询所有
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    //新增
    @Transactional
    public void save(Spit spit) {
        //获取分布式id
        String id = idWorker.nextId() + "";
        //设置id到spit中
        spit.set_id(id);

        //初始化吐槽其他数据
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态

        //判断是否有父吐槽
        if (spit.getParentid() != null && !"".equals(spit.getParentid())) {
            //如果有父吐槽,把父吐槽的回复数加一

            //设置查询条件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));

            //设置修改的数值
            Update update = new Update();
            update.inc("comment", 1);

            mongoTemplate.updateFirst(query, update, "spit");
        }

        //保存数据
        spitDao.save(spit);
    }

    //根据id修改
    public void updateById(Spit spit) {
        spitDao.save(spit);
    }

    //根据id删除
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }

    public Page<Spit> comment(String parentid, Integer page, Integer size) {
        //设置分页查询对象
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        //执行查询
        Page<Spit> pageData = spitDao.findByParentid(parentid, pageRequest);

        //返回结果
        return pageData;
    }

    @Transactional
    public void thumbup(String spitId) {
        ////根据id查询数据
        //Spit spit = spitDao.findById(spitId).get();
        ////把点赞数据加一
        //spit.setThumbup(spit.getThumbup() + 1);
        ////修改吐槽数据
        //spitDao.save(spit);

        //以上操作需要对数据库访问两次,性能较低,
        //以下是直接点赞加一,访问数据库一次,主要是使用inc

        //修改的条件
        Query query = new Query();
        //Criteria.where("_id"):设置查询条件是哪个字段
        //is(spitId):查询条件的数据是什么
        //相当于:_id=spitId作为条件
        query.addCriteria(Criteria.where("_id").is(spitId));

        //修改的值
        Update update = new Update();
        //第一个参数是操作的那个字段
        //第二个参数是每次增加的数值
        update.inc("thumbup", 1);

        //直接对数据进行修改
        //第一个参数是修改的条件
        //第二个参数是修改的值
        //第三个参数是集合的名字
        mongoTemplate.updateFirst(query, update, "spit");
    }
}
