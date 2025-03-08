package team.patpat.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于从数据库中获取时间信息的操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class GetTime {

    /**
     * 获取时间信息。
     *
     * @return 包含时间信息的 Rank 对象列表。
     */
    public ArrayList<Rank> getTime() {
        ArrayList<Rank> returnValue = new ArrayList();
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
     * 执行数据库操作，获取时间信息。
     *
     * @param logger Logger 对象。
     * @return 包含时间信息的 Rank 对象列表。
     */
    private static ArrayList<Rank> performDatabaseOperation(Logger logger) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM sidandtime";
            resultSet = statement.executeQuery(sql);
            Map<String, Rank> ranks = new HashMap<>();
            while (resultSet.next()) {
                String id = resultSet.getString("sid");
                if (ranks.containsKey(id)) {
                    String courseName = resultSet.getString("course");
                    int time = resultSet.getInt("time");
                    ranks.get(id).addTime(courseName, time);
                } else {
                    String courseName = resultSet.getString("course");
                    int time = resultSet.getInt("time");
                    Rank rank = new Rank(id);
                    rank.addTime(courseName, time);
                    ranks.put(id, rank);
                }
            }
            ArrayList<Rank> returnValue = new ArrayList<>();
            for (String key : ranks.keySet()) {
                returnValue.add(ranks.get(key));
            }
            // 按 sumTime 排序
            returnValue.sort((o1, o2) -> {
                if (o1.getSum() > o2.getSum()) {
                    return 1;
                } else if (o1.getSum() < o2.getSum()) {
                    return -1;
                } else {
                    return 0;
                }
            });
            return returnValue;
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
