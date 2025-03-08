package team.patpat.cloudobjectstorage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import team.patpat.database.GV;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于将学生代码上传到腾讯云对象存储的工具类。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class UploadStudentCode {

    /**
     * 将学生代码上传到指定路径。
     *
     * @param localFilePath 本地文件路径。
     * @param courseName    课程名称。
     * @param testName      测试名称。
     * @param sid           学生ID。
     * @return 成功返回1，失败返回-1，文件类型非zip返回-2。
     */
    public int uploadStudentCode(String localFilePath, String courseName, String testName, String sid) {
        Logger globalLogger = Logger.getLogger("globalLogger");
        FileHandler fileHandler;

        if (!localFilePath.endsWith(".zip")) {
            return -2;
        }

        try {
            fileHandler = new FileHandler("log/globalError.log");
            globalLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            globalLogger.severe("初始化 FileHandler 时发生错误: " + e.getMessage());
        }

        // 初始化 COS 客户端
        try {
            Logger logger = initializeLogger("log/databaseError.log", globalLogger);
            String secretId = GV.secretId;
            String secretKey = GV.secretKey;
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            Region region = new Region(GV.region);
            ClientConfig clientConfig = new ClientConfig(region);
            COSClient cosClient = new COSClient(cred, clientConfig);

            // 在 COS 中的存储路径（key）
            String cosKey = courseName + "/" + testName + "/" + sid + ".zip";

            File localFile = new File(localFilePath);
            PutObjectRequest putObjectRequest = new PutObjectRequest(GV.bucketName, cosKey, localFile);
            cosClient.putObject(putObjectRequest);
            cosClient.shutdown();
            return 1;
        } catch (Exception ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles(globalLogger);
        return -1;
    }

    /**
     * 初始化 Logger 对象。
     *
     * @param logFilePath Logger 文件路径。
     * @param logger      待初始化的 Logger 对象。
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
            logger.severe("在初始化 logger 时发生异常: " + e.getMessage());
        }

        return logger;
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
