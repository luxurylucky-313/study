package com.zyt.xxt.controller;

import com.zyt.xxt.common.ResponseResult;
import com.zyt.xxt.entity.Teacher;
import com.zyt.xxt.service.TeacherService;
import com.zyt.xxt.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger log = LoggerFactory.getLogger(TeacherController.class); // 新增日志记录器


    @PostMapping("/login")
    public ResponseResult<String> login(@RequestParam String username, @RequestParam String password) {

        Teacher teacher = teacherService.login(username, password);

        if (teacher != null) {
            String token = jwtUtils.generateToken(username);
            return ResponseResult.success(token);
        } else {
            return ResponseResult.error("用户名或密码错误");
        }
    }


    @PostMapping("/register")
    public ResponseResult<String> register(@RequestBody Teacher teacher) {
        boolean exists = teacherService.checkUsernameExists(teacher.getUsername());
        if (exists) {
            return ResponseResult.error("用户名已存在");
        }
        boolean result = teacherService.addTeacher(teacher);
        if (result) {
            return ResponseResult.success("注册成功");
        } else {
            return ResponseResult.error("注册失败");
        }
    }


    // 增加教师
    @PostMapping("/add")
    public ResponseResult<String> addTeacher(@RequestBody Teacher teacher) {
        boolean result = teacherService.addTeacher(teacher);
        if (result) {
            return ResponseResult.success("教师添加成功");
        } else {
            return ResponseResult.error("教师添加失败");
        }
    }

    // 删除教师
    @DeleteMapping("/delete/{id}")
    public ResponseResult<String> deleteTeacher(@PathVariable int id) {
        boolean result = teacherService.deleteTeacher(id);
        if (result) {
            return ResponseResult.success("教师删除成功");
        } else {
            return ResponseResult.error("教师删除失败");
        }
    }

    // 更新教师信息
    @PutMapping("/update")
    public ResponseResult<String> updateTeacher(@RequestBody Teacher teacher) {
        boolean result = teacherService.updateTeacherById(teacher);
        if (result) {
            return ResponseResult.success("教师更新成功");
        } else {
            return ResponseResult.error("教师更新失败");
        }
    }

    // 获取教师信息
    @GetMapping("/{id}")
    public ResponseResult<Teacher> getTeacher(@PathVariable int id) {
        Teacher teacher = teacherService.getTeacherById(id);
        if (teacher != null) {
            return ResponseResult.success(teacher);
        } else {
            return ResponseResult.error("未找到该教师");
        }
    }

    // 更新教师头像
    @PostMapping("/update-avatar/{id}")
    public ResponseResult<String> updateAvatar(@PathVariable int id, @RequestParam String avatarUrl) {
        boolean result = teacherService.updateTeacherAvatar(id, avatarUrl);
        if (result) {
            return ResponseResult.success("头像更新成功");
        } else {
            return ResponseResult.error("头像更新失败");
        }
    }

    // 搜索教师
    @GetMapping("/search")
    public ResponseResult<List<Teacher>> searchTeachers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String gender) {
        List<Teacher> teachers = teacherService.searchTeachers(username, gender);
        return ResponseResult.success(teachers);
    }

}