package sample;

import client.ClientThread;
import server.ServerThread;

import java.awt.*;
import java.util.Scanner;

/**
 * @author Miyuki
 * createdBy Miyuki 2021/3/23 18:16
 * modifiedBy Miyuki 18:16
 **/

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请选择功能：\n1. 服务端模式（调用您的2333端口）\n2. 客户端模式（需要对方的IP地址和端口）");
        System.out.println("请输入 1 或者 2 选择");
        Integer choice = null;
        try{
            choice = Integer.valueOf(scanner.next());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("请重新输入");
        } while(choice != 1 && choice != 2){
            System.out.println("请选择功能：\n1. 服务端模式（调用您的2333端口）\n2. 客户端模式（需要对方的IP地址和端口）");
            System.out.println("请输入 1 或者 2 选择");
            try{
                choice = Integer.valueOf(scanner.next());
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("请重新输入");
            }
        }
        if(choice == Integer.valueOf(1)){
            ServerThread server = new ServerThread(2333);
            scanner = null;
            server.start();
        } else if (choice == Integer.valueOf(2)){

            String IPAddress = null;
            do {
                System.out.println("请输入对方IP地址");
                IPAddress = scanner.next();
            } while(judgeIPLegal(IPAddress) == false);

            ClientThread clientThread=new ClientThread(IPAddress, 2333);
            scanner = null;
            clientThread.start();
        }
    }
    public static boolean judgeIPLegal(String IPAddress){
        //if(IPAddress.length() > 15)
        //    return false;
        String[] ip = IPAddress.split("\\.");
        if(ip.length != 4 )
            return false;
        for(int i = 0;i < 4; i++){
            int num = Integer.parseInt(ip[i]);
            if(num < 0 || num > 255)
                return false;
        }
        return true;
    }
}
