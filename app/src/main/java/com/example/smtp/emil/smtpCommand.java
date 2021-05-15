package com.example.smtp.emil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class smtpCommand {
    public String send(String userName,String password,String mailFrom,String sendTO,String subject,String content){
        String ip="";
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            ip="10.0.2.2";//实际测试时更改为服务器地址
            System.out.println(ip4.getHostAddress());
            int port=25;//getPort();
            Socket socket=new Socket(ip,port);


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
