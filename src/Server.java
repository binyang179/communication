/**
 * @Author Crab
 * 2019-10-20 18:22
 **/

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {
    private JButton b1 = new JButton("发送");
    private JButton b2 = new JButton("清屏");
    // 输入框
    private JTextField text = new JTextField(10);
    // 显示连接状态
    private JTextField info = new JTextField(10);
    // 显示文本框
    private JTextArea area = new JTextArea(10, 25);

    private DataInputStream in = null;
    private DataOutputStream out = null;
    // 是否和客户端已建立连接标识
    private boolean connect = false;

    public static void main(String[] args){
        Server frame = new Server();
        frame.setTitle("QQ-服务器端");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setVisible(true);
        frame.showInfo("QQ服务器开始启动...");

        frame.workCycle(frame);
    }

    public Server() {
        // 为b1按钮添加监听事件，“发送”
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (connect) {
                    String str = text.getText();
                    text.setText(""); // 设置为空

                    Date date = new Date();
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    area.append(format.format(date) + '\n');
                    area.append("我：" + str + '\n');
                    try {
                        out.writeBytes(str + '\n'); // 加上最后的'\n'为了在另一端显示换行
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 为b2按钮添加监听事件，“清屏”
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                area.setText("");
            }
        });

        add(BorderLayout.NORTH, new JScrollPane(area));
        add(BorderLayout.CENTER, text);
        add(BorderLayout.SOUTH, info);
        add(BorderLayout.EAST, b1);
        add(BorderLayout.WEST, b2);
    }

    private void showInfo(String str) {
        info.setText(str);
    }

    public void workCycle(Server frame) {
        try {
            ServerSocket server = new ServerSocket(60000);

            Socket socket = server.accept();

            frame.showInfo("客户端连接成功...");
            connect = true;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            for (;;) { // 始终和客户端连接
				/*
				char c = in.readChar();
				if (c != '\n')
					area.append("" + c); // (String)c 这样转换竟然不行...
				else
					area.append("" + '\n');
				*/
                String str = in.readLine();

                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                area.append(format.format(date) + '\n');
                area.append("client: " + str + '\n');
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
