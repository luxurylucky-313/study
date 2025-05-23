package com.zyt.xxt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher")
public class Teacher {
    @TableId
    private Integer id;
    private String name;
    private String gender;
    private String avatar;
    private String phone;
    private String username;
    private String password;
}
