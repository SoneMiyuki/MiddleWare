package server;

/**
 * @author Miyuki
 * createdBy Miyuki 2021/3/15 21:12
 * modifiedBy Miyuki 21:12
 **/

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class ServerThread extends Thread{
    //建立Server Socket
    ServerSocket server = null;
    //建立Socket
    Socket socket = null;

    public static void main(String[] args) {
        ServerThread server = new ServerThread(2333);
        server.start();
    }

    public ServerThread(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error Occcurs:");
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        super.run();
        try{
            System.out.println("Waiting For Connection");
            socket = server.accept();
            //发送消息进程
            new sendMessageThread().start();
            System.out.println("Get Connected By IP:" + socket.getInetAddress()
                + "At Port: " + socket.getPort());
            InputStream in = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer))!=-1) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                System.out.println("Client at " + df.format(new Date()) + ":");
                System.out.println(new String(buffer, 0, len));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    class sendMessageThread extends Thread{
        @Override
        public void run(){
            super.run();
            Scanner scanner=null;
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


}
