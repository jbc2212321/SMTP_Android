package com.example.smtp.emil;

import com.example.smtp.util.HttpUtil;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class smtpCommand {
    public synchronized String send(String userName,String password,String mailFrom,String sendTO,String subject,String content){
        String ip="";
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            ip=HttpUtil.ip;//实际测试时更改为服务器地址
            System.out.println(ip4.getHostAddress());
            final int[] get_port = {25};//getPort();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    get_port[0] =Integer.valueOf( HttpUtil.doGet("getSMTP"));
                }
            });
            t.start();

            int port=get_port[0];
            System.out.println("smtp port:"+port);
            Socket socket=new Socket(ip, port);


            Scanner sc = new Scanner(System.in);
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

            String ret="";

            socketOut.println("EHLO");
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            socketOut.println("LOGIN "+userName+" "+password);
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);
            if(ret=="-ERR user identify failed!")return "login failed";

            socketOut.println("MAILFROM "+mailFrom);
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            socketOut.println("RCPTO "+sendTO);
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            socketOut.println("SUBJECT "+subject);
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            socketOut.println("data");
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            if(content.equals("")){content="null";}
            socketOut.println(content);
            socketOut.flush();
//            ret = socketIn.readLine();
//            System.out.println(ret);

            socketOut.println('.');
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            socketOut.println("QUIT");
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "send!";
    }
}
