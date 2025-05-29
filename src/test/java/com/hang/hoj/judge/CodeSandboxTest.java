package com.hang.hoj.judge;

import com.hang.hoj.judge.model.ExecuteCodeRequest;
import com.hang.hoj.judge.sandbox.CodeSandbox;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;


@SpringBootTest
public class CodeSandboxTest {

    @Resource(name = "exampleCodeSandbox")
    private CodeSandbox exampleCodeSandbox;

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

        writeUserCodeToFile(userCode, "Main.java");

//        ExecuteCodeRequest request = new ExecuteCodeRequest();
//        request.setCode(userCode);
//        request.setLanguage("java");
//        request.setJudgeCaseList(null);
//        exampleCodeSandbox.executeCode(request);
    }

    private void writeUserCodeToFile(String userCode, String filename) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get(filename);
        java.nio.file.Files.write(path, userCode.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }


}
