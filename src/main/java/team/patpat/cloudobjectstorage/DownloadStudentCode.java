package team.patpat.cloudobjectstorage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.region.Region;
import team.patpat.database.GV;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于从腾讯云对象存储下载学生代码的工具类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class DownloadStudentCode {

    /**
     * 初始化 Logger 对象。
     *
     * @param logFilePath Logger 文件路径。
     * @param logger      待初始化的 Logger 对象。
     * @return 初始化后的 Logger 对象。
     */
    private static Logger initializeLogger(String logFilePath, Logger logger) {
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler(logFilePath);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            logger.severe("在初始化 logger 时发生异常: " + e.getMessage());
        }
        return logger;
    }

    /**
     * 关闭日志文件。
     *
     * @param globalLogger 全局 Logger 对象。
     */
    private static void closeLogFiles(Logger globalLogger) {
        for (Handler handler : globalLogger.getHandlers()) {
            if (handler instanceof FileHandler) {
                handler.close();
            }
        }
    }

    /**
     * 初始化 Logger 对象。
     *
     * @param logFilePath Logger 文件路径。
     * @return 初始化后的 Logger 对象。
     */
    private static Logger initializeLogger(String logFilePath) {
        Logger logger = Logger.getLogger("MyLogger");
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler(logFilePath);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            logger.severe("在初始化 logger 时发生异常: " + e.getMessage());
        }

        return logger;
    }

    /**
     * 执行数据库操作，查询指定课程的学生 ID 列表。
     *
     * @param logger    Logger 对象。
     * @param iteration 课程名称。
     * @return 学生 ID 列表。
     */
    private static List<String> performDatabaseOperation(Logger logger, String iteration) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM studentscore";
            resultSet = statement.executeQuery(sql);
            List<String> sidList = new ArrayList<>();
            while (resultSet.next()) {
                String course = resultSet.getString("courseName");
                if (course.equals(iteration)) {
                    String sid = resultSet.getString("sid");
                    sidList.add(sid);
                }
            }
            return sidList;
        } catch (Exception e) {
            logger.severe("发生异常: " + e.getMessage());
        } finally {
            closeResource(statement, connection, resultSet, logger);
        }
        return null;
    }

    /**
     * 关闭数据库资源。
     *
     * @param statement  Statement 对象。
     * @param connection Connection 对象。
     * @param resultSet  ResultSet 对象。
     * @param logger     Logger 对象。
     */
    private static void closeResource(Statement statement, Connection connection, ResultSet resultSet, Logger logger) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("在关闭资源时发生异常: " + e.getMessage());
        }
    }

    /**
     * 关闭日志文件。
     */
    private static void closeLogFiles() {
        Logger globalLogger = Logger.getLogger("MyLogger");
        for (Handler handler : globalLogger.getHandlers()) {
            if (handler instanceof FileHandler) {
                handler.close();
            }
        }
    }

    /**
     * 下载学生代码。
     *
     * @param outputFilePath 输出文件路径。
     * @param courseName     课程名称。
     * @return 成功返回1，失败返回-1。
     */
    public int downloadStudentCode(String outputFilePath, String courseName) {
        // courseName, testName, sid 可为空!
        Logger globalLogger = Logger.getLogger("globalLogger");
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("log/globalError.log");
            globalLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            globalLogger.severe("初始化 FileHandler 时发生错误: " + e.getMessage());
        }
        outputFilePath += "/" + courseName + "/";

        // 检查输出目录是否存在，不存在则创建
        File outputDirectory = new File(outputFilePath);
        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                globalLogger.severe("无法创建输出目录: " + outputFilePath);
                closeLogFiles(globalLogger);
                return -1;
            }
        }
        List<String> sidList = querySid(courseName);
        for (String sid : sidList) {
            String outputFile = outputDirectory.getAbsolutePath() + "/";
            outputFile += sid;
            outputFile += ".zip";
            File file = new File(outputFile);
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        globalLogger.severe("无法创建输出文件: " + outputFile);
                        closeLogFiles(globalLogger);
                        return -1;
                    }
                } catch (IOException ex) {
                    globalLogger.severe("发生异常: " + ex.getMessage());
                }
            }

            // 初始化 COS 客户端
            try {
                Logger logger = initializeLogger("log/databaseError.log", globalLogger);
                String secretId = GV.secretId;
                String secretKey = GV.secretKey;
                COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
                Region region = new Region(GV.region);
                ClientConfig clientConfig = new ClientConfig(region);
                COSClient cosClient = new COSClient(cred, clientConfig);

                // COS 存储路径 (key)
                String cosKey = "";
                cosKey += courseName;
                cosKey += "/testOne/";
                cosKey += sid + ".zip";

                // 获取对象内容输入流
                GetObjectRequest getObjectRequest = new GetObjectRequest(GV.bucketName, cosKey);
                COSObject cosObject = cosClient.getObject(getObjectRequest);
                COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

                // 创建本地文件输出流
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                    // 将 COS 对象输入流写入本地文件
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = cosObjectInput.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }

                // 下载对象的 CRC64
                String crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();

                // 关闭输入流
                cosObjectInput.close();
                cosClient.shutdown();
            } catch (IOException ex) {
                globalLogger.severe("发生异常: " + ex.getMessage());
            }
        }
        closeLogFiles(globalLogger);
        return 1;
    }

    /**
     * 查询学生 ID 列表。
     *
     * @param iteration 课程名称。
     * @return 学生 ID 列表。
     */
    private List<String> querySid(String iteration) {
        List<String> sidList = new ArrayList<>();
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            sidList = performDatabaseOperation(logger, iteration);
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles();
        return sidList;
        // 返回 null 代表查询失败，请查看日志文件
    }
}
