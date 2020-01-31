package com.datamaster.testtask.controller;

import com.datamaster.testtask.domain.Poll;
import com.datamaster.testtask.domain.Question;
import com.datamaster.testtask.domain.assembler.PollModelAssembler;
import com.datamaster.testtask.domain.dto.PollModel;
import com.datamaster.testtask.service.PollService;
import com.datamaster.testtask.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/poll")
@Api(tags = "Poll Controller", description = "Contains Polls Operations")
public class PollController
{
	private PollService pollService;
	private QuestionService questionService;
	private PollModelAssembler pollModelAssembler;
	private PagedResourcesAssembler<Poll> pagedResourcesAssembler;

	public PollController(PollService pollService, QuestionService questionService,
						  PollModelAssembler pollModelAssembler,
						  PagedResourcesAssembler<Poll> pagedResourcesAssembler)
	{
		this.pollService = pollService;
		this.questionService = questionService;
		this.pollModelAssembler = pollModelAssembler;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@ApiOperation(value = "Get one Poll by ID")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	@ResponseBody
	public ResponseEntity<PollModel> getOne(@PathVariable Long id)
	{
		Poll poll = pollService.getPollById(id);
		if (poll != null)
			return new ResponseEntity<>(pollModelAssembler.toResource(poll), HttpStatus.OK);
		else
			return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Delete one Poll by ID")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	@ResponseBody
	public ResponseEntity deleteOne(@PathVariable Long id)
	{
		Poll poll = pollService.getPollById(id);
		if (poll != null)
		{
			pollService.deletePoll(poll);
			return new ResponseEntity(HttpStatus.OK);
		}
		else
			return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Create Poll, put Poll Dto Model with nested Questions in Request Body for filling fields")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<PollModel> createOne(@RequestBody PollModel pollModel)
	{
		return new ResponseEntity<>(pollModelAssembler.toResource(pollService.savePoll(pollModelAssembler.fromModel(pollModel))), HttpStatus.OK);
	}

	@ApiOperation(value = "Update Poll by ID, put Poll Dto Model with nested Questions in Request Body for updating fields")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	@ResponseBody
	public ResponseEntity<PollModel> updateOne(@RequestBody PollModel pollModel, @PathVariable Long id)
	{
		Poll poll = pollService.getPollById(id);
		if (poll != null)
		{
			poll.setName(pollModel.getName());
			poll.setBeginDate(pollModel.getBeginDate());
			poll.setEndDate(pollModel.getEndDate());
			poll.setActive(pollModel.getActive());

			questionService.deleteQuestions(poll.getQuestions());
			poll.setQuestions(pollModel.getQuestions());

			poll = pollService.savePoll(poll);
			return new ResponseEntity<>(pollModelAssembler.toResource(poll), HttpStatus.OK);
		}
		else
			return ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Get all sorted Polls by parameters without paging")
	@RequestMapping(method = RequestMethod.GET, path = "/all")
	@ResponseBody
	public ResponseEntity<Resources<PollModel>> getAll(@RequestParam(value = "name", required = false) String name,
													   @RequestParam(value = "begin_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Calendar beginDate,
													   @RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Calendar endDate,
													   @RequestParam(value = "active", required = false) Boolean isActive,
													   Sort sort)
	{
		sort = updateSortByAliases(sort);
		List<Poll> polls = pollService.getAll(name, beginDate, endDate, isActive, sort);
		return new ResponseEntity<>(new Resources<>(polls.stream().map(poll -> pollModelAssembler.toResource(poll)).collect(Collectors.toList())), HttpStatus.OK);
	}

	@ApiOperation(value = "Get all sorted and paging Polls by parameters")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PagedResources<PollModel>> getAllPageable(@PageableDefault Pageable pageable, Sort sort,
																	@RequestParam(value = "name", required = false) String name,
																	@RequestParam(value = "begin_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Calendar beginDate,
																	@RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Calendar endDate,
																	@RequestParam(value = "active", required = false) Boolean isActive)
	{
		sort = updateSortByAliases(sort);
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<Poll> polls = pollService.getAllPageable(pageable, name, beginDate, endDate, isActive);
		return new ResponseEntity<>(pagedResourcesAssembler.toResource(polls, pollModelAssembler), HttpStatus.OK);
	}

	private Sort updateSortByAliases(Sort sort)
	{
		if (sort.isSorted())
		{
			List<Sort.Order> orders = new ArrayList<>();
			for (Sort.Order order : sort.stream().collect(Collectors.toList()))
			{
				String property = order.getProperty();
				switch (order.getProperty())
				{
					case "begin_date": property = "beginDate";break;
					case "end_date": property = "endDate";
				}
				orders.add(new Sort.Order(order.getDirection(), property));
			}
			return Sort.by(orders);
		}
		return sort;
	}
}
