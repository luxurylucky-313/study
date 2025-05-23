package com.zyt.xxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        return list(); // 使用父类ServiceImpl中的list方法
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
}