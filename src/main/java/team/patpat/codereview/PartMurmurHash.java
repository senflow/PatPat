package team.patpat.codereview;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * {@code PartMurmurHash} 类是 {@code MurmurHash} 类的子类，提供了对代码文件分段进行哈希处理的功能。
 * 该类主要用于生成代码文件分段的哈希值，并计算不同分段之间的 Jaccard 相似性。
 *
 * @deprecated 该类在当前项目中未使用，可能会在未来的版本中移除。
 * 如果有类似的功能需求，请查看其他替代方案。
 */
public class PartMurmurHash extends MurmurHash {
    /**
     * 对指定文件中的代码进行分段哈希处理。
     *
     * @param filePath    代码文件路径
     * @param hashSize    哈希集合的大小
     * @param segmentSize 每个分段的行数
     * @return 分段代码的哈希值列表
     * @throws IOException 读取文件时可能发生的 I/O 异常
     */
    public static List<Set<Integer>> hashCodeSegments(String filePath, int hashSize, int segmentSize)
            throws IOException {
        List<Set<Integer>> codeSegmentsHashes = new ArrayList<>();

        // 列出指定目录下所有的.java文件
        List<File> javaFiles = listJavaFiles(new File(filePath));

        for (File file : javaFiles) {
            List<String> codeLines = Files.readAllLines(file.toPath());

            for (int i = 0; i < codeLines.size(); i += segmentSize) {
                int endIndex = Math.min(i + segmentSize, codeLines.size());
                List<String> codeSegment = codeLines.subList(i, endIndex);
                Set<Integer> hashSet = hashSegment(codeSegment, hashSize);
                codeSegmentsHashes.add(hashSet);
            }
        }

        return codeSegmentsHashes;
    }

    /**
     * 对指定目录下的所有文件进行递归搜索，将所有的 .java 文件添加到列表中。
     *
     * @param directory 目录
     * @return 包含所有 .java 文件的列表
     */
    private static List<File> listJavaFiles(File directory) {
        List<File> javaFiles = new ArrayList<>();

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    javaFiles.addAll(listJavaFiles(file));
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }

        return javaFiles;
    }

    /**
     * 对代码分段进行哈希处理。
     *
     * @param codeSegment 代码分段
     * @param hashSize    哈希集合的大小
     * @return 代码分段的哈希值集合
     */
    private static Set<Integer> hashSegment(List<String> codeSegment, int hashSize) {
        Set<Integer> hashSet = new HashSet<>();

        for (String line : codeSegment) {
            line = removeCommentsAndWhitespace(line);
            int hash = Objects.hash(line);
            hashSet.add(hash % hashSize);
        }

        return hashSet;
    }

    /**
     * 计算两个分段的 Jaccard 相似性。
     *
     * @param set1 第一个分段的哈希值集合
     * @param set2 第二个分段的哈希值集合
     * @return Jaccard 相似性分数
     */
    public static double calculateJaccardSimilarity(List<Set<Integer>> set1, List<Set<Integer>> set2) {
        // 省略计算相似性的部分，根据需要进行调整
        double totalSimilarity = 0.0;

        for (int i = 0; i < set1.size() && i < set2.size(); i++) {
            Set<Integer> segment1 = set1.get(i);
            Set<Integer> segment2 = set2.get(i);

            double similarity = calculateJaccardSimilarity(segment1, segment2);
            totalSimilarity += similarity;
        }

        // 求平均相似性
        return totalSimilarity / set1.size();
    }

    /**
     * 计算多个分段的平均 Jaccard 相似性。
     *
     * @param set1 第一个代码文件的分段哈希值列表
     * @param set2 第二个代码文件的分段哈希值列表
     * @return 平均 Jaccard 相似性分数
     */
    public static double calculateJaccardSimilarity(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 0.0; // 避免除以零错误
        }

        return (double) intersection.size() / union.size();
    }
}
