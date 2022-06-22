package Net;

import Chat.Message;
import Chat.User;
import Net.Feedback.LoginFeedback;
import Net.Feedback.PullMessageFeedback;
import Net.Feedback.RegisterFeedback;
import Net.Request.*;
import Sql.MysqlUtils;
import Utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Handler implements Runnable{
    private final InputStream inputStream;
    private final OutputStream outputStream;
    public Handler(Socket connectionSocket) throws IOException {
        inputStream = connectionSocket.getInputStream();
        outputStream = connectionSocket.getOutputStream();
        ReceiveObj receiveMsg = new ReceiveObj();
        receiveMsg.start();
    }

    public class ReceiveObj extends Thread{
        @Override
        public synchronized void run(){
            while(true){
                try {
                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(inputStream));
                    Object obj = ois.readObject();
                    if(obj instanceof PullMessageRequest){
                        PullMessageFeedback pullMessageFeedback = new PullMessageFeedback(Utils.getNowTimestamp());
                        SendObj(pullMessageFeedback);
                    }
                    if(obj instanceof SendMessageRequest sendMessageRequest){
                        MysqlUtils.AddMessage(sendMessageRequest.getMessage());
                    }
                    if(obj instanceof LoginRequest loginRequest){
                        User user = MysqlUtils.QueryUser(loginRequest.getUser());
                        if(user == null){
                            LoginFeedback loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), "Login", 401, "�û�������", null);
                            SendObj(loginFeedback);
                            continue;
                        }
                        String password = MysqlUtils.QueryUserPassword(loginRequest.getUser());
                        LoginFeedback loginFeedback;
                        if(password.equals(Utils.getShaPassword(loginRequest.getPassword(), loginRequest.getUser()))){
                            loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), "Login", 200, "��¼�ɹ�", user);
                        } else {
                            loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), "Login", 401, "�������", null);
                        }
                        SendObj(loginFeedback);
                    }
                    if(obj instanceof RegisterRequest registerRequest){
                        User user = MysqlUtils.QueryUser(registerRequest.getUserid());
                        if(user != null){
                            RegisterFeedback registerFeedback = new RegisterFeedback(Utils.getNowTimestamp(), "Register", 401, "�û����Ѵ���");
                            SendObj(registerFeedback);
                            continue;
                        }
                        MysqlUtils.AddUser(registerRequest.getUserid(), Utils.getShaPassword(registerRequest.getPassword(), registerRequest.getUserid()), registerRequest.getNickname());
                        RegisterFeedback registerFeedback = new RegisterFeedback(Utils.getNowTimestamp(), "Register", 200, "ע��ɹ�");
                        SendObj(registerFeedback);
                    }
                } catch (SocketException e) {
                    System.out.println("�ͻ��˶Ͽ�����:"+ e.getMessage());
                } catch (Exception e){
                    e.getStackTrace();
                }
            }
        }
    }

    public void SendObj(Object object) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(object);
        oos.flush();
    }

    @Override
    public void run() {

    }
}