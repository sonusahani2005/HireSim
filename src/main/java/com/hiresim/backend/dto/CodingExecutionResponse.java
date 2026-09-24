package com.hiresim.backend.dto;

public class CodingExecutionResponse {

    private boolean success;
    private String output;
    private String error;
    private int passedTests;
    private int totalTests;

    public CodingExecutionResponse() {
    }

    public CodingExecutionResponse(
            boolean success,
            String output,
            String error,
            int passedTests,
            int totalTests) {

        this.success = success;
        this.output = output;
        this.error = error;
        this.passedTests = passedTests;
        this.totalTests = totalTests;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }
}