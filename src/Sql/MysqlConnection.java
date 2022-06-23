package Sql;

import java.sql.*;

public class MysqlConnection {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/quickchat?useSSL=false&allowPublicKeyRetrieval=true&useTimezone=false&serverTimezone=Asia/Shanghai";

    static final String USER = "root";
    static final String PASS = "TqOMaR6zrMQUm3BG";

    static Connection conn = null;
    public static void connect(){
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }
}
