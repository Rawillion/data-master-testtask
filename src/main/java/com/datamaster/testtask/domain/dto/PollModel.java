package com.datamaster.testtask.domain.dto;

import com.datamaster.testtask.domain.Question;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.Calendar;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "Poll DTO")
public class PollModel extends ResourceSupport
{
	private String name;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty(value = "begin_date")
	private Calendar beginDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty(value = "end_date")
	private Calendar endDate;

	private Boolean active;
	private List<Question> questions;
}
