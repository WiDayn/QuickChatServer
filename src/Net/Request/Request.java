package Net.Request;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = -8345207475385392718L;
    private final Timestamp sendTime;
    private final String type;


    public Request(Timestamp sendTime, String type) {
        this.sendTime = sendTime;
        this.type = type;
    }
}
