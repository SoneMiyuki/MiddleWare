package service.remotingservice;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2021/4/8 18:16
 * modifiedBy Yancheng Lai 18:16
 **/
import service.service.ChattingService;
import service.serviceImpl.ChattingServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
public class ServerProgram{

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
}