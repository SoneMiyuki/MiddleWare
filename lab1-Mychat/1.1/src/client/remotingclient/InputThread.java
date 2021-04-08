package client.remotingclient;

import service.service.ChattingService;

import java.rmi.Naming;
import java.util.Scanner;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2021/4/9 0:28
 * modifiedBy Yancheng Lai 0:28
 **/

public class InputThread implements Runnable{
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
}
