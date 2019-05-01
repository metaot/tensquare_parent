package com.tensquare.controller;

import com.tensquare.pojo.Label;
import com.tensquare.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@Controller
@RestController
//以下两种写法效果一样,因为springMVC会自动补全第一个/
//@RequestMapping("/label")
@RequestMapping("label")

@CrossOrigin//使用CORS解决跨域问题的注解

//当配置文件修改的时候,同时刷新当前类的读取的配置文件内容
@RefreshScope
public class LabelController {

    @Autowired
    private LabelService labelService;

    //POST /label/search/{page}/{size} 标签条件查询分页
    @RequestMapping(value = "search/{page}/{size}", method = RequestMethod.POST)
    public Result search(@RequestBody Label label,
                         @PathVariable Integer page,
                         @PathVariable Integer size) {
        Page<Label> pageData = labelService.search(label, page, size);

        //封装分页查询结果
        PageResult<Label> pageResult = new PageResult<>(
                pageData.getTotalElements(), pageData.getContent()
        );

        //返回结果
        return new Result(true, StatusCode.OK, "查询成功", pageResult);

    }

    //POST /label/search 标签条件查询
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Result search(@RequestBody Label label) {
        List<Label> list = labelService.search(label);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    //DELETE /label/{labelId} 根据ID删除
    @RequestMapping(value = "{labelId}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId) {
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    //PUT /label/{labelId} 修改标签
    @RequestMapping(value = "{labelId}", method = RequestMethod.PUT)
    public Result updateById(@PathVariable String labelId,
                             @RequestBody Label label) {
        //设置要修改的id的值
        label.setId(labelId);

        labelService.updateById(label);

        return new Result(true, StatusCode.OK, "修改成功");

    }

    //POST /label 增加标签
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label) {
        labelService.save(label);
        return new Result(true, StatusCode.OK, "新增成功");
    }

    //GET /label  标签全部列表
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Label> list = labelService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    @Value("${sms.ip}")
    private String ip;

    //GET /label/{labelId} 根据ID查询
    @RequestMapping(value = "{labelId}", method = RequestMethod.GET)
    //@ResponseBody
    public Result findById(@PathVariable String labelId, HttpServletRequest request) {
        //制造异常进行演示
        //int a = 1 / 0;
        System.out.println("获取到的配置文件内容是:" + ip);
        //获取请求头信息
        String header = request.getHeader("Authorization");
        System.out.println(header);

        //使用标签服务根据id查询标签数据
        Label label = labelService.findById(labelId);

        //封装返回对象
        return new Result(true, StatusCode.OK, "查询成功", label);
    }
}
