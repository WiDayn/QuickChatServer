package Net.Request;

import Chat.Room;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;

public class PullMessageRequest extends Request{
    private static final long serialVersionUID = -5474785724122187430L;
    private Timestamp beginTime;
    private Room room;
    public PullMessageRequest(Timestamp sendTime){
        super(sendTime, "PullMsg");
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public Room getRoom() {
        return room;
    }
}
