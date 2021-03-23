###1 引言：
####1.0 本项目还在不断完善之中，欢迎提出宝贵的建议。
####1.1 实验目的
初步需求如下：用你熟悉的语言，开发一个基于网络（比如Socket）的简单的互操作程序。要求在局域网内实现网络版的QQ，多个终端可以进行文本聊天。
####1.2 技术路线和开发环境
本次任务的技术路线和开发环境：
开发语言：

  - Java(Java 11)

图形化界面内容：

  - JavaFX

开发环境和工具：

  - IntelliJ IDEA Educational
  - JavaFX Scene Builder 2.0

技术路线：建立C/S架构，基于Java-Socket建立TCP连接传递信息
开发模式：原型开发
备注：由于这个项目比较小，出于效率考虑，不会完全按照软件工程的步骤严格执行流程。

###2 初步设计思路
####2.0 总体设计
总体上，在功能的实现中，我们在概念上让一台机器担任服务端，若干台机器担任客户端进行通讯。
![C/S架构示意图](https://upload-images.jianshu.io/upload_images/21786743-99ecced85ce2d486.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这些机器共享一个聊天室。由于不同机器在对话上具有对等性，我们考虑在这个程序内让同一台机器既可以当客户端，也可以当服务端。
换言之，让概念上的客户端和服务端位于同一个程序中。不同计算机的地位区别在于程序使用的时候提前进行多方约定，由某一台计算机担任服务端开放端口，而让其他计算机担任客户端连接该端口。
在本设计中客户端和服务端的区别有：
 - 聊天室起源于服务端开放端口，由客户端进行连接
 - （施工中。。。）

####2.1 客户端设计

客户端主要需要考虑以下功能：

 - 连接到局域网中的IP与端口，加入虚拟聊天室
 - 在聊天室中发言
 - （施工中。。。）

####2.2 服务端设计

服务端主要需要考虑以下功能：

 - 在会话中开放端口，创建虚拟聊天室
 - 在聊天室中发言
 - （施工中。。。）

####2.3 界面设计
（施工中。。。）

### 3 协议设计
这是一个基于TCP的连接，然而我们需要设定一定的协议，使得通讯过程更加顺畅，没有差错。

单方发送小写字母q可以掐断联系

（施工中。。。）

### 4 类设计
设计两个类，一个类是Clinet，另一个类是Server。目前先考虑命令行的基本功能操作。

#### 4.1 服务器相关类
##### 地址
src/Server/
##### 继承：
父类Thread
ServerThread类继承Java中的Thread类，这是为了支持网络进行的多线程设计。
##### 属性：
其中有一个ServerSocket类型的属性和一个Socket类型的属性
> ServerSocket与Socket不同，ServerSocket是等待客户端的请求，一旦获得一个连接请求，就创建一个Socket示例来与客户端进行通信。[参考链接](https://blog.csdn.net/qq_41517936/article/details/81015711)
然后我们需要这个类的构造函数，当然主要指定Port对ServerSocket初始化即可
```
    public ServerThread(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error Occcurs:");
            e.printStackTrace();
        }
    }
```
由于目前我们只使用这个类进行通信，并且为了便于之后对这个类进行测试，所以添加一个函数入口，初始化线程为2333端口。

```java
    public static void main(String[] args) {
        ServerThread server = new ServerThread(2333);
        server.start();
    }
```
在通信的过程中，我们需要一个新的线程来帮助我们发送消息，所以我们建立一个内部类，并且重载run函数帮助我们进行消息发送。主要机理是不断读取缓冲区，如果读取到退出则退出，不然就继续。
```
    class sendMessageThread extends Thread{
        @Override
        public void run(){
            super.run();
            Scanner scanner = null;
            OutputStream out = null;
            try{
                if(socket != null){
                    scanner = new Scanner(System.in);
                    out = socket.getOutputStream();
                    String in = "";
                    do {
                        in = scanner.next();
                        out.write((in).getBytes());
                        out.flush();
                    }while (!in.equals("q"));
                    scanner.close();
                    try{
                        out.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```

[参考链接](https://blog.csdn.net/qq_16949707/article/details/79042455)
#### 4.2 客户端相关类
##### 地址
src/Clinet/
##### 继承：
父类Thread
ClinetThread类继承Java中的Thread类，这是为了支持网络进行的多线程设计。
##### 属性：
其中有一个一个Socket类型的属性以建立连接
我们需要一个构造函数提供IP和地址初始化这一个线程
```java
    public ClientThread(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
Clinet同样需要一个发送消息的内部类
```java
    class sendMessageThread extends Thread{
        @Override
        public void run() {
            super.run();
            Scanner scanner=null;
            OutputStream out= null;
            try {
                scanner=new Scanner(System.in);
                out= socket.getOutputStream();
                String message="";
                do{
                    message=scanner.next();
                    out.write((""+message).getBytes());
                    out.flush();
                }while(!message.contentEquals("q"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
```
继承线程类并重载的run函数大同小异
```java
    @Override
    public void run() {
        new sendMessageThread().start();
        super.run();
        try {
            InputStream s = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            String txt = "";
            boolean exist = false;
            while ((len = s.read(buffer)) != -1) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("Server at " +  df.format(new Date()) + ":");
                System.out.println(new String(buffer, 0, len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```
主方法
```
    public static void main(String[] args) {
        ClientThread clientThread=new ClientThread("127.0.0.1", 2333);
        clientThread.start();
    }
```

#### 4.3 功能选择/协调类
位置src/sample
这里姑且以Main.java命名这个类
其实我们只要做一个功能选择就可以了，然后让客户输入对应的信息进行选择C或者S模式。此处略。

#### 其他
关于多线程：
Java中可以由主线程派生其他线程，也可以由其他线程派生其他线程，线程一般有以下状态：
![线程状态图](https://upload-images.jianshu.io/upload_images/21786743-e0c3770ef42b3ab7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
我们这个例子的状态转换是这样的：线程被构造，并且进入start态，在一般的网络通信中，资源不会很多，会直接进入running态，然后持续。
[参考链接](https://blog.csdn.net/tongxuexie/article/details/80145663)
### 5.测试
编译代码，在根目录输入 java sample/Main
开启两个窗口，按照提示操作
![图片.png](https://upload-images.jianshu.io/upload_images/21786743-256cdbddac9b6303.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看到完成基本功能

### 6. 不足与下一个改进点：
 * 由于buffer调用的特性，空格间隔的消息会被截断成若干条
 * 退出还比较僵硬，可以考虑用个协议对消息进行封装
 * 缺乏连接失败的错误提醒
 * 可以考虑自行指定port的方法
---
项目链接地址：
https://github.com/SoneMiyuki/MiddleWare/lab1-Mychat/1.0