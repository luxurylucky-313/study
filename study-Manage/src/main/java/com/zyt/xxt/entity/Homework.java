package com.zyt.xxt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("homework")
public class Homework {
    @TableId
    private Integer id;
    private Integer teacherId;
    private Integer courseId;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer status; // 0:未发布,1:已发布
    private Date createdAt;
    private Date updatedAt;
}
