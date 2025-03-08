//package team.patpat.view;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import team.patpat.database.ChangePassword;
//
//public class ChangePasswordAfter extends JFrame{
//    public ChangePasswordAfter(){
//        this.setLayout(new GridLayout(5, 1));
//        JLabel lblname = new JLabel("修改密码", JLabel.CENTER); //创建登录标签
////        JLabel lblUser = new JLabel("用户名:"); //创建用户名标签
////        JTextField txtUser = new JTextField(10); //创建用户名文本框
////        JPanel pnlName = new JPanel(); //创建面板,用于放置用户名标签与文本框
////        pnlName.add(lblUser); //将用户名标签添加到面板
////        pnlName.add(txtUser); //将用户名文本框添加到面板
//
//        JLabel lblPwd = new JLabel("旧密码:"); //创建密码标签
//        JPasswordField txtPwd = new JPasswordField(10); //创建密码框
//        txtPwd.setEchoChar('*'); //设置密码框的掩码
//        JPanel pnlPwd = new JPanel(); //创建面板,用于放置密码标签和密码框
//        pnlPwd.add(lblPwd); //将标签添加到面板
//        pnlPwd.add(txtPwd); //将密码框添加到面板
//
//        JLabel lblPwdnew =new JLabel("新密码:");
//        JPasswordField txtPwdnew =new JPasswordField(10);
//        txtPwdnew.setEchoChar('*'); //设置密码框的掩码
//        JPanel pnlPwdnew = new JPanel(); //创建面板,用于放置密码标签和密码框
//        pnlPwdnew.add(lblPwdnew); //将标签添加到面板
//        pnlPwdnew.add(txtPwdnew); //将密码框添加到面板
//
//        JLabel lblPwdnewagain =new JLabel("确认密码:");
//        JPasswordField txtPwdnewagain =new JPasswordField(10);
//        txtPwdnewagain.setEchoChar('*'); //设置密码框的掩码
//        JPanel pnlPwdnewagain = new JPanel(); //创建面板,用于放置密码标签和密码框
//        pnlPwdnewagain.add(lblPwdnewagain); //将标签添加到面板
//        pnlPwdnewagain.add(txtPwdnewagain); //将密码框添加到面板
//
//        JButton confirm =new JButton("确认");
//        JButton back =new JButton("返回");
//        JPanel pnlBtn = new JPanel(); //创建面板,用于放置登录与注册按钮
//        pnlBtn.add(confirm);
//        pnlBtn.add(back);
//
//        this.add(lblname); //将登录标签添加到主窗体
//        //this.add(pnlName); //将用户面板添加到主窗体
//        this.add(pnlPwd); //将密码面板添加到主窗体
//        this.add(pnlPwdnew);
//        this.add(pnlPwdnewagain);
//        this.add(pnlBtn);
//        //this.add(back);
//        this.setTitle("patpat pro");
//        this.setBounds(600, 200, 300, 400);
//        this.setResizable(false);
//        this.setVisible(true);
//        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        confirm.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username= Login.userid;
//                String pwd=String.valueOf(txtPwd.getPassword());
//                String pwdnew=String.valueOf(txtPwdnew.getPassword());
//                String pwdnewagain=String.valueOf(txtPwdnewagain.getPassword());
//                ChangePassword f=new ChangePassword();
//                //-1:未知错误，请查看日志文件
//                //1:修改密码成功
//                //2:密码错误
//                //3:学号不存在或未注册
//                //4:两次输入的密码不一致
//                int flag=f.changePassword(username,pwd,pwdnew,pwdnewagain);
//                if(flag==4){
//                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "两次输入的密码不一致");
//                }else if(flag==2){
//                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "旧密码错误");
//                }else if(flag==3){
//                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "用户名不存在或未注册");
//                }else if(flag==1){
//                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "修改成功");
//                }else if(flag==5){
//                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "新密码格式错误");
//                }else{
//                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "未知错误");
//                }
//            }
//        });
//
//
//        back.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                Information f=new Information();
//                ChangePasswordAfter.this.dispose();
//            }
//        });
//
//    }
//}
package team.patpat.view;

import team.patpat.database.ChangePassword;

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
 * ChangePasswordAfter 类是修改密码的窗口实现。
 * 该窗口包含用户名、旧密码、新密码和确认密码输入框，以及确认和返回按钮。
 * 使用示例:
 * <pre>{@code
 * ChangePasswordAfter changePasswordAfter = new ChangePasswordAfter();
 * }</pre>
 * <p>
 * 该类依赖了以下类：
 * {@link team.patpat.database.ChangePassword}
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */

public class ChangePasswordAfter extends JFrame {
    public ChangePasswordAfter() {
        JPanel panels = new JPanel(new GridLayout(6, 1));
        this.setLayout(null);
        JLabel lblname = new JLabel("修改密码", JLabel.CENTER); //创建登录标签
        JLabel lblUser = new JLabel("用户名:"); //创建用户名标签
        JTextField txtUser = new JTextField(10); //创建用户名文本框
        JPanel pnlName = new JPanel(); //创建面板,用于放置用户名标签与文本框
        pnlName.add(lblUser); //将用户名标签添加到面板


        JLabel lblPwd = new JLabel("旧密码:"); //创建密码标签
        JPasswordField txtPwd = new JPasswordField(10); //创建密码框
        txtPwd.setEchoChar('*'); //设置密码框的掩码
        JPanel pnlPwd = new JPanel(); //创建面板,用于放置密码标签和密码框
        pnlPwd.add(lblPwd); //将标签添加到面板

        JLabel lblPwdnew = new JLabel("新密码:");
        JPasswordField txtPwdnew = new JPasswordField(10);
        txtPwdnew.setEchoChar('*'); //设置密码框的掩码
        JPanel pnlPwdnew = new JPanel(); //创建面板,用于放置密码标签和密码框
        pnlPwdnew.add(lblPwdnew); //将标签添加到面板

        JLabel lblPwdnewagain = new JLabel("确认密码:");
        JPasswordField txtPwdnewagain = new JPasswordField(10);
        txtPwdnewagain.setEchoChar('*'); //设置密码框的掩码
        JPanel pnlPwdnewagain = new JPanel(); //创建面板,用于放置密码标签和密码框
        pnlPwdnewagain.add(lblPwdnewagain); //将标签添加到面板

        this.add(txtUser);
        this.add(txtPwd);
        this.add(txtPwdnew);
        this.add(txtPwdnewagain);
        txtUser.setBounds(122, 67, 105, 20);
        txtPwd.setBounds(122, 128, 105, 20);
        txtPwdnew.setBounds(122, 191, 105, 20);
        txtPwdnewagain.setBounds(122, 258, 105, 20);
        Border empty = BorderFactory.createEmptyBorder();
        txtPwd.setBorder(empty);
        txtUser.setBorder(empty);
        txtPwdnew.setBorder(empty);
        txtPwdnewagain.setBorder(empty);
        txtPwd.setOpaque(false);
        txtUser.setOpaque(false);
        txtPwdnew.setOpaque(false);
        txtPwdnewagain.setOpaque(false);

        JButton confirm = new JButton("确认");
        JButton back = new JButton("返回");
        JPanel pnlBtn = new JPanel(); //创建面板,用于放置登录与注册按钮
        pnlBtn.add(confirm);
        pnlBtn.add(back);

        panels.add(lblname); //将登录标签添加到主窗体
        panels.add(pnlName); //将用户面板添加到主窗体
        panels.add(pnlPwd); //将密码面板添加到主窗体
        panels.add(pnlPwdnew);
        panels.add(pnlPwdnewagain);
        panels.add(pnlBtn);
        panels.setBounds(0, 0, 294, 380);
        pnlBtn.setOpaque(false);
        confirm.setOpaque(false);
        confirm.setBorderPainted(false);
        back.setOpaque(false);
        back.setBorderPainted(false);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("picture/change.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(300, 380, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));
        this.add(label);
        label.setBounds(-2, 0, 300, 380);

        this.add(panels);
        panels.setOpaque(false);
        //this.add(back);
        this.setTitle("patpat pro");
        this.setBounds(600, 200, 300, 400);
        this.setResizable(false);
        this.setVisible(true);

        BufferedImage icon = null;
        try {
            icon = ImageIO.read(new File("picture/icon.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image icons = icon;
        this.setIconImage(icons);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                String pwd = String.valueOf(txtPwd.getPassword());
                String pwdnew = String.valueOf(txtPwdnew.getPassword());
                String pwdnewagain = String.valueOf(txtPwdnewagain.getPassword());
                ChangePassword f = new ChangePassword();
                //-1:未知错误，请查看日志文件
                //1:修改密码成功
                //2:密码错误
                //3:学号不存在或未注册
                //4:两次输入的密码不一致
                int flag = f.changePassword(username, pwd, pwdnew, pwdnewagain);
                if (flag == 4) {
                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "两次输入的密码不一致");
                } else if (flag == 2) {
                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "旧密码错误");
                } else if (flag == 3) {
                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "用户名不存在或未注册");
                } else if (flag == 1) {
                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "修改成功");
                } else if (flag == 5) {
                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "新密码格式错误");
                } else {
                    JOptionPane.showMessageDialog(ChangePasswordAfter.this, "未知错误");
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Information f = new Information();
                ChangePasswordAfter.this.dispose();
            }
        });

    }
}
