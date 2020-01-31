package com.datamaster.testtask.repository;

import com.datamaster.testtask.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long>
{
}
