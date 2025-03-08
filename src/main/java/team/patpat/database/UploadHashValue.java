package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 上传哈希值的类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class UploadHashValue {

    /**
     * 上传哈希值信息。
     *
     * @param sid        学生ID
     * @param courseName 课程名称
     * @param testName   考试名称
     * @param hashValue  哈希值
     * @return 上传结果
     * -1: 未知错误，请查看日志文件
     * 1: 上传成功
     * 2: 指定的学生、课程、考试不存在，请检查是否上传评测成绩
     */
    public int uploadHashValue(String sid, String courseName, String testName, String hashValue) {
        int returnValue = -1;
        if (hashValue.isEmpty()) {
            return -1;
        }
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            returnValue = performDatabaseOperation(logger, sid, courseName, testName, hashValue);
        } catch (Exception ex) {
            globalLogger.severe("Exception occurred: " + ex.getMessage());
        }
        closeLogFiles();
        return returnValue;
    }

    /**
     * 初始化日志记录器。
     *
     * @param logFilePath 日志文件路径
     * @return 日志记录器
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
            logger.severe("初始化日志记录器时发生异常: " + e.getMessage());
        }

        return logger;
    }

    /**
     * 执行数据库操作，上传哈希值信息。
     *
     * @param logger     日志记录器
     * @param sid        学生ID
     * @param courseName 课程名称
     * @param testName   考试名称
     * @param hashValue  哈希值
     * @return 上传结果
     */
    public static int performDatabaseOperation(Logger logger, String sid, String courseName, String testName, String hashValue) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement updateStatement;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM studentscore";
            resultSet = statement.executeQuery(sql);
            Grades grades = new Grades(sid);
            while (resultSet.next()) {
                String id = resultSet.getString("sid");
                if (id.equals(sid)) {
                    String currentCourseName = resultSet.getString("courseName");
                    String currentTestName = resultSet.getString("testName");
                    if (currentCourseName.equals(courseName) && currentTestName.equals(testName)) {
                        String sql2 = "UPDATE studentscore SET hashvalue = ? WHERE sid = ? AND courseName = ? AND testName = ?";
                        updateStatement = connection.prepareStatement(sql2);
                        updateStatement.setString(1, hashValue);
                        updateStatement.setString(2, sid);
                        updateStatement.setString(3, courseName);
                        updateStatement.setString(4, testName);
                        updateStatement.executeUpdate();
                        return 1;
                    }
                }
            }
            return 2;
        } catch (Exception e) {
            logger.severe("执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(statement, connection, resultSet, logger);
        }
        return -1;
    }

    /**
     * 关闭资源。
     *
     * @param statement  数据库语句对象
     * @param connection 数据库连接对象
     * @param logger     日志记录器
     * @param resultSet  数据库结果集
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
            logger.severe("关闭资源时发生异常: " + e.getMessage());
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
}
