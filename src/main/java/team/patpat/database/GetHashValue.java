package team.patpat.database;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于从数据库中获取哈希值数据的操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class GetHashValue {

    /**
     * 获取哈希值数据。
     *
     * @return 包含哈希值数据的列表。
     */
    public List<Grades> getHashValue() {
        List<Grades> returnValue = new ArrayList<>();
        Logger globalLogger = Logger.getLogger("globalLogger");
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("log/globalError.log");
            globalLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException e) {
            globalLogger.severe("初始化FileHandler时发生错误: " + e.getMessage());
        }
        try {
            Logger logger = initializeLogger("log/databaseError.log", globalLogger);
            Map<String, Grades> gradesPack = performDatabaseOperation(logger);

            returnValue.addAll(gradesPack.values());

            Collections.sort(returnValue, new Comparator<Grades>() {
                @Override
                public int compare(Grades g1, Grades g2) {
                    return g1.getSid().compareTo(g2.getSid());
                }
            });

            for (Grades grades : returnValue) {
                Collections.sort(grades.getGrades(), new Comparator<Grade>() {
                    @Override
                    public int compare(Grade grade1, Grade grade2) {
                        int courseNameComparison = grade1.getCourseName().compareTo(grade2.getCourseName());
                        if (courseNameComparison != 0) {
                            return courseNameComparison;
                        }

                        return grade1.getTestName().compareTo(grade2.getTestName());
                    }
                });
            }
            return returnValue;
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles(globalLogger);
        // 返回空List代表查询失败，请查看日志文件
        return null;
    }

    /**
     * 初始化 Logger 对象。
     *
     * @param logFilePath 日志文件路径。
     * @param logger      Logger 对象。
     * @return 初始化后的 Logger 对象。
     */
    private static Logger initializeLogger(String logFilePath, Logger logger) {
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
     * 执行数据库操作，获取哈希值数据。
     *
     * @param logger Logger 对象。
     * @return 包含哈希值数据的 Map。
     */
    private Map<String, Grades> performDatabaseOperation(Logger logger) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM studentscore";
            resultSet = statement.executeQuery(sql);
            Map<String, Grades> gradesPack = new HashMap<>();
            while (resultSet.next()) {
                String id = resultSet.getString("sid");
                String courseName = resultSet.getString("courseName");
                String testName = resultSet.getString("testName");
                String hashValue = resultSet.getString("hashValue");
                if (gradesPack.containsKey(id)) {
                    Grades grades = gradesPack.get(id);
                    grades.addHashValue(courseName, testName, hashValue);
                } else {
                    Grades newGrades = new Grades(id);
                    newGrades.addHashValue(courseName, testName, hashValue);
                    gradesPack.put(id, newGrades);
                }
            }
            return gradesPack;
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
     *
     * @param globalLogger 全局 Logger 对象。
     */
    private static void closeLogFiles(Logger globalLogger) {
        for (Handler handler : globalLogger.getHandlers()) {
            if (handler instanceof FileHandler) {
                handler.close();
            }
        }
    }
}
