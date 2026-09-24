package com.hiresim.backend.repository;

import com.hiresim.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodingQuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByCategoryIgnoreCase(String category);
}