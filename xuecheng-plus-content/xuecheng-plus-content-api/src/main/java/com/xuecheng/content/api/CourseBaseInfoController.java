package com.xuecheng.content.api;


import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(value = "课程信息编程接口", tags = "课程信息编程接口")
@RestController
public class CourseBaseInfoController {

    //service模块没有配置连接数据库的yml文件，但是api模块调用service模块依然可以查到数据是因为：
    // service模块是以bean对象的形式加载到api模块环境的
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    /**
     * @param pageParams           分页参数
     * @param queryCourseParamsDto 查询条件
     */
    @ApiOperation("课程查询分页接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(
            @ApiParam(value = "查询参数") PageParams pageParams,
            @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {

        //调用service的方法
        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto);
        return courseBasePageResult;
    }


    /**
     * @param addCourseDto 新增课程参数
     * @return 返回新增的课程信息
     */
    @ApiOperation("新增课程基础信息")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated({ValidationGroups.Insert.class}) AddCourseDto addCourseDto) {
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        CourseBaseInfoDto courseBaseInfoDtoNew = courseBaseInfoService.createCourseBase(1232141425L, addCourseDto);
        return courseBaseInfoDtoNew;
    }

    /**
     * @param courseId 根据id查询课程
     * @return
     */
    @ApiOperation("根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId) {
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }


    /**
     * @param editCourseDto 修改基础课程
     * @return
     */
    @ApiOperation("修改课程基础信息")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated EditCourseDto editCourseDto) {
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        CourseBaseInfoDto courseBaseInfoDto=  courseBaseInfoService.updateCourseBase(companyId,editCourseDto);
        return courseBaseInfoDto;
    }



}
