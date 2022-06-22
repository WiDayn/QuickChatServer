package Sql;

import Chat.Message;
import Utils.StaticConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MysqlUtils {
    public static void AddMessage(Message message){
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`message` (user, timestamp, message)\n" +
                    "VALUES ('"+ message.getUser().getName() +"', '"+ StaticConfig.df.format(message.getTimestamp())+"', '"+message.getMassage()+"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void QueryMessage(Timestamp timestamp){

    }
}
