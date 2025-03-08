package team.patpat.filerunning;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 这是一个在项目运行时处理文件操作的工具类，包含了读取文件、创建目录、创建文件、比较文件等功能。
 * 使用时，请确保文件路径的正确性，并注意异常处理。
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */

public class FileProcess {
    public static final Logger logger = initializeLogger();

    /**
     * 从两个文件中读取数据，并返回包含其中一个文件的最后一行和另一个文件中相同行数内容的字符串数组。
     *
     * @param filePath1 第一个文件路径
     * @param filePath2 第二个文件路径
     * @return 含其中一个文件的最后一行和另一个文件中相同行数内容的字符串数组
     */
    public static String[] readLinesFromTwoFiles(String filePath1, String filePath2) {

        int lastLineOfFile1 = readLastLine(filePath1);

        String lineOfFile1 = readLine(filePath1, lastLineOfFile1);
        // 获取第一个文件最后一行的行数

        String lineOfFile2 = readLine(filePath2, lastLineOfFile1 == 0 ? lastLineOfFile1 + 1 : lastLineOfFile1);
        // 读取第二个文件相同行数的内容


        return new String[]{lineOfFile1, lineOfFile2};
    }

    /**
     * 读取文件的最后一行内容。
     *
     * @param filePath 文件路径
     * @return 最后一行的行数
     */
    private static int readLastLine(String filePath) {
        Path path = Paths.get(filePath);

        List<String> lines = new LinkedList<>();
        try {
            lines = Files.readAllLines(path);
        } catch (Exception e) {
            logger.severe("读取文件失败");
            System.out.println(e.getMessage());
            closeLogFiles();
        }
        return lines.size();
    }

    /**
     * 读取指定行数的内容。
     *
     * @param filePath   文件路径
     * @param lineNumber 行数
     * @return 指定行的内容
     */
    private static String readLine(String filePath, int lineNumber) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            int currentLineNumber = 0;
            while ((line = reader.readLine()) != null) {
                currentLineNumber++;
                if (currentLineNumber == lineNumber) {
                    return line;
                }
            }
        } catch (Exception e) {
            logger.severe("读取文件失败");
            System.out.println(e.getMessage());
            closeLogFiles();
        }

        return null;
    }

    /**
     * 递归创建目录，如果目录不存在的话。
     *
     * @param directory 要创建的目录
     */

    public static void createDirectories(File directory) {
        if (directory != null && !directory.exists()) {
            createDirectories(directory.getParentFile());
            try {
                directory.mkdir();
            } catch (Exception e) {
                logger.severe("创建文件夹时发生错误: " + e.getMessage());
            }
        }
    }

    /**
     * 创建文件，包括递归创建多层父目录。
     *
     * @param path 文件路径
     */
    public static void createFile(String path) {
        File file = new File(path);
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            if (!parentDirectory.exists()) {
                // 递归创建多层父目录
                createDirectories(parentDirectory);
            }
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                logger.severe("创建文件失败: " + e.getMessage());
            }
        }
    }

    /**
     * 比较两个文件是否相同。
     *
     * @param path1 标准文件路径
     * @param path2 待比较文件路径
     * @return 0表示比较成功，1表示有文件不存在，2表示待测文件行数多于标准文件，3表示待测文件行数少于标准文件，4表示待测文件与标准文件内容不同
     */
    public static int compareFile(String path1, String path2) {

        File file1 = new File(path1);
        File file2 = new File(path2);
        if (!file1.exists() || !file2.exists()) {
            logger.warning("在比较文件时文件可能不存在: " + path1 + ", " + path2);
            closeLogFiles();
            return 1;
        }
        try {
            Scanner scanner1 = new Scanner(file1);
            Scanner scanner2 = new Scanner(file2);
            while (true) {
                if (!scanner1.hasNextLine() && !scanner2.hasNextLine()) {
                    scanner1.close();
                    scanner2.close();
                    break;
                } else if (!scanner1.hasNextLine() && scanner2.hasNextLine()) {
                    scanner1.close();
                    scanner2.close();
                    logger.warning("比较文件比标准文件行数多: " + path1 + ", " + path2);
                    closeLogFiles();
                    return 2;
                } else if (scanner1.hasNextLine() && !scanner2.hasNextLine()) {
                    scanner1.close();
                    scanner2.close();
                    logger.warning("比较文件比标准文件行数少: " + path1 + ", " + path2);
                    closeLogFiles();
                    return 3;
                }
                String line1 = scanner1.nextLine();
                String line2 = scanner2.nextLine();
                if (!line1.equals(line2)) {
                    closeLogFiles();
                    scanner1.close();
                    scanner2.close();
                    return 4;
                }
            }
        } catch (Exception e) {
            logger.severe("比较文件时出现错误: " + e.getMessage());
        }
        closeLogFiles();
        return 0;
    }

    /**
     * 初始化日志。
     *
     * @return 初始化的Logger
     */
    private static Logger initializeLogger() {
        Logger logger = Logger.getLogger("FileProcessLogger");
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler("./log/fileProcessError.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            logger.severe("初始化日志出现问题: " + e.getMessage());
        }

        return logger;
    }

    /**
     * 关闭所有日志文件。
     */
    private static void closeLogFiles() {
        for (Handler handler : FileProcess.logger.getHandlers()) {
            if (handler instanceof FileHandler) {
                handler.close();
            }
        }
    }
}
