package com.zyt.xxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyt.xxt.entity.Teacher;
import com.zyt.xxt.mapper.TeacherMapper;
import com.zyt.xxt.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public boolean checkUsernameExists(String username) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper) != null;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return list(); // 使用父类 ServiceImpl 中的 list 方法
    }

    @Override
    public boolean addTeacher(Teacher teacher) {
        return save(teacher);
    }

    @Override
    public boolean deleteTeacher(int id) {
        return removeById(id);
    }

    @Override
    public boolean updateTeacherById(Teacher teacher) {
        return updateById(teacher);
    }

    @Override
    public Teacher getTeacherById(int id) {
        return getById(id);
    }

    @Override
    public Teacher login(String username, String password) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("password", password);
        return getOne(queryWrapper); // 返回查询到的唯一一条数据
    }

    @Override
    public Teacher findByUsername(String username) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }


    @Override
    public boolean updateTeacherAvatar(int id, String avatarUrl) {
        Teacher teacher = getById(id); // 获取原始教师对象
        if (teacher == null) return false; // 如果找不到对应ID的教师，返回false

        teacher.setAvatar(avatarUrl); // 更新头像URL

        return updateById(teacher); // 使用 MyBatis-Plus 的 updateById 方法更新数据库中的记录
    }

    @Override
    public List<Teacher> searchTeachers(String username, String gender) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (username != null && !username.trim().isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (gender != null && !gender.trim().isEmpty()) {
            queryWrapper.like("gender", gender);
        }
        return list(queryWrapper);
    }
}