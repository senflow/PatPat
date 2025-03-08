package team.patpat.cloudobjectstorage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.region.Region;
import team.patpat.database.GV;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 用于从腾讯云对象存储下载测试用例的工具类。
 *
 * @author 贾锡冰
 * @version 1.0
 */
public class DownloadTestCase {

    /**
     * 下载指定课程和测试的测试用例。
     *
     * @param courseName 课程名称。
     * @param testName   测试名称。
     * @return 下载的测试用例内容，如果失败则返回null。
     */
    public String downloadTestCase(String courseName, String testName) {
        Logger globalLogger = Logger.getLogger("globalLogger");
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("log/globalError.log");
            globalLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            globalLogger.severe("初始化FileHandler时发生错误: " + e.getMessage());
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

            // COS 存储路径（key）
            String cosKey = "testcase/" + courseName + "/" + testName + ".txt";

            // 获取对象内容输入流
            GetObjectRequest getObjectRequest = new GetObjectRequest(GV.bucketName, cosKey);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

            String resultString = "";
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                // 将 COS 对象输入流写入 ByteArrayOutputStream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = cosObjectInput.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                // 将 ByteArrayOutputStream 中的数据转换为字符串
                resultString = byteArrayOutputStream.toString("UTF-8");

            } catch (Exception e) {
                e.printStackTrace();
            }

            // 下载对象的 CRC64
            String crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();

            // 关闭输入流
            cosObjectInput.close();
            cosClient.shutdown();
            return resultString;
        } catch (IOException ex) {
            globalLogger.severe("发生异常: " + ex.getMessage());
        }
        closeLogFiles(globalLogger);
        return null;
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
            logger.severe("初始化 logger 时发生异常: " + e.getMessage());
        }
        return logger;
    }

    /**
     * 关闭 Logger 文件。
     *
     * @param globalLogger 待关闭的 Logger 对象。
     */
    private static void closeLogFiles(Logger globalLogger) {
        for (Handler handler : globalLogger.getHandlers()) {
            if (handler instanceof FileHandler) {
                handler.close();
            }
        }
    }
}
