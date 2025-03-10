package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

public interface CourseBaseInfoService {

    /**
     * 根据条件查询课程
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);


    /**
     * 新增课程
     * @param addCourseDto 课程相关参数
     * @return  CourseBaseInfoDto
     */
    CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);

    /**
     * 根据id查询课程信息
     * @param courseId
     * @return
     */
    public CourseBaseInfoDto getCourseBaseInfo(long courseId);


    /**
     * 修改课程信息
     * @param editCourseDto
     * @return
     */
    CourseBaseInfoDto updateCourseBase(Long companyId,EditCourseDto editCourseDto);
}
