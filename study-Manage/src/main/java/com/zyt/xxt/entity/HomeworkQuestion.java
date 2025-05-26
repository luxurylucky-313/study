package com.zyt.xxt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("homework_question")
public class HomeworkQuestion {
    @TableId
    private Integer id;
    private Integer homeworkId;
    private Integer type; // 1:选择题,2:判断题,3:简答题
    private String content;
    private String options; // JSON格式
    private String answer;
    private Integer score;
    private Integer sequence;
    private Date createdAt;
    private Date updatedAt;
}
