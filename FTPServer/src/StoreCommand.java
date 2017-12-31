import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.Socket;



public class StoreCommand implements Command{

    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {
        System.out.println("execute Store command @@@StoreCommand");
        try{
            writer.write("150 Binary data connection\r\n");
            writer.flush();
            RandomAccessFile inFile = new
                    RandomAccessFile(t.getNowDir()+"/"+data,"rw");
            //数据连接
            Socket tempSocket = new Socket(t.getDataIp(),Integer.parseInt(t.getDataPort()));
            InputStream inSocket
                    = tempSocket.getInputStream();
            byte byteBuffer[] = new byte[1024];
            int amount;
            //这里又会阻塞掉，无法从客户端输出流里面获取数据？是因为客户端没有发送数据么
            while((amount =inSocket.read(byteBuffer) )!= -1){
                inFile.write(byteBuffer, 0, amount);
            }
            System.out.println("传输完成，关闭连接。。。");
            inFile.close();
            inSocket.close();
            tempSocket.close();
            //断开数据连接

            writer.write("226 transfer complete\r\n");
            writer.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }



    }
}  