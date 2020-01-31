package com.datamaster.testtask.service;

import com.datamaster.testtask.domain.Poll;
import com.datamaster.testtask.domain.Question;
import com.datamaster.testtask.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService
{
	private QuestionRepository questionRepository;

	public QuestionService(QuestionRepository questionRepository)
	{
		this.questionRepository = questionRepository;
	}

	public void deleteQuestions(List<Question> questions)
	{
		for (Question question :  questions)
		{
			question.setPoll(null);
			questionRepository.delete(question);
		}
	}

	public void saveQuestions(List<Question> questions)
	{
		questionRepository.saveAll(questions);
	}
}
