import java.awt.EventQueue;

import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.net.ftp.FTPFile;

import java.awt.ScrollPane;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.awt.Scrollbar;

public class Frame_Main implements ActionListener{


    //初始化参数--------------------------------
    static FTPFile[] file;
    static String FTP="127.0.0.1";
    static String username="liyz";
    static String password="000000";
    //初始化参数--------------------------------


    private JFrame frame;
    private JTable table;
    static Ftp_by_me_active ftp;
    public static Ftp_by_me_active getFtp() {
        return ftp;
    }
    public static FTPFile[] getFile(){
        return file;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Frame_Main window = new Frame_Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Create the application.
     */
    public Frame_Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Frame_Main.class.getResource("/com/sun/java/swing/plaf/windows/icons/UpFolder.gif")));
        frame.setTitle("FTP Client");
        frame.setBounds(100, 100, 470, 534);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);


        //显示基本信息(FTP username)-----------------------------------------------
        JLabel lblNewLabel = new JLabel("FTP IP地址");
        lblNewLabel.setBounds(32, 8, 70, 15);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("用户名");
        lblNewLabel_1.setBounds(32, 25, 70, 15);
        frame.getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("密码");
        lblNewLabel_2.setBounds(32, 40, 70, 15);
        frame.getContentPane().add(lblNewLabel_2);

        JTextField url = new JTextField("127.0.0.1");   //FTP服务地址
        url.setBounds(110,8,82,15);
        frame.getContentPane().add(url);

        JTextField usernameField = new JTextField("admin"); //用户名
        usernameField.setBounds(110,25,82,15);
        frame.getContentPane().add(usernameField);

        JPasswordField passwordField = new JPasswordField("000000");  //密码
        passwordField.setBounds(110,40,82,15);
        frame.getContentPane().add(passwordField);



        //登录按钮------------------------------------------------
        JButton login=new JButton("登录");
        login.setFont(new Font("宋体", Font.PLAIN, 12));
        login.setBackground(UIManager.getColor("Button.highlight"));
        login.setBounds(210, 15, 82, 23);
        frame.getContentPane().add(login);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("登录==============");
                try {
                    FTP=url.getText().trim();
                    username=usernameField.getText().trim();
                    password=passwordField.getText().trim();

                    ftp=new Ftp_by_me_active(FTP,username,password);
                    if(Ftp_by_me_active.isLogined)
                    {
                        file=ftp.getAllFile();
                        setTableInfo();//显示所有文件信息

                        url.setEditable(false);
                        usernameField.setEditable(false);
                        passwordField.setEditable(false);
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showConfirmDialog(null, "用户名或者密码错误\n username："+username, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        //上传按钮--------------------------------------------------
        JButton upload = new JButton("上传");
        upload.setFont(new Font("宋体", Font.PLAIN, 12));
        upload.setBackground(UIManager.getColor("Button.highlight"));
        upload.setBounds(312, 45, 82, 23);
        frame.getContentPane().add(upload);
        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //上传点击按钮触发------------------------------------
                System.out.println("上传！！！！！");
                int result = 0;
                File file = null;
                String path = null;
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                System.out.println(fsv.getHomeDirectory());                //得到桌面路径  
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("请选择要上传的文件...");
                fileChooser.setApproveButtonText("确定");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                result = fileChooser.showOpenDialog(null);
                if (JFileChooser.APPROVE_OPTION == result) {
                    path=fileChooser.getSelectedFile().getPath();
                    System.out.println("path: "+path);
                    try {
                        //下载
                        ftp.upload(path);
                        System.out.println("文件上传成功");
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                //上传点击按钮触发------------------------------------
            }
        });

        //上传按钮--------------------------------------------------



        //刷新按钮--------------------------------------------------
        JButton refresh = new JButton("刷新");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try{
                    file=ftp.getAllFile();
                    setTableInfo();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        refresh.setFont(new Font("宋体", Font.PLAIN, 12));
        refresh.setBackground(UIManager.getColor("Button.highlight"));
        refresh.setBounds(312, 15, 82, 23);
        frame.getContentPane().add(refresh);
        //刷新按钮--------------------------------------------------


    }

    //显示基本信息-----------------------------------------------
    private void setTableInfo()
    {
        //table数据初始化  从FTP读取所有文件
        String[][] data1=new String[file.length][4];
        for(int row=0;row<file.length;row++)
        {

            data1[row][0]=file[row].getName();
            if(file[row].isDirectory())
            {
                data1[row][1]="文件夹";
            }
            else if(file[row].isFile()){
                String[] geshi=file[row].getName().split("\\.");
                data1[row][1]=geshi[1];
            }
            data1[row][2]=file[row].getSize()+"";
            data1[row][3]="下载";
        }



        //table列名-----------------------------------------------------
        String[] columnNames = {"文件", "文件类型", "文件大小(B)", ""  };
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(data1, columnNames);

        //加滚动条--------------------------------------------------------
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(32, 73, 400, 384);
        frame.getContentPane().add(scrollPane);
        //加滚动条-----------------------------------------------------

        //table功能------------------------------------------------------
        table = new JTable(model);
        scrollPane.setViewportView(table);
        table.setColumnSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.setToolTipText("可以点击下载");

        //table button初始化(最后一列的按键)--------------------
        ButtonColumn buttonsColumn = new ButtonColumn(table, 3);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }
}