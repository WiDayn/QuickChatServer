package Sql;

import Chat.Message;
import Chat.PrivateMessage;
import Chat.Room;
import Chat.User;
import Utils.StaticConfig;
import Utils.Utils;

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
                    "VALUES ('"+ userid +"', '"+ timestamp +"', '"+ message+"','"+ roomId +"');";
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
            while(rs.next()){
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
                        rs.getString("avatar"),
                        rs.getTimestamp("lastactive"));
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
                String name = null;
                try {
                    stmt = MysqlConnection.conn.createStatement();
                    sql = "SELECT * FROM quickchat.room where id = '"+ rs.getInt("room") +"';";
                    ResultSet rss = stmt.executeQuery(sql);
                    while (rss.next()){
                        name = rss.getString("name");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                roomList.add(new Room(rs.getInt("room"), name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return roomList;
    }

    public static List<String> QueryRoom(int roomId) throws SQLException {
        Statement stmt;
        List<String> userList = new ArrayList<>();
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.user_room where room = '"+ roomId +"';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                userList.add(rs.getString("user"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return userList;
    }

    public static Room QueryRoomInf(int roomId) throws SQLException {
        Statement stmt;
        Room room = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.room where id = '"+ roomId +"';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                room = new Room(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return room;
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
                    "VALUES ('"+ user +"', '"+ roomId+"');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomId;
    }

    public static void UpdateActive(String userid) throws SQLException {
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "UPDATE `quickchat`.`user` SET lastactive = '"+ Utils.getNowTimestamp() +"' WHERE userid = '"+ userid +"'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
    }

    public static void AddPrivateMessage(String from_user, String to_user, String message, Timestamp timestamp) throws SQLException {
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "INSERT INTO `quickchat`.`private_message` (from_user, to_user, message, timestamp, isread)\n" +
                    "VALUES ('"+ from_user +"', '"+ to_user +"', '"+ message +"', '"+ timestamp +"', 0);";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
    }

    public static List<PrivateMessage> QueryPrivateMessageFrom(String userid) throws SQLException {
        // 返回所有来源是user的私密聊天
        Statement stmt;
        List<PrivateMessage> privateMessageList = new ArrayList<>();
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.private_message where from_user = '"+ userid +"';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                PrivateMessage privateMessage = new PrivateMessage(rs.getTimestamp("timestamp"),
                        rs.getString("from_user"),
                        rs.getString("to_user"),
                        rs.getString("message"));
                privateMessageList.add(privateMessage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return privateMessageList;
    }

    public static List<PrivateMessage> QueryPrivateMessageTo(String userid) throws SQLException {
        // 返回所有来源是user的私密聊天
        Statement stmt;
        List<PrivateMessage> privateMessageList = new ArrayList<>();
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.private_message where to_user = '"+ userid +"';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                PrivateMessage privateMessage = new PrivateMessage(rs.getTimestamp("timestamp"),
                        rs.getString("from_user"),
                        rs.getString("to_user"),
                        rs.getString("message"));
                privateMessageList.add(privateMessage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return privateMessageList;
    }

    public static List<PrivateMessage> QueryPrivateMessageToUnread(String userid) throws SQLException {
        // 返回所有来源是user的未读私密聊天
        Statement stmt;
        List<PrivateMessage> privateMessageList = new ArrayList<>();
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "SELECT * FROM quickchat.private_message where to_user = '"+ userid +"' and isread = 0;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                PrivateMessage privateMessage = new PrivateMessage(rs.getTimestamp("timestamp"),
                        rs.getString("from_user"),
                        rs.getString("to_user"),
                        rs.getString("message"));
                privateMessageList.add(privateMessage);
                // 标记为已读
                UpdatePrivateMessageUnRead(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
        return privateMessageList;
    }

    public static void UpdatePrivateMessageUnRead(int id) throws SQLException{
        Statement stmt = null;
        try {
            stmt = MysqlConnection.conn.createStatement();
            String sql;
            sql = "UPDATE `quickchat`.`private_message` SET isread = 1 WHERE id = '"+ id +"'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.close();
    }
}
