package service.service;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2021/4/8 17:41
 * modifiedBy Yancheng Lai 17:41
 **/

import model.Message;
import model.MessageList;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//继承自 Remote 类，远程对象调用的接口
public interface ChattingService extends Remote {
    public MessageList getBuffer(Integer lastidx) throws RemoteException;

    public void sendMessage(String message) throws RemoteException;
}
