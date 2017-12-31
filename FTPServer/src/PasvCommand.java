import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//设置为被动模式
public class PasvCommand implements Command {
    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {
        System.out.println("execute the PASV command......");
        String response = "";
        try {
            int tempport = -1;
            ServerSocket serverSocket = null;
            while (serverSocket == null) {
                tempport = (int) (Math.random() * 100000) % 9999 + 1024;
                serverSocket = getDataServerSocket(tempport);
            }
            if (tempport != -1 && serverSocket != null) {
                int p1 = tempport / 256;
                int p2 = tempport - p1 * 256;
                response = "2271 Entering Passive Mode (127,0,0,1," + p1 + "," + p2 + ")";
                System.out.println(response);
            }

            writer.write(response);
            writer.write("\r\n");
            writer.flush();
            System.out.println("set PASV successful");

            //数据传输的socket
            Socket datasocket = null;

            datasocket = serverSocket.accept();
            ControllerThread thread = new ControllerThread(datasocket,"data");
            thread.start();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //创建一个新的datasocket
    public static ServerSocket getDataServerSocket(int port) {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
        return socket;
    }


}
