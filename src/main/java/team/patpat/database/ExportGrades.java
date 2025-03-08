package team.patpat.database;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于导出成绩数据到Excel的数据库操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class ExportGrades {

    /**
     * 初始化 Logger 对象。
     *
     * @param logger Logger 对象。
     * @return 初始化后的 Logger 对象。
     */
    private static Logger initializeLogger(Logger logger) {
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler("log/databaseError.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            logger.severe("在初始化日志记录器时发生异常: " + e.getMessage());
        }

        return logger;
    }

    /**
     * 导出成绩数据到Excel文件。
     *
     * @param gradesList 成绩数据列表。
     * @param outputPath 导出Excel文件的路径。
     */
    private static void exportToExcel(List<Grades> gradesList, String outputPath) {
        Workbook workbook = new XSSFWorkbook();

        try {
            Sheet sheet = workbook.createSheet("Grades Data");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("SID");
            headerRow.createCell(1).setCellValue("Course Name");
            headerRow.createCell(2).setCellValue("Test Name");
            headerRow.createCell(3).setCellValue("Score");

            // 填充数据行
            int rowNum = 1;
            for (Grades grades : gradesList) {
                for (Grade grade : grades.getGrades()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(grades.getSid());
                    row.createCell(1).setCellValue(grade.getCourseName());
                    String testNumber = grade.getTestName();
                    int intTestNumber = Integer.parseInt(testNumber);
                    intTestNumber++;
                    testNumber = Integer.toString(intTestNumber);
                    row.createCell(2).setCellValue(testNumber);
                    row.createCell(3).setCellValue(grade.getScore());
                }
            }

            // 将工作簿写入输出文件
            try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    /**
     * 导出成绩数据到Excel文件。
     *
     * @param path 导出文件的路径。
     */
    public void exportGrades(String path) {
        List<Grades> returnValue = new ArrayList<>();
        Logger globalLogger = Logger.getLogger("globalLogger");
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("log/globalError.log");
            globalLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            globalLogger.severe("在初始化日志记录器时发生错误: " + e.getMessage());
        }
        try {
            Logger logger = initializeLogger(globalLogger);
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
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles(globalLogger);
        exportToExcel(returnValue, path + "/grades.xlsx");
        // 返回空List代表查询失败，请查看日志文件
    }

    /**
     * 执行数据库操作，获取成绩数据。
     *
     * @param logger Logger 对象。
     * @return 成绩数据包。
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
                int score = resultSet.getInt("score");
                if (gradesPack.containsKey(id)) {
                    Grades grades = gradesPack.get(id);
                    grades.addGrade(courseName, testName, score);
                } else {
                    Grades newGrades = new Grades(id);
                    newGrades.addGrade(courseName, testName, score);
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
}
