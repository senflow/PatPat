package team.patpat.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Help 类是帮助窗口的实现。
 * 该窗口包含主页、作业、排行榜、个人信息、帮助和登出按钮，以及一个用于显示帮助信息的文本区域。
 * 使用示例:
 * <pre>{@code
 * Help helpWindow = new Help();
 * }</pre>
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class Help extends JFrame {
    public Help() {
        this.setLayout(null);
        JButton btnmenu = new JButton("主页");
        JButton btnhomework = new JButton("作业");
        JButton btnrank = new JButton("排行榜");
        JButton btninformation = new JButton("个人信息");
        JButton btnhelp = new JButton("帮助");
        JButton btnlogout = new JButton("登出");
        JPanel btn = new JPanel();
        JPanel logout = new JPanel();
        btn.add(btnmenu);
        btn.add(btnhomework);
        btn.add(btnrank);
        btn.add(btninformation);
        btn.add(btnhelp);
        logout.add(btnlogout);
        btn.setBounds(-20, 0, 400, 50);
        logout.setBounds(510, 0, 80, 50);

        btn.setOpaque(false);
        logout.setOpaque(false);
//        btn.setBorder(null);

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

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/help.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));
        Font font = new Font(Font.DIALOG, Font.BOLD, 12);
        JTextArea announcementArea = new JTextArea();
        announcementArea.setFont(font);
        announcementArea.setText("用户使用说明书 \n" +
                "学生端 \n" +
                "登录 \n" +
                "输入自身用户名（学号），并输入对应的密码，点击登录按钮即可完成登录。若没有账号，\n" +
                "则参考下一步注册。 \n" +
                "注册 \n" +
                "在登录页面点击左下角注册按钮进入注册页面，按照页面上的提示完成用户名与密码的输\n" +
                "入，然 后点击左下角的注册按钮，即可完成注册。注册完成后请点击返回按钮，进入登录\n" +
                "界面，操作参考登录说明。\n" +
                "修改密码 \n" +
                "在登录页面点击右下角修改密码按钮进入修改密码界面，填写用户名与旧密码、新密码，\n" +
                "然后点 击修改按钮即可完成密码修改。（若忘记密码，请及时联系助教后台修改密码）\n" +
                "提交作业 \n" +
                "登录之后会直接进入作业界面，若处于其它界面可点击上方作业按钮进入界面。 \n" +
                "首先，点击选择作业按钮，选定正确的作业选项；其次，点击上传作业按钮，选定正确的文\n" +
                "件进行上传；最后，点击提交按钮，即可完成作业提交。 \n" +
                "（若希望查看自己以往的代码，可通过下载历史作业完成）\n" +
                "个人信息 \n" +
                "在首次注册登录时，希望同学们点击上方个人信息按钮查看个人信息，确保信息无误，负责\n" +
                "可能会影响测评结果的录入！！！\n" +
                "查看公告 \n" +
                "点击左上角主页按钮，即可查看助教发布的各项公告。 \n" +
                "查看帮助 \n" +
                "点击上方帮助按钮，即可查看助教发布的教程。 \n" +
                "查看排行榜 \n" +
                "为激励同学们完成Java迭代作业，我们设置了排行榜功能，点击上方排行榜按钮即可查看\n" +
                "排行榜。\n" +
                "登出 \n" +
                "点击右上方登出按钮即可。");
        announcementArea.setOpaque(false);
        announcementArea.setBorder(null);
        announcementArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(announcementArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setBounds(20, 70, 500, 400);
        label.setBounds(-4, 0, 600, 600);
        this.add(scrollPane);
        this.add(label);

        this.add(btn);
        this.add(logout);
        this.setTitle("patpat pro 帮助");
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

        btnmenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu f = new Menu();
                Help.this.dispose();
            }
        });

        btnhomework.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Homework f = new Homework();
                Help.this.dispose();
            }
        });
        btnrank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ranks f = new Ranks();
                Help.this.dispose();
            }
        });
        btnhelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Help f = new Help();
                Help.this.dispose();
            }
        });
        btninformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Information f = new Information();
                Help.this.dispose();
            }
        });
        btnlogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login f = new Login();
                Help.this.dispose();
            }
        });
    }
}
