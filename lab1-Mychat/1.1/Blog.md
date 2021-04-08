### 1 引言：
#### 1.0 本项目还在不断完善之中，欢迎提出宝贵的建议。
#### 1.1 实验目的
初步需求如下：用你熟悉的语言，开发一个基于远程调用（比如Java RMI）的简单的互操作程序。
要求在局域网内实现网络版的QQ，多个终端可以进行文本聊天。
#### 1.2 技术路线和开发环境
本次任务的技术路线和开发环境：
开发语言：

  - Java(Java 11)

图形化界面内容：

  - JavaFX

开发环境和工具：

  - IntelliJ IDEA Educational
  - JavaFX Scene Builder 2.0

技术路线：建立C/S架构，基于RMI进行远程传输信息<br>开发模式：原型开发<br>备注：由于这个项目比较小，出于效率考虑，不会完全按照软件工程的步骤严格执行流程。
### 1.3 JavaRMI简介
Java RMI 扩展了 Java 的对象模型，以便为 Java 语言中的分布式对象提供支持。它允许对象用相同的调用语法调用远程对象上的方法。而且，类型检查也等效的应用到本地调用和远程调用，远程调用的对象知道它的目标对象是远程的，因为它必须处理 RemoteException;并且远程对象的实现者也知道它是远程的，因为它必须实现 Remote 接口。下面给出一个远程方法调用的例子，在该例子中，允许用户调用查找函数来获得满足条件的学生信息。服务器为用户提供一个操作，通过客户端将用户的输入条件告诉服务器，服务器段端按条件查找足条件的学生并返回给客户端。

#### 1.3.1 定义一个远程接口 
定义的远程接口，也是远程对象调用的接口，远程接口通过扩展一个在 java.rmi 包中提供的称为 Remote 的接口来定义，该接口中的方法必须抛出RemoteException 异常。该接口拥有主要定义函数的参数和返回值

#### 1.3.2 服务端对接口的服务进行实现
服务端需要实现远程的接口所声称的服务，服务端就在此远程接口的实现类中。该类将实现生命的方法

#### 1.3.3 创建一个服务器
服务器的主要功能是绑定，注册，并且为客户端调用服务

#### 1.3.4 创建一个客户端
创建一个客户程序执行 RMI 调用。任何客户程序都需要从使用绑定程序远程对象引用开始。在客户程序中，通过 lookup 操作为远程对象查找一个远程对象引用。在获取了一个初始的远程对象引用后，调用远程方法。

### 2 初步设计思路

#### 2.0 总体设计
总体上，在功能的实现中，我们在概念上让一台机器担任服务端，若干台机器担任客户端进行通讯。
![C/S架构示意图](https://upload-images.jianshu.io/upload_images/21786743-99ecced85ce2d486.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这些机器共享一个聊天室。在1.1版本的设计中，C端与S端不再对等，我们需要一台机器充当S端，若干机器充当C端。

 - 聊天室起源于服务端开放端口，由客户端进行连接，并且进行文本传输
 - （施工中。。。）

#### 2.1 客户端设计

客户端主要需要考虑以下功能：

 - 连接到局域网中的特定IP与端口，加入虚拟聊天室
 - 在聊天室中发言
 - 客户端可以获取之前的发言

#### 2.2 服务端设计

服务端主要需要考虑以下功能：

 - 在会话中开放端口，创建虚拟聊天室
 - 信息队列的维护和保存
 - （施工中。。。）

#### 2.3 界面设计
（施工中。。。）

### 3 协议设计
（施工中。。。）

### 4 类设计


#### 4.1 服务器相关类
#### 4.1.1 服务接口
##### 地址：
`src/service/ChattingService`
##### 继承：
 - `extends java.rmi.Remote`
##### 属性方法：
我们的服务提供两个方法：
1. `getBuffer(Integer lastidx)`:<br>
服务器维护着整个聊天室发出消息的队列。通过这个函数可以获取上一次获得消息之后的所有消息
2. `sendMessage(String message)`:<br>
接受客户端传来的消息并且并入消息队列中
#### 4.1.2 服务实现类
##### 地址：
`src/serviceImpl/ChattingServiceImpl`
##### 继承：
 - `extends java.rmi.server.UnicastRemoteObject`
 - `implements service.service.ChattingService`
##### 属性方法：
1. 维护的是消息队列`List<Message> buffer`和队列长度`index`
2. 实现`void EditBuffer(String text)`方法往队列后面添加消息
3. 实现`getBuffer(Integer lastidx)`方法让客户端向服务端查询上次查询之后的内容
4. `sendMessage(String message)`方法本身调用`getBuffer`方法
#### 4.1.3 服务端
##### 地址：
`src/remotingservice/ServerProgram`
##### 核心代码
```java
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            ChattingService chattingService=new ChattingServiceImpl();
            //注册服务的端口
            LocateRegistry.createRegistry(2333);
            //绑定本地地址和服务的路径
            Naming.rebind("rmi://127.0.0.1:2333/SearchService", chattingService);
            System.out.println("开始服务!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```
#### 4.2 客户端相关类
#### 4.2.1 客户端
##### 地址：
/src/client/remotingclient/ClientProgram
##### 思路：
主要需要把服务和相关端口绑定，然后发信息用新线程处理，并且定时（1s）对服务端进行访问拿新的信息。
##### 核心代码
```java
public static void main(String[] args){
        try{
            //调用远程对象，RMI 路径与接口必须与服务器配置一致
            ChattingService chattingService=(ChattingService)
                    Naming.lookup("rmi://127.0.0.1:2333/SearchService");
            Timer timer = new Timer();
            Scanner scanner = new Scanner(System.in);
            Integer idx = -1;
            InputThread inputThread = new InputThread();
            Thread t1 = new Thread(inputThread);
            t1.start();
            timer.schedule(new TimerTask() {
                Integer lastidx = idx;
                public void run() {
                    try {
                        //System.out.println(lastidx);
                        List<Message> newRet = chattingService.getBuffer(lastidx).getMessage();
                        if(newRet.size() == 0){

                        } else if(newRet.size() != 0 && newRet.get(0).getIndex() > lastidx){
                            for (Message m: newRet) {
                                System.out.println(m);
                            }
                            lastidx = newRet.get(newRet.size() - 1).getIndex();
                        } else {
                        }
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            },0, 1000);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
```

#### 4.2.2 线程
##### 地址：
src/client/remotingclient/InputThread
##### 继承：
 - implements Runnable
##### 核心代码：
```java
Scanner scanner = new Scanner(System.in);
    public void run(){
        try{
            ChattingService chattingService=(ChattingService)
                    Naming.lookup("rmi://127.0.0.1:2333/SearchService");
            while(scanner.hasNext()){
                String s = scanner.nextLine();
                if(s != null && s != ""){
                    chattingService.sendMessage(s);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
```

#### 4.3 定义的实体
#### 4.3.1 Message
##### 地址：
src/model/Message
##### 属性：
```java
    String IP;
    String date;
    String text;
    Integer index;
```
 - 重写了构造函数，equals和hashCode
 - 由于需要传输当然 implements Serializable
#### 4.3.2 MessageList
##### 地址：
src/model/MessageList
##### 属性：
```java
    List<Message> message = new ArrayList<>();
```
 - 由于需要传输当然 implements Serializable

#### 其他
关于多线程：
Java中可以由主线程派生其他线程，也可以由其他线程派生其他线程，线程一般有以下状态：
![线程状态图](https://upload-images.jianshu.io/upload_images/21786743-e0c3770ef42b3ab7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
我们这个例子的状态转换是这样的：线程被构造，并且进入start态，在一般的网络通信中，资源不会很多，会直接进入running态，然后持续。
[参考链接](https://blog.csdn.net/tongxuexie/article/details/80145663)
### 5.测试
1. 进入/out/production/lab1-Mychat
2. 用java指令运行服务端`java service.remotingservice ServerProgram`
3.用java指令运行一个客户端`java client.remotingclient.ClientProgram`，输入一句话按回车键画面上出现IP，时间，文本信息
4. 跑一个新的客户端，进入聊天室之后该客户端立刻出现历史信息
5. 在新的客户端输入一句话按回车键画面，在两个客户端均上出现IP，时间，文本信息
可以看到完成基本功能

### 6. 不足与下一个改进点：
 * 还没办法显示客户端IP，这需要设计协议进行封装
 * 图形界面
 
### 7. 踩过的坑
1. 由于sublist是创造了一个原list的映像，那么显然它不是一个可序列化对象，当需要用它进行传输的时候，最好还是把该部分重新用一个List进行逐项拷贝。

2. timer.schedule本质上是利用内部类所以参数只进不出，但是好像是引用传参，所以不会重复得到历史信息
---
项目链接地址：
https://github.com/SoneMiyuki/MiddleWare/tree/main/lab1-Mychat/1.1