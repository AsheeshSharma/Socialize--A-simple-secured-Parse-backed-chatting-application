package animelabs.socialise.ConversationModel;

import java.util.Date;

import animelabs.socialise.UserList;

/**
 * Created by Asheesh on 9/29/2015.
 */
public class Conversation {
    public static final int STATUS_SENDING=0;
    public static final int STATUS_SENT=1;
    public static final int STATUS_FAILED=2;
    private String msg;
    private String sender;
    private Date date;

    public Conversation()
    {

    }
    public Conversation(String m_msg,String s_sender,Date d_date)
    {
        msg=m_msg;
        sender=s_sender;
        date=d_date;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public  boolean isSent()
    {
        return UserList.user.getUsername().equals(sender);
    }
    public void setStatus(int status) {
        this.status = status;
    }

    private int status=STATUS_SENT;


}
