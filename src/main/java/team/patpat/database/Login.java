package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 登录类处理用户登录认证。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class Login {

    /**
     * 执行登录操作。
     *
     * @param sid      登录的学生ID。
     * @param password 登录的密码。
     * @return -1 表示未知错误，1 表示成功，2 表示密码错误，3 表示学号不存在或未注册。
     */
    public int login(String sid, String password) {
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
     * 初始化日志记录器。
     *
     * @param logFilePath 日志文件路径。
     * @return 初始化后的日志记录器。
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
     * 执行数据库登录操作。
     *
     * @param logger 日志记录器实例。
     * @param sid    登录的学生ID。
     * @param pwd    登录的密码。
     * @return -1 表示未知错误，1 表示成功，2 表示密码错误，3 表示学号不存在或未注册。
     */
    private static int performDatabaseOperation(Logger logger, String sid, String pwd) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM sidandpassword";
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
     * 关闭与数据库相关的资源。
     *
     * @param statement  SQL语句。
     * @param connection 数据库连接。
     * @param resultSet  数据库查询结果集。
     * @param logger     日志记录器实例。
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
