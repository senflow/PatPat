package team.patpat.filerunning;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 处理新程序的类，包括编译和运行Java程序，检查输出是否匹配等功能。
 * 使用时请确保文件路径和其他参数的正确性。
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
public class ProgramHandle {
    static long timeoutMillis = 10000000;
    static final Logger logger = initializeLogger();
    String line;
    long startTime;
    long endTime;

    static int flag;


    /**
     * 检查实际输出是否匹配期望输出。
     *
     * @param expectedOutputPath 期望输出文件路径
     * @param actualOutputPath   实际输出文件路径
     * @return 如果匹配返回 true，否则返回 false
     */
    private static boolean checkOutputMatch(String expectedOutputPath, String actualOutputPath) {
        try {
            Scanner expectedScanner = new Scanner(Files.newInputStream(Paths.get(expectedOutputPath)));
            Scanner actualScanner = new Scanner(Files.newInputStream(Paths.get(actualOutputPath)));

            while (actualScanner.hasNextLine()) {
                String expectedLine = expectedScanner.nextLine();
                String actualLine = actualScanner.nextLine();

                if (!expectedLine.equals(actualLine)) {
                    expectedScanner.close();
                    actualScanner.close();
                    return false;
                }
            }

            int lineCount = (int) Files.lines(Paths.get(actualOutputPath)).count();

            if (lineCount >= flag - 1) {
                flag = lineCount + 1;
                expectedScanner.close();
                actualScanner.close();
                return true;
            } else {
                expectedScanner.close();
                actualScanner.close();
                return false;
            }

            // 实际输出至少应包含期望输出的内容
        } catch (IOException e) {
            logger.severe("检查输出匹配时发生异常：" + e.getMessage());
            return false; // 发生异常，输出不匹配
        }
    }

    /**
     * 运行Java程序并进行评测。
     *
     * @param programName 项目名称
     * @param outPath     输出文件路径
     * @param inPath      输入文件路径
     * @param answerPath  标准答案文件路径
     * @return 评测结果字符串
     */
    public String runJavaProgram(String programName, String outPath, String inPath, String answerPath) {
        int runExitCode, compileExitCode;
        StringBuilder compileError = new StringBuilder();
        StringBuilder runtimeError = new StringBuilder();
        try {
            Process process = new ProcessBuilder("javac", "-encoding", "UTF-8", "-cp", "./mytest/" + programName + "/src",
                    "-d",
                    "./mytest/" + programName + "/out", "./mytest/" + programName + "/src" + "/*.java").start();

            // 获取编译错误信息
            BufferedReader compileErrorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String compileErrorLine;
            while ((compileErrorLine = compileErrorReader.readLine()) != null) {
                compileError.append(compileErrorLine);
                compileError.append("\n");
            }

            compileExitCode = process.waitFor();
            if (compileExitCode == 0) {
                ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-classpath", "./mytest/" + programName + "/out",
                        "Test");

                // 设置输出流
                if (outPath != null) {
                    //FileProcess.createFile(outPath);
                    File outputFile = new File(outPath);
                    runProcessBuilder.redirectOutput(outputFile);
                } else {
                    logger.warning("Invalid output file path");
                    closeLogFiles();
                    return "RE\n输出文件路径设置有误，请联系助教";

                }
                // 设置输入流
                if (inPath == null) {
                    logger.warning("Input file path is null.");
                    closeLogFiles();
                    return "RE\n输入文件不存在，请联系助教";
                }
                // 运行程序

                Process runProcess = runProcessBuilder.start();
                BufferedReader runtimeErrorReader = null;
                flag = 0;
                try {
                    startTime = System.currentTimeMillis();
                    runtimeErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
                    Scanner scanner = new Scanner(Files.newInputStream(Paths.get(inPath)));
                    while (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                        Thread.sleep(50);
                        writer.write(line);
                        writer.newLine();
                        writer.flush();
                        Thread.sleep(50);
                        flag++;

                        endTime = System.currentTimeMillis();
                        if (endTime - startTime > timeoutMillis) {
                            runProcess.destroy();
                            return "TLE\n运行超时，请检查代码是否有死循环";
                        }

                        // 检查输出是否匹配
                        if (!checkOutputMatch(answerPath, outPath)) {
                            runProcess.destroy(); // 中断运行
                            writer.close();
                            scanner.close();
                            String[] answer = FileProcess.readLinesFromTwoFiles(outPath, answerPath);
                            return "WA\n实际输出:" + answer[0] + "\n理论输出:" + answer[1] + "\n";
                        }
                    }
                    writer.close();
                    scanner.close();

                } catch (IOException e) {
                    String runtimeErrorLine;
                    while ((runtimeErrorLine = runtimeErrorReader.readLine()) != null) {
                        runtimeError.append(runtimeErrorLine).append("\n");
                    }
                    logger.severe("发生异常：" + e.getMessage());
                    closeLogFiles();
                    return "RE\n运行时错误，请检查代码提交格式" + runtimeError;
                }

                runExitCode = runProcess.waitFor();

            } else {
                logger.warning("项目编译失败：" + programName);
                logger.warning("编译错误：\n" + compileError);
                closeLogFiles();
                return "CE\n编译错误，请检查代码是否符合Java语法规范";
            }

            if (runExitCode == 0) {
                int ans = FileProcess.compareFile(answerPath, outPath);
                if (ans == 0) {
                    return "AC\n";
                } else if (ans == 3) {
                    return "WA\n输出部分正确，但是实际输出行数少于理论输出";
                }

            } else {
                logger.warning("项目运行失败：" + programName);
                closeLogFiles();
                return "RE\n" + runtimeError;
            }

        } catch (IOException | InterruptedException e) {
            logger.severe("发生异常：" + e.getMessage());
            return "RE\n" + runtimeError;
        }
        return "RE\n" + runtimeError;
    }

    /**
     * 运行Java程序并进行评测。
     *
     * @param programName 项目名称
     * @return 运行结果代码
     * <ul>
     * <li>0: 运行项目成功</li>
     * <li>1: 编译项目失败</li>
     * <li>2: 运行项目失败</li>
     * <li>3: 出现未知异常</li>
     * </ul>
     * @deprecated 该方法已弃用，请使用 {@link #runJavaProgram(String, String, String, String)} 方法
     */
    public int runJavaProgram(String programName) {
        /*
         * 返回值0表示运行项目成功
         * 返回值1表示编译项目失败
         * 返回值2表示运行项目失败
         * 返回值3表示出现未知异常
         */
        int runExitCode, compileExitCode;
        try {
            Process process = new ProcessBuilder("javac", "-encoding", "UTF-8", "-cp", "../mytest/" + programName + "/src",
                    "-d",
                    "./" + programName + "/out", "./" + programName + "/src" + "/*.java").start();
            compileExitCode = process.waitFor();

            if (compileExitCode == 0) {
                ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-classpath", "../mytest/" + programName + "/out",
                        "TestHello");

                try {
                    Process runProcess = runProcessBuilder.start();
                    runExitCode = runProcess.waitFor();

                } catch (IOException | InterruptedException e) {
                    logger.severe("Exception occurred while running the program: " + e.getMessage());
                    return 3;
                }
            } else {
                logger.warning("Compilation failed for program: " + programName);
                return 1;
            }
            if (runExitCode == 0) {
                return 0;
            } else {
                logger.warning("Program execution failed for program: " + programName);
                return 2;
            }

        } catch (IOException | InterruptedException e) {
            logger.severe("Exception occurred: " + e.getMessage());
            return 3;
        }
    }

    /**
     * 初始化日志。
     *
     * @return 初始化的Logger
     */
    private static Logger initializeLogger() {
        Logger logger = Logger.getLogger("ProgramHandleLogger");
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler("./log/programHandleError.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            logger.severe("Exception occurred while initializing logger: " + e.getMessage());
        }

        return logger;
    }

    /**
     * 关闭所有日志文件。
     */
    private static void closeLogFiles() {
        for (Handler handler : ProgramHandle.logger.getHandlers()) {
            if (handler instanceof FileHandler) {
                handler.close();
            }
        }
    }
}
