package team.patpat;

import team.patpat.view.Login;

/**
 * Main 类作为 Patpat Pro 应用程序的入口点。
 * 它初始化并启动登录窗口以启动应用程序。
 * 用法示例:
 * <pre>{@code
 * public class Main {
 *     public static void main(String[] args) {
 *         Login loginWindow = new Login();
 *     }
 * }
 * }</pre>
 *
 * @author 陈燕翔
 * @author 贾锡冰
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class Main {
    /*
     * 主方法，应用程序的起始点。
     * 它初始化登录窗口。
     *
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        Login loginWindow = new Login();
    }
}

