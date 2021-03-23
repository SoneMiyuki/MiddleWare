package client;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
/**
 * @author Miyuki
 * createdBy Miyuki 2021/3/15 21:13
 * modifiedBy Miyuki 21:13
 **/


public class ClientThread extends Thread  {

    Socket socket = null;
    //构造函数
    public ClientThread(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //主函数
    public static void main(String[] args) {
        ClientThread clientThread=new ClientThread("127.0.0.1", 2333);
        clientThread.start();
    }
    //继承线程类的重载run函数
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
}
