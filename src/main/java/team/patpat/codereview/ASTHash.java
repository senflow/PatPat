package team.patpat.codereview;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * {@code MethodCollector} 类是一个继承自 {@code VoidVisitorAdapter<Set<String>>} 的访问器类，
 * 用于在 AST（抽象语法树） 中收集 Java 方法的名称。
 *
 * <p>该类通过重写 {@code visit} 方法，将遇到的方法名添加到指定的集合中。</p>
 *
 * <p>使用时，请创建一个实例，并通过 {@code visit} 方法遍历 AST，传入方法名的集合，即可实现方法名的收集。</p>
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
class MethodCollector extends VoidVisitorAdapter<Set<String>> {
    /**
     * 重写的 visit 方法，用于收集方法名。
     *
     * @param method    表示 AST 中的方法声明
     * @param collector 用于存储方法名的集合
     */
    @Override
    public void visit(MethodDeclaration method, Set<String> collector) {
        // 收集方法名
        collector.add(method.getNameAsString());
        super.visit(method, collector);
    }
}

/**
 * {@code MethodBodyCollector} 类是一个继承自 {@code VoidVisitorAdapter<Set<String>>} 的访问器类，
 * 用于在 AST（抽象语法树） 中收集 Java 方法的名称。
 *
 * <p>该类通过重写 {@code visit} 方法，将遇到的方法体添加到指定的集合中。</p>
 *
 * <p>使用时，请创建一个实例，并通过 {@code visit} 方法遍历 AST，传入方法体的集合，即可实现方法体的收集。</p>
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
class MethodBodyCollector extends VoidVisitorAdapter<Set<String>> {
    /**
     * 重写的 visit 方法，用于收集方法体。
     *
     * @param method    表示 AST 中的方法声明
     * @param collector 用于存储方法体的集合
     */
    @Override
    public void visit(MethodDeclaration method, Set<String> collector) {
        // 收集方法体
        collector.add(method.getBody().toString());
        super.visit(method, collector);
    }
}

/*
 * {@code VariableCollector} 类是一个继承自 {@code VoidVisitorAdapter<Set<String>>} 的访问器类，
 * 用于在 AST（抽象语法树） 中收集 Java 变量的名称。
 *
 * <p>该类通过重写 {@code visit} 方法，将遇到的变量名添加到指定的集合中。</p>
 *
 * <p>使用时，请创建一个实例，并通过 {@code visit} 方法遍历 AST，传入变量名的集合，即可实现变量名的收集。</p>
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
class VariableCollector extends VoidVisitorAdapter<Set<String>> {
    /**
     * 重写的 visit 方法，用于收集变量名。
     *
     * @param variable  表示 AST 中的变量声明
     * @param collector 用于存储变量名的集合
     */
    @Override
    public void visit(VariableDeclarator variable, Set<String> collector) {
        // 收集变量名
        collector.add(variable.getNameAsString());
        super.visit(variable, collector);
    }
}

/*
 *
 * {@code ControlStructureCollector} 类是一个继承自 {@code VoidVisitorAdapter<Void>} 的访问器类，
 * 用于在 AST（抽象语法树） 中收集控制结构的信息。
 *
 * <p>该类通过重写 {@code visit} 方法，将遇到的控制结构类型添加到内部的字符串构建器中。</p>
 *
 * <p>使用时，可以创建一个实例，通过 {@code visit} 方法遍历 AST，然后通过 {@code getControlStructureInfo} 方法获取收集到的信息。</p>
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
class ControlStructureCollector extends VoidVisitorAdapter<Void> {
    /**
     * 用于存储控制结构信息的字符串构建器。
     */
    private final StringBuilder controlStructureInfo = new StringBuilder();

    /**
     * 重写的 visit 方法，用于收集控制结构信息。
     *
     * @param ifStmt 表示 AST 中的 if 语句
     * @param arg    无需使用的参数
     */
    @Override
    public void visit(IfStmt ifStmt, Void arg) {
        controlStructureInfo.append("if");
        super.visit(ifStmt, arg);
    }

    /**
     * 重写的 visit 方法，用于收集控制结构信息。
     *
     * @param forStmt 表示 AST 中的 for 循环语句
     * @param arg     无需使用的参数
     */
    @Override
    public void visit(ForStmt forStmt, Void arg) {
        controlStructureInfo.append("for");
        super.visit(forStmt, arg);
    }

    /**
     * 重写的 visit 方法，用于收集控制结构信息。
     *
     * @param whileStmt 表示 AST 中的 while 循环语句
     * @param arg       无需使用的参数
     */
    @Override
    public void visit(WhileStmt whileStmt, Void arg) {
        controlStructureInfo.append("while");
        super.visit(whileStmt, arg);
    }

    /**
     * 重写的 visit 方法，用于收集控制结构信息。
     *
     * @param doStmt 表示 AST 中的 do-while 循环语句
     * @param arg    无需使用的参数
     */
    @Override
    public void visit(DoStmt doStmt, Void arg) {
        controlStructureInfo.append("do");
        super.visit(doStmt, arg);
    }

    /**
     * 获取收集到的控制结构信息。
     *
     * @return 一个字符串，包含收集到的控制结构信息
     */
    public String getControlStructureInfo() {
        return controlStructureInfo.toString();
    }
}

/**
 * {@code MethodCallCollector} 类是一个继承自 {@code VoidVisitorAdapter<Void>} 的访问器类，
 * 用于在 AST（抽象语法树） 中收集方法调用的信息。
 *
 * <p>该类通过重写 {@code visit} 方法，将遇到的方法调用名称添加到内部的集合中。</p>
 *
 * <p>使用时，可以创建一个实例，通过 {@code visit} 方法遍历 AST，然后通过 {@code getMethodCallInfo} 方法获取收集到的信息。</p>
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
class MethodCallCollector extends VoidVisitorAdapter<Void> {
    /**
     * 用于存储方法调用信息的集合。
     */
    private final Set<String> methodCalls = new HashSet<>();

    /**
     * 重写的 visit 方法，用于收集方法调用信息。
     *
     * @param methodCall 表示 AST 中的方法调用表达式
     * @param arg        无需使用的参数
     */
    @Override
    public void visit(MethodCallExpr methodCall, Void arg) {
        // 收集方法调用
        methodCalls.add(methodCall.getNameAsString());
        super.visit(methodCall, arg);
    }

    /**
     * 获取收集到的方法调用信息。
     *
     * @return 一个集合，包含收集到的方法调用名称
     */
    public Set<String> getMethodCallInfo() {
        return methodCalls;
    }
}

/**
 * {@code ImportCollector} 类是一个继承自 {@code VoidVisitorAdapter<Void>} 的访问器类，
 * 用于在 AST（抽象语法树） 中收集 import 语句的信息。
 *
 * <p>该类通过重写 {@code visit} 方法，将遇到的 import 语句添加到内部的集合中。</p>
 *
 * <p>使用时，可以创建一个实例，通过 {@code visit} 方法遍历 AST，然后通过 {@code getImportInfo} 方法获取收集到的信息。</p>
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
class ImportCollector extends VoidVisitorAdapter<Void> {
    /**
     * 用于存储 import 语句信息的集合。
     */
    private final Set<String> imports = new HashSet<>();

    /**
     * 重写的 visit 方法，用于收集 import 语句信息。
     *
     * @param importDeclaration 表示 AST 中的 import 声明
     * @param arg               无需使用的参数
     */
    @Override
    public void visit(ImportDeclaration importDeclaration, Void arg) {
        // 收集import语句
        imports.add(importDeclaration.getNameAsString());
        super.visit(importDeclaration, arg);
    }

    /**
     * 获取收集到的 import 语句信息。
     *
     * @return 一个集合，包含收集到的 import 语句
     */
    public Set<String> getImportInfo() {
        return imports;
    }
}

/**
 * {@code ASTHash} 类提供了使用哈希方法进行 AST（抽象语法树） 代码查重的功能。
 *
 * <p>通过该类，可以对指定文件或文件夹中的 Java 代码进行 AST 分析，并计算其哈希值，用于查重和相似性分析。</p>
 *
 * @author 陈燕翔
 * @version 1.0
 * @since 2023-12-17
 */
public class ASTHash {
    /**
     * 使用哈希方法对单个 Java 文件的 AST 进行代码查重。
     *
     * @param filePath 要查重的 Java 文件路径
     * @param hashSize 哈希表的大小
     * @return 一个包含哈希值的集合
     * @throws IOException 如果文件读取失败
     */
    public static Set<Integer> hashAST(String filePath, int hashSize) throws IOException {
        Set<Integer> hashSet = new HashSet<>();

        // 创建 JavaParser 实例
        JavaParser javaParser = new JavaParser();

        // 使用 ParseResult 类型
        ParseResult<CompilationUnit> parseResult;
        parseResult = javaParser.parse(new File(filePath));

        CompilationUnit cu = parseResult.getResult().get();
        traverseASTForHash(cu, hashSet, hashSize);

        return hashSet;
    }

    /**
     * 使用哈希方法对 AST 进行遍历，收集不同结构的哈希值。
     *
     * @param node     AST 中的节点
     * @param hashSet  存储哈希值的集合
     * @param hashSize 哈希表的大小
     */
    private static void traverseASTForHash(Node node, Set<Integer> hashSet, int hashSize) {
        if (node instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) node;
            // 获取方法名
            Set<String> methodNames = new HashSet<>();
            MethodCollector methodCollector = new MethodCollector();
            methodCollector.visit(methodDeclaration, methodNames);
            // 获取方法体
            Set<String> methodBodies = new HashSet<>();
            MethodBodyCollector methodBodyCollector = new MethodBodyCollector();
            methodBodyCollector.visit(methodDeclaration, methodBodies);
            // 收集变量名
            Set<String> variableNames = new HashSet<>();
            VariableCollector variableCollector = new VariableCollector();
            variableCollector.visit(methodDeclaration, variableNames);
            // 收集控制结构
            ControlStructureCollector controlStructureCollector = new ControlStructureCollector();
            controlStructureCollector.visit(methodDeclaration, null);
            // 收集方法调用
            MethodCallCollector methodCallCollector = new MethodCallCollector();
            methodCallCollector.visit(methodDeclaration, null);
            // 收集import语句
            ImportCollector importCollector = new ImportCollector();
            importCollector.visit(methodDeclaration, null);

            //组合信息
            // 计算哈希值，给不同的结构类型分配不同的权重
            int methodNameWeight = 1;
            int methodBodyWeight = 4;
            int variableNameWeight = 1;
            int controlStructureWeight = 8;
            int methodCallWeight = 5;
            int importStatementWeight = 3;

            int hash = (methodNames.hashCode() * methodNameWeight
                    + methodBodies.hashCode() * methodBodyWeight
                    + variableNames.hashCode() * variableNameWeight
                    + controlStructureCollector.getControlStructureInfo().hashCode() * controlStructureWeight
                    + methodCallCollector.getMethodCallInfo().hashCode() * methodCallWeight
                    + importCollector.getImportInfo().hashCode() * importStatementWeight)
                    % hashSize;

            hashSet.add(hash);
        }

        for (Node child : node.getChildNodes()) {
            traverseASTForHash(child, hashSet, hashSize);
        }
    }

    /**
     * 使用哈希方法对文件夹中的所有文件生成hash值。
     *
     * @param folderPath 要查重的文件夹路径
     * @param hashSize   哈希大小，用于确定哈希值的范围
     * @return 一个包含所有文件的哈希值的集合
     * @throws IOException 读取文件时可能发生的 IO 异常
     */
    public static Set<Integer> hashFolder(String folderPath, int hashSize) throws IOException {
        Set<Integer> fileHashes = new HashSet<>();
        processFolder(new File(folderPath), fileHashes, hashSize);
        return fileHashes;
    }

    /**
     * 创建给定路径下所有文件的哈希值，并将其转化为字符串。
     *
     * @param path     文件夹路径
     * @param hashSize 哈希大小，用于确定哈希值的范围
     * @return 由文件哈希值组成的字符串
     * @throws IOException 读取文件时可能发生的 IO 异常
     */
    public static String createMyHash(String path, int hashSize) throws IOException {
        Set<Integer> hashSet = hashFolder(path, hashSize);
        return hashSet.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    /**
     * 使用哈希方法对整个文件夹进行遍历，收集文件的哈希值。
     *
     * @param folder     文件夹
     * @param fileHashes 用于存储文件哈希值的集合
     * @param hashSize   哈希大小，用于确定哈希值的范围
     * @throws IOException 读取文件时可能发生的 IO 异常
     */
    private static void processFolder(File folder, Set<Integer> fileHashes, int hashSize)
            throws IOException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    processFolder(file, fileHashes, hashSize);
                } else if (file.getName().endsWith(".java")) {
                    Set<Integer> hashSet = hashAST(file.getPath(), hashSize);
                    fileHashes.addAll(hashSet);
                }
            }
        }
    }

    /**
     * 计算两个哈希值集合的相似性分数。
     *
     * <p>本项目使用的函数均为 {@link #calculateHashSimilarity(String, String)}，该方法将哈希值集合表示为字符串，
     * 然后计算字符串形式的哈希值集合的相似性分数。</p>
     *
     * @param set1 第一个哈希值集合
     * @param set2 第二个哈希值集合
     * @return 相似性分数
     * @see #calculateHashSimilarity(String, String)
     */
    public static double calculateHashSimilarity(Set<Integer> set1, Set<Integer> set2) {
        // 计算交集大小
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        int intersectionSize = intersection.size();

        // 计算并集大小
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        int unionSize = union.size();

        // 计算相似性分数
        if (unionSize == 0) {
            return 0.0; // 避免除零错误
        } else {
            return (double) intersectionSize / unionSize;
        }
    }

    /**
     * 计算两个以字符串表示的哈希值集合的相似性分数。
     *
     * @param setS1 第一个哈希值集合的字符串表示
     * @param setS2 第二个哈希值集合的字符串表示
     * @return 相似性分数
     */
    public static double calculateHashSimilarity(String setS1, String setS2) {
        Set<Integer> set1 = new HashSet<>();
        for (String s : setS1.split(",")) {
            set1.add(Integer.parseInt(s));
        }

        Set<Integer> set2 = new HashSet<>();
        for (String s : setS2.split(",")) {
            set2.add(Integer.parseInt(s));
        }
        // 计算交集大小
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        int intersectionSize = intersection.size();

        // 计算并集大小
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        int unionSize = union.size();

        // 计算相似性分数
        if (unionSize == 0) {
            return 0.0; // 避免除零错误
        } else {
            return (double) intersectionSize / unionSize;
        }
    }
}

