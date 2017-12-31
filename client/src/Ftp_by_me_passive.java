import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class Ftp_by_me_passive {

    private BufferedReader controlReader;
    private PrintWriter controlOut;
    private FileInputStream is;

    private static String host;

    private String passHost="127.0.0.1";
    private int passPort=21;

    private String ftpusername;
    private String ftppassword;

    private boolean isPassMode = false;

    private static final int PORT = 21;


    public Ftp_by_me_passive(String url, String username, String password) {

        try {
            Socket socket = new Socket(url, PORT);

            setUsername(username);
            setPassword(password);

            controlReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            controlOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);


            initftp();  //登录到ftp服务器
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void initftp() throws Exception {
        String msg;
        do {
            msg = controlReader.readLine();
            System.out.println(msg);
        } while (!msg.startsWith("220 "));

        controlOut.println("USER " + ftpusername);

        String response = controlReader.readLine();
        System.out.println(response);

        if (!response.startsWith("331 ")) {
            throw new IOException(
                    "SimpleFTP received an unknown response after sending the user: "
                            + response);
        }

        controlOut.println("PASS " + ftppassword);

        response = controlReader.readLine();
        System.out.println(response);
        if (!response.startsWith("230 ")) {
            throw new IOException(
                    "SimpleFTP was unable to log in with the supplied password: "
                            + response);
        }
    }

    private void setUsername(String username) {
        this.ftpusername = username;
    }

    private void setPassword(String password) {
        this.ftppassword = password;
    }

    //获取所有文件和文件夹的名字
    public FTPFile[] getAllFile() throws Exception {
        String response;
        // Go to passive mode
        checkIsPassiveMode();

        // Send LIST command
        controlOut.println("LIST");

        // Open data connection
        Socket dataSocket = new Socket(passHost, passPort);

        // Read command response
        response = controlReader.readLine();
        System.out.println(response);


        // Read data from server
        Vector<FTPFile> tempfiles = new Vector<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            FTPFile temp = new FTPFile();
            setFtpFileInfo(temp, line);
            tempfiles.add(temp);
        }


        reader.close();
        dataSocket.close();
        // Read command response
        response = controlReader.readLine();
        System.out.println(response);

        FTPFile[] files = new FTPFile[tempfiles.size()];
        tempfiles.copyInto(files);//将vector数据存到数组里

        return files;

    }

    //通过字符串解析构造一个FTPfile对象
    private void setFtpFileInfo(FTPFile in, String info) {
        String infos[] = info.split(" ");
        Vector<String> vinfos = new Vector<>();
        for (int i = 0; i < infos.length; i++) {
            if (!infos[i].equals(""))
                vinfos.add(infos[i]);
        }
        in.setName(vinfos.get(8));
        in.setSize(Integer.parseInt(vinfos.get(4)));
        String type=info.substring(0,1);
        if(type.equals("d"))
        {
            in.setType(1);//设置为文件夹
        }else
        {
            in.setType(0);//设置为文件
        }

    }


    //生成InputStream用于上传本地文件
    public void upload(String File_path) throws Exception {
        System.out.print("File Path :" + File_path);

        File f = new File(File_path);
        if (!f.exists()) {
            System.out.println("File not Exists...");
            return;
        }

        is = new FileInputStream(f);
        BufferedInputStream input = new BufferedInputStream(is);
        String response;

        //判断是不是在passive模式
        checkIsPassiveMode();

        // Send command STOR
        controlOut.println("STOR " + f.getName());

        // Open data connection

        Socket dataSocket = new Socket(passHost, passPort);

        // Read command response
        response = controlReader.readLine();
        System.out.println(response);

        // Read data from server
        BufferedOutputStream output = new BufferedOutputStream(
                dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        output.flush();
        input.close();
        output.close();
        dataSocket.close();


        response = controlReader.readLine();
        System.out.println(response);

    }

    //将服务器设置为passive模式
    private void checkIsPassiveMode() throws Exception {
        String response;
        if (!isPassMode) {
            controlOut.println("PASV mode");
            response = controlReader.readLine();
            System.out.println(response);
            if (!response.startsWith("2271 ")) {
                throw new IOException("FTPClient could not request passive mode: " + response);
            }

            int opening = response.indexOf('(');
            int closing = response.indexOf(')', opening + 1);
            if (closing > 0) {
                String dataLink = response.substring(opening + 1, closing);
                StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
                try {
                    passHost = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                            + tokenizer.nextToken() + "." + tokenizer.nextToken();
                    passPort = Integer.parseInt(tokenizer.nextToken()) * 256
                            + Integer.parseInt(tokenizer.nextToken());
                } catch (Exception e) {
                    throw new IOException(
                            "FTPClient received bad data link information: "
                                    + response);
                }
            }
//            isPassMode = true;
        }
    }


    //下载 from_file_name是下载的文件名,to_path是下载到的路径地址
    public void download(String from_file_name, String to_path) throws Exception {
        // Go to passive mode
        checkIsPassiveMode();

        // Send RETR command
        controlOut.println("RETR " + from_file_name);
        // Open data connection

        Socket dataSocket = new Socket(passHost, passPort);


        // Read data from server
        BufferedOutputStream output = new BufferedOutputStream(
                new FileOutputStream(new File(to_path, from_file_name)));
        BufferedInputStream input = new BufferedInputStream(
                dataSocket.getInputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        output.flush();
        output.close();
        input.close();
        dataSocket.close();

        String response;
        response = controlReader.readLine();
        System.out.println(response);

        response = controlReader.readLine();
        System.out.println(response);
    }

}