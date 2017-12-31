import java.io.IOException;
import java.io.Writer;

public class PassCommand implements Command{

    @Override
    public void getResult(String data, Writer writer,ControllerThread t) {

        System.out.println("execute the pass command");
        System.out.println("the data is "+data);
        //获得用户名  
        String key = ControllerThread.USER.get();
        String pass = Share.users.get(key);

        String response = null;
        if(pass.equals(data)) {
            System.out.println("登录成功");
            Share.loginedUser.add(key);
            t.setIsLogin(true);
            response = "230 User "+key+" logged in";
        }
        else {
            System.out.println("登录失败，密码错误");
            response = "530   密码错误";
        }
        try {
            writer.write(response);
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }

    }

}  