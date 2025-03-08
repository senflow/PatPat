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
 * Login 类是登录窗口的实现。
 * 该窗口包含用户名、密码输入框，以及登录、注册、助教登录、修改密码按钮。
 * 使用示例:
 * <pre>{@code
 * Login loginWindow = new Login();
 * }</pre>
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class Login extends JFrame {
    static String userid;
    static boolean islogin = false;

    public Login() {
        JPanel panels = new JPanel(new GridLayout(5, 1));
        this.setLayout(null); //设置为表格布局
        JLabel lblLogin = new JLabel("登  录", JLabel.CENTER); //创建登录标签
        JLabel lblUser = new JLabel("用户名:"); //创建用户名标签
        JTextField txtUser = new JTextField(10); //创建用户名文本框
        JPanel pnlName = new JPanel(); //创建面板,用于放置用户名标签与文本框
        pnlName.add(lblUser); //将用户名标签添加到面板

        JLabel lblPwd = new JLabel("密 码:"); //创建密码标签
        JPasswordField txtPwd = new JPasswordField(10); //创建密码框
        txtPwd.setEchoChar('*'); //设置密码框的掩码
        JPanel pnlPwd = new JPanel(); //创建面板,用于放置密码标签和密码框
        pnlPwd.add(lblPwd); //将标签添加到面板
        this.add(txtUser);
        this.add(txtPwd);
        txtUser.setBounds(120, 86, 105, 20);
        txtPwd.setBounds(120, 162, 105, 20);
        Border empty = BorderFactory.createEmptyBorder();
        txtPwd.setBorder(empty);
        txtUser.setBorder(empty);
        txtPwd.setOpaque(false);
        txtUser.setOpaque(false);
        JButton btnLogin = new JButton("登     录"); //创建登录按钮
        btnLogin.setPreferredSize(new Dimension(150, 50));
        Font font = new Font("A", Font.BOLD, 20);
        btnLogin.setFont(font);
        JButton btnRegister = new JButton("注册"); //创建注册按钮
        JButton assistantLogin = new JButton("助教登录");
        JButton changePwd = new JButton("修改密码");
        JPanel pnlBtn = new JPanel(); //创建面板,用于放置登录与注册按钮
        JPanel pnlLogin = new JPanel();
        pnlLogin.add(btnLogin); //将登录按钮添加到面板
        pnlBtn.add(btnRegister); //将注册按钮添加到面板
        pnlBtn.add(assistantLogin);
        pnlBtn.add(changePwd);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/login.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));
        this.add(label);
        label.setBounds(-2, 0, 300, 400);
        panels.setBounds(0, 0, 300, 400);
        this.add(panels);
        panels.add(lblLogin); //将登录标签添加到主窗体
        panels.add(pnlName); //将用户面板添加到主窗体
        panels.add(pnlPwd); //将密码面板添加到主窗体
        panels.add(pnlLogin);
        panels.add(pnlBtn); //将按钮面板添加到主窗体
        panels.setOpaque(false);
        lblLogin.setOpaque(false);
        pnlName.setOpaque(false);
        pnlPwd.setOpaque(false);
        pnlLogin.setOpaque(false);
        pnlBtn.setOpaque(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(false);
        btnRegister.setOpaque(false);
        btnRegister.setBorderPainted(false);
        assistantLogin.setBorderPainted(false);
        assistantLogin.setOpaque(false);
        changePwd.setOpaque(false);
        changePwd.setBorderPainted(false);
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
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                String password = String.valueOf(txtPwd.getPassword());
                team.patpat.database.Login log = new team.patpat.database.Login();
                int flag = log.login(username, password);
//                ImageIcon icon1 = new ImageIcon("picture/icon.png");
//                Image images = icon1.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
//                ImageIcon icon2 = new ImageIcon(images);
                if (flag == 2) {
                    JOptionPane.showMessageDialog(Login.this, "密码错误");
                } else if (flag == 1) {
                    Menu f = new Menu();
                    Login.this.dispose();
                    userid = username;
                    islogin = true;
                } else if (flag == 3) {
                    JOptionPane.showMessageDialog(Login.this, "用户名不存在或未注册");
                } else {
                    JOptionPane.showMessageDialog(Login.this, "未知错误");
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register f = new Register();
                Login.this.dispose();
            }
        });

        assistantLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                AssistantLogin f = new AssistantLogin();
                Login.this.dispose();
            }
        });

        changePwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePasswordBefore f = new ChangePasswordBefore();
                Login.this.dispose();
            }
        });

    }

}


