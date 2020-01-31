package com.datamaster.testtask.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "question")
@ToString(of = {"text"})
public class Question
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_sequence")
	@SequenceGenerator(name = "question_sequence", sequenceName = "question_sequence", allocationSize = 1)
	@JsonIgnore
	private Long id;

	@ManyToOne
	@JoinColumn(name = "poll_id")
	@JsonIgnore
	private Poll poll;

	@Column
	private String text;

	@Column(name = "sort_order")
	private Integer sortOrder;
}
