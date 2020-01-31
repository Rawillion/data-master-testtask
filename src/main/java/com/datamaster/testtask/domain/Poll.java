package com.datamaster.testtask.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "poll")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(of = {"name"})
public class Poll
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poll_sequence")
	@SequenceGenerator(name = "poll_sequence", sequenceName = "poll_sequence", allocationSize = 1)
	private Long id;

	@Column
	private String name;

	@Column(name = "begin_date", columnDefinition = "timestamp with no time zone")
	@Temporal(TemporalType.DATE)
	private Calendar beginDate;

	@Column(name = "end_date", columnDefinition = "timestamp with no time zone")
	@Temporal(TemporalType.DATE)
	private Calendar endDate;

	@Column
	private Boolean active;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "poll",cascade = CascadeType.ALL)
	private List<Question> questions = new ArrayList<>();

}
