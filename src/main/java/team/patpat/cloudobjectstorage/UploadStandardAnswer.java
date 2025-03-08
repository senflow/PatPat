package team.patpat.cloudobjectstorage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import team.patpat.database.GV;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于上传标准答案至腾讯云对象存储的工具类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class UploadStandardAnswer {

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
            logger.severe("发生异常: " + e.getMessage());
        }

        return logger;
    }

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
     * 执行数据库操作。
     *
     * @param logger    Logger 对象。
     * @param iteration 迭代值。
     * @return 数据库操作结果。
     */
    private static int performDatabaseOperation(Logger logger, String iteration) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement updateStatement;

        try {
            int currentNumber = -1;

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM testcase";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String currentIteration = resultSet.getString("iteration");
                if (currentIteration.equals(iteration)) {
                    currentNumber = resultSet.getInt("number");
                    String updateSql = "UPDATE testcase SET number = ? WHERE iteration = ?";
                    updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setInt(1, currentNumber);
                    updateStatement.setString(2, iteration);
                    int updateCount = updateStatement.executeUpdate();
                    if (updateCount != 1) {
                        connection.rollback();
                        return -1;
                    }
                }

            }
            return currentNumber;
        } catch (Exception e) {
            logger.severe("发生异常: " + e.getMessage());
        } finally {
            closeResource(statement, connection, resultSet, logger);
        }
        return -1;
    }

    /**
     * 关闭资源。
     *
     * @param statement  JDBC Statement 对象。
     * @param connection JDBC Connection 对象。
     * @param resultSet  JDBC ResultSet 对象。
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
     * 上传标准答案至 COS。
     *
     * @param localFilePath 本地文件路径。
     * @param courseName    课程名称。
     * @return 上传结果，成功返回1，失败返回-1。
     */
    public int uploadStandardAnswer(String localFilePath, String courseName) {
        int testName = -1;
        testName = getTestNumber(courseName);
        if (testName == -1) {
            return -1;
        }
        int res = updateTestCase(localFilePath, courseName, String.valueOf(testName));
        if (res == -1) {
            return -1;
        }
        return 1;
    }

    /**
     * 更新测试用例。
     *
     * @param localFilePath 本地文件路径。
     * @param courseName    课程名称。
     * @param testName      测试名称。
     * @return 更新结果，成功返回1，失败返回-1。
     */
    public int updateTestCase(String localFilePath, String courseName, String testName) {
        Logger globalLogger = Logger.getLogger("globalLogger");
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("log/globalError.log");
            globalLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException e) {
            globalLogger.severe("初始化 FileHandler 时发生错误: " + e.getMessage());
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

            // 在 COS 中的存储路径（key）
            String cosKey = "standard/" + courseName + "/" + testName + ".txt";

            File localFile = new File(localFilePath);
            PutObjectRequest putObjectRequest = new PutObjectRequest(GV.bucketName, cosKey, localFile);
            cosClient.putObject(putObjectRequest);
            cosClient.shutdown();
            return 1;
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles(globalLogger);
        return -1;
    }

    /**
     * 获取测试编号。
     *
     * @param iteration 迭代值。
     * @return 测试编号，失败返回-1。
     */
    public int getTestNumber(String iteration) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            returnValue = performDatabaseOperation(logger, iteration);
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles();
        return returnValue;
    }
}
