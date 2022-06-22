package Sql;

import Chat.Message;
import Chat.Room;
import Chat.User;
import Utils.StaticConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MysqlUtils {
    public static void AddMessage(String userid, Timestamp timestamp, String message, int roomId) throws SQLException {
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`message` (user, timestamp, message, roomid)\n" +
                    "VALUES ('"+ userid +"', '"+ StaticConfig.df.format(timestamp)+"', '"+ message+"','"+ roomId +"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
    }

    public static List<Message> QueryMessage(Timestamp timestamp, int roomId) throws SQLException {
        Statement stmt = null;
        List<Message> messages = new ArrayList<>();
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.message where roomId = "+ roomId +" and timestamp > '"+ timestamp +"' order by timestamp;";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                messages.add(new Message(rs.getString("user"),
                        rs.getTimestamp("timestamp"),
                        rs.getString("message")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return messages;
    }

    public static User QueryUser(String userid) throws SQLException {
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
        stmt.close();
        return user;
    }

    public static String QueryUserPassword(String userid) throws SQLException {
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
        stmt.close();
        return password;
    }

    public static void AddUser(String userid, String passwd, String nickname) throws SQLException {
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
        stmt.close();
    }

    public static List<Room> QueryRoom(String userid) throws SQLException {
        Statement stmt;
        List<Room> roomList = new ArrayList<>();
        String password = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.user_room where user = '"+ userid +"';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                roomList.add(new Room(rs.getInt("room"),rs.getString("room")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return roomList;
    }

    public static void JoinRoom(String userid, int roomId) throws SQLException {
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`user_room` (user, room)\n" +
                    "VALUES ('"+ userid +"', '"+ roomId +"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
    }

    public static int CreateRoom(String user, String name){
        // 返回roomId
        Statement stmt = null;
        try {
            //先插入一条
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`room` (name)\n" +
                    "VALUES ('"+ name +"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int roomId = -1;
        try {
            // 查询刚刚创建的房间的id
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            // 降序查询出插入的最后一条
            sql = "SELECT * FROM `quickchat`.`room` where name = '"+ name +"' order by id DESC;";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()){
                roomId = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 关联表关联user和roomId
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`user_room` (user, room)\n" +
                    "VALUES ('"+ name +"', '"+ roomId+"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomId;
    }
}
