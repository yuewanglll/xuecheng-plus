package com.xuecheng.content.api;


import com.xuecheng.base.model.RestResponse;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@Api("课程计划相关接口")
public class TeachplanController {


    @Autowired
    private TeachplanService teachplanService;

    /**
     * 查询课程计划以树形结构显示
     *
     * @param courseId 课程id
     * @return
     */
    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId", name = "课程Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanService.findTeachplanTree(courseId);
    }

    /**
     * 修改或是新增课程计划
     * @param teachplan 课程计划
     */
    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan( @RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }


    /**
     *
     * @param move 逻辑字段
     * @param id 课程计划id
     * @return 统一封装结果
     */
    @ApiOperation("移动课程计划")
    @PutMapping("/techplan/{move}/{id}")
    public RestResponse<String> MoveTechPlan(@PathVariable String move,@PathVariable Long id){
        if("moveUp".equals(move)){
            //课程计划向上移动
            teachplanService.moveUpTechPlan(id);
        }else {
            //课程计划向下移动
            teachplanService.moveDownTechPlan(id);
        }

        return RestResponse.success();
    }


}
