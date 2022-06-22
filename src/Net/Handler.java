package Net;

import Chat.Message;
import Chat.Room;
import Chat.User;
import Net.Feedback.LoginFeedback;
import Net.Feedback.PullMessageFeedback;
import Net.Feedback.QueryRoomFeedback;
import Net.Feedback.RegisterFeedback;
import Net.Request.*;
import Sql.MysqlUtils;
import Utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

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
                    if(obj instanceof PullMessageRequest pullMessageRequest){
                        List<Message> messageList = MysqlUtils.QueryMessage(pullMessageRequest.getBeginTime(), pullMessageRequest.getRoom().getId());
                        PullMessageFeedback pullMessageFeedback = new PullMessageFeedback(Utils.getNowTimestamp(), pullMessageRequest.getRoom().getId());
                        for(Message message : messageList){
                            pullMessageFeedback.addMessage(message);
                        }
                        SendObj(pullMessageFeedback);
                    }
                    if(obj instanceof SendMessageRequest sendMessageRequest){
                        MysqlUtils.AddMessage(sendMessageRequest.getMessage().getUserid(),
                                sendMessageRequest.getMessage().getTimestamp(),
                                sendMessageRequest.getMessage().getMassage(),
                                sendMessageRequest.getRoom().getId());
                    }
                    if(obj instanceof LoginRequest loginRequest){
                        // ����û��Ƿ����
                        User user = MysqlUtils.QueryUser(loginRequest.getUser());
                        if(user == null){
                            LoginFeedback loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), "Login", 401, "�û�������", null);
                            SendObj(loginFeedback);
                            continue;
                        }
                        // �������
                        String password = MysqlUtils.QueryUserPassword(loginRequest.getUser());
                        LoginFeedback loginFeedback;
                        if(!password.equals(Utils.getShaPassword(loginRequest.getPassword(), loginRequest.getUser()))){
                            loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), "Login", 401, "�������", null);
                            SendObj(loginFeedback);
                            continue;
                        }
                        // ��ѯ�û����ڵķ���
                        user.setRoomList(MysqlUtils.QueryRoom(user.getUserid()));
                        loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), "Login", 200, "��¼�ɹ�", user);
                        SendObj(loginFeedback);
                    }
                    if(obj instanceof RegisterRequest registerRequest){
                        // ����û��Ƿ����
                        User user = MysqlUtils.QueryUser(registerRequest.getUserid());
                        if(user != null){
                            // ����ֱ�ӷ���
                            RegisterFeedback registerFeedback = new RegisterFeedback(Utils.getNowTimestamp(), "Register", 401, "�û����Ѵ���");
                            SendObj(registerFeedback);
                            continue;
                        }
                        // ��ӵ����ݿ�
                        MysqlUtils.AddUser(registerRequest.getUserid(), Utils.getShaPassword(registerRequest.getPassword(), registerRequest.getUserid()), registerRequest.getNickname());
                        RegisterFeedback registerFeedback = new RegisterFeedback(Utils.getNowTimestamp(), "Register", 200, "ע��ɹ�");
                        SendObj(registerFeedback);
                    }
                    if(obj instanceof QueryRoomRequest queryRoomRequest){
                        List<Room> roomList = MysqlUtils.QueryRoom(queryRoomRequest.getUserId());
                        QueryRoomFeedback queryRoomFeedback = new QueryRoomFeedback(Utils.getNowTimestamp(), "QueryRoom", roomList);
                        SendObj(queryRoomFeedback);
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