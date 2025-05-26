package com.zyt.xxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyt.xxt.common.ResponseResult;
import com.zyt.xxt.entity.Homework;
import com.zyt.xxt.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    // 获取所有作业
    @GetMapping("/all")
    public List<Homework> getAllHomework() {
        return homeworkService.getAllHomework();
    }

    // 根据作业ID获取作业信息
    @GetMapping("/{id}")
    public Homework getHomeworkById(@PathVariable Integer id) {
        return homeworkService.getHomeworkById(id);
    }



    // 根据教师ID查询作业
    @GetMapping("/teacher/{teacherId}")
    public List<Homework> getHomeworkByTeacherId(@PathVariable Integer teacherId) {
        return homeworkService.getHomeworkByTeacherId(teacherId);
    }

    // 根据课程ID查询作业
    @GetMapping("/course/{courseId}")
    public List<Homework> getHomeworkByCourseId(@PathVariable Integer courseId) {
        return homeworkService.getHomeworkByCourseId(courseId);
    }

    // 添加作业
    @PostMapping("add")
    public boolean addHomework(@RequestBody Homework homework) {
        return homeworkService.addHomework(homework);
    }

    // 更新作业信息
    @PutMapping("update")
    public boolean updateHomework(@RequestBody Homework homework) {
        return homeworkService.updateHomeworkById(homework);
    }

    // 删除作业
    @DeleteMapping("/delete/{id}")
    public boolean deleteHomework(@PathVariable Integer id) {
        return homeworkService.deleteHomework(id);
    }

    @GetMapping("/filter-by-teacher")
    public ResponseResult<Page<Homework>> getHomeworkByTeacherIdWithFilter(
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<Homework> homeworkPage = homeworkService.getHomeworkByTeacherIdWithFilter(teacherId, title, courseId, status, pageNum, pageSize);
        return ResponseResult.success(homeworkPage);
    }

}