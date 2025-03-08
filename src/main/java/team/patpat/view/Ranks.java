package team.patpat.view;

import team.patpat.database.GetTime;
import team.patpat.database.Rank;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Ranks 类是排行榜窗口的实现。
 * 该窗口包含主页、作业、排行榜、个人信息、帮助、登出按钮以及排行榜展示区域。
 * 使用示例:
 * <pre>{@code
 * Ranks ranksWindow = new Ranks();
 * }</pre>
 *
 * @author 安帅君
 * @author 王赟涵
 * @version 1.0
 * @since 2023-12-17
 */
public class Ranks extends JFrame {
    public Ranks() {
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
            img = ImageIO.read(new File("picture/rank.png"));
        } catch (IOException e) {
            System.out.println("图片加载失败");
        }
        Image image1 = img.getScaledInstance(590, 580, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(image1));

        label.setBounds(-10, -10, 600, 600);
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("");
        tableModel.addColumn("");
        tableModel.addColumn("");
        tableModel.addColumn("");
        tableModel.addColumn("");
        tableModel.addColumn("");
        tableModel.addColumn("");
        GetTime g = new GetTime();
        ArrayList<Rank> ranks = g.getTime();
        int place = 1;
        for (Rank rank : ranks) {
            if (place == 11) break;
            String places = Integer.toString(place);
            tableModel.addRow(new Object[]{places, rank.getName(), rank.getIterationOne(), rank.getIterationTwo(), rank.getIterationThree(), rank.getIterationFour(), rank.getSum()});
            place++;
        }
        JTable table = new JTable(tableModel);
        table.setEnabled(false);
        table.setRowHeight(42);  // 设置行高
        table.setIntercellSpacing(new Dimension(15, 10));
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer(Color.WHITE));
        table.setShowGrid(false);
        table.setOpaque(false);
        table.setBackground(new Color(0, 0, 0, 0));  // 设置背景色为透明
        table.setBounds(5, 143, 600, 500);
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
        this.add(table);
        this.add(label);

        this.add(btn);
        this.add(logout);
        this.setTitle("patpat pro 排行榜");
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
                Ranks.this.dispose();
            }
        });

        btnhomework.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Homework f = new Homework();
                Ranks.this.dispose();
            }
        });
        btnrank.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ranks f = new Ranks();
                Ranks.this.dispose();
            }
        });
        btnhelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Help f = new Help();
                Ranks.this.dispose();
            }
        });
        btninformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Information f = new Information();
                Ranks.this.dispose();
            }
        });
        btnlogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login f = new Login();
                Ranks.this.dispose();
            }
        });
    }

    static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        Color foregroundColor;

        public CustomTableCellRenderer(Color foregroundColor) {
            this.foregroundColor = foregroundColor;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(new Color(0, 0, 0, 0));  // 设置背景色为透明
            c.setFont(c.getFont().deriveFont(Font.BOLD, 15));
            c.setForeground(foregroundColor);
            return c;
        }
    }
}

