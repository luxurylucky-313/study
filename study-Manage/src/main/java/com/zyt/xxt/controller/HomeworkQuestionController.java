package com.zyt.xxt.controller;

import com.zyt.xxt.common.ResponseResult;
import com.zyt.xxt.entity.HomeworkQuestion;
import com.zyt.xxt.service.HomeworkQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/homework-questions")
public class HomeworkQuestionController {

    @Autowired
    private HomeworkQuestionService homeworkQuestionService;

    /**
     * 获取作业下的所有问题
     * @param homeworkId 作业ID
     * @return 问题列表
     */
    @GetMapping("/{homeworkId}")
    public ResponseResult<List<HomeworkQuestion>> getQuestions(@PathVariable Integer homeworkId) {
        List<HomeworkQuestion> questions = homeworkQuestionService.getQuestionsByHomeworkId(homeworkId);
        return ResponseResult.success(questions);
    }

    /**
     * 批量保存作业问题
     * @param questions 问题列表
     * @return 操作结果
     */
    @PostMapping("/batch")
    public ResponseResult<Boolean> saveBatch(@RequestBody List<HomeworkQuestion> questions) {
        boolean result = homeworkQuestionService.saveBatchQuestions(questions);
        return result ? ResponseResult.success(true) : ResponseResult.error("保存失败");
    }

    /**
     * 删除作业下的所有问题
     * @param homeworkId 作业ID
     * @return 操作结果
     */
    @DeleteMapping("/{homeworkId}")
    public ResponseResult<Boolean> deleteByHomeworkId(@PathVariable Integer homeworkId) {
        boolean result = homeworkQuestionService.deleteByHomeworkId(homeworkId);
        return result ? ResponseResult.success(true) : ResponseResult.error("删除失败");
    }

    /**
     * 删除作业下的问题
     * @param questionId
     * @return
     */
    @DeleteMapping("/delete/{questionId}")
    public ResponseResult<Boolean> deleteQuestionById(@PathVariable Integer questionId) {
        boolean result = homeworkQuestionService.deleteQuestionById(questionId);
        return result ? ResponseResult.success(true) : ResponseResult.error("删除失败");
    }

}
