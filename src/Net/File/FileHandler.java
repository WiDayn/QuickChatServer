package Net.File;

import Sql.MysqlUtils;
import Utils.StaticConfig;
import Utils.Utils;

import java.io.*;
import java.net.Socket;

public class FileHandler extends Thread {

    Socket socket;

    public FileHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            FileOutputStream fileOutputStream;
            FileInputStream fileInputStream;
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String type = dataInputStream.readUTF();
            if(type.equals("download")){
                String fileName = dataInputStream.readUTF();
                File file = new File(StaticConfig.FILE_PATH + fileName);
                long AllSize = file.length(); //获取文件的大小,单位字节
                fileInputStream = new FileInputStream(file);
                dataOutputStream = new DataOutputStream(outputStream);
                long read_size = 0;
                byte[] filebuffeer = new byte[1024];
                int len = 0;//每次上传的大小

                //发送文件大小
                dataOutputStream.writeLong(AllSize);

                while((len = fileInputStream.read(filebuffeer)) != -1){
                    read_size += len;//计算当前传输的总字节数
                    outputStream.write(filebuffeer,0,len);
                    outputStream.flush();
                }
            }
            if(type.equals("upload")){
                String realName = dataInputStream.readUTF();
                String fileName = realName + Utils.getNowTimestamp().hashCode();
                File file = new File(StaticConfig.FILE_PATH + fileName);//通过选择的文件路径+传输的文件名，确定文件保存位置
                fileOutputStream  = new FileOutputStream(file);
                long read_size = 0;
                long filesize = dataInputStream.readLong();// 获取文件大小
                int roomId = dataInputStream.readInt();
                int len = 0;// 每次接收的大小
                byte[] bytes = new byte[1024];
                // 循环读
                while((len = inputStream.read(bytes)) != -1){
                    read_size += len;
                    fileOutputStream.write(bytes,0,len);
                    fileOutputStream.flush();
                    if(read_size == filesize){
                        System.out.println("接收完成");
                    }
                }

                // 记录文件信息到数据库
                MysqlUtils.AddFile(fileName, realName, Utils.getNowTimestamp(), roomId, filesize);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
