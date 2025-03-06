package com.xuecheng.content.api;


import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "课程信息编程接口", tags = "课程信息编程接口")
@RestController
public class CourseBaseInfoController {

    //service模块没有配置连接数据库的yml文件，但是api模块调用service模块依然可以查到数据是因为：
    // service模块是以bean对象的形式加载到api模块环境的
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(
            PageParams pageParams,
            @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {

        //调用service的方法
        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto);
        return courseBasePageResult;
    }
}
