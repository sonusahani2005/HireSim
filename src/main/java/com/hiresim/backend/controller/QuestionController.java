package com.hiresim.backend.controller;

import com.hiresim.backend.entity.Question;
import com.hiresim.backend.repository.QuestionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // =========================
    // ALL QUESTIONS
    // =========================

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // =========================
    // APTITUDE QUESTIONS
    // =========================

    @GetMapping("/aptitude")
    public List<Question> getAptitudeQuestions() {

        List<Question> aptitudeQuestions = new ArrayList<>();

        aptitudeQuestions.addAll(
                questionRepository.findByCategory("Quantitative")
        );

        aptitudeQuestions.addAll(
                questionRepository.findByCategory("Logical Reasoning")
        );

        aptitudeQuestions.addAll(
                questionRepository.findByCategory("Verbal")
        );

        aptitudeQuestions.addAll(
                questionRepository.findByCategory("Probability")
        );

        aptitudeQuestions.addAll(
                questionRepository.findByCategory("Time and Work")
        );

        return aptitudeQuestions;
    }

    // =========================
    // TECHNICAL QUESTIONS
    // =========================

    @GetMapping("/technical")
    public List<Question> getTechnicalQuestions() {

        List<Question> technicalQuestions = new ArrayList<>();

        technicalQuestions.addAll(
                questionRepository.findByCategory("Java")
        );

        technicalQuestions.addAll(
                questionRepository.findByCategory("OOP")
        );

        technicalQuestions.addAll(
                questionRepository.findByCategory("DSA")
        );

        technicalQuestions.addAll(
                questionRepository.findByCategory("DBMS")
        );

        technicalQuestions.addAll(
                questionRepository.findByCategory("OS")
        );

        technicalQuestions.addAll(
                questionRepository.findByCategory("Computer Networks")
        );

        return technicalQuestions;
    }

    // =========================
    // CATEGORY
    // =========================

    @GetMapping("/category/{category}")
    public List<Question> getByCategory(
            @PathVariable String category) {

        return questionRepository.findByCategory(category);
    }

    // =========================
    // DIFFICULTY
    // =========================

    @GetMapping("/difficulty/{difficulty}")
    public List<Question> getByDifficulty(
            @PathVariable String difficulty) {

        return questionRepository.findByDifficulty(difficulty);
    }

    // =========================
    // SOURCE
    // =========================

    @GetMapping("/source/{source}")
    public List<Question> getBySource(
            @PathVariable String source) {

        return questionRepository.findBySource(source);
    }
}