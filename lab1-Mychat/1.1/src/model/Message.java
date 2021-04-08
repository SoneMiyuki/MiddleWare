package model;

/**
 * @author Yancheng Lai
 * createdBy Yancheng Lai 2021/4/8 17:45
 * modifiedBy Yancheng Lai 17:45
 **/

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Message implements Serializable{

    String IP;

    String date;

    String text;

    Integer index;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(IP, message.IP) &&
                Objects.equals(date, message.date) &&
                Objects.equals(text, message.text) &&
                Objects.equals(index, message.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IP, date, text, index);
    }

    @Override
    public String toString() {
        String s = getIP() + "     " + getDate() + "\n" + getText();
        return s;
    }

    public Message(){}

    public Message(String IP,String date,String text,Integer index){
        setIP(IP);setDate(date);setText(text);setIndex(index);
    }
}
