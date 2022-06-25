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
                long AllSize = file.length(); //��ȡ�ļ��Ĵ�С,��λ�ֽ�
                fileInputStream = new FileInputStream(file);
                dataOutputStream = new DataOutputStream(outputStream);
                long read_size = 0;
                byte[] filebuffeer = new byte[1024];
                int len = 0;//ÿ���ϴ��Ĵ�С

                //�����ļ���С
                dataOutputStream.writeLong(AllSize);

                while((len = fileInputStream.read(filebuffeer)) != -1){
                    read_size += len;//���㵱ǰ��������ֽ���
                    outputStream.write(filebuffeer,0,len);
                    outputStream.flush();
                }
            }
            if(type.equals("upload")){
                String realName = dataInputStream.readUTF();
                String fileName = realName + Utils.getNowTimestamp().hashCode();
                File file = new File(StaticConfig.FILE_PATH + fileName);//ͨ��ѡ����ļ�·��+������ļ�����ȷ���ļ�����λ��
                fileOutputStream  = new FileOutputStream(file);
                long read_size = 0;
                long filesize = dataInputStream.readLong();// ��ȡ�ļ���С
                int roomId = dataInputStream.readInt();
                int len = 0;// ÿ�ν��յĴ�С
                byte[] bytes = new byte[1024];
                // ѭ����
                while((len = inputStream.read(bytes)) != -1){
                    read_size += len;
                    fileOutputStream.write(bytes,0,len);
                    fileOutputStream.flush();
                    if(read_size == filesize){
                        System.out.println("�������");
                    }
                }

                // ��¼�ļ���Ϣ�����ݿ�
                MysqlUtils.AddFile(fileName, realName, Utils.getNowTimestamp(), roomId, filesize);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
