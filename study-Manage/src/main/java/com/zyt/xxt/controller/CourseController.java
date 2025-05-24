package com.zyt.xxt.controller;

import com.zyt.xxt.common.ResponseResult;
import com.zyt.xxt.entity.Course;
import com.zyt.xxt.entity.Teacher;
import com.zyt.xxt.service.CourseService;
import com.zyt.xxt.service.OssService;
import com.zyt.xxt.service.TeacherService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private OssService ossService;

    @Autowired
    private TeacherService teacherService;

    // 获取所有课程
    @GetMapping("/list")
    public ResponseResult<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseResult.success(courses);
    }

    // 增加课程
    @PostMapping("/add")
    public ResponseResult<String> addCourse(@RequestBody Course course, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String username = claims.getSubject();

        Teacher teacher = teacherService.findByUsername(username);
        course.setTeacherId(teacher.getId());

        boolean result = courseService.addCourse(course);
        if (result) {
            return ResponseResult.success("课程添加成功");
        } else {
            return ResponseResult.error("课程添加失败");
        }
    }


    // 删除课程
    @DeleteMapping("/delete/{id}")
    public ResponseResult<String> deleteCourse(@PathVariable int id) {
        boolean result = courseService.deleteCourse(id);
        if (result) {
            return ResponseResult.success("课程删除成功");
        } else {
            return ResponseResult.error("课程删除失败");
        }
    }

    // 修改课程
    @PutMapping("/update/{id}")
    public ResponseResult<String> updateCourseById(@RequestBody Course course) {
        boolean result = courseService.updateCourseById(course);
        if (result) {
            return ResponseResult.success("课程更新成功");
        } else {
            return ResponseResult.error("课程更新失败");
        }
    }

    @GetMapping("/my-courses")
    public ResponseResult<List<Course>> getMyCourses(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String username = claims.getSubject(); // 从 token 中拿到用户名

        Teacher teacher = teacherService.findByUsername(username); // 查找该教师

        if (teacher == null) {
            return ResponseResult.error("教师不存在");
        }

        List<Course> courses = courseService.getCoursesByTeacherId(teacher.getId());
        return ResponseResult.success(courses);
    }



    // 查询课程
    @GetMapping("/get/{id}")
    public ResponseResult<Course> getCourse(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            return ResponseResult.success(course);
        } else {
            return ResponseResult.error("未找到该课程");
        }
    }

    // 图片上传
    @PostMapping("/{id}/upload-image")
    public ResponseResult<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return ResponseResult.error("无效的文件名");
            }

            // 上传文件到 OSS
            ossService.uploadFile(fileName, file.getInputStream());
            // 获取上传后的文件 URL
            String fileUrl = ossService.getFileUrl(fileName);

            // 更新数据库中的图片路径
            courseService.updateCourseImage(Math.toIntExact(id), fileUrl);

            return ResponseResult.success("图片上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("图片上传失败");
        }
    }

    // 上传课程资源文件（如 PPT、Word）
    @PostMapping("/{id}/upload-resource")
    public ResponseResult<String> uploadResource(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return ResponseResult.error("无效的文件名");
            }

            // 上传文件到 OSS
            ossService.uploadFile(fileName, file.getInputStream());
            // 获取上传后的文件 URL
            String fileUrl = ossService.getFileUrl(fileName);

            // 更新课程的 resourceUrl 字段
            boolean result = courseService.updateCourseResource(Math.toIntExact(id), fileUrl);
            if (result) {
                return ResponseResult.success("资源上传成功");
            } else {
                return ResponseResult.error("资源上传失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("文件上传失败");
        }
    }


    // 根据课程名称和班级名称查询
    @GetMapping("/search")
    public ResponseResult<List<Course>> searchCourses(
            HttpServletRequest request,
            @RequestParam(required = false) String course_name,
            @RequestParam(required = false) String class_name,
            @RequestParam(defaultValue = "false") boolean all) {

        Claims claims = (Claims) request.getAttribute("claims");
        String username = claims.getSubject();

        Teacher teacher = teacherService.findByUsername(username);
        List<Course> courses;

        if (all && teacher != null) {
            // 查询所有课程
            courses = courseService.searchCourses(course_name, class_name);
        } else if (teacher != null) {
            // 只查当前教师的课程
            courses = courseService.searchCoursesByTeacherIdAndKeyword(teacher.getId(), course_name, class_name);
        } else {
            // 非教师用户或其他情况，默认不返回数据或抛出异常
            return ResponseResult.error("无权访问");
        }

        return ResponseResult.success(courses);
    }

}