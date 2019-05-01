package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Recruit;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    //查询推荐职位,查询state为2的,按照发布时间进行倒序排序
    List<Recruit> getByStateOrderByCreatetimeDesc(String state);

    //查询最新职位,查询state不为0的,按照发布时间进行倒序排序
    List<Recruit> findByStateNotOrderByCreatetimeDesc(String state);

}
