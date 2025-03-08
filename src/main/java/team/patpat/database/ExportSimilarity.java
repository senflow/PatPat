package team.patpat.database;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于导出相似度数据到Excel的数据库操作类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class ExportSimilarity {

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
     * 导出相似度数据到Excel文件。
     *
     * @param similarityList 相似度数据列表。
     * @param outputPath     导出Excel文件的路径。
     */
    private static void exportToExcel(List<Similarity> similarityList, String outputPath) {
        Workbook workbook = new XSSFWorkbook();

        try {
            Sheet sheet = workbook.createSheet("Similarity Data");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Sid1");
            headerRow.createCell(1).setCellValue("Sid2");
            headerRow.createCell(2).setCellValue("Similarity1");
            headerRow.createCell(3).setCellValue("Similarity2");
            headerRow.createCell(4).setCellValue("SimilaritySum");

            // 填充数据行
            int rowNum = 1;
            for (Similarity similarity : similarityList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(similarity.getSid1());
                row.createCell(1).setCellValue(similarity.getSid2());
                row.createCell(2).setCellValue(similarity.getSimilarity1());
                row.createCell(3).setCellValue(similarity.getSimilarity2());
                row.createCell(4).setCellValue(similarity.getSimilaritySum());
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
     * 导出相似度数据到Excel文件。
     *
     * @param path 导出文件的路径。
     */
    public void exportSimilarity(String path) {
        Logger globalLogger = Logger.getLogger("globalLogger");
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("globalError.log");
            globalLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            globalLogger.severe("在初始化日志记录器时发生错误: " + e.getMessage());
        }
        List<Similarity> similarityPack = null;
        try {
            Logger logger = initializeLogger("log/databaseError.log", globalLogger);
            similarityPack = performDatabaseOperation(logger);

            Collections.sort(similarityPack, new Comparator<Similarity>() {
                @Override
                public int compare(Similarity s1, Similarity s2) {
                    return s1.getSid1().compareTo(s2.getSid2());
                }
            });

        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles(globalLogger);
        exportToExcel(similarityPack, path + "/Similarity.xlsx");
        // 返回空List代表查询失败，请查看日志文件
    }

    /**
     * 执行数据库操作，获取相似度数据。
     *
     * @param logger Logger 对象。
     * @return 相似度数据列表。
     */
    private List<Similarity> performDatabaseOperation(Logger logger) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(GV.url, GV.username, GV.password);
            statement = connection.createStatement();
            String sql = "SELECT * FROM similarity";
            resultSet = statement.executeQuery(sql);
            List<Similarity> similarityList = new ArrayList<>();
            while (resultSet.next()) {
                String sid1 = resultSet.getString("Sid1");
                String sid2 = resultSet.getString("Sid2");
                Double similarity1 = resultSet.getDouble("Similarity1");
                Double similarity2 = resultSet.getDouble("Similarity2");
                Double similaritySum = resultSet.getDouble("SimilaritySum");
                Similarity similarity = new Similarity(sid1, sid2, similarity1, similarity2, similaritySum);
                similarityList.add(similarity);
            }
            return similarityList;
        } catch (Exception e) {
            logger.severe("在执行数据库操作时发生异常: " + e.getMessage());
        } finally {
            closeResource(statement, connection, resultSet, logger);
        }
        return null;
    }
}
