package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2021/4/8 23:29
 * modifiedBy Yancheng Lai 23:29
 **/

public class MessageList implements Serializable {
    List<Message> message = new ArrayList<>();

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }
}
