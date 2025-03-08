package team.patpat.database;

/**
 * 表示两个学生之间基于不同指标的相似度。
 *
 * @author 贾锡冰
 * @version 1.0
 * @since 2023-11-1
 */
public class Similarity {

    /**
     * 第一个学生的ID。
     */
    private final String sid1;

    /**
     * 第二个学生的ID。
     */
    private final String sid2;

    /**
     * 第一个相似度指标。
     */
    private final Double similarity1;

    /**
     * 第二个相似度指标。
     */
    private final Double similarity2;

    /**
     * 两个相似度指标的总和。
     */
    private final Double similaritySum;

    /**
     * 使用提供的信息构造 Similarity 对象。
     *
     * @param sid1          第一个学生的ID。
     * @param sid2          第二个学生的ID。
     * @param similarity1   第一个相似度指标。
     * @param similarity2   第二个相似度指标。
     * @param similaritySum 两个相似度指标的总和。
     */
    public Similarity(String sid1, String sid2, Double similarity1, Double similarity2, Double similaritySum) {
        this.sid1 = sid1;
        this.sid2 = sid2;
        this.similarity1 = similarity1;
        this.similarity2 = similarity2;
        this.similaritySum = similaritySum;
    }

    /**
     * 获取第一个学生的ID。
     *
     * @return 第一个学生的ID。
     */
    public String getSid1() {
        return sid1;
    }

    /**
     * 获取第二个学生的ID。
     *
     * @return 第二个学生的ID。
     */
    public String getSid2() {
        return sid2;
    }

    /**
     * 获取第一个相似度指标。
     *
     * @return 第一个相似度指标。
     */
    public Double getSimilarity1() {
        return similarity1;
    }

    /**
     * 获取第二个相似度指标。
     *
     * @return 第二个相似度指标。
     */
    public Double getSimilarity2() {
        return similarity2;
    }

    /**
     * 获取两个相似度指标的总和。
     *
     * @return 两个相似度指标的总和。
     */
    public Double getSimilaritySum() {
        return similaritySum;
    }
}
