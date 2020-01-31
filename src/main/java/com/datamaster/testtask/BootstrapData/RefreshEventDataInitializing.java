package com.datamaster.testtask.BootstrapData;

import com.datamaster.testtask.domain.Poll;
import com.datamaster.testtask.domain.Question;
import com.datamaster.testtask.repository.PollRepository;
import com.datamaster.testtask.repository.QuestionRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class RefreshEventDataInitializing implements ApplicationListener<ContextRefreshedEvent>
{
	private PollRepository pollRepository;
	private QuestionRepository questionRepository;

	public RefreshEventDataInitializing(PollRepository pollRepository,
										QuestionRepository questionRepository)
	{
		this.pollRepository = pollRepository;
		this.questionRepository = questionRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
	{
		Calendar beginDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		beginDate.set(2020, 1, 15);
		endDate.set(2020, 1, 15);

		for (int i = 0; i < 24; ++i)
		{
			beginDate.add(Calendar.DAY_OF_YEAR, -1);
			endDate.add(Calendar.DAY_OF_YEAR, 1);

			Poll poll = new Poll();
			poll.setBeginDate(beginDate);
			poll.setEndDate(endDate);
			poll.setActive(i % 2 == 0);
			poll.setName("poll n" + (i + 1));
			pollRepository.save(poll);

			for (int j = 0; j < 10; ++j)
			{
				Question question = new Question();
				question.setText(String.format("Question n%d Poll n%d", j + 1, i + 1));
				question.setSortOrder(10 - j);
				question.setPoll(poll);
				questionRepository.save(question);
			}
		}
	}
}
