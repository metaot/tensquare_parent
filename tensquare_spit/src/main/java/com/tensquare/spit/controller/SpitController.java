package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spit")
public class SpitController {

    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    //PUT /spit/thumbup/{spitId} 吐槽点赞
    @RequestMapping(value = "thumbup/{spitId}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId) {
        //现在的做法是每个用户都可以无限点赞,这是不合理的
        //把点赞的数据进行保存(保存到redis中),例如保存  用户id+吐槽id
        //下一次用户点赞,查询用户id+吐槽id,如果有值,表示用户已经点过赞,不能再点
        //如果没有值,表示用户没有点过赞,可以点赞

        //模拟获取用户id
        String userid = "1013";

        //在点赞之前,查询用户id+吐槽id
        Object result = redisTemplate.opsForValue().get("thumbup_" + userid + "_" + spitId);


        if (result != null) {
            //如果查询到数据,不为空,表示用户已经点赞,提示用户不能点赞
            //也可以实现用户点赞取消,删除点赞关系的数据,同时点赞数减一
            return new Result(false, StatusCode.REPERROR, "用户已点赞,不能重复点赞");
        } else {
            //如果查询到数据为空,表示用户没有点赞,就可以点赞
            spitService.thumbup(spitId);

            //保存点赞关系数据
            redisTemplate.opsForValue().set("thumbup_" + userid + "_" + spitId, 1);

            return new Result(true, StatusCode.OK, "点赞成功");
        }

    }

    //GET /spit/comment/{parentid}/{page}/{size} 根据上级ID查询吐槽数据（分页）
    @RequestMapping(value = "comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result comment(@PathVariable String parentid,
                          @PathVariable Integer page,
                          @PathVariable Integer size) {

        Page<Spit> pageData = spitService.comment(parentid, page, size);
        PageResult<Spit> pageResult = new PageResult<>(
                pageData.getTotalElements(), pageData.getContent()
        );

        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }


    //GET /spit/{spitId} 根据ID查询吐槽
    @RequestMapping(value = "{spitId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        Spit spit = spitService.findById(spitId);
        return new Result(true, StatusCode.OK, "查询成功", spit);
    }

    //GET /spit Spit全部列表
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Spit> list = spitService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    //POST /spit 增加吐槽
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit) {
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    //PUT /spit/{spitId} 修改吐槽
    @RequestMapping(value = "{spitId}", method = RequestMethod.PUT)
    public Result updateById(@RequestBody Spit spit,
                             @PathVariable String spitId) {
        spit.set_id(spitId);
        spitService.updateById(spit);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    //DELETE /spit/{spitId} 根据ID删除吐槽
    @RequestMapping(value = "{spitId}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId) {
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
