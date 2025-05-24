package com.zyt.xxt.service;

import com.zyt.xxt.entity.Course;
import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    boolean addCourse(Course course);
    boolean deleteCourse(int id);
    boolean updateCourseById(Course course);
    Course getCourseById(int id);
    boolean updateCourseImage(int id, String fileUrl);
    List<Course> searchCourses(String courseName, String className);
    List<Course> searchCoursesByTeacherIdAndKeyword(int teacherId, String courseName, String className);
    List<Course> getCoursesByTeacherId(int teacherId);
    boolean updateCourseResource(int id, String resourceUrl);
// 新增方法
}
