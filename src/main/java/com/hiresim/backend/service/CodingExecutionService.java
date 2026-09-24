package com.hiresim.backend.service;

import com.hiresim.backend.dto.CodingExecutionResponse;
import com.hiresim.backend.entity.CodingTestCase;
import com.hiresim.backend.repository.CodingTestCaseRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CodingExecutionService {

    private final CodingTestCaseRepository testCaseRepository;

    public CodingExecutionService(CodingTestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    // =========================================================
    // JAVA
    // =========================================================

    public CodingExecutionResponse executeJavaCode(
            Long questionId,
            String code) {

        return executeCode(
                questionId,
                code,
                "java"
        );
    }

    // =========================================================
    // PYTHON
    // =========================================================

    public CodingExecutionResponse executePythonCode(
            Long questionId,
            String code) {

        return executeCode(
                questionId,
                code,
                "python"
        );
    }

    // =========================================================
    // C++
    // =========================================================

    public CodingExecutionResponse executeCppCode(
            Long questionId,
            String code) {

        return executeCode(
                questionId,
                code,
                "cpp"
        );
    }

    // =========================================================
    // COMMON EXECUTION METHOD
    // =========================================================

    private CodingExecutionResponse executeCode(
            Long questionId,
            String code,
            String language) {

        Path tempDirectory = null;

        try {

            // 1. Get test cases
            List<CodingTestCase> testCases =
                    testCaseRepository.findByQuestionId(questionId);

            if (testCases.isEmpty()) {

                return new CodingExecutionResponse(
                        false,
                        "",
                        "No test cases found for this question.",
                        0,
                        0
                );
            }

            // 2. Create temporary directory
            tempDirectory =
                    Files.createTempDirectory(
                            "hiresim-" + language + "-"
                    );

            // =================================================
            // LANGUAGE FILE + COMPILATION
            // =================================================

            ProcessBuilder compileProcess;

            String runCommand;
            String runArgument;

            // ---------------- JAVA ----------------

            if (language.equalsIgnoreCase("java")) {

                Path javaFile =
                        tempDirectory.resolve("Main.java");

                Files.writeString(
                        javaFile,
                        code,
                        StandardCharsets.UTF_8
                );

                compileProcess =
                        new ProcessBuilder(
                                "javac",
                                "Main.java"
                        );

                compileProcess.directory(
                        tempDirectory.toFile()
                );

                compileProcess.redirectErrorStream(true);

                Process compiler =
                        compileProcess.start();

                boolean compiled =
                        compiler.waitFor(
                                10,
                                TimeUnit.SECONDS
                        );

                String compileOutput =
                        readProcessOutput(compiler);

                if (!compiled) {

                    compiler.destroyForcibly();

                    return new CodingExecutionResponse(
                            false,
                            "",
                            "Java compilation timed out.",
                            0,
                            testCases.size()
                    );
                }

                if (compiler.exitValue() != 0) {

                    return new CodingExecutionResponse(
                            false,
                            "",
                            compileOutput,
                            0,
                            testCases.size()
                    );
                }

                runCommand = "java";
                runArgument = "Main";
            }

            // ---------------- PYTHON ----------------

            else if (language.equalsIgnoreCase("python")) {

                Path pythonFile =
                        tempDirectory.resolve("main.py");

                Files.writeString(
                        pythonFile,
                        code,
                        StandardCharsets.UTF_8
                );

                /*
                 * Windows Python command.
                 * "python" is tried first.
                 */
                compileProcess =
                        new ProcessBuilder(
                                "python",
                                "-m",
                                "py_compile",
                                "main.py"
                        );

                compileProcess.directory(
                        tempDirectory.toFile()
                );

                compileProcess.redirectErrorStream(true);

                Process compiler =
                        compileProcess.start();

                boolean compiled =
                        compiler.waitFor(
                                10,
                                TimeUnit.SECONDS
                        );

                String compileOutput =
                        readProcessOutput(compiler);

                if (!compiled) {

                    compiler.destroyForcibly();

                    return new CodingExecutionResponse(
                            false,
                            "",
                            "Python compilation/check timed out.",
                            0,
                            testCases.size()
                    );
                }

                if (compiler.exitValue() != 0) {

                    return new CodingExecutionResponse(
                            false,
                            "",
                            compileOutput,
                            0,
                            testCases.size()
                    );
                }

                runCommand = "python";
                runArgument = "main.py";
            }

            // ---------------- C++ ----------------

            else if (language.equalsIgnoreCase("cpp")) {

                Path cppFile =
                        tempDirectory.resolve("main.cpp");

                Path executable =
                        tempDirectory.resolve("main.exe");

                Files.writeString(
                        cppFile,
                        code,
                        StandardCharsets.UTF_8
                );

                compileProcess =
                        new ProcessBuilder(
                                "g++",
                                "main.cpp",
                                "-o",
                                "main.exe"
                        );

                compileProcess.directory(
                        tempDirectory.toFile()
                );

                compileProcess.redirectErrorStream(true);

                Process compiler =
                        compileProcess.start();

                boolean compiled =
                        compiler.waitFor(
                                10,
                                TimeUnit.SECONDS
                        );

                String compileOutput =
                        readProcessOutput(compiler);

                if (!compiled) {

                    compiler.destroyForcibly();

                    return new CodingExecutionResponse(
                            false,
                            "",
                            "C++ compilation timed out.",
                            0,
                            testCases.size()
                    );
                }

                if (compiler.exitValue() != 0) {

                    return new CodingExecutionResponse(
                            false,
                            "",
                            compileOutput,
                            0,
                            testCases.size()
                    );
                }

                runCommand =
                        executable.toAbsolutePath().toString();

                runArgument = null;
            }

            else {

                return new CodingExecutionResponse(
                        false,
                        "",
                        "Unsupported language: " + language,
                        0,
                        testCases.size()
                );
            }

            // =================================================
            // RUN TEST CASES
            // =================================================

            int passedTests = 0;

            StringBuilder resultOutput =
                    new StringBuilder();

            StringBuilder errors =
                    new StringBuilder();

            for (CodingTestCase testCase : testCases) {

                ProcessBuilder runProcess;

                if (runArgument != null) {

                    runProcess =
                            new ProcessBuilder(
                                    runCommand,
                                    runArgument
                            );

                } else {

                    runProcess =
                            new ProcessBuilder(
                                    runCommand
                            );
                }

                runProcess.directory(
                        tempDirectory.toFile()
                );

                runProcess.redirectErrorStream(true);

                Process program =
                        runProcess.start();

                // -----------------------------------------
                // Send input
                // -----------------------------------------

                if (testCase.getInput() != null) {

                    try (
                            BufferedWriter writer =
                                    new BufferedWriter(
                                            new OutputStreamWriter(
                                                    program.getOutputStream(),
                                                    StandardCharsets.UTF_8
                                            )
                                    )
                    ) {

                        writer.write(
                                testCase.getInput()
                        );

                        writer.newLine();

                        writer.flush();
                    }
                }

                // -----------------------------------------
                // 5 second execution limit
                // -----------------------------------------

                boolean finished =
                        program.waitFor(
                                5,
                                TimeUnit.SECONDS
                        );

                if (!finished) {

                    program.destroyForcibly();

                    errors.append(
                            "Test Case "
                                    + testCase.getTestCaseNumber()
                                    + ": Time limit exceeded.\n"
                    );

                    continue;
                }

                String actualOutput =
                        readProcessOutput(program).trim();

                String expectedOutput =
                        testCase.getExpectedOutput() == null
                                ? ""
                                : testCase
                                    .getExpectedOutput()
                                    .trim();

                // -----------------------------------------
                // Runtime error
                // -----------------------------------------

                if (program.exitValue() != 0) {

                    errors.append(
                            "Test Case "
                                    + testCase.getTestCaseNumber()
                                    + ": Runtime error.\n"
                                    + actualOutput
                                    + "\n"
                    );

                    continue;
                }

                // -----------------------------------------
                // Show output
                // -----------------------------------------

                resultOutput.append(
                        "Test Case "
                                + testCase.getTestCaseNumber()
                                + ": "
                                + actualOutput
                                + "\n"
                );

                // -----------------------------------------
                // Compare answer
                // -----------------------------------------

                if (actualOutput.equalsIgnoreCase(
                        expectedOutput)) {

                    passedTests++;

                } else {

                    errors.append(
                            "Test Case "
                                    + testCase.getTestCaseNumber()
                                    + ": Wrong Answer. "
                                    + "Expected = "
                                    + expectedOutput
                                    + ", Actual = "
                                    + actualOutput
                                    + "\n"
                    );
                }
            }

            // =================================================
            // FINAL RESULT
            // =================================================

            boolean success =
                    passedTests == testCases.size();

            return new CodingExecutionResponse(
                    success,
                    resultOutput.toString().trim(),
                    errors.toString().trim(),
                    passedTests,
                    testCases.size()
            );

        } catch (Exception e) {

            return new CodingExecutionResponse(
                    false,
                    "",
                    e.getMessage(),
                    0,
                    0
            );

        } finally {

            // Delete temporary files
            if (tempDirectory != null) {

                deleteDirectory(
                        tempDirectory
                );
            }
        }
    }

    // =========================================================
    // READ PROCESS OUTPUT
    // =========================================================

    private String readProcessOutput(
            Process process)
            throws IOException {

        try (
                InputStream inputStream =
                        process.getInputStream()
        ) {

            return new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }
    }

    // =========================================================
    // DELETE TEMP DIRECTORY
    // =========================================================

    private void deleteDirectory(
            Path directory) {

        try {

            Files.walk(directory)
                    .sorted(
                            (a, b) ->
                                    b.compareTo(a)
                    )
                    .forEach(path -> {

                        try {

                            Files.deleteIfExists(path);

                        } catch (IOException ignored) {
                        }

                    });

        } catch (IOException ignored) {
        }
    }
}