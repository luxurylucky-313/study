package com.zyt.xxt.controller;

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
    public ResponseResult<List<Homework>> getAllHomework() {
        List<Homework> homeworkList = homeworkService.getAllHomework();
        return ResponseResult.success(homeworkList);
    }

    // 根据ID获取单个作业
    @GetMapping("/{id}")
    public ResponseResult<Homework> getHomeworkById(@PathVariable("id") Integer id) {
        Homework homework = homeworkService.getHomeworkById(id);
        if (homework != null) {
            return ResponseResult.success(homework);
        } else {
            return ResponseResult.error("未找到该作业");
        }
    }

    // 添加新作业
    @PostMapping("/add")
    public ResponseResult<String> addHomework(@RequestBody Homework homework) {
        boolean success = homeworkService.addHomework(homework);
        if (success) {
            return ResponseResult.success("作业添加成功");
        } else {
            return ResponseResult.error("作业添加失败");
        }
    }

    // 更新作业
    @PutMapping("/update")
    public ResponseResult<String> updateHomework(@RequestBody Homework homework) {
        boolean success = homeworkService.updateHomeworkById(homework);
        if (success) {
            return ResponseResult.success("作业更新成功");
        } else {
            return ResponseResult.error("作业更新失败");
        }
    }

    // 删除作业
    @DeleteMapping("/delete/{id}")
    public ResponseResult<String> deleteHomework(@PathVariable("id") Integer id) {
        boolean success = homeworkService.deleteHomework(id);
        if (success) {
            return ResponseResult.success("作业删除成功");
        } else {
            return ResponseResult.error("作业删除失败");
        }
    }
}