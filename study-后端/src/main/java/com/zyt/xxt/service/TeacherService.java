package com.zyt.xxt.service;

import com.zyt.xxt.entity.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();
    boolean addTeacher(Teacher teacher);
    boolean deleteTeacher(int id);
    boolean updateTeacherById(Teacher teacher);
    Teacher getTeacherById(int id);
    Teacher login(String username, String password);
    boolean checkUsernameExists(String username);
    boolean updateTeacherAvatar(int id, String avatarUrl);
    List<Teacher> searchTeachers(String username, String gender);
    Teacher findByUsername(String username);
}