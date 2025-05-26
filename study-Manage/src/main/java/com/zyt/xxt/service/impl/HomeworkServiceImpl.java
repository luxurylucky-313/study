package com.zyt.xxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyt.xxt.entity.Homework;
import com.zyt.xxt.mapper.HomeworkMapper;
import com.zyt.xxt.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Override
    public List<Homework> getAllHomework() {
        return list();
    }

    @Override
    public boolean addHomework(Homework homework) {
        return save(homework);
    }

    @Override
    public boolean deleteHomework(Integer id) {
        return removeById(id);
    }

    @Override
    public boolean updateHomeworkById(Homework homework) {
        return updateById(homework);
    }

    @Override
    public Homework getHomeworkById(Integer id) {
        return getById(id);
    }

    @Override
    public List<Homework> getHomeworkByTeacherId(Integer teacherId) {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        return list(queryWrapper);
    }

    @Override
    public List<Homework> getHomeworkByCourseId(Integer courseId) {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        return list(queryWrapper);
    }

    @Override
    public Page<Homework> getHomeworkByTeacherIdWithFilter(Integer teacherId, String title, Integer courseId, Integer status, int pageNum, int pageSize) {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        if (teacherId != null) {
            queryWrapper.eq("teacher_id", teacherId);
        }
        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }
        if (courseId != null) {
            queryWrapper.eq("course_id", courseId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        return homeworkMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

}
