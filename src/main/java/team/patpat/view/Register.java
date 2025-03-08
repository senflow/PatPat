package team.patpat.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Register 类是注册窗口的实现。
 * 该窗口包含用户名、密码、确认密码等输入框，以及注册和返回按钮。
 * 使用示例:
 * <pre>{@code
 * Register registerWindow = new Register();
 * }</pre>
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class Register extends JFrame {
    public Register() {
        JPanel panels = new JPanel();
        panels.setLayout(new GridLayout(7, 1));
        this.setLayout(null); //设置为表格布局
        JLabel lblregister = new JLabel("注册", JLabel.CENTER); //创建登录标签
        JLabel lblUser = new JLabel("用户名:"); //创建用户名标签
        JLabel tipsuser = new JLabel("请输入学号");
        Font font = new Font("A", Font.ITALIC, 10);
        tipsuser.setFont(font);
        JTextField txtUser = new JTextField(10); //创建用户名文本框
        JPanel pnlName = new JPanel(); //创建面板,用于放置用户名标签与文本框
        JPanel pnltipsuser = new JPanel();
        pnlName.add(lblUser); //将用户名标签添加到面板
        pnltipsuser.add(tipsuser);
        JLabel lblPwd = new JLabel("密 码:"); //创建密码标签
        JPasswordField txtPwd = new JPasswordField(10); //创建密码框
        JLabel tipspwd = new JLabel("密码8-16位，必须含有数字，大小写字母的至少一种");
        tipspwd.setFont(font);
        txtPwd.setEchoChar('*'); //设置密码框的掩码
        JLabel lblPwdagain = new JLabel("确认密码:");
        JPasswordField txtPwdagain = new JPasswordField(10);
        txtPwdagain.setEchoChar('*');
        JPanel pnlPwd = new JPanel(); //创建面板,用于放置密码标签和密码框
        JPanel pnlPwdagain = new JPanel();
        JPanel pnlPwdtips = new JPanel();
        pnlPwd.add(lblPwd); //将标签添加到面板
        pnlPwdtips.add(tipspwd);
        pnlPwdagain.add(lblPwdagain);
        JButton btnRegister = new JButton("注册"); //创建注册按钮
        JButton btnBack = new JButton("返回");
        JPanel pnlBtn = new JPanel(); //创建面板,用于放置登录与注册按钮
        pnlBtn.add(btnRegister); //将注册按钮添加到面板
        pnlBtn.add(btnBack);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/register.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(298, 370, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));
        label.setBounds(-2, 0, 298, 370);
        panels.setOpaque(false);
        panels.setBounds(0, 0, 298, 370);
        panels.add(lblregister); //将登录标签添加到主窗体
        panels.add(pnlName); //将用户面板添加到主窗体
        panels.add(pnltipsuser);
        panels.add(pnlPwd); //将密码面板添加到主窗体
        panels.add(pnlPwdtips);
        panels.add(pnlPwdagain);
        panels.add(pnlBtn); //将按钮面板添加到主窗体
        pnlBtn.setOpaque(false);
        btnBack.setOpaque(false);
        btnBack.setBorderPainted(false);
        btnRegister.setOpaque(false);
        btnRegister.setBorderPainted(false);
        this.add(txtUser);
        this.add(txtPwd);
        this.add(txtPwdagain);
        txtUser.setBounds(122, 59, 105, 20);
        txtPwd.setBounds(122, 158, 105, 20);
        txtPwdagain.setBounds(122, 265, 105, 20);
        Border empty = BorderFactory.createEmptyBorder();
        txtPwd.setBorder(empty);
        txtUser.setBorder(empty);
        txtPwdagain.setBorder(empty);
        txtPwd.setOpaque(false);
        txtUser.setOpaque(false);
        txtPwdagain.setOpaque(false);
        this.add(label);
        this.add(panels);
        this.setTitle("patpat pro");
        this.setBounds(600, 200, 300, 400);
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

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                String pwd = String.valueOf(txtPwd.getPassword());
                String pwdagain = String.valueOf(txtPwdagain.getPassword());
                team.patpat.database.Register reg = new team.patpat.database.Register();
                int flag = reg.register(username, pwd, pwdagain);
                if (flag == 3) {
                    JOptionPane.showMessageDialog(Register.this, "学号不存在或不正确");
                } else if (flag == 5) {
                    JOptionPane.showMessageDialog(Register.this, "两次密码输入不一致");
                } else if (flag == 4) {
                    JOptionPane.showMessageDialog(Register.this, "密码格式不正确");
                } else if (flag == 2) {
                    JOptionPane.showMessageDialog(Register.this, "用户名已注册");
                } else if (flag == 1) {
                    JOptionPane.showMessageDialog(Register.this, "注册成功");
                } else {
                    JOptionPane.showMessageDialog(Register.this, "未知错误");
                }
            }
        });
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login f = new Login();
                Register.this.dispose();
            }
        });
    }
}
