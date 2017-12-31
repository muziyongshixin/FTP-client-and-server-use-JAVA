import java.io.IOException;
import java.io.Writer;

public class PortCommand implements Command{

    @Override
    public void getResult(String data, Writer writer,ControllerThread t) {
        String response = "200 the port an ip have been transfered";
        try {

            String[] iAp =  data.split(",");
            String ip = iAp[0];
            String port = Integer.toString(Integer.parseInt(iAp[1]));
            System.out.println("ip is "+ip);
            System.out.println("port is "+port);
            t.setDataIp(ip);
            t.setDataPort(port);
            writer.write(response);
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}  