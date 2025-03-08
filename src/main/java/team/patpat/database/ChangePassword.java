package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于修改密码的数据库操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class ChangePassword {

    /**
     * 修改密码。
     *
     * @param sid              学号。
     * @param originalPassword 原密码。
     * @param newPassword      新密码。
     * @param confirmPassword  确认新密码。
     * @return 返回修改密码状态：
     * -1: 未知错误，请查看日志文件。
     * 1: 修改密码成功。
     * 2: 原密码错误。
     * 3: 学号不存在或未注册。
     * 4: 两次输入的密码不一致。
     * 5: 密码不符合要求。
     */
    public int changePassword(String sid, String originalPassword, String newPassword, String confirmPassword) {
        int returnValue = -1;
        if (!newPassword.equals(confirmPassword)) {
            return 4;
        }
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            if (!isPasswordValid(newPassword)) {
                return 5;
            }
            returnValue = performDatabaseOperation(logger, sid, originalPassword, newPassword);
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
     * @param logger           Logger 对象。
     * @param sid              学号。
     * @param originalPassword 原密码。
     * @param newPassword      新密码。
     * @return 返回修改密码状态：
     * 1: 修改密码成功。
     * 2: 原密码错误。
     * 3: 学号不存在或未注册。
     */
    private static int performDatabaseOperation(Logger logger, String sid, String originalPassword, String newPassword) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement updateStatement;

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
                    if (currentPwd.equals(originalPassword)) {
                        // 开始重置密码
                        String updateSql = "UPDATE sidandpassword SET Password = ? WHERE Sid = ?";
                        updateStatement = connection.prepareStatement(updateSql);
                        updateStatement.setString(1, newPassword);
                        updateStatement.setString(2, sid);
                        int updateCount = updateStatement.executeUpdate();
                        if (updateCount != 1) {
                            connection.rollback();
                            return -1;
                        }
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
     * 检查密码是否符合要求。
     *
     * @param password 密码。
     * @return 如果密码符合要求，返回 true；否则返回 false。
     */
    private static boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (password.length() > 16) {
            return false;
        }
        boolean hasDigit = false;
        boolean hasLetter = false;
        for (int i = 0; i < password.length(); i++) {
            char currentChar = password.charAt(i);
            if (Character.isDigit(currentChar)) {
                hasDigit = true;
            }
            if (Character.isLetter(currentChar)) {
                hasLetter = true;
            }
        }
        return hasDigit && hasLetter;
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
