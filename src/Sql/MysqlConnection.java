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
            // ע�� JDBC ����
            Class.forName(JDBC_DRIVER);
            // ������
            System.out.println("�������ݿ�...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }catch(SQLException se){
            // ���� JDBC ����
            se.printStackTrace();
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
        }
    }
}
