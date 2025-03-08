package team.patpat.codereview;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MurmurHash算法类，用于对代码文件进行哈希处理并计算相似性。
 * 提供了多种计算相似性的方法，包括Jaccard相似性、Cosine相似性和编辑距离相似性。
 * 在项目中，我们使用Jaccard相似性来计算代码文件的相似性。
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
public class MurmurHash {
    /**
     * 对指定文件夹中的代码文件进行哈希处理。
     *
     * @param folderPath 文件夹路径
     * @param hashSize   哈希集合的大小
     * @param suffix     代码文件的后缀
     * @return 文件夹中所有文件的哈希集合
     * @throws IOException 读取文件时可能发生的IO异常
     */
    public static Set<Integer> hashFolder(String folderPath, int hashSize, String suffix) throws IOException {
        Set<Integer> fileHashes = new HashSet<>();
        processFolder(new File(folderPath), fileHashes, hashSize, suffix);
        return fileHashes;
    }

    /**
     * 递归处理文件夹中的代码文件，对每个文件进行哈希处理，并将结果添加到给定的哈希集合中。
     *
     * @param folder     文件夹
     * @param fileHashes 哈希集合，用于存储文件的哈希结果
     * @param hashSize   哈希集合的大小
     * @param suffix     代码文件的后缀
     * @throws IOException 读取文件时可能发生的IO异常
     */
    private static void processFolder(File folder, Set<Integer> fileHashes, int hashSize, String suffix)
            throws IOException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    processFolder(file, fileHashes, hashSize, suffix);
                } else if (file.getName().endsWith(suffix)) {
                    Set<Integer> hashSet = hashFile(file.getPath(), hashSize);
                    fileHashes.addAll(hashSet);
                }
            }
        }
    }

    /**
     * 对指定文件进行哈希处理。
     *
     * @param filePath 文件路径
     * @param hashSize 哈希集合的大小
     * @return 文件的哈希集合
     * @throws IOException 读取文件时可能发生的IO异常
     */
    public static Set<Integer> hashFile(String filePath, int hashSize) throws IOException {
        Set<Integer> hashSet = new HashSet<>();

        Files.lines(new File(filePath).toPath())
                .forEach(line -> {
                    line = removeCommentsAndWhitespace(line);
                    int hash = Objects.hash(line);
                    hashSet.add(hash % hashSize);
                });

        return hashSet;
    }

    /**
     * 移除代码行中的注释和空格。
     *
     * @param line 代码行
     * @return 移除注释和空格后的代码行
     */
    public static String removeCommentsAndWhitespace(String line) {
        // 移除单行注释
        if (line.contains("//")) {
            line = line.substring(0, line.indexOf("//"));
        }
        // 移除多行注释
        if (line.contains("/*")) {
            int start = line.indexOf("/*");
            int end = line.indexOf("*/");
            if (start >= 0 && end >= 0 && end > start) {
                line = line.substring(0, start) + line.substring(end + 2);
            }
        }
        // 移除空格
        line = line.trim();

        return line;
    }

    /**
     * 计算两个哈希集合的Jaccard相似性。
     *
     * @param set1 第一个哈希集合
     * @param set2 第二个哈希集合
     * @return Jaccard相似性分数
     * @deprecated 该方法已被弃用，请使用 {@link #calculateJaccardSimilarity(String, String)} 方法，
     * 并通过将哈希集合转换为逗号分隔的字符串形式进行计算。
     */
    public static double calculateJaccardSimilarity(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    /**
     * 计算两个用字符串表示的集合的Jaccard相似性。
     *
     * @param setS1 第一个哈希集合的字符串表示
     * @param setS2 第二个哈希集合的字符串表示
     * @return Jaccard相似性分数
     */
    public static double calculateJaccardSimilarity(String setS1, String setS2) {
        Set<Integer> set1 = new HashSet<>();
        for (String s : setS1.split(",")) {
            set1.add(Integer.parseInt(s));
        }

        Set<Integer> set2 = new HashSet<>();
        for (String s : setS2.split(",")) {
            set2.add(Integer.parseInt(s));
        }

        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    /**
     * 对指定文件夹中的代码文件进行哈希处理，然后返回哈希集合的字符串表示。
     *
     * @param path     文件夹路径
     * @param hashSize 哈希集合的大小
     * @param suffix   代码文件的后缀
     * @return 哈希集合的字符串表示
     * @throws IOException 读取文件时可能发生的IO异常
     */
    public static String createMyHash(String path, int hashSize, String suffix) throws IOException {
        Set<Integer> hashSet = hashFolder(path, hashSize, suffix);
        return hashSet.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    /**
     * 计算两个哈希集合的Cosine相似性。
     *
     * @param setS1 第一个哈希集合的字符串表示
     * @param setS2 第二个哈希集合的字符串表示
     * @return Cosine相似性分数
     * @deprecated 该方法为拓展方法，未在当前项目中使用。在未来的版本中可能会使用，
     * 建议使用 {@link #calculateJaccardSimilarity(String, String)} 方法进行计算。
     */
    @Deprecated
    public static double calculateCosineSimilarity(String setS1, String setS2) {
        Set<Integer> set1 = new HashSet<>();
        for (String s : setS1.split(",")) {
            set1.add(Integer.parseInt(s));
        }

        Set<Integer> set2 = new HashSet<>();
        for (String s : setS2.split(",")) {
            set2.add(Integer.parseInt(s));
        }

        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (int commonElement : intersection) {
            dotProduct += commonElement;
        }

        for (int element : set1) {
            magnitude1 += Math.pow(element, 2);
        }

        for (int element : set2) {
            magnitude2 += Math.pow(element, 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 * magnitude2 == 0) {
            return 0.0; // 避免除以零错误
        }

        return dotProduct / (magnitude1 * magnitude2);
    }

    /**
     * 计算两个哈希集合的编辑距离相似性。
     *
     * @param set1 第一个哈希集合
     * @param set2 第二个哈希集合
     * @return 编辑距离相似性分数
     * @deprecated 该方法为拓展方法，未在当前项目中使用。在未来的版本中可能会使用，
     * 建议使用  {@link #calculateJaccardSimilarity(String, String)} 方法进行计算。
     */
    @Deprecated
    public static double calculateEditDistance(Set<Integer> set1, Set<Integer> set2) {
        // 将整个哈希集合转换为字符串
        String s1 = set1.toString();
        String s2 = set2.toString();

        // 计算编辑距离
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        return 1.0 - (double) dp[s1.length()][s2.length()] / Math.max(s1.length(), s2.length());
    }
}
