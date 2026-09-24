package com.hiresim.backend.controller;

import com.hiresim.backend.dto.CodingExecutionRequest;
import com.hiresim.backend.dto.CodingExecutionResponse;
import com.hiresim.backend.service.CodingExecutionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coding")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class CodingExecutionController {

    private final CodingExecutionService codingExecutionService;

    public CodingExecutionController(
            CodingExecutionService codingExecutionService) {

        this.codingExecutionService =
                codingExecutionService;
    }

    @PostMapping("/run")
    public CodingExecutionResponse runCode(
            @RequestBody CodingExecutionRequest request) {

        if (request.getLanguage() == null ||
                request.getLanguage().trim().isEmpty()) {

            return new CodingExecutionResponse(
                    false,
                    "",
                    "Programming language is required.",
                    0,
                    0
            );
        }

        if (request.getQuestionId() == null) {

            return new CodingExecutionResponse(
                    false,
                    "",
                    "Question ID is required.",
                    0,
                    0
            );
        }

        if (request.getCode() == null ||
                request.getCode().trim().isEmpty()) {

            return new CodingExecutionResponse(
                    false,
                    "",
                    "Code is required.",
                    0,
                    0
            );
        }

        String language =
                request.getLanguage()
                        .trim()
                        .toLowerCase();

        switch (language) {

            case "java":

                return codingExecutionService.executeJavaCode(
                        request.getQuestionId(),
                        request.getCode()
                );

            case "python":

                return codingExecutionService.executePythonCode(
                        request.getQuestionId(),
                        request.getCode()
                );

            case "cpp":
            case "c++":

                return codingExecutionService.executeCppCode(
                        request.getQuestionId(),
                        request.getCode()
                );

            default:

                return new CodingExecutionResponse(
                        false,
                        "",
                        "Unsupported language. "
                                + "Use Java, Python, or C++.",
                        0,
                        0
                );
        }
    }
}