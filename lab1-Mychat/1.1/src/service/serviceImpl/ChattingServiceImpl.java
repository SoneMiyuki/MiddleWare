package service.serviceImpl;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2021/4/8 18:04
 * modifiedBy Yancheng Lai 18:04
 **/

import model.Message;
import model.MessageList;
import service.service.ChattingService;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;

//继承自 UnicastRemoteObject，为远程对象的实现类
public class ChattingServiceImpl extends UnicastRemoteObject implements ChattingService {
    public ChattingServiceImpl() throws RemoteException {
        super();
    }

    public List<Message> buffer = new ArrayList<Message>();

    public Integer index = 0;

    public void EditBuffer(String text) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message();
        InetAddress ia = null;
        try {
            ia = ia.getLocalHost();
            msg.setIP(ia.getHostAddress());
        } catch (Exception e) {
            msg.setIP("Unknown IP");
        }
        msg.setText(text);
        msg.setDate(df.format(new Date()));
        msg.setIndex(index);
        index++;
        System.out.println("index = "+ String.valueOf(index));
        buffer.add(msg);
    }

    @Override
    public MessageList getBuffer(Integer lastidx) throws RemoteException {
        //System.out.println(lastidx);
        MessageList messageList = new MessageList();
        if(lastidx >= buffer.size()){
            messageList.setMessage(new ArrayList<>());
        } else {
            List<Message> mq = new ArrayList<>();
            for(int i = lastidx + 1;i < buffer.size();i++){
                mq.add(buffer.get(i));
            }
            messageList.setMessage(mq);
        }
        return messageList;
    }

    @Override
    public void sendMessage(String message) throws RemoteException{
        EditBuffer(message);
    }
}