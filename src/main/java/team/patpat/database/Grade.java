package team.patpat.database;

/**
 * 成绩类，表示学生的一门考试成绩。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class Grade {
    private String courseName;  // 课程名称
    private String testName;    // 考试名称
    private int score;          // 分数
    private String hashValue;   // 哈希值

    /**
     * 构造函数，初始化成绩对象。
     *
     * @param courseName 课程名称
     * @param testName   考试名称
     * @param score      分数
     */
    public Grade(String courseName, String testName, int score) {
        this.courseName = courseName;
        this.testName = testName;
        this.score = score;
        this.hashValue = "";
    }

    /**
     * 设置哈希值。
     *
     * @param hashValue 哈希值
     */
    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    /**
     * 获取课程名称。
     *
     * @return 课程名称
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * 获取考试名称。
     *
     * @return 考试名称
     */
    public String getTestName() {
        return testName;
    }

    /**
     * 获取分数。
     *
     * @return 分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 获取哈希值。
     *
     * @return 哈希值
     */
    public String getHashValue() {
        return hashValue;
    }
}
