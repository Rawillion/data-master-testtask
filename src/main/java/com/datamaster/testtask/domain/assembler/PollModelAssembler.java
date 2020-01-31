package com.datamaster.testtask.domain.assembler;

import com.datamaster.testtask.controller.PollController;
import com.datamaster.testtask.domain.Poll;
import com.datamaster.testtask.domain.Question;
import com.datamaster.testtask.domain.dto.PollModel;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class PollModelAssembler implements ResourceAssembler<Poll, PollModel>
{

	@Override
	public PollModel toResource(Poll entity)
	{
		PollModel model = new PollModel();
		model.setName(entity.getName());
		model.setBeginDate(entity.getBeginDate());
		model.setEndDate(entity.getEndDate());
		model.setActive(entity.getActive());
		model.setQuestions(entity.getQuestions().stream().sorted(Comparator.comparing(Question::getSortOrder)).collect(Collectors.toList()));
		model.add(linkTo(PollController.class).slash(entity.getId()).withSelfRel());
		return model;
	}

	public Poll fromModel(PollModel model)
	{
		Poll poll = new Poll();
		poll.setName(model.getName());
		poll.setBeginDate(model.getBeginDate());
		poll.setEndDate(model.getEndDate());
		poll.setActive(model.getActive());
		poll.setQuestions(model.getQuestions());
		return poll;
	}
}
