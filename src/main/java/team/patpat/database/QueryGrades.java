package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 查询成绩的类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class QueryGrades {

    /**
     * 查询指定学生的成绩。
     *
     * @param sid 学生ID
     * @return 成绩对象
     */
    public Grades queryGrades(String sid) {
        Grades returnValue = new Grades(sid);
        Logger globalLogger = initializeLogger("globalError.log");
        try {
            Logger logger = initializeLogger("databaseError.log");
            returnValue = performDatabaseOperation(logger, sid);
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles();
        return returnValue;
        // 返回null代表查询失败，请查看日志文件
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
     * 执行数据库操作，查询指定学生的成绩。
     *
     * @param logger 日志记录器
     * @param sid    学生ID
     * @return 成绩对象
     */
    private static Grades performDatabaseOperation(Logger logger, String sid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM studentscore";
            resultSet = statement.executeQuery(sql);
            Grades grades = new Grades(sid);
            while (resultSet.next()) {
                String id = resultSet.getString("sid");
                if (id.equals(sid)) {
                    String courseName = resultSet.getString("courseName");
                    String testName = resultSet.getString("testName");
                    int score = resultSet.getInt("score");
                    grades.addGrade(courseName, testName, score);
                }
            }
            return grades;
        } catch (Exception e) {
            logger.severe("执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(statement, connection, resultSet, logger);
        }
        return null;
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
