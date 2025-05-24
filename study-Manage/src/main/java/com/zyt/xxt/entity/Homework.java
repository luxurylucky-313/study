package com.zyt.xxt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("Homework")
public class Homework {
    @TableId
    private Integer id;
    private String className;
    private String courseName;
    private Date startTime;
    private Date endTime;
}
