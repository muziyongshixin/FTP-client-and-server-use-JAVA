import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * 处理文件的发送
 * */
public class RetrCommand implements Command{

    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {
        Socket s;
        String desDir = t.getNowDir()+File.separator+data;
        File file = new File(desDir);
        System.out.println(desDir);
        if(file.exists())
        {
            try {
                writer.write("150 open ascii mode...\r\n");
                writer.flush();
                s = new Socket(t.getDataIp(), Integer.parseInt(t.getDataPort()));
                BufferedOutputStream dataOut = new BufferedOutputStream(s.getOutputStream());
                byte[] buf = new byte[1024];
                InputStream is = new FileInputStream(file);
                while(-1 != is.read(buf)) {
                    dataOut.write(buf);
                }
                dataOut.flush();
                s.close();
                writer.write("220 transfer complete...\r\n");
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                writer.write("220  该文件不存在\r\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
