package team.patpat.database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 更新通知的类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class UpdateNotice {

    /**
     * 更新通知内容。
     *
     * @param notice 要更新的通知内容
     * @return 更新结果
     * -1: 未知错误，请查看日志文件
     * 1: 上传成功
     */
    public int updateNotice(String notice) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            // 获取当前时间
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            returnValue = performDatabaseOperation(logger, notice, time);
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
     * 执行数据库操作，更新通知内容。
     *
     * @param logger 日志记录器
     * @param notice 要更新的通知内容
     * @param time   更新时间
     * @return 更新结果
     */
    private static int performDatabaseOperation(Logger logger, String notice, String time) {

        Connection connection = null;
        Statement statement = null;
        PreparedStatement insertStatement;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String insertSql = "INSERT INTO notice (time, notice) VALUES (?, ?)";
            insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, time);
            insertStatement.setString(2, notice);
            int insertCount = insertStatement.executeUpdate();
            if (insertCount != 1) {
                connection.rollback();
                return -1;
            }
            return 1;
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
     * @param resultSet  结果集对象
     * @param logger     日志记录器
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
