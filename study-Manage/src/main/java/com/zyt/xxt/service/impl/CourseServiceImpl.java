package com.zyt.xxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyt.xxt.entity.Course;
import com.zyt.xxt.mapper.CourseMapper;
import com.zyt.xxt.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getAllCourses() {
        return list(); // 使用父类ServiceImpl中的list方法
    }

    @Override
    public boolean addCourse(Course course) {
        return save(course);
    }

    @Override
    public boolean deleteCourse(int id) {
        return removeById(id);
    }

    @Override
    public boolean updateCourseById(Course course) {
        return updateById(course);
    }

    @Override
    public Course getCourseById(int id) {
        return getById(id);
    }

    @Override
    public boolean updateCourseImage(int id, String fileUrl) {
        Course course = getById(id); // 获取原始课程对象
        if (course == null) return false; // 如果找不到对应ID的课程，返回false

        course.setImage(fileUrl); // 更新图片URL

        return updateById(course); // 使用MyBatis-Plus的updateById方法更新数据库中的记录
    }
    @Override
    public List<Course> getCoursesByTeacherId(int teacherId) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        return list(queryWrapper);
    }


    @Override
    public List<Course> searchCourses(String courseName, String className) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (courseName != null && !courseName.trim().isEmpty()) {
            queryWrapper.like("course_name", courseName);
        }
        if (className != null && !className.trim().isEmpty()) {
            queryWrapper.like("class_name", className);
        }
        return list(queryWrapper);
    }
    @Override
    public List<Course> searchCoursesByTeacherIdAndKeyword(int teacherId, String courseName, String className) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);

        if (courseName != null && !courseName.trim().isEmpty()) {
            queryWrapper.like("course_name", courseName);
        }
        if (className != null && !className.trim().isEmpty()) {
            queryWrapper.like("class_name", className);
        }

        return list(queryWrapper);
    }

    @Override
    public boolean updateCourseResource(int id, String resourceUrl) {
        Course course = getById(id); // 获取原始课程对象
        if (course == null) return false;

        course.setResourceUrl(resourceUrl); // 设置资源URL

        return updateById(course); // 使用MyBatis-Plus更新
    }


}