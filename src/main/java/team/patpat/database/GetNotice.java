package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于从数据库中获取通知信息的操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class GetNotice {

    /**
     * 获取数据库中的通知信息。
     *
     * @return 包含通知信息的字符串，如果查询失败则返回 null。
     */
    public String getNotice() {
        String returnValue = null;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            returnValue = performDatabaseOperation(logger);
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles();
        return returnValue;
        // 返回 null 代表查询失败，请查看日志文件
    }

    /**
     * 初始化 Logger 对象。
     *
     * @param logFilePath 日志文件路径。
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
            logger.severe("在初始化日志记录器时发生异常: " + e.getMessage());
        }

        return logger;
    }

    /**
     * 执行数据库操作，获取通知信息。
     *
     * @param logger Logger 对象。
     * @return 包含通知信息的字符串，如果查询失败则返回 null。
     */
    private static String performDatabaseOperation(Logger logger) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM notice";
            resultSet = statement.executeQuery(sql);
            String notice = "";
            while (resultSet.next()) {
                String time = resultSet.getString("time");
                String content = resultSet.getString("notice");
                // 在 content 中每隔 15 个字符加一个换行符
                int length = content.length();
                int i = 0;
                while (i < length) {
                    if (i + 15 < length) {
                        content = content.substring(0, i + 15) + "\n" + content.substring(i + 15);
                        i += 16;
                    } else {
                        break;
                    }
                }
                notice = time + ":\n" + content + "\n" + notice;
            }
            return notice;
        } catch (Exception e) {
            logger.severe("在执行数据库操作时发生异常: " + e.getMessage());
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
}
