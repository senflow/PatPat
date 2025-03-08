package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于从数据库中获取测试编号的操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class GetTestNumber {

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
     * 执行数据库操作，获取测试编号。
     *
     * @param logger    Logger 对象。
     * @param iteration 迭代名称。
     * @return 如果查询成功，返回测试编号；否则返回 -1。
     */
    private static int performDatabaseOperation(Logger logger, String iteration) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM testcase";
            resultSet = statement.executeQuery(sql);
            int currentNumber = -1;
            while (resultSet.next()) {
                String interation = resultSet.getString("iteration");
                if (interation.equals(iteration)) {
                    currentNumber = resultSet.getInt("number");
                    break;
                }
            }
            return currentNumber;
        } catch (Exception e) {
            logger.severe("在执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(statement, connection, resultSet, logger);
        }
        return -1;
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
     * 获取指定迭代的测试编号。
     *
     * @param iteration 迭代名称。
     * @return 如果查询成功，返回测试编号；否则返回 -1。
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
        // 返回 -1 代表查询失败，请查看日志文件
    }
}
