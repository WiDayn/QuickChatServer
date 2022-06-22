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
    //SSLЭ��汾
    private static final String TLS = "TLSv1.2";
    //KeyStore������
    private static final String PROVIDER = "SunX509";
    //��Կ����,javaĬ����JKS
    private static final String STORE_TYPE = "JKS";
    //��Կ��·��
    private static final String KEY_STORE_NAME = "server_ks.jks";

    private static final String TRUST_STORE_NAME = "clientTrust_ks.jks";
    //Server�Ķ˿�
    private static final int DEFAULT_PORT = 8080; //�Զ���˿�
    //��Կ������

    private static final String SERVER_KEY_STORE_PASSWORD = "server_password"; //��Կ������
    private static final String SERVER_TRUST_KEY_STORE_PASSWORD = "server";//����


    public static void main(String[] args) {
        MysqlConnection.connect();

        SSLServerSocket sslServerSocket = null;
        try{
            //��ȡSSLContext
            SSLContext sslContext = SSLContext.getInstance(TLS);

            //������Կ��manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(PROVIDER);
            //�������ε�֤��
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(PROVIDER);
            //������Կ
            KeyStore keyStoreOne = KeyStore.getInstance(STORE_TYPE);
            KeyStore keyStoreTwo = KeyStore.getInstance(STORE_TYPE);
            keyStoreOne.load(new FileInputStream(KEY_STORE_NAME), SERVER_KEY_STORE_PASSWORD.toCharArray());
            keyStoreTwo.load(new FileInputStream(TRUST_STORE_NAME), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());
            //��Կ��ʼ��
            keyManagerFactory.init(keyStoreOne, SERVER_KEY_STORE_PASSWORD.toCharArray());
            trustManagerFactory.init(keyStoreTwo);
            //��ʼ��SSLContext
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            //��ȡSSLContext��SocketFactory
            sslServerSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(DEFAULT_PORT);
            //�Ƿ���˫����֤
            sslServerSocket.setNeedClientAuth(true);
            System.out.println("�������ѿ���,�ȴ����� .....");
            while(true){
                Socket socket = sslServerSocket.accept();
                socket.setKeepAlive(true);
                System.out.println("�ͻ��� : " + socket.getInetAddress().getHostAddress());
                new Thread(new Handler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
