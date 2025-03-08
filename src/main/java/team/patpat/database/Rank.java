package team.patpat.database;

/**
 * 排名信息类，用于存储学生的排名信息。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class Rank {
    private String sid;
    private String name;
    private int iterationOne;
    private int iterationTwo;
    private int iterationThree;
    private int iterationFour;
    private int sum;

    /**
     * 构造函数，初始化排名信息。
     *
     * @param sid 学生ID
     */
    public Rank(String sid) {
        this.sid = sid;
        GetName getName = new GetName();
        this.name = getName.getName(sid);
        this.iterationOne = 0;
        this.iterationTwo = 0;
        this.iterationThree = 0;
        this.iterationFour = 0;
        this.sum = 0;
    }

    /**
     * 获取学生ID。
     *
     * @return 学生ID
     */
    public String getSid() {
        return sid;
    }

    /**
     * 获取学生姓名。
     *
     * @return 学生姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 获取第一次迭代时间。
     *
     * @return 第一次迭代时间
     */
    public int getIterationOne() {
        return iterationOne;
    }

    /**
     * 获取第二次迭代时间。
     *
     * @return 第二次迭代时间
     */
    public int getIterationTwo() {
        return iterationTwo;
    }

    /**
     * 获取第三次迭代时间。
     *
     * @return 第三次迭代时间
     */
    public int getIterationThree() {
        return iterationThree;
    }

    /**
     * 获取第四次迭代时间。
     *
     * @return 第四次迭代时间
     */
    public int getIterationFour() {
        return iterationFour;
    }

    /**
     * 获取总时间。
     *
     * @return 总时间
     */
    public int getSum() {
        return sum;
    }

    /**
     * 添加迭代时间。
     *
     * @param courseName 迭代名称
     * @param time       时间
     */
    public void addTime(String courseName, int time) {
        if (courseName.equals("iterationOne")) {
            this.iterationOne += time;
        } else if (courseName.equals("iterationTwo")) {
            this.iterationTwo += time;
        } else if (courseName.equals("iterationThree")) {
            this.iterationThree += time;
        } else if (courseName.equals("iterationFour")) {
            this.iterationFour += time;
        }
        this.sum += time;
    }
}
