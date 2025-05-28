package com.zyt.xxt;

import com.zyt.xxt.entity.Teacher;
import com.zyt.xxt.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Test
    void testGetAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();

        assertNotNull(teachers, "教师列表不应为 null");
        assertFalse(teachers.isEmpty(), "教师列表不应为空");

        // 打印所有教师信息（可选）
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
    }
}
