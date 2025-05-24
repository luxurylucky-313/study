package com.zyt.xxt.service;

import com.zyt.xxt.entity.Homework;
import java.util.List;

public interface HomeworkService {
    List<Homework> getAllHomework();
    boolean addHomework(Homework homework);
    boolean deleteHomework(Integer id);
    boolean updateHomeworkById(Homework homework);
    Homework getHomeworkById(Integer id);
}