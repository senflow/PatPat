package team.patpat.codereview;

import java.io.IOException;
import java.util.Set;

public class ProjectSimilarity {

    public static double compareProjects(String projectPath1, String projectPath2, int hashSize) throws IOException {
        // 生成项目1的哈希集合
        Set<Integer> hashSet1 = ASTHash.hashFolder(projectPath1, hashSize);

        // 生成项目2的哈希集合
        Set<Integer> hashSet2 = ASTHash.hashFolder(projectPath2, hashSize);

        String hashString1 = MurmurHash.createMyHash(projectPath1, hashSize, "java");

        String hashString2 = MurmurHash.createMyHash(projectPath2, hashSize, "java");

        System.out.println(ASTHash.calculateHashSimilarity(hashSet1, hashSet2));
        System.out.println(MurmurHash.calculateJaccardSimilarity(hashString1, hashString2));

        // 计算相似度
        return ASTHash.calculateHashSimilarity(hashSet1, hashSet2);
    }


    public static void main(String[] args) {
        String path1 = "hjc";
        String path2 = "cyx";
        String path3 = "jxb";
        String path4 = "asj";
        int hashSize = 600; // 定义哈希表大小

        try {
            // 分别计算四个项目的相似度
            double similarity1 = compareProjects(path1, path2, hashSize);
            double similarity2 = compareProjects(path1, path3, hashSize);
            double similarity3 = compareProjects(path2, path3, hashSize);
            double similarity4 = compareProjects(path1, path4, hashSize);
            double similarity5 = compareProjects(path2, path4, hashSize);
            double similarity6 = compareProjects(path3, path4, hashSize);

            // 输出相似度
            System.out.println(path1 + "和" + path2 + "的相似度：" + similarity1);
            System.out.println(path1 + "和" + path3 + "的相似度：" + similarity2);
            System.out.println(path2 + "和" + path3 + "的相似度：" + similarity3);
            System.out.println(path1 + "和" + path4 + "的相似度：" + similarity4);
            System.out.println(path2 + "和" + path4 + "的相似度：" + similarity5);
            System.out.println(path3 + "和" + path4 + "的相似度：" + similarity6);
        } catch (IOException e) {
            System.err.println("处理文件时出错: " + e.getMessage());
        }
    }
}