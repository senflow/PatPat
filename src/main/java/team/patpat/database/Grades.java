package team.patpat.database;

import java.util.ArrayList;

/**
 * 成绩集合类，表示一个学生的所有考试成绩。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class Grades {
    private String sid;                  // 学生ID
    private ArrayList<Grade> grades;     // 成绩列表

    /**
     * 构造函数，初始化成绩集合对象。
     *
     * @param sid 学生ID
     */
    public Grades(String sid) {
        this.sid = sid;
        this.grades = new ArrayList<Grade>();
    }

    /**
     * 添加考试成绩。
     *
     * @param courseName 课程名称
     * @param testName   考试名称
     * @param score      分数
     */
    public void addGrade(String courseName, String testName, int score) {
        Grade grade = new Grade(courseName, testName, score);
        grades.add(grade);
    }

    /**
     * 添加哈希值。
     *
     * @param courseName 课程名称
     * @param testName   考试名称
     * @param hashValue  哈希值
     */
    public void addHashValue(String courseName, String testName, String hashValue) {
        Grade grade = new Grade(courseName, testName, 0);
        grade.setHashValue(hashValue);
        grades.add(grade);
    }

    /**
     * 获取所有考试成绩。
     *
     * @return 考试成绩列表
     */
    public ArrayList<Grade> getGrades() {
        return grades;
    }

    /**
     * 获取学生ID。
     *
     * @return 学生ID
     */
    public String getSid() {
        return sid;
    }
}
