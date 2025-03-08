package team.patpat.view;

import team.patpat.cloudobjectstorage.DownloadStudentCode;
import team.patpat.cloudobjectstorage.UploadStandardAnswer;
import team.patpat.cloudobjectstorage.UploadTestCase;
import team.patpat.database.CreateTime;
import team.patpat.database.ExportGrades;
import team.patpat.database.ExportSimilarity;
import team.patpat.database.UpdateNotice;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * AssistantMenu 类是助教主菜单窗口的实现。
 * 该窗口包含导出成绩、上传数据点、上传公告、导出查重结果、批量下载学生代码、登出等功能按钮。
 * 使用示例:
 * <pre>{@code
 * AssistantMenu assistantMenu = new AssistantMenu();
 * }</pre>
 * <p>
 * 该类依赖了以下类：
 * {@link team.patpat.cloudobjectstorage.DownloadStudentCode}
 * {@link team.patpat.cloudobjectstorage.UploadStandardAnswer}
 * {@link team.patpat.cloudobjectstorage.UploadTestCase}
 * {@link team.patpat.database.CreateTime}
 * {@link team.patpat.database.ExportGrades}
 * {@link team.patpat.database.ExportSimilarity}
 * {@link team.patpat.database.UpdateNotice}
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */

public class AssistantMenu extends JFrame {
    String path;
    File file;

    //String filePath;
    public AssistantMenu() {
        this.setLayout(null);
        JPanel panels = new JPanel(new GridLayout(3, 3));
        panels.setBounds(0, 0, 600, 600);
        JButton export = new JButton("导出成绩");
        JButton upload = new JButton("上传数据点");
        JButton news = new JButton("上传公告");
        JButton exportcheat = new JButton("导出查重结果");
        JButton download = new JButton("批量下载学生代码");
        JButton logout = new JButton("登出");
        JButton uploadanswer = new JButton("上传标准答案");
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/assistant.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));

        label.setBounds(-1, -3, 600, 600);
        this.add(label);
        panels.setOpaque(false);
        export.setOpaque(false);
        export.setBorderPainted(false);
        logout.setOpaque(false);
        logout.setBorderPainted(false);
        uploadanswer.setOpaque(false);
        uploadanswer.setBorderPainted(false);
        upload.setOpaque(false);
        upload.setBorderPainted(false);
        download.setOpaque(false);
        download.setBorderPainted(false);
        exportcheat.setOpaque(false);
        exportcheat.setBorderPainted(false);
        news.setOpaque(false);
        news.setBorderPainted(false);
        panels.add(export);
        panels.add(upload);
        panels.add(news);
        panels.add(exportcheat);
        panels.add(download);
        panels.add(uploadanswer);
        panels.add(logout);
        this.add(panels);

        this.setTitle("patpat pro 主页");
        this.setBounds(600, 200, 600, 600);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BufferedImage icon = null;
        try {
            icon = ImageIO.read(new File("picture/icon.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image icons = icon;
        this.setIconImage(icons);

        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame p = new JFrame("选择作业");
                p.setIconImage(icons);
                String[] homework1 = {"迭代一", "迭代二", "迭代三", "迭代四"};
                JComboBox<String> homeworkbox1 = new JComboBox<>(homework1);
                p.add(homeworkbox1);
                p.setBounds(600, 200, 300, 150);
                p.setVisible(true);
                homeworkbox1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String select = (String) homeworkbox1.getSelectedItem();
                        if (select.equals("迭代一")) {
                            select = "iterationOne";
                        } else if (select.equals("迭代二")) {
                            select = "iterationTwo";
                        } else if (select.equals("迭代三")) {
                            select = "iterationThree";
                        } else if (select.equals("迭代四")) {
                            select = "iterationFour";
                        }
                        JFileChooser fileChooser1 = new JFileChooser();
                        int result = fileChooser1.showOpenDialog(homeworkbox1);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            java.io.File selectfile = fileChooser1.getSelectedFile();
                            //File selectedpath=fileChooser1.getSelectedFile();
                            String selectedpath1 = selectfile.getPath();
                            UploadTestCase f = new UploadTestCase();
                            int flag = f.uploadTestCase(selectedpath1, select);
                            CreateTime createTime = new CreateTime();
                            createTime.createTime(select);
                            if (flag == 1) {
                                JOptionPane.showMessageDialog(AssistantMenu.this, "上传成功");
                            } else {
                                JOptionPane.showMessageDialog(AssistantMenu.this, "上传失败");
                            }
                            //
                        }
                    }
                });

            }
        });
        uploadanswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame p = new JFrame("选择作业");
                p.setIconImage(icons);
                String[] homework1 = {"迭代一", "迭代二", "迭代三", "迭代四"};
                JComboBox<String> homeworkbox1 = new JComboBox<>(homework1);
                p.add(homeworkbox1);
                p.setBounds(600, 200, 300, 150);
                p.setVisible(true);
                homeworkbox1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String select = (String) homeworkbox1.getSelectedItem();
                        if (select.equals("迭代一")) {
                            select = "iterationOne";
                        } else if (select.equals("迭代二")) {
                            select = "iterationTwo";
                        } else if (select.equals("迭代三")) {
                            select = "iterationThree";
                        } else if (select.equals("迭代四")) {
                            select = "iterationFour";
                        }
                        JFileChooser fileChooser1 = new JFileChooser();
                        int result = fileChooser1.showOpenDialog(homeworkbox1);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            java.io.File selectfile = fileChooser1.getSelectedFile();
                            String selectedpath1 = selectfile.getPath();
                            UploadStandardAnswer f = new UploadStandardAnswer();
                            int flag = f.uploadStandardAnswer(selectedpath1, select);
                            if (flag == 1) {
                                JOptionPane.showMessageDialog(AssistantMenu.this, "上传成功");
                            } else {
                                JOptionPane.showMessageDialog(AssistantMenu.this, "上传失败");
                            }
                            //
                        }
                    }
                });
            }
        });
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame p = new JFrame("选择作业");
                p.setIconImage(icons);
                String[] homework1 = {"迭代一", "迭代二", "迭代三", "迭代四"};
                JComboBox<String> homeworkbox1 = new JComboBox<>(homework1);
                p.add(homeworkbox1);
                p.setBounds(600, 200, 300, 150);
                p.setVisible(true);
                homeworkbox1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String select = (String) homeworkbox1.getSelectedItem();
                        if (select.equals("迭代一")) {
                            select = "iterationOne";
                        } else if (select.equals("迭代二")) {
                            select = "iterationTwo";
                        } else if (select.equals("迭代三")) {
                            select = "iterationThree";
                        } else if (select.equals("迭代四")) {
                            select = "iterationFour";
                        }
                        JFileChooser fileChooser1 = new JFileChooser();
                        fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int result = fileChooser1.showOpenDialog(homeworkbox1);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedpath = fileChooser1.getSelectedFile();
                            String selectedpath1 = selectedpath.getPath();
                            DownloadStudentCode f = new DownloadStudentCode();
                            int flag = f.downloadStudentCode(selectedpath1, select);
                            if (flag == 1) {
                                JOptionPane.showMessageDialog(AssistantMenu.this, "下载成功");
                            } else {
                                JOptionPane.showMessageDialog(AssistantMenu.this, "下载失败");
                            }

                        }
                    }
                });
            }
        });

        news.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPage();
            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                path = choosePath();
                ExportGrades exportGrades = new ExportGrades();
                exportGrades.exportGrades(path);
                JOptionPane.showMessageDialog(AssistantMenu.this, "导出成功");
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                team.patpat.view.Login f = new Login();
                AssistantMenu.this.dispose();
            }
        });
        exportcheat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                path = choosePath();
                ExportSimilarity exportSimilarity = new ExportSimilarity();
                exportSimilarity.exportSimilarity(path);
                JOptionPane.showMessageDialog(AssistantMenu.this, "导出成功");
            }
        });


    }

    private static void showPage() {
        JFrame pageFrame = new JFrame("patpat pro");
        pageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(10, 40);

        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = textArea.getText();
                UpdateNotice u = new UpdateNotice();
                u.updateNotice(input);
                JOptionPane.showMessageDialog(pageFrame, "上传成功");
                pageFrame.dispose();
            }
        });

        JPanel pagePanel = new JPanel();
        pagePanel.add(textArea);
        pagePanel.add(confirmButton);
        pageFrame.setBounds(400, 300, 200, 200);
        pageFrame.add(pagePanel, BorderLayout.CENTER);
        pageFrame.pack();
        pageFrame.setVisible(true);
    }

    private static String choosePath() {
        String filePath = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int choice = fileChooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
            return filePath;
        }
        return null;
    }
}
