package team.patpat.database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 更新学生AC时间的类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class UpdateTime {

    /**
     * 更新学生学习时间信息。
     *
     * @param sid        学生ID
     * @param courseName 课程名称
     * @return 更新结果
     * -1: 未知错误，请查看日志文件
     * 1: 上传成功
     */
    public int updateTime(String sid, String courseName) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            long currentTimeMillis = System.currentTimeMillis();
            int time = (int) (currentTimeMillis / (60 * 60 * 1000));
            returnValue = performDatabaseOperation(logger, sid, courseName, time);
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
     * 执行数据库操作，更新学生学习时间信息。
     *
     * @param logger     日志记录器
     * @param sid        学生ID
     * @param courseName 课程名称
     * @param time       AC时间
     * @return 更新结果
     */
    public static int performDatabaseOperation(Logger logger, String sid, String courseName, int time) {
        Connection connection = null;
        PreparedStatement timeStatement = null;
        PreparedStatement getStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);

            String getSql = "SELECT * FROM releasetime";
            getStatement = connection.prepareStatement(getSql);
            ResultSet resultSet = getStatement.executeQuery();
            int releaseTime = 0;
            while (resultSet.next()) {
                String course = resultSet.getString("course");
                if (course.equals(courseName)) {
                    releaseTime = resultSet.getInt("time");
                }
            }

            // 在成绩表中创建值，如果已存在则更新
            time = time - releaseTime;
            String scoreSql = "INSERT INTO sidandtime (sid, course, time) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE time = VALUES(time)";
            timeStatement = connection.prepareStatement(scoreSql);
            timeStatement.setString(1, sid);
            timeStatement.setString(2, courseName);
            timeStatement.setInt(3, time);

            int timeCount = timeStatement.executeUpdate();

            return 1;
        } catch (Exception e) {
            logger.severe("执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(timeStatement, connection, logger);
        }

        return -1;
    }

    /**
     * 获取当前时间的字符串表示。
     *
     * @return 当前时间的字符串表示
     */
    private static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date now = new java.util.Date();
        return dateFormat.format(now);
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
