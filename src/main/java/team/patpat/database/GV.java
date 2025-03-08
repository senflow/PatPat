package team.patpat.database;

/**
 * 全局变量类，存储数据库连接和其他全局常量。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class GV {
    /**
     * 数据库连接 URL。
     */
    public static final String url = "jdbc:mysql://bj-cdb-8qx5pnsc.sql.tencentcdb.com:63665/studentinfo";

    /**
     * 数据库用户名。
     */
    public static final String username = "root";

    /**
     * 数据库密码。
     */
    public static final String password = "a12345678";

    /**
     * 腾讯云密钥的 SecretId。
     */
    public static final String secretId = "AKIDvodHvc9UnwxmwsYw67uXx98vdBVfeS3V";

    /**
     * 腾讯云密钥的 SecretKey。
     */
    public static final String secretKey = "0sXx87tQo165rOMDrIajhejstjKfnutg";

    /**
     * 腾讯云服务所在的区域。
     */
    public static final String region = "ap-beijing";

    /**
     * 存储桶的名称。
     */
    public static final String bucketName = "code-1258739797";
}
