import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class DirCommand implements Command{

    /**
     * 获取ftp目录里面的文件列表 
     * */
    @Override
    public void getResult(String data, Writer writer,ControllerThread t) {
        System.out.println("execute LIST command........");
        String desDir = t.getNowDir()+data;
        //desDir=Share.rootDir;
        System.out.println(desDir);
        File dir = new File(desDir);
        if(!dir.exists()) {
            try {
                writer.write("210  文件目录不存在\r\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            StringBuilder dirs = new StringBuilder();
            System.out.println("文件目录如下：");
            dirs.append("文件目录如下:\n");

            Vector<String> allfiles=new Vector<>();
            String[] lists= dir.list();
            String flag = null;
            for(String name : lists) {
                File temp = new File(desDir+File.separator+name);
                if(temp.isDirectory()) {
                    flag = "d";
                }
                else {
                    flag = "f";
                }
                String oneinfo=flag+"rw-rw-rw-   1 ftp      ftp            "+temp.length()+" Dec 30 17:07 "+name;
                System.out.println(oneinfo);
                allfiles.add(oneinfo);
            }

            //开启数据连接，将数据发送给客户端，这里需要有端口号和ip地址  

            try {
                writer.write("150 Opening data connection for directory list...\r\n");
                writer.flush();

                for(String oneinfo : allfiles)
                {
                    writer.write(oneinfo);
                    writer.write("\r\n");
                    writer.flush();
                }

                writer.write("end of files\r\n");
                writer.flush();

                writer.write("226 transfer complete...\r\n");
                writer.flush();
            } catch (NumberFormatException e) {

                e.printStackTrace();
            } catch (UnknownHostException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

}  