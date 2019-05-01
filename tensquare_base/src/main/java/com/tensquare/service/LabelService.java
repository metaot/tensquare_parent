package com.tensquare.service;

import com.tensquare.dao.LabelDao;
import com.tensquare.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

//微服务,服务是最小功能
//如果有不同的实现,应该实现不同的微服务,既然如此,接口开发意义不大
//这里就不使用接口,直接写实现类,如果非要使用接口,也没有任何问题
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public Label findById(String labelId) {
        return labelDao.findById(labelId).get();
    }

    public List<Label> findAll() {
        return labelDao.findAll();
    }


    //可以不使用注解管理事务,默认会提交事务
    //当需要多次操作数据库,且有事务要求,就一定要加这个注解
    @Transactional//事务管理注解
    public void save(Label label) {
        //使用分布式id生成器生成id
        String id = idWorker.nextId() + "";

        //设置id
        label.setId(id);

        labelDao.save(label);
    }

    public void updateById(Label label) {
        labelDao.save(label);
    }

    public void deleteById(String labelId) {
        labelDao.deleteById(labelId);
    }

    public List<Label> search(Label label) {
        //构建查询条件
        Specification<Label> specification = getSpecification(label);

        //执行查询
        List<Label> list = labelDao.findAll(specification);

        //返回结果
        return list;

    }

    private Specification<Label> getSpecification(Label label) {
        return new Specification<Label>() {
            //Predicate封装查询的条件
            //Root<Label> root,构建查询对象相关信息
            //CriteriaQuery,构建条件查询
            //CriteriaBuilder:创建查询对象
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();

                if (label.getId() != null && !"".equals(label.getId())) {
                    //设置id条件
                    Predicate predicate = cb.equal(root.get("id").as(String.class), label.getId());
                    predicateList.add(predicate);
                }

                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                    //根据标签名称模糊查询标签
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), label.getLabelname());

                    //把条件放到list集合中
                    predicateList.add(predicate);
                }
                if (label.getState() != null && !"".equals(label.getState())) {
                    //设置状态条件
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());
                    predicateList.add(predicate);
                }
                if (label.getCount() != null) {
                    //设置使用数量条件
                    Predicate predicate = cb.equal(root.get("count").as(Long.class), label.getCount());
                    predicateList.add(predicate);
                }
                if (label.getRecommend() != null && !"".equals(label.getRecommend())) {
                    //设置是否推荐条件
                    Predicate predicate = cb.equal(root.get("recommend").as(String.class), label.getRecommend());
                    predicateList.add(predicate);
                }
                if (label.getFans() != null) {
                    //设置使用数量条件
                    Predicate predicate = cb.equal(root.get("fans").as(Long.class), label.getFans());
                    predicateList.add(predicate);
                }

                //声明条件数组
                Predicate[] predicates = new Predicate[predicateList.size()];
                //把条件放到数组中
                predicates = predicateList.toArray(predicates);

                //设置条件对象,是数组类型
                Predicate predicate = cb.and(predicates);

                //返回条件对象
                return predicate;
            }
        };

    }

    public Page<Label> search(Label label, Integer page, Integer size) {
        //创建分页查询条件,传递进来的页码数是从1开始
        //第一个参数是从哪一页开始查,数据是从0开始
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        //获取查询条件
        Specification<Label> specification = getSpecification(label);

        //根据条件分页查询
        Page<Label> pageData = labelDao.findAll(specification, pageRequest);

        //返回结果
        return pageData;
    }
}
