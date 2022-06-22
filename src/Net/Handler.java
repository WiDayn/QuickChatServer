package Net;

import Chat.Message;
import Chat.User;
import Net.Feedback.PullMessageFeedback;
import Net.Request.PullMessageRequest;
import Net.Request.Request;
import Net.Request.SendMessageRequest;
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
                        pullMessageFeedback.addMessage(new Message(new User("Widayn"), Utils.getNowTimestamp(), "Hello"));
                        SendObj(pullMessageFeedback);
                    }
                    if(obj instanceof SendMessageRequest sendMessageRequest){
                        MysqlUtils.AddMessage(sendMessageRequest.getMessage());
                    }
                } catch (SocketException e) {
                    System.out.println("客户端断开连接:"+ e.getMessage());
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