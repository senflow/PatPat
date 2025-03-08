package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 更新学生成绩的类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class UpdateScore {

    /**
     * 更新学生成绩信息。
     *
     * @param sid        学生ID
     * @param courseName 课程名称
     * @param testName   考试名称
     * @param score      分数
     * @return 更新结果
     * -1: 未知错误，请查看日志文件
     * 1: 上传成功
     */
    public int updateScore(String sid, String courseName, String testName, int score) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            returnValue = performDatabaseOperation(logger, sid, courseName, testName, score);
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
     * 执行数据库操作，更新学生成绩信息。
     *
     * @param logger     日志记录器
     * @param sid        学生ID
     * @param courseName 课程名称
     * @param testName   考试名称
     * @param score      分数
     * @return 更新结果
     */
    public static int performDatabaseOperation(Logger logger, String sid, String courseName, String testName, int score) {
        Connection connection = null;
        PreparedStatement scoreStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);

            // 在成绩表中创建值，如果已存在则更新
            String scoreSql = "INSERT INTO studentscore (sid, coursename, testname, score) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE score = VALUES(score)";
            scoreStatement = connection.prepareStatement(scoreSql);
            scoreStatement.setString(1, sid);
            scoreStatement.setString(2, courseName);
            scoreStatement.setString(3, testName);
            scoreStatement.setInt(4, score);

            int scoreCount = scoreStatement.executeUpdate();

            return 1;
        } catch (Exception e) {
            logger.severe("执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(scoreStatement, connection, logger);
        }

        return -1;
    }

    /**
     * 关闭资源。
     *
     * @param statement  数据库语句对象
     * @param connection 数据库连接对象
     * @param logger     日志记录器
     */
    private static void closeResource(Statement statement, Connection connection, Logger logger) {
        try {
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
