package com.datamaster.testtask.service;

import com.datamaster.testtask.domain.Poll;
import com.datamaster.testtask.domain.Question;
import com.datamaster.testtask.repository.PollRepository;
import com.datamaster.testtask.repository.QuestionRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class PollService
{
	private PollRepository pollRepository;
	private QuestionRepository questionRepository;

	public PollService(PollRepository pollRepository, QuestionRepository questionRepository)
	{
		this.pollRepository = pollRepository;
		this.questionRepository = questionRepository;
	}

	public Poll getPollById(Long id)
	{
		return pollRepository.findById(id).orElse(null);
	}

	public void deletePoll(Poll poll)
	{
		questionRepository.deleteAll(poll.getQuestions());
		pollRepository.delete(poll);
	}

	public Poll savePoll(Poll poll)
	{
		pollRepository.save(poll);
		for (Question question : poll.getQuestions())
		{
			question.setPoll(poll);
			questionRepository.save(question);
		}
		return poll;
	}

	public Page<Poll> getAllPageable(Pageable pageable, String name, Calendar beginDate, Calendar endDate, Boolean isActive)
	{
		Page<Poll> polls = pollRepository.findAll(getExampleObject(name, beginDate, endDate, isActive), pageable);
		return polls;
	}

	public List<Poll> getAll(String name, Calendar beginDate, Calendar endDate, Boolean isActive, Sort sort)
	{
		List<Poll> polls = pollRepository.findAll(getExampleObject(name, beginDate, endDate, isActive), sort);
		return polls;
	}

	private Example<Poll> getExampleObject(String name, Calendar beginDate, Calendar endDate, Boolean isActive)
	{
		Poll exampleObject = new Poll();
		exampleObject.setName(name);
		exampleObject.setBeginDate(beginDate);
		exampleObject.setEndDate(endDate);
		exampleObject.setActive(isActive);
		ExampleMatcher matcher = ExampleMatcher.matching()
											   .withMatcher("name", match -> match.ignoreCase());
		return Example.of(exampleObject, matcher);
	}
}
