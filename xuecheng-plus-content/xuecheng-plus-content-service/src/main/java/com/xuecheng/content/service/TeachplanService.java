package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author itcast
 * @since 2022-10-07
 */
public interface TeachplanService extends IService<Teachplan> {

    /**
     * 查询课程计划信息
     * @param courseId
     * @return
     */
    public List<TeachplanDto> findTeachplanTree(long courseId);

    /**
     * @description 添加或修改课程计划信息
     * @param teachplanDto  课程计划信息
     * @return void
     * @author Mr.M
     * @date 2022/9/9 13:39
     */
    public void saveTeachplan(SaveTeachplanDto teachplanDto);
}
