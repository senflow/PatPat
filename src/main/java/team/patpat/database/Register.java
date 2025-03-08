package team.patpat.database;

import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 注册类，用于学生注册。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class Register {

    /**
     * 学生注册方法。
     *
     * @param sid             学号
     * @param password        密码
     * @param confirmPassword 确认密码
     * @return 注册结果
     * -1: 未知错误，请查看日志文件
     * 1: 注册成功
     * 2: 该学生已注册
     * 3: 学号不存在
     * 4: 密码不符合要求
     * 5: 两次输入的密码不一致
     */
    public int register(String sid, String password, String confirmPassword) {
        int returnValue = -1;
        Logger globalLogger = initializeLogger("log/globalError.log");
        try {
            Logger logger = initializeLogger("log/databaseError.log");
            returnValue = performDatabaseOperation(logger, sid, password, confirmPassword);
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
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
     * 执行数据库操作，进行学生注册。
     *
     * @param logger   日志记录器
     * @param sid      学号
     * @param pwd      密码
     * @param pwdTwice 确认密码
     * @return 注册结果
     */
    private static int performDatabaseOperation(Logger logger, String sid, String pwd, String pwdTwice) {
        if (!pwd.equals(pwdTwice)) {
            return 5;
        }
        if (!isPasswordValid(pwd)) {
            return 4;
        }

        Connection connection = null;
        Statement statement = null;
        PreparedStatement updateStatement;
        PreparedStatement insertStatement;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM allstudentsid";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String id = resultSet.getString("sid");
                boolean isRegistered = resultSet.getBoolean("isregistered");
                if (id.equals(sid)) {
                    if (isRegistered) {
                        return 2;
                    }
                    // 开始注册操作
                    String insertSql = "INSERT INTO sidandpassword (sid, password) VALUES (?, ?)";
                    insertStatement = connection.prepareStatement(insertSql);
                    insertStatement.setString(1, sid);
                    insertStatement.setString(2, pwd);
                    int insertCount = insertStatement.executeUpdate();
                    if (insertCount != 1) {
                        connection.rollback();
                        return -1;
                    }
                    // 更新状态
                    String updateSql = "UPDATE allstudentsid SET isregistered = true WHERE sid = ?";
                    updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setString(1, sid);
                    int updateCount = updateStatement.executeUpdate();
                    if (updateCount != 1) {
                        connection.rollback();
                        return -1;
                    }
                    return 1;
                }
            }
            return 3;
        } catch (Exception e) {
            logger.severe("执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(statement, connection, resultSet, logger);
        }
        return -1;
    }

    /**
     * 检查密码是否符合要求。
     *
     * @param password 密码
     * @return 是否符合要求
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
