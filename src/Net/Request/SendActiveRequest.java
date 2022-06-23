package Net.Request;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

public class SendActiveRequest extends Request implements Serializable {
    // ȷ������

    @Serial
    private static final long serialVersionUID = -2562308820351255101L;

    private String userid;

    public SendActiveRequest(Timestamp sendTime, String type, String userid) {
        super(sendTime, type);
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }
}
