package com.xl.tmall.utils;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

//检查redis端口是否启动
public class PortUtil {

    public static boolean  testPort(int port){
        try{
//            创建一个检查端口的socket连接,如果正常连接,说明此端口没被占用,redis服务器没有启动
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return false;
//            若抛出绑定端口异常,则说明服务器启动了,不做处理
        }catch (BindException e){
            return true;
        }catch (IOException e){
            return true;
        }
    }

    public static void checkPort(int port,String server,boolean shutdown){
        if(!testPort(port)){
            if(shutdown){
                String message = String.format("在端口%d未检查到%s启动%n",port,server);
                JOptionPane.showMessageDialog(null,message);
                System.exit(1);
            }else {
                String message = String.format("在端口%d未检查到%s启动%n,是否继续?",port,server);
                if(JOptionPane.OK_OPTION!=JOptionPane.showConfirmDialog(null,message))
                    System.exit(1);
            }
        }
    }

}
