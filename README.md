# FTP client and server （use  java socket）
**Authors:** muziyongshixin

**dependence**: intellij IDEA，jdk1.8，

- 本项目是使用java Socket编程实现的一个简单的FTP服务器和客户端。
- 客户端目前实现的功能是登录，刷新，上传和下载。同时具有主动模式和被动模式两种模式。
- 服务器端实现的功能有登录、刷新、上传、下载、列出文件列表、更换目录等

## 界面展示：
![主界面](https://github.com/muziyongshixin/FTP-client-and-server-use-JAVA/blob/master/pic/client.png?raw=true "主界面")  
![文件选择界面](https://github.com/muziyongshixin/FTP-client-and-server-use-JAVA/blob/master/pic/dir_choose.png?raw=true"文件选择界面")  
- ---
## 使用介绍：

### client：


- client文件夹里有三个文件，分别是是使用apache ftp实现的文件传输（ftp_by_apache.java），和用java socket实现的被动模式下的数据传输（ftp_by_me_passive.java），和java socket实现的主动模式下的数据传输（ftp_by_me_active.java）


- 如果你使用的是标准的ftp server，例如Home Ftp Server文件夹里的服务器端，你可以使用ftp_by_apache或者ftp_by_me_passive来实例化Frame_Main.java里的ftp成员变量。例如：

```
   ftp=new Ftp_by_me_passive(FTP,username,password);

```
或者：

```
   ftp=new Ftp_by_apache(FTP,username,password);

```

- 如果你使用的是本项目中的ftp server，那么建议你使用ftp_by_me_active.java来初始化ftp成员变量，因为本项目中的ftp server暂时还没有实现被动模式。即你应该这样初始化：

```
   ftp=new Ftp_by_me_active(FTP,username,password);

```
其他具体细节可以直接查看代码，注释都比较清晰。


### server：
本项目中实现的ftp server，通过读取config文件夹下的server.xml文件来初始化。server.xml文件里纪录的是server的根目录，以及用户的信息。你需要配置一个自己电脑上有效的文件夹地址。

配置格式如下：

```  
<config>
    <rootDir>C:\Users\32706\Desktop\FTP\FTPServer</rootDir>
    <users>
        <user>
            <username>admin</username>
            <password>000000</password>
        </user>
        <user>
            <username>admin1</username>
            <password>111111</password>
        </user>

    </users>

</config>

```

1. 服务端程序通过serversocket监控客户端的连接请求，并实例化一个controllerThread对象来处理这个请求的所有消息。
2. 具体的消息处理是利用工厂模式来实例化一个具体的命令处理对象，得到客户端传递过来的消息后根据命令实例化不同的处理对象，从而实现对不同命令的处理。
3. 你可以根据自己的需求实现不同的命令和个性化的命令的处理。
  

-- -------

# 本项目主要是为了学习socket编程和FTP传输协议，同时使用了比较好的设计模式，从而能够有较好的的扩展性。但是其并不适合作为真正的FTP传输软件使用。如果本项目对你有帮助，请不要吝啬你的star ：）





