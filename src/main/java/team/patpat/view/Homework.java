package team.patpat.view;

import team.patpat.cloudobjectstorage.DownloadMyCode;
import team.patpat.cloudobjectstorage.UploadStudentCode;
import team.patpat.filerunning.JudgeUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Homework 类是作业窗口的实现。
 * 该窗口包含主页、作业、排行榜、个人信息、帮助和登出按钮，以及用于选择作业、上传作业、提交作业、下载历史作业的按钮和下拉框。
 * 使用示例:
 * <pre>{@code
 * Homework homeworkWindow = new Homework();
 * }</pre>
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class Homework extends JFrame {
    String path;
    File file;
    String iteration;

    public Homework() {
        this.setLayout(null);
        JButton btnmenu = new JButton("主页");
        JButton btnhomework = new JButton("作业");
        JButton btnrank = new JButton("排行榜");
        JButton btninformation = new JButton("个人信息");
        JButton btnhelp = new JButton("帮助");
        JButton btnlogout = new JButton("登出");
        JButton btnupload = new JButton("上传作业");
        JButton btnsubmit = new JButton("提交");
        JButton btndownload = new JButton("下载历史作业");

        JPanel btn = new JPanel();
        JPanel logout = new JPanel();
        btn.add(btnmenu);
        btn.add(btnhomework);
        btn.add(btnrank);
        btn.add(btninformation);
        btn.add(btnhelp);

        logout.add(btnlogout);
        btn.setBounds(-20, 0, 400, 50);
        logout.setBounds(510, 0, 80, 100);
        String[] homeworks = {"选择作业", "迭代一", "迭代二", "迭代三", "迭代四"};
        JComboBox<String> homeworkbox = new JComboBox<>(homeworks);
        homeworkbox.setUI(new CustomComboBoxUI());
        btnupload.setEnabled(false);
        btnsubmit.setEnabled(false);
        homeworkbox.setBounds(150, 70, 310, 80);
        btnupload.setBounds(150, 200, 310, 80);
        btnsubmit.setBounds(150, 300, 310, 80);
        btndownload.setBounds(150, 410, 310, 80);
        class CenteredRenderer extends DefaultListCellRenderer {
            @Override
            public void setHorizontalAlignment(int alignment) {
                // 将下拉菜单的选择内容居中显示
                super.setHorizontalAlignment(CENTER);
            }
        }
        homeworkbox.setRenderer(new CenteredRenderer());
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/homework.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));
        this.add(label);
        label.setBounds(-4, 0, 600, 600);
        this.add(btn);
        this.add(logout);
        this.add(homeworkbox);
        this.add(btnupload);
        this.add(btnsubmit);
        this.add(btndownload);
        btn.setOpaque(false);
        logout.setOpaque(false);

        btnmenu.setOpaque(false);
        btnmenu.setBorderPainted(false);
        btnhelp.setOpaque(false);
        btnhelp.setBorderPainted(false);
        btnrank.setOpaque(false);
        btnrank.setBorderPainted(false);
        btninformation.setOpaque(false);
        btninformation.setBorderPainted(false);
        btnlogout.setOpaque(false);
        btnlogout.setBorderPainted(false);
        btnhomework.setOpaque(false);
        btnhomework.setBorderPainted(false);
        homeworkbox.setOpaque(false);
        btnsubmit.setOpaque(false);
        btnsubmit.setBorderPainted(false);
        btnupload.setOpaque(false);
        btnupload.setBorderPainted(false);
        btndownload.setOpaque(false);
        btndownload.setBorderPainted(false);

        this.setTitle("patpat pro 作业");
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

        homeworkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String select = (String) homeworkbox.getSelectedItem();
                if (!select.equals("选择作业") || select == null) {
                    btnupload.setEnabled(true);
                    //btnsubmit.setEnabled(true);
                    if (select.equals("迭代一")) {
                        select = "iterationOne";
                    } else if (select.equals("迭代二")) {
                        select = "iterationTwo";
                    } else if (select.equals("迭代三")) {
                        select = "iterationThree";
                    } else if (select.equals("迭代四")) {
                        select = "iterationFour";
                    }
                    iteration = select;
                } else {
                    btnupload.setEnabled(false);
                    btnsubmit.setEnabled(false);
                }
            }
        });

        btnupload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser filechooser = new JFileChooser();
                int result = filechooser.showOpenDialog(btnupload);
                if (result == JFileChooser.APPROVE_OPTION) {
                    java.io.File selectfile = filechooser.getSelectedFile();
                    file = selectfile;
                    path = selectfile.getAbsolutePath();
                    UploadStudentCode uploadStudentCode = new UploadStudentCode();
                    int temp = uploadStudentCode.uploadStudentCode(path, iteration, "testOne", Login.userid);
                    if (temp == 1) {
                        btnsubmit.setEnabled(true);
                        JOptionPane.showMessageDialog(Homework.this, "上传成功");
                    } else if (temp == -1) {
                        JOptionPane.showMessageDialog(Homework.this, "上传失败");
                    } else if (temp == -2) {
                        JOptionPane.showMessageDialog(Homework.this, "文件格式不正确");
                    }


                }
            }
        });
        btnsubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //File files=new File(path);
                try {
                    btnsubmit.setEnabled(false);
                    btnupload.setEnabled(false);
                    homeworkbox.setSelectedIndex(0);
                    JFrame answer = new JFrame();
                    answer.setIconImage(icons);
                    String[] answers = new String[20];
                    JDialog dialog = new JDialog((Dialog) null, "请稍后", false);
                    dialog.setIconImage(icons);
                    //dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    //dialog.setUndecorated(true);
                    dialog.setBounds(500, 150, 300, 150);
                    dialog.setVisible(true);
                    //JOptionPane.showMessageDialog(dialog, "请稍后", "提示", JOptionPane.INFORMATION_MESSAGE);

                    answers = JudgeUser.runningAndReview(iteration, Login.userid, path);

                    JTextArea grades = new JTextArea();
                    grades.setEditable(false);
                    int i = 0;
                    while (answers[i] != null) {
                        grades.append("第" + (i + 1) + "次评测的结果是:\n" + answers[i] + "\n");
                        i++;
                    }
                    grades.setBounds(500, 150, 300, 500);
                    answer.add(grades);
                    answer.setBounds(500, 150, 300, 500);
                    dialog.dispose();

                    answer.setVisible(true);
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btndownload.addActionListener(new ActionListener() {
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
                            DownloadMyCode f = new DownloadMyCode();
                            int flag = f.downloadMyCode(selectedpath1, select, "testOne", Login.userid);
                            if (flag == 1) {
                                JOptionPane.showMessageDialog(Homework.this, "下载成功");
                            } else {
                                JOptionPane.showMessageDialog(Homework.this, "下载失败");
                            }

                        }
                    }
                });
            }
        });

        btnmenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu f = new Menu();
                Homework.this.dispose();
            }
        });

        btnhomework.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Homework f = new Homework();
                Homework.this.dispose();
            }
        });
        btnrank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ranks f = new Ranks();
                Homework.this.dispose();
            }
        });
        btnhelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Help f = new Help();
                Homework.this.dispose();
            }
        });
        btninformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Information f = new Information();
                Homework.this.dispose();
            }
        });
        btnlogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login f = new Login();
                Homework.this.dispose();
            }
        });
    }

}

class CustomComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
        return null;
    }
}
