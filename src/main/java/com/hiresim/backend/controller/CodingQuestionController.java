package com.hiresim.backend.controller;

import com.hiresim.backend.entity.Question;
import com.hiresim.backend.repository.CodingQuestionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coding/questions")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class CodingQuestionController {

    private final CodingQuestionRepository codingQuestionRepository;

    public CodingQuestionController(
            CodingQuestionRepository codingQuestionRepository) {

        this.codingQuestionRepository =
                codingQuestionRepository;
    }

    @GetMapping
    public List<Question> getCodingQuestions() {

        return codingQuestionRepository
                .findByCategoryIgnoreCase("Coding");
    }
}