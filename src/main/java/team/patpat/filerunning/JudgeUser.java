package team.patpat.filerunning;

import team.patpat.cloudobjectstorage.DownloadStandardAnswer;
import team.patpat.cloudobjectstorage.DownloadTestCase;
import team.patpat.codereview.ASTHash;
import team.patpat.codereview.MurmurHash;
import team.patpat.codereview.UnionReview;
import team.patpat.database.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 这个类包含了与文件处理、代码运行和评测相关的各种功能，是与UI界面对接的主要类，包含了代码运行和代码查重的多种功能。
 * 使用时请确保文件路径和其他参数的正确性。
 * 这个类是本次项目的核心类之一，包含了下载和上传等与数据库有关的功能。
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */

public class JudgeUser {

    private static final String destDirectory = "/mytest";
    public static final Logger logger = initializeLogger();

    /**
     * 解压缩文件到指定目录。
     *
     * @param zipFilePath 压缩文件路径
     * @param user        用户名
     * @deprecated This method is deprecated. Use {@link #unzip(String)} instead.
     */
    public static void unzip(String zipFilePath, String user) {
        try {
            File destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }

            try (FileInputStream fis = new FileInputStream(zipFilePath);
                 ZipInputStream zipInputStream = new ZipInputStream(fis)) {

                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    String entryName = user + entry.getName();
                    Path filePath = Paths.get(destDirectory, entryName);

                    if (entry.isDirectory()) {
                        Files.createDirectories(filePath);
                    } else {
                        extractFile(zipInputStream, filePath);
                    }

                    zipInputStream.closeEntry();
                }
            }

        } catch (IOException e) {
            logger.severe("解压缩时出现错误");
        }
    }

    /**
     * 解压缩文件到默认目录。
     *
     * @param zipFilePath 压缩文件路径
     */
    public static void unzip(String zipFilePath) {
        try {
            Path tempDirectory = Paths.get("mytest").toAbsolutePath().normalize(); // 创建一个临时目录

            if (!Files.exists(tempDirectory)) {
                Files.createDirectories(tempDirectory);
            }

            try (FileInputStream fis = new FileInputStream(zipFilePath);
                 ZipInputStream zipInputStream = new ZipInputStream(fis, StandardCharsets.ISO_8859_1)) {

                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    Path filePath = tempDirectory.resolve(entryName); // 使用临时目录

                    if (entry.isDirectory()) {
                        Files.createDirectories(filePath);
                    } else {
                        extractFile(zipInputStream, filePath);
                    }

                    zipInputStream.closeEntry();
                }
            }

        } catch (IOException e) {
            logger.severe("解压缩时出现错误");
        }
    }

    /**
     * 从ZipInputStream中提取文件。
     *
     * @param zipInputStream ZipInputStream对象
     * @param filePath       目标文件路径
     * @throws IOException 如果发生I/O异常
     */
    private static void extractFile(ZipInputStream zipInputStream, Path filePath) throws IOException {
        Files.createDirectories(filePath.getParent());

        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(filePath.toFile().toPath()))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipInputStream.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    /**
     * 创建包含指定内容的临时文件,在文件中写入指定的内容。
     *
     * @param content 文件内容
     * @param prefix  文件名前缀
     * @param suffix  文件名后缀
     * @return 临时文件路径
     * @throws IOException 如果发生I/O异常
     */
    public static Path createTempFile(String content, String prefix, String suffix) throws IOException {
        // 获取默认的临时目录
        Path tempDirectory = Paths.get("mytest").toAbsolutePath().normalize(); // 创建一个临时目录

        if (!Files.exists(tempDirectory)) {
            Files.createDirectories(tempDirectory);
        }
        // 构造临时文件路径
        Path tempFilePath = tempDirectory.resolve(prefix + suffix);
        //构建临时文件
        Files.createFile(tempFilePath);
        // 写入内容到临时文件
        Files.write(tempFilePath, content.getBytes(), StandardOpenOption.WRITE);
        // 在JVM退出时删除临时文件
        tempFilePath.toFile().deleteOnExit();
        return tempFilePath;
    }

    /**
     * 创建空白的临时文件。
     *
     * @param prefix 文件名前缀
     * @param suffix 文件名后缀
     * @return 临时文件路径
     * @throws IOException 如果发生I/O异常
     */
    public static Path createTempFile(String prefix, String suffix) throws IOException {
        // 获取默认的临时目录
        Path tempDirectory = Paths.get("mytest").toAbsolutePath().normalize();
        // 构造临时文件路径
        Path tempFilePath = tempDirectory.resolve(prefix + suffix);
        Files.createFile(tempFilePath);
        // 在JVM退出时删除临时文件
        tempFilePath.toFile().deleteOnExit();
        return tempFilePath;
    }

    /**
     * 删除指定路径的文件或目录。
     *
     * @param filePath 文件或目录路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile())  //判断是否为文件，是，则删除
        {
            file.delete();
        } else //不为文件，则为文件夹
        {
            String[] childFilePath = file.list();//获取文件夹下所有文件相对路径
            if (childFilePath != null) {
                for (String path : childFilePath) {
                    deleteFile(file.getAbsoluteFile() + "/" + path);//递归，对每个都进行判断
                }
            }
            file.delete(); // 如果不保留文件夹本身 则执行此行代码
        }
    }

    /**
     * 运行Java程序，执行评测，并返回评测结果。
     *
     * @param zipFilePath 压缩文件路径
     * @param userName    用户名
     * @param data        输入数据
     * @param answer      标准答案
     * @param num         评测编号
     * @return 评测结果字符串
     * @throws IOException          如果发生I/O异常
     * @throws InterruptedException 如果线程被中断
     */
    public static String runJavaProgram(String zipFilePath, String userName, String data, String answer, int num) throws IOException, InterruptedException {
        unzip(zipFilePath);
        Thread.sleep(200);
        Path inPath = createTempFile(data, "in" + num, ".txt");
        Thread.sleep(200);
        String inPathString = inPath.toString();
        Path answerPath = createTempFile(answer, "answer" + num, ".txt");
        Thread.sleep(200);
        String answerPathString = answerPath.toString();
        Path outPath = createTempFile("out" + num, ".txt");
        Thread.sleep(200);
        String outPathString = outPath.toString();
        ProgramHandle newProgramHandle = new ProgramHandle();

        String f = newProgramHandle.runJavaProgram(userName, outPathString, inPathString, answerPathString);
//        Files.deleteIfExists(inPath);
//        Files.deleteIfExists(answerPath);
//        Files.deleteIfExists(outPath);
        return f;
    }

    /**
     * 运行并评测多组测试用例，并返回评测结果，类的主要方法。
     *
     * @param interationNum 迭代次数
     * @param sid           学生ID
     * @param path          压缩文件路径
     * @return 评测结果数组
     * @throws IOException          如果发生I/O异常
     * @throws InterruptedException 如果线程被中断
     */
    public static String[] runningAndReview(String interationNum, String sid, String path) throws IOException, InterruptedException {
        int flag = 0;
        final int MAX_TEST_NUMBER = 10;
        GetTestNumber getTestNumber = new GetTestNumber();
        int num = getTestNumber.getTestNumber(interationNum);
        int[] score = new int[MAX_TEST_NUMBER];


        DownloadTestCase downloadTestCase = new DownloadTestCase();
        UpdateScore updateScore = new UpdateScore();
        String[] answers = new String[MAX_TEST_NUMBER];
        boolean isAllCorrect = true;
        for (int i = 0; i < num; i++) {
            String input = downloadTestCase.downloadTestCase(interationNum, i + "");
            Thread.sleep(100);
            DownloadStandardAnswer downloadStandardAnswer = new DownloadStandardAnswer();
            String standardAnswer = downloadStandardAnswer.downloadStandardAnswer(interationNum, (i + 1) + "");
            Thread.sleep(100);
            if (input == null || standardAnswer == null) System.out.println("数据点下载失败");

            answers[i] = JudgeUser.runJavaProgram(path, sid, input, standardAnswer, i);

            if (answers[i].equals("AC\n")) {
                score[i] += 100;
            } else {
                isAllCorrect = false;
            }
            updateScore.updateScore(sid, interationNum, i + "", score[i]);
        }

        if (isAllCorrect) {
            UpdateTime updateTime = new UpdateTime();
            updateTime.updateTime(sid, interationNum);
        }

        int hashSize = 800;
        String filePath = "mytest/" + sid;
        String hashString1 = MurmurHash.createMyHash(filePath, hashSize, "java");

        String hashString2 = ASTHash.createMyHash(filePath, hashSize);

        UploadHashValue uploadHashValue = new UploadHashValue();
        uploadHashValue.uploadHashValue(sid, interationNum, "0", hashString1);
        uploadHashValue.uploadHashValue(sid, interationNum, "1", hashString2);

        GetHashValue getHashValue = new GetHashValue();
        List<Grades> hashValueList;
        hashValueList = getHashValue.getHashValue();
        for (Grades grades : hashValueList) {
            String otherSid = grades.getSid();
            ArrayList<Grade> gradeArrayList = grades.getGrades();
            String hashValue1 = "";
            String hashValue2 = "";
            for (Grade grade : gradeArrayList) {
                String courseName = grade.getCourseName();
                if (courseName.equals(interationNum)) {

                    if (grade.getTestName().equals("0"))
                        hashValue1 = grade.getHashValue();

                    else if (grade.getTestName().equals("1")) {
                        hashValue2 = grade.getHashValue();
                    }

                }
            }

            if (!Objects.equals(otherSid, sid)) {
                if (hashValue1.equals("") || hashValue2.equals(""))
                    continue;
                double similarity1 = MurmurHash.calculateJaccardSimilarity(hashValue1, hashString1);
                if (similarity1 > 0.8) {
                    flag = 1;
                }
                double similarity2 = ASTHash.calculateHashSimilarity(hashValue2, hashString2);
                if (similarity2 > 0.8) {
                    flag = 1;
                }
                double similarity = UnionReview.codeReview(similarity1, similarity2);
                if (similarity > 0.7) {
                    flag = 1;
                }


                //if (flag == 1) {
                if (true) {
                    UploadSimilarity uploadSimilarity = new UploadSimilarity();
                    uploadSimilarity.uploadSimilarity(sid, otherSid, similarity1, similarity2, similarity);
                }

            }
        }
        deleteFile("mytest");
        return answers;
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
            closeLogFiles();
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
