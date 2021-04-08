package client.remotingclient;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2021/4/8 17:55
 * modifiedBy Yancheng Lai 17:55
 **/

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import model.Message;
import service.service.*;

public class ClientProgram {
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
}