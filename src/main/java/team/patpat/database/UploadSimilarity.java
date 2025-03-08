package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 上传相似度的类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class UploadSimilarity {

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
     * 执行数据库操作，上传相似度信息。
     *
     * @param logger        日志记录器
     * @param sid1          学生1 ID
     * @param sid2          学生2 ID
     * @param similarity1   相似度1
     * @param similarity2   相似度2
     * @param similaritySum 相似度总和
     * @return 上传结果
     * -1: 未知错误，请查看日志文件
     * 1: 上传成功
     */
    public static int performDatabaseOperation(Logger logger, String sid1, String sid2, double similarity1, double similarity2, double similaritySum) {
        Connection connection = null;
        PreparedStatement similarityStatement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);

            // 在相似度表中创建值，如果已存在则更新
            String scoreSql = "INSERT INTO similarity (Sid1, Sid2, Similarity1, Similarity2, SimilaritySum) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE Similarity1 = VALUES(Similarity1), " +
                    "Similarity2 = VALUES(Similarity2), SimilaritySum = VALUES(SimilaritySum)";
            similarityStatement = connection.prepareStatement(scoreSql);
            similarityStatement.setString(1, sid1);
            similarityStatement.setString(2, sid2);
            similarityStatement.setDouble(3, similarity1);
            similarityStatement.setDouble(4, similarity2);
            similarityStatement.setDouble(5, similaritySum);

            int scoreCount = similarityStatement.executeUpdate();

            return 1;
        } catch (Exception e) {
            logger.severe("执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(similarityStatement, connection, logger);
        }

        return -1;
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

    /**
     * 上传相似度信息。
     *
     * @param sid1        学生1 ID
     * @param sid2        学生2 ID
     * @param similarity1 相似度1
     * @param similarity2 相似度2
     * @param similarity3 相似度3
     * @return 上传结果
     * -1: 未知错误，请查看日志文件
     * 1: 上传成功
     * 2: 指定的学生、课程、考试不存在，请检查是否上传评测成绩
     */
    public int uploadSimilarity(String sid1, String sid2, double similarity1, double similarity2, double similarity3) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            returnValue = performDatabaseOperation(logger, sid1, sid2, similarity1, similarity2, similarity3);
        } catch (Exception ex) {
            globalLogger.severe("Exception occurred: " + ex.getMessage());
        }
        closeLogFiles();
        return returnValue;
    }
}
