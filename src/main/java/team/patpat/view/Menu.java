package team.patpat.view;

import team.patpat.database.GetNotice;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Menu 类是主页窗口的实现。
 * 该窗口包含主页、作业、 排行榜、个人信息、帮助、登出按钮以及公告信息展示区域。
 * 使用示例:
 * <pre>{@code
 * Menu menuWindow = new Menu();
 * }</pre>
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class Menu extends JFrame {
    public Menu() {
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
        btn.setBounds(-20, 0, 400, 100);
        logout.setBounds(510, 0, 80, 100);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/menu.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));
        label.setBounds(-4, 0, 600, 600);

        JTextArea announcementArea = new JTextArea();

        announcementArea.setOpaque(false);
        announcementArea.setBorder(null);
        GetNotice g = new GetNotice();
        announcementArea.setText(g.getNotice());
        announcementArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(announcementArea);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(50, 50, 400, 300);
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

        this.add(scrollPane);
        this.add(label);
        this.add(btn);
        this.add(logout);
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

        btnmenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Menu f = new Menu();
                Menu.this.dispose();
            }
        });

        btnhomework.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Homework f = new Homework();
                Menu.this.dispose();
            }
        });
        btnrank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ranks f = new Ranks();
                Menu.this.dispose();
            }
        });
        btnhelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Help f = new Help();
                Menu.this.dispose();
            }
        });
        btninformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Information f = new Information();
                Menu.this.dispose();
            }
        });
        btnlogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                team.patpat.view.Login f = new Login();
                Menu.this.dispose();
            }
        });
    }
}
