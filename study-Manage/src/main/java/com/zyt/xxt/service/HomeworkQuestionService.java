package com.zyt.xxt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyt.xxt.entity.HomeworkQuestion;

import java.util.List;

public interface HomeworkQuestionService extends IService<HomeworkQuestion> {
    /**
     * 根据作业ID获取问题列表
     * @param homeworkId 作业ID
     * @return 问题列表
     */
    List<HomeworkQuestion> getQuestionsByHomeworkId(Integer homeworkId);
    
    /**
     * 批量保存作业问题
     * @param questions 问题列表
     * @return 是否成功
     */
    boolean saveBatchQuestions(List<HomeworkQuestion> questions);
    
    /**
     * 删除作业下的所有问题
     * @param homeworkId 作业ID
     * @return 是否成功
     */
    boolean deleteByHomeworkId(Integer homeworkId);

    /**
     * 根据题目ID删除单个作业问题
     * @param questionId 题目ID
     * @return 是否删除成功
     */
    boolean deleteQuestionById(Integer questionId);

}
