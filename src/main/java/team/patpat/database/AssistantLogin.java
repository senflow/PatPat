package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于助教登录验证的数据库操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class AssistantLogin {

    /**
     * 验证助教登录。
     *
     * @param sid      学号。
     * @param password 密码。
     * @return 返回登录状态：
     * -1: 未知错误，请查看日志文件。
     * 1: 登录成功。
     * 2: 密码错误。
     * 3: 学号不存在或未注册。
     */
    public int assistantLogin(String sid, String password) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            returnValue = performDatabaseOperation(logger, sid, password);
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
     * @param sid    学号。
     * @param pwd    密码。
     * @return 返回登录状态：
     * 1: 登录成功。
     * 2: 密码错误。
     * 3: 学号不存在或未注册。
     */
    private static int performDatabaseOperation(Logger logger, String sid, String pwd) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM assistantsidandpassword";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String currentSid = resultSet.getString("Sid");

                if (currentSid.equals(sid)) {
                    String currentPwd = resultSet.getString("Password");
                    if (currentPwd.equals(pwd)) {
                        return 1;
                    } else {
                        return 2;
                    }
                }

            }
            return 3;
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
