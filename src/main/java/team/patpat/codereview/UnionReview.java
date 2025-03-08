package team.patpat.codereview;

/**
 * 代码审查工具的主要类，用于合并不同相似性度量方法的代码审查分数。
 */
public class UnionReview {
    /**
     * 使用加权平均值将来自MurmurHash和ASTHash的代码审查分数进行合并。
     *
     * @param murmurSimilarity MurmurHash的相似性分数
     * @param ASTSimilarity    ASTHash的相似性分数
     * @return 合并的代码审查分数
     */
    public static double codeReview(double murmurSimilarity, double ASTSimilarity) {
        // MurmurHash相似性的权重
        double murmurWeight = 0.2;

        // ASTHash相似性的权重
        double ASTWeight = 0.8;

        // 使用加权平均值合并代码审查分数
        return murmurWeight * murmurSimilarity + ASTWeight * ASTSimilarity;
    }
}
