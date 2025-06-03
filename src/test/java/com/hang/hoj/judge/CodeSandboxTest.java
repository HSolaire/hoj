package com.hang.hoj.judge;

import com.hang.hoj.judge.model.ExecuteCodeRequest;
import com.hang.hoj.judge.sandbox.CodeSandbox;
import com.hang.hoj.model.dto.topic.JudgeCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;


@SpringBootTest
public class CodeSandboxTest {

    @Resource(name = "exampleCodeSandbox")
    private CodeSandbox exampleCodeSandbox;

    @Resource(name = "localCodeSandbox")
    private CodeSandbox localCodeSandbox;

    @Test
    public void exampleCodeSandboxTest() throws IOException, InterruptedException {
        String userCode = "import java.util.*;\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = 1;\n" +
                "        int b = 2;\n" +
                "        System.out.println(a + b);\n" +
                "    }\n" +
                "}";

        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setCode(userCode);
        request.setLanguage("java");
        request.setJudgeCaseList(null);
        exampleCodeSandbox.executeCode(request);
    }

    @Test
    public void exampleCodeSandboxScannerTest() throws IOException, InterruptedException {
        String userCode = "import java.util.*;\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        Scanner scanner = new Scanner(System.in);\n" +
                "        int a = scanner.nextInt();\n" +
                "        int b = scanner.nextInt();\n" +
                "        System.out.println(a + b);\n" +
                "    }\n" +
                "}";
//        Files.write(Paths.get("Main.java"), userCode.getBytes(StandardCharsets.UTF_8));

        ExecuteCodeRequest request = new ExecuteCodeRequest();
        request.setCode(userCode);
        request.setLanguage("java");
        request.setJudgeCaseList(
                Arrays.asList(
                        new JudgeCase("1 2", "3"),
                        new JudgeCase("3 4", "7"),
                        new JudgeCase("5 6", "11")
                )
        );
        localCodeSandbox.executeCode(request);
    }


}
