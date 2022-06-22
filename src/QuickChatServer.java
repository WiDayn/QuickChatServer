import Net.Handler;
import Sql.MysqlConnection;

import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

public class QuickChatServer {
    //SSL协议版本
    private static final String TLS = "TLSv1.2";
    //KeyStore的类型
    private static final String PROVIDER = "SunX509";
    //秘钥类型,java默认是JKS
    private static final String STORE_TYPE = "JKS";
    //秘钥的路径
    private static final String KEY_STORE_NAME = "server_ks.jks";

    private static final String TRUST_STORE_NAME = "clientTrust_ks.jks";
    //Server的端口
    private static final int DEFAULT_PORT = 8080; //自定义端口
    //秘钥的密码

    private static final String SERVER_KEY_STORE_PASSWORD = "server_password"; //秘钥的密码
    private static final String SERVER_TRUST_KEY_STORE_PASSWORD = "server";//密码


    public static void main(String[] args) {
        MysqlConnection.connect();

        SSLServerSocket sslServerSocket = null;
        try{
            //获取SSLContext
            SSLContext sslContext = SSLContext.getInstance(TLS);

            //生成秘钥的manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(PROVIDER);
            //加载信任的证书
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(PROVIDER);
            //加载秘钥
            KeyStore keyStoreOne = KeyStore.getInstance(STORE_TYPE);
            KeyStore keyStoreTwo = KeyStore.getInstance(STORE_TYPE);
            keyStoreOne.load(new FileInputStream(KEY_STORE_NAME), SERVER_KEY_STORE_PASSWORD.toCharArray());
            keyStoreTwo.load(new FileInputStream(TRUST_STORE_NAME), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());
            //秘钥初始化
            keyManagerFactory.init(keyStoreOne, SERVER_KEY_STORE_PASSWORD.toCharArray());
            trustManagerFactory.init(keyStoreTwo);
            //初始化SSLContext
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            //获取SSLContext的SocketFactory
            sslServerSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(DEFAULT_PORT);
            //是否开启双向验证
            sslServerSocket.setNeedClientAuth(true);
            System.out.println("服务器已开启,等待连接 .....");
            while(true){
                Socket socket = sslServerSocket.accept();
                socket.setKeepAlive(true);
                System.out.println("客户端 : " + socket.getInetAddress().getHostAddress());
                new Thread(new Handler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
