package team.patpat.database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于创建课程发布时间的数据库操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class CreateTime {

    /**
     * 创建课程发布时间。
     *
     * @param courseName 课程名称。
     * @return 返回创建课程发布时间状态：
     * -1: 未知错误，请查看日志文件。
     * 1: 上传成功。
     */
    public int createTime(String courseName) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            long currentTimeMillis = System.currentTimeMillis();
            int time = (int) (currentTimeMillis / (60 * 60 * 1000));
            returnValue = performDatabaseOperation(logger, courseName, time);
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles();
        return returnValue;
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
            logger.severe("发生异常: " + e.getMessage());
        }

        return logger;
    }

    /**
     * 执行数据库操作。
     *
     * @param logger Logger 对象。
     * @param course 课程名称。
     * @param time   发布时间。
     * @return 返回创建课程发布时间状态：
     * 1: 上传成功。
     */
    public static int performDatabaseOperation(Logger logger, String course, int time) {
        Connection connection = null;
        PreparedStatement timeStatement = null;
        PreparedStatement getStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);

            String getSql = "SELECT * FROM releasetime";
            getStatement = connection.prepareStatement(getSql);
            ResultSet resultSet = getStatement.executeQuery();
            while (resultSet.next()) {
                String courseName = resultSet.getString("course");
                if (courseName.equals(course)) {
                    return 1;
                }
            }

            // 在表中创建值
            String scoreSql = "INSERT INTO releasetime (course,time) VALUES (?, ?) ";
            timeStatement = connection.prepareStatement(scoreSql);
            timeStatement.setString(1, course);
            timeStatement.setInt(2, time);

            int timeCount = timeStatement.executeUpdate();

            return 1;
        } catch (Exception e) {
            logger.severe("发生异常: " + e.getMessage());
        } finally {
            closeResource(timeStatement, connection, logger);
        }

        return -1;
    }

    /**
     * 获取当前时间。
     *
     * @return 当前时间的字符串表示。
     */
    private static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date now = new java.util.Date();
        return dateFormat.format(now);
    }

    /**
     * 关闭资源。
     *
     * @param statement  Statement 对象。
     * @param connection Connection 对象。
     * @param logger     Logger 对象。
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
