package com.zyt.xxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyt.xxt.entity.Homework;
import java.util.List;

public interface HomeworkService {
    List<Homework> getAllHomework();
    boolean addHomework(Homework homework);
    boolean deleteHomework(Integer id);
    boolean updateHomeworkById(Homework homework);
    Homework getHomeworkById(Integer id);
    List<Homework> getHomeworkByTeacherId(Integer teacherId);
    List<Homework> getHomeworkByCourseId(Integer courseId);
    Page<Homework> getHomeworkByTeacherIdWithFilter(Integer teacherId, String title, Integer courseId, Integer status, int pageNum, int pageSize);

}
