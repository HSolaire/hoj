package com.hang.hoj.judge.sandbox.impl;

import com.hang.hoj.judge.model.ExecuteCodeRequest;
import com.hang.hoj.judge.model.ExecuteCodeResponse;
import com.hang.hoj.judge.sandbox.CodeSandbox;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Author: HSolaire
 * Date: 2025/5/17 21:25
 */
@Service
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) throws IOException {
        System.out.println("示例代码沙箱");
        File compileFile = null;
        File binaryFile = null;
        Process compileProcess = null;
        Process runProcess = null;
        BufferedWriter codeStrWriter = null;
        try {
            compileFile = new File("/Users/hsola/Coder/Data/Main.java");
            // 1. 写入代码到 Main.java
            if (compileFile.exists()) {
                compileFile.delete();
            }
            compileFile.createNewFile();
            codeStrWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(compileFile.toPath()), StandardCharsets.UTF_8));
            codeStrWriter.write(request.getCode());
            codeStrWriter.flush();

            binaryFile = new File("/Users/hsola/Data/Main.class");
            if (binaryFile.exists()) {
                binaryFile.delete();
            }
            // 2. 编译代码到 Main.class
            compileProcess = Runtime.getRuntime().exec("javac /Users/hsola/Coder/Data/Main.java");
//        BufferedReader br = new BufferedReader(new InputStreamReader(compileProcess.getInputStream(), Charset.forName("UTF-8")));
            int compileVal = compileProcess.waitFor();
            if (compileVal == 0) {
                System.out.println("Main.java Success Compile!");
                readInputStream(compileProcess.getInputStream());
                // 3. 执行Main 获取 Process
                runProcess = Runtime.getRuntime().exec("java -cp /Users/hsola/Coder/Data/ Main");

                // 循环提供输入，测试用例是否正确



                int runVal = runProcess.waitFor();
                if (runVal == 0) {
                    System.out.println("Main.java Success Run!");
                    readInputStream(runProcess.getInputStream());
                    // 4. 利用输出流，把测试用例写入程序
                } else {
                    System.out.println("Main.java Failure Run!");
                    readInputStream(runProcess.getErrorStream());
                }
            } else {
                System.out.println("Main.java Failure Compile!");
                readInputStream(compileProcess.getErrorStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭流，销毁文件
            if (codeStrWriter != null) {
                codeStrWriter.flush();
                codeStrWriter.close();
            }
            if (compileFile != null && compileFile.exists()) {
                compileFile.delete();
            }
            if (compileProcess != null) {
                compileProcess.destroy();
            }
            if (runProcess != null) {
                runProcess.destroy();
            }
            if (binaryFile != null && binaryFile.exists()) {
                binaryFile.delete();
            }
        }
        return null;
    }

    void readInputStream(InputStream in) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        ) {
            String line;
            // 输出正常信息
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串写入 OutputStream
     */
    void writeToOutputStream(OutputStream outputStream, String str) {
        try {
            outputStream.write(str.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("Error writing to OutputStream: " + e.getMessage());
        }
    }

}
