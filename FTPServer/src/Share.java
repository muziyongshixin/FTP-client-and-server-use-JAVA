import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 所有线程共享的变量 
 * */
public class Share {

    /*根目录的路径*/
    public static  String rootDir = new File("").getAbsolutePath();//获取当前文件的绝对路径

    /*允许登录的用户*/
    public static Map<String,String> users = new HashMap<String,String>();

    /*已经登录的用户*/
    public static HashSet<String> loginedUser = new HashSet<String>();

    /*拥有权限的用户*/
    public static HashSet<String> adminUsers = new HashSet<String>();

    //初始化根目录，权限用户，能够登录的用户信息  
    public static void init(){
        String path = System.getProperty("user.dir") + "/config/server.xml";

        File file = new File(path);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document parse = builder.build(file);
            Element root = parse.getRootElement();

            //配置服务器的默认目录  
            rootDir = root.getChildText("rootDir");
            System.out.print("rootDir is:");
            System.out.println(rootDir);

            //允许登录的用户  
            Element usersE = root.getChild("users");
            List<Element> usersEC = usersE.getChildren();
            String username = null;
            String password = null;
            System.out.println("\n所有用户的信息：");
            for(Element user : usersEC) {
                username = user.getChildText("username");
                password = user.getChildText("password");
                System.out.println("用户名："+username);
                System.out.println("密码："+password);
                users.put(username,password);
            }  
              
            /* 
            //拥有put权限和delete权限的用户 
            System.out.println("\n管理员用户："); 
            Element adminUsersE = root.getChild("adminUsers"); 
            for(Element adminUserTemp: (List<Element>)adminUsersE.getChildren()) { 
                username = adminUserTemp.getText(); 
                //System.out.println("用户名："+username); 
                adminUsers.add(username); 
            }   */
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}  