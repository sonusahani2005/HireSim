package com.hiresim.backend.dto;

public class CodingExecutionRequest {

    private Long questionId;
    private String language;
    private String code;
    private String input;

    public CodingExecutionRequest() {
    }

    public CodingExecutionRequest(
            Long questionId,
            String language,
            String code,
            String input) {

        this.questionId = questionId;
        this.language = language;
        this.code = code;
        this.input = input;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}