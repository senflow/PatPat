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
 * AssistantLogin 类是助教登录窗口的实现。
 * 该窗口包含用户名、密码输入框以及登录、返回按钮。
 * 使用示例:
 * <pre>{@code
 * AssistantLogin assistantLogin = new AssistantLogin();
 * }</pre>
 * <p>
 * 该类继承自 JFrame 类，并实现了助教登录的相关功能。
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class AssistantLogin extends JFrame {
    public AssistantLogin() {
        JPanel panels = new JPanel();
        panels.setLayout(new GridLayout(4, 1));
        this.setLayout(null); //设置为表格布局
        JLabel lblLogin = new JLabel("助教登录", JLabel.CENTER); //创建登录标签
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
        txtUser.setBounds(120, 106, 105, 20);
        txtPwd.setBounds(118, 204, 105, 20);
        Border empty = BorderFactory.createEmptyBorder();
        txtPwd.setBorder(empty);
        txtUser.setBorder(empty);
        txtPwd.setOpaque(false);
        txtUser.setOpaque(false);
        JButton btnLogin = new JButton("登录"); //创建登录按钮
        JButton btnBack = new JButton("返回"); //创建返回按钮

        JPanel pnlBtn = new JPanel(); //创建面板,用于放置登录与注册按钮
        pnlBtn.add(btnLogin); //将登录按钮添加到面板
        pnlBtn.add(btnBack); //将注册按钮添加到面板

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/assistantlogin.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));
        this.add(label);
        label.setBounds(-2, 0, 300, 400);

        this.add(panels);
        panels.setBounds(0, 0, 300, 400);
        panels.add(lblLogin); //将登录标签添加到主窗体
        panels.add(pnlName); //将用户面板添加到主窗体
        panels.add(pnlPwd); //将密码面板添加到主窗体
        panels.add(pnlBtn); //将按钮面板添加到主窗体
        panels.setOpaque(false);
        pnlBtn.setOpaque(false);
        btnLogin.setOpaque(false);
        btnLogin.setBorderPainted(false);
        btnBack.setOpaque(false);
        btnBack.setBorderPainted(false);
        this.setTitle("patpat pro");
        this.setBounds(600, 200, 300, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

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
                team.patpat.database.AssistantLogin f = new team.patpat.database.AssistantLogin();
                int flag = f.assistantLogin(username, password);
                //-1:未知错误，请查看日志文件
                //1:登录成功
                //2:密码错误
                //3:学号不存在或未注册
                //此处为判断逻辑
                if (flag == 3) {
                    JOptionPane.showMessageDialog(AssistantLogin.this, "用户名不存在或未注册");
                } else if (flag == 1) {
                    //JOptionPane.showMessageDialog(assitantlogin.this, "登录成功");
                    AssistantMenu t = new AssistantMenu();
                    AssistantLogin.this.dispose();
                } else if (flag == 2) {
                    JOptionPane.showMessageDialog(AssistantLogin.this, "密码错误");
                } else {
                    JOptionPane.showMessageDialog(AssistantLogin.this, "未知错误");
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login f = new Login();
                AssistantLogin.this.dispose();

            }
        });
    }
}
