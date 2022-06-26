import Net.Handler;
import Net.FileHandler;
import Sql.MysqlConnection;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.*;

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
    private static final int DEFAULT_PORT = 25570; // �Զ���˿�
    private static final int FILE_PORT = 25571; // �ļ�����˿�
    //��Կ������

    private static final String SERVER_KEY_STORE_PASSWORD = "server_password"; //��Կ������
    private static final String SERVER_TRUST_KEY_STORE_PASSWORD = "server";//����


    public static void main(String[] args) {
        MysqlConnection.connect();
        DefaultServer defaultServer = new DefaultServer();
        defaultServer.start();
        FileServer fileServer = new FileServer();
        fileServer.start();
    }

    public static class DefaultServer extends Thread{
        @Override
        public void run(){
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

    public static class FileServer extends Thread{
        @Override
        public void run(){
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
                sslServerSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(FILE_PORT);
                //�Ƿ���˫����֤
                sslServerSocket.setNeedClientAuth(true);
                System.out.println("�ļ��������ѿ���,�ȴ����� .....");
                while(true){
                    Socket socket = sslServerSocket.accept();
                    System.out.println("�ͻ��� : " + socket.getInetAddress().getHostAddress());
                    new Thread(new FileHandler(socket)).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
