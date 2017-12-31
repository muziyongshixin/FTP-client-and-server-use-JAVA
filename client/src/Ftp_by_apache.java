
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.net.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


public class Ftp_by_apache {


    FTPClient f=null;
    //默认构造函数
    public Ftp_by_apache(String url,String username,String password)
    {
        f=new FTPClient();
        //得到连接
        this.get_connection(url,username,password);

    }


    //连接服务器方法
    public void get_connection(String url,String username,String password){

        try {
            //连接指定服务器，默认端口为21
            f.connect(url);
            System.out.println("connect success!");

            //设置链接编码，windows主机UTF-8会乱码，需要使用GBK或gb2312编码
            f.setControlEncoding("GBK");

            //登录
            boolean login=f.login(username, password);
            if(login)
                System.out.println("登录成功!");
            else
                System.out.println("登录失败！");

        }
        catch (IOException e) {

            e.printStackTrace();
        }


    }

    public void close_connection() {

        boolean logout=false;
        try {
            logout = f.logout();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (logout) {
            System.out.println("注销成功!");
        } else {
            System.out.println("注销失败!");
        }

        if(f.isConnected())
            try {
                System.out.println("关闭连接！");
                f.disconnect();
            } catch (IOException e) {

                e.printStackTrace();
            }

    }


    //获取所有文件和文件夹的名字
    public FTPFile[] getAllFile(){


        FTPFile[] files = null;
        try {
            files = f.listFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for(FTPFile file:files)
        {

//            if(file.isDirectory())
//                System.out.println(file.getName()+"是文件夹");
//            if(file.isFile())
//                System.out.println(file.getName()+"是文件");
        }
        return files;

    }


    //生成InputStream用于上传本地文件
    public void upload(String File_path) throws IOException{

        InputStream input=null;
        String[] File_name = null;
        try {
            input = new FileInputStream(File_path);
            File_name=File_path.split("\\\\");
            System.out.println(File_name[File_name.length-1]);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //上传文件
        System.out.println(File_name[File_name.length-1]);
        f.storeFile(File_name[File_name.length-1], input);
        System.out.println("上传成功!");

        if(input!=null)
            input.close();



    }


    //下载 from_file_name是下载的文件名,to_path是下载到的路径地址
    public void download(String from_file_name,String to_path) throws IOException{



        OutputStream output=null;
        try {
            output = new FileOutputStream(to_path+from_file_name);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        f.retrieveFile(from_file_name, output);
        if(output!=null)
        {
            try {
                if(output!=null)
                    output.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }







}