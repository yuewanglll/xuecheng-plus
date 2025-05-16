package com.xuecheng.search.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * @author Mr.M
 * @version 1.0
 * @description 搜索课程参数dtl
 */
@Data
@ToString
public class SearchCourseParamDto {

    //搜索关键字
    private String keywords;

    //大分类
    private String mt;

    //小分类
    private String st;
    //难度等级
    private String grade;
    //大分类列表
    List<String> mtList;
    //小分类列表
    List<String> stList;
}
