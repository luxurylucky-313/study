package com.zyt.xxt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("course")
public class Course {
    @TableId
    private Integer id;
    private Integer teacherId;
    private String courseName;
    private String className;
    private Date startTime;
    private Date endTime;
    private String image;
    private String description;
}
