package com.zyt.xxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyt.xxt.entity.HomeworkQuestion;
import com.zyt.xxt.mapper.HomeworkQuestionMapper;
import com.zyt.xxt.service.HomeworkQuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeworkQuestionServiceImpl 
    extends ServiceImpl<HomeworkQuestionMapper, HomeworkQuestion> 
    implements HomeworkQuestionService {

    @Override
    public List<HomeworkQuestion> getQuestionsByHomeworkId(Integer homeworkId) {
        QueryWrapper<HomeworkQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("homework_id", homeworkId)
               .orderByAsc("sequence");
        return this.list(wrapper);
    }

    @Override
    public boolean saveBatchQuestions(List<HomeworkQuestion> questions) {
        return this.saveBatch(questions);
    }

    @Override
    public boolean deleteByHomeworkId(Integer homeworkId) {
        QueryWrapper<HomeworkQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("homework_id", homeworkId);
        return this.remove(wrapper);
    }

    @Override
    public boolean deleteQuestionById(Integer questionId) {
        return removeById(questionId);
    }

}
