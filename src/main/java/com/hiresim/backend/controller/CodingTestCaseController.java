package com.hiresim.backend.controller;

import com.hiresim.backend.entity.CodingTestCase;
import com.hiresim.backend.repository.CodingTestCaseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coding/testcases")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class CodingTestCaseController {

    private final CodingTestCaseRepository testCaseRepository;

    public CodingTestCaseController(
            CodingTestCaseRepository testCaseRepository) {

        this.testCaseRepository = testCaseRepository;
    }

    @GetMapping("/{questionId}")
    public List<CodingTestCase> getTestCases(
            @PathVariable Long questionId) {

        return testCaseRepository.findByQuestionId(questionId);
    }

    @PostMapping
    public CodingTestCase addTestCase(
            @RequestBody CodingTestCase testCase) {

        return testCaseRepository.save(testCase);
    }
}
