package Sql;

import Chat.Message;
import Chat.Room;
import Chat.User;
import Utils.StaticConfig;
import Utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MysqlUtils {
    public static void AddMessage(Message message){
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`message` (user, timestamp, message)\n" +
                    "VALUES ('"+ message.getUser().getUserid() +"', '"+ StaticConfig.df.format(message.getTimestamp())+"', '"+message.getMassage()+"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void QueryMessage(Timestamp timestamp){

    }

    public static User QueryUser(String userid){
        Statement stmt = null;
        User user = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.user where userid = '"+ userid +"';";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                user = new User(rs.getInt("id"),
                        rs.getString("userid"),
                        rs.getString("nickname"),
                        rs.getString("avatar"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static String QueryUserPassword(String userid){
        Statement stmt = null;
        String password = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT passwd FROM quickchat.user where userid = '"+ userid +"';";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                password = rs.getString("passwd");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return password;
    }

    public static void AddUser(String userid, String passwd, String nickname){
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`user` (userid, passwd, nickname)\n" +
                    "VALUES ('"+ userid +"', '"+ passwd +"', '"+ nickname +"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Room> QueryRoom(String userid){
        Statement stmt;
        List<Room> roomList = new ArrayList<>();
        String password = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.user_room where user = '"+ userid +"';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                roomList.add(new Room(rs.getString("room")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomList;
    }
}
