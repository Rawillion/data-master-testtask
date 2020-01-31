package com.datamaster.testtask.repository;

import com.datamaster.testtask.domain.Poll;
import com.datamaster.testtask.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>
{
	List<Question> getAllByPollOrderBySortOrderAsc(Poll poll);
}
