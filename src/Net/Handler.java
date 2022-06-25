package Net;

import Chat.File;
import Chat.Message;
import Chat.PrivateMessage;
import Chat.Room;
import Chat.User;
import Net.Feedback.*;
import Net.Request.*;
import Sql.MysqlUtils;
import Utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
                    Request request = (Request) obj;
                    if(request.getType().equals("PullMessage")){
                        PullMessageRequest pullMessageRequest = (PullMessageRequest) obj;
                        List<Message> messageList = MysqlUtils.QueryMessage(pullMessageRequest.getBeginTime(), pullMessageRequest.getRoom().getId());
                        PullMessageFeedback pullMessageFeedback = new PullMessageFeedback(Utils.getNowTimestamp(), pullMessageRequest.getRoom().getId());
                        for(Message message : messageList){
                            pullMessageFeedback.addMessage(message);
                        }
                        SendObj(pullMessageFeedback);
                    }
                    if(request.getType().equals("SendMessage")){
                        SendMessageRequest sendMessageRequest = (SendMessageRequest) obj;
                        MysqlUtils.AddMessage(sendMessageRequest.getMessage().getUserid(),
                                sendMessageRequest.getMessage().getTimestamp(),
                                sendMessageRequest.getMessage().getMassage(),
                                sendMessageRequest.getRoom().getId());
                    }
                    if(request.getType().equals("Login")){
                        LoginRequest loginRequest = (LoginRequest) obj;
                        // 检查用户是否存在
                        User user = MysqlUtils.QueryUser(loginRequest.getUser());
                        if(user == null){
                            LoginFeedback loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), 401, "用户不存在", null);
                            SendObj(loginFeedback);
                            continue;
                        }
                        // 检查密码
                        String password = MysqlUtils.QueryUserPassword(loginRequest.getUser());
                        LoginFeedback loginFeedback;
                        if(!password.equals(Utils.getShaPassword(loginRequest.getPassword(), loginRequest.getUser()))){
                            loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), 401, "密码错误", null);
                            SendObj(loginFeedback);
                            continue;
                        }
                        // 查询用户所在的房间
                        user.setRoomList(MysqlUtils.QueryRoom(user.getUserid()));
                        loginFeedback = new LoginFeedback(Utils.getNowTimestamp(), 200, "登录成功", user);
                        SendObj(loginFeedback);
                    }
                    if(request.getType().equals("Register")){
                        RegisterRequest registerRequest = (RegisterRequest) obj;
                        // 检查用户是否存在
                        User user = MysqlUtils.QueryUser(registerRequest.getUserid());
                        if(user != null){
                            // 存在直接返回
                            RegisterFeedback registerFeedback = new RegisterFeedback(Utils.getNowTimestamp(), 401, "用户名已存在");
                            SendObj(registerFeedback);
                            continue;
                        }
                        // 添加到数据库
                        MysqlUtils.AddUser(registerRequest.getUserid(), Utils.getShaPassword(registerRequest.getPassword(), registerRequest.getUserid()), registerRequest.getNickname());
                        RegisterFeedback registerFeedback = new RegisterFeedback(Utils.getNowTimestamp(), 200, "注册成功");
                        // 默认加入1号房间
                        MysqlUtils.JoinRoom(registerRequest.getUserid(), 1);
                        SendObj(registerFeedback);
                    }
                    if(request.getType().equals("QueryRoom")){
                        QueryRoomRequest queryRoomRequest = (QueryRoomRequest) obj;
                        List<Room> roomList = MysqlUtils.QueryRoom(queryRoomRequest.getUserId());
                        QueryRoomFeedback queryRoomFeedback = new QueryRoomFeedback(Utils.getNowTimestamp(), roomList);
                        SendObj(queryRoomFeedback);
                    }
                    if(request.getType().equals("JoinRoom")){
                        JoinRoomRequest joinRoomRequest = (JoinRoomRequest) obj;
                        List<Room> roomList = MysqlUtils.QueryRoom(joinRoomRequest.getUserid());
                        boolean flag = false;
                        // 先判断用户是否已经加入过该房间
                        for(Room room : roomList){
                            if(room.getId() == joinRoomRequest.getRoomId()){
                                JoinRoomFeedback joinRoomFeedback = new JoinRoomFeedback(Utils.getNowTimestamp(), 401, "您已经加入过该房间了，请刷新房间列表");
                                SendObj(joinRoomFeedback);
                                flag = true;
                                break;
                            }
                        }
                        if(flag) continue;
                        // 再判断房间是否存在
                        if(MysqlUtils.QueryRoomInf(joinRoomRequest.getRoomId()) == null){
                            JoinRoomFeedback joinRoomFeedback = new JoinRoomFeedback(Utils.getNowTimestamp(), 401, "房间不存在");
                            SendObj(joinRoomFeedback);
                            continue;
                        }
                        // 写入关联表
                        MysqlUtils.JoinRoom(joinRoomRequest.getUserid(), joinRoomRequest.getRoomId());
                        JoinRoomFeedback joinRoomFeedback = new JoinRoomFeedback(Utils.getNowTimestamp(), 200, "加入成功，请刷新房间列表");
                        SendObj(joinRoomFeedback);
                    }
                    if(request.getType().equals("SendActive")){
                        SendActiveRequest sendActiveRequest = (SendActiveRequest) obj;
                        MysqlUtils.UpdateActive(sendActiveRequest.getUserid());
                    }
                    if(request.getType().equals("QueryOnline")){
                        QueryOnlineRequest queryOnlineRequest = (QueryOnlineRequest) obj;
                        List<User> userList = new ArrayList<>();
                        List<String> useridList = MysqlUtils.QueryRoom(queryOnlineRequest.getRoomId());
                        for(String userid : useridList){
                            User user = MysqlUtils.QueryUser(userid);
                            Calendar beforeTime = Calendar.getInstance();
                            beforeTime.add(Calendar.MINUTE, -1);
                            if(user.getLastActive().after(new Timestamp(beforeTime.getTime().getTime()))){
                                userList.add(user);
                            }
                        }
                        QueryOnlineFeedback queryOnlineFeedback = new QueryOnlineFeedback(Utils.getNowTimestamp(), queryOnlineRequest.getRoomId(), userList);
                        SendObj(queryOnlineFeedback);
                    }
                    if(request.getType().equals("CreateRoom")){
                        CreateRoomRequest createRoomRequest = (CreateRoomRequest) obj;
                        int roomId = MysqlUtils.CreateRoom(createRoomRequest.getUserid(), createRoomRequest.getName());
                        CreateRoomFeedback createRoomFeedback = new CreateRoomFeedback(Utils.getNowTimestamp(), 200, "创建成功！房间ID为:" + roomId);
                        SendObj(createRoomFeedback);
                    }
                    if(request.getType().equals("PullRoom")){
                        PullRoomRequest pullRoomRequest = (PullRoomRequest) obj;
                        if(MysqlUtils.QueryUser(pullRoomRequest.getUserid()) == null){
                            // 用户不存在
                            PullRoomFeedback pullRoomFeedback = new PullRoomFeedback(Utils.getNowTimestamp(), 401, "用户不存在");
                            SendObj(pullRoomFeedback);
                            continue;
                        }
                        if(MysqlUtils.QueryRoomInf(pullRoomRequest.getRoomId()) == null){
                            // 房间不存在
                            PullRoomFeedback pullRoomFeedback = new PullRoomFeedback(Utils.getNowTimestamp(), 401, "房间不存在");
                            SendObj(pullRoomFeedback);
                            continue;
                        }
                        List<Room> roomList = MysqlUtils.QueryRoom(pullRoomRequest.getUserid());
                        boolean flag = false;
                        for(Room room : roomList){
                            if(room.getId() == pullRoomRequest.getRoomId()){
                                // 已在房间
                                PullRoomFeedback pullRoomFeedback = new PullRoomFeedback(Utils.getNowTimestamp(), 401, "用户已经在房间");
                                SendObj(pullRoomFeedback);
                                flag = true;
                                break;
                            }
                        }
                        if (flag) continue;
                        MysqlUtils.JoinRoom(pullRoomRequest.getUserid(), pullRoomRequest.getRoomId());
                        PullRoomFeedback pullRoomFeedback = new PullRoomFeedback(Utils.getNowTimestamp(), 200, "拉入成功");
                        SendObj(pullRoomFeedback);
                    }
                    if(request.getType().equals("SendPrivateMessage")){
                        SendPrivateMessageRequest sendPrivateMessageRequest = (SendPrivateMessageRequest) obj;
                        PrivateMessage privateMessage = sendPrivateMessageRequest.getPrivateMessage();
                        MysqlUtils.AddPrivateMessage(privateMessage.getFrom_user(),
                                privateMessage.getTo_user(),
                                privateMessage.getMessage(),
                                privateMessage.getSendTime());
                    }
                    if(request.getType().equals("QueryUnreadPrivateMessage")){
                        QueryUnreadPrivateMessageRequest queryUnreadPrivateMessageRequest = (QueryUnreadPrivateMessageRequest) obj;
                        // 这里会同时标记消息为已读
                        List<PrivateMessage> privateMessageList = MysqlUtils.QueryPrivateMessageToUnread(queryUnreadPrivateMessageRequest.getUserid());
                        QueryUnreadPrivateMessageFeedback queryUnreadPrivateMessageFeedback = new QueryUnreadPrivateMessageFeedback(Utils.getNowTimestamp(), privateMessageList);
                        SendObj(queryUnreadPrivateMessageFeedback);
                    }
                    if(request.getType().equals("QueryFiles")){
                        QueryFilesRequest queryFilesRequest = (QueryFilesRequest) obj;
                        List<File> fileList = MysqlUtils.QueryFiles(queryFilesRequest.getRoomId());
                        QueryFilesFeedback queryFilesFeedback = new QueryFilesFeedback(Utils.getNowTimestamp(), fileList);
                        SendObj(queryFilesFeedback);
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