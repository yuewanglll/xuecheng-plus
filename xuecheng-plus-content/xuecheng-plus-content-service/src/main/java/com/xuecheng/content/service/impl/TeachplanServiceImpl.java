package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    /**
     * 查询课程计划信息
     *
     * @param courseId
     * @return
     */
    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }


    /**
     * 修改或新增课程计划
     *
     * @param teachplanDto 课程计划信息
     */
    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {

        //课程计划id
        Long id = teachplanDto.getId();
        //修改课程计划
        if (id != null) {
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        } else {
            //取出同父同级别的课程计划数量
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            //设置排序号
            teachplanNew.setOrderby(count + 1);
            BeanUtils.copyProperties(teachplanDto, teachplanNew);
            teachplanMapper.insert(teachplanNew);
        }
    }

    //上移课程计划
    @Override
    public void moveUpTechPlan(Long id) {
        //获取当前课程计划信息
        Teachplan teachplan = teachplanMapper.selectById(id);

        //查询具有相同父ID和课程ID的教学计划，并按照orderby排序
        List<Teachplan> teachplans = teachplanMapper.selectList(
                new LambdaQueryWrapper<Teachplan>()
                        .eq(Teachplan::getCourseId, teachplan.getCourseId())
                        .eq(Teachplan::getParentid, teachplan.getParentid())
                        .orderByAsc(Teachplan::getOrderby)
        );

        //查询当前课程计划在查询结果中的位置
        int currenIndex = teachplans.indexOf(teachplan);
        if (currenIndex == -1 || currenIndex == 0) {
            XueChengPlusException.cast("没有可交换的课程");
            return;
        }

        //获取上一个课程计划
        Teachplan tempTecherplan = teachplans.get(currenIndex - 1);

        //交换两个课程计划位置
        Integer teachplanOrderby = teachplan.getOrderby();
        teachplan.setOrderby(tempTecherplan.getOrderby());
        tempTecherplan.setOrderby(teachplanOrderby);

        //更新课程计划
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(tempTecherplan);
    }

    //课程计划下移
    @Override
    public void moveDownTechPlan(Long id) {
        //获取当前课程计划信息
        Teachplan teachplan = teachplanMapper.selectById(id);

        //查询具有相同父ID和课程ID的教学计划，并按照orderby排序
        List<Teachplan> teachplans = teachplanMapper.selectList(
                new LambdaQueryWrapper<Teachplan>()
                        .eq(Teachplan::getCourseId, teachplan.getCourseId())
                        .eq(Teachplan::getParentid, teachplan.getParentid())
                        .orderByAsc(Teachplan::getOrderby)
        );

        //查询当前课程计划在查询结果中的位置
        int currenIndex = teachplans.indexOf(teachplan);
        if (currenIndex == -1 || currenIndex==teachplans.size()-1) {
            XueChengPlusException.cast("没有可交换的课程");
            return;
        }

        //获取下一个课程计划
        Teachplan tempTecherplan = teachplans.get(currenIndex+1);

        //交换两个课程计划位置
        Integer teachplanOrderby = teachplan.getOrderby();
        teachplan.setOrderby(tempTecherplan.getOrderby());
        tempTecherplan.setOrderby(teachplanOrderby);

        //更新课程计划
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(tempTecherplan);

    }

    private int getTeachplanCount(long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }


}
