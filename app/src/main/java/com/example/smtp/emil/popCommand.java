package com.example.smtp.emil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.smtp.util.HttpUtil;

public  class popCommand {
    Socket  socket;
    private  boolean login(String userName, String password){
        //登录账号（密码为333即可以登录）
        String ip="";
        try {
//            InetAddress ip4 = Inet4Address.getLocalHost();
            ip=HttpUtil.ip;//实际测试时更改为服务器地址
            //   System.out.println(ip4.getHostAddress());
            final int[] get_port = {100};//getPort();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    get_port[0] =Integer.parseInt( HttpUtil.doGet("getPOP"));
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int port=get_port[0];
            System.out.println("pop port:"+port);
            socket=new Socket(ip,port);
      //      System.out.println("popcommond!");
            Scanner sc = new Scanner(System.in);
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
            String ret="";
            System.out.println("popcommond!");
        socketOut.println("pop3");
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);
            System.out.println(userName);
            socketOut.println("USER "+userName);
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            socketOut.println("PASS "+password);
            socketOut.flush();
            ret = socketIn.readLine();
            System.out.println(ret);

            if(ret=="+OK user successfully logged on!")return true;
            else return false;
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public   void quit() throws IOException {
        //退出界面时调用，中断pop3链接
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        String ret;
        socketOut.println("QUIT");
        socketOut.flush();
        ret = socketIn.readLine();
        System.out.println(ret);
        socket.close();
    }

    public   JSONArray LIST(String userName) throws IOException {
        //用于显示邮件列表
        this.login(userName,"333");
        System.out.println("login!!");

        BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        String ret;
        socketOut.println("LIST");
        socketOut.flush();
        ret = socketIn.readLine();
        //     System.out.println(ret);
        String mail[]=ret.split("<mail>");
        //if(mail.length==1)
        JSONArray jsonArray=new JSONArray();
        //   ArrayList<JSONObject> arr=new ArrayList<JSONObject>();
        //     System.out.println("splitNUM:"+mail.length);
        for(int i=1;i<mail.length;++i){
            JSONObject js=new JSONObject();
                      System.out.println(mail[i]);
            //     mail[i]+="$$";
            String temp[]=mail[i].split("<sen>");
            System.out.println("splitNUM:"+temp.length);
            js.put("email_no",temp[1]);
            js.put("email_from",temp[2]);
            js.put("email_to",temp[3]);
            js.put("email_subject",temp[4]);
            jsonArray.add(js);
        }
        System.out.println(ret);
        this.quit();
        return jsonArray;
    }

    public   void DELETE(int num) throws IOException {
        //删除对应emailno的邮件

        this.login("jbc2","333");
        System.out.println("login!!");


        BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        String ret;
        socketOut.println("DELE "+num);
        socketOut.flush();
        ret = socketIn.readLine();
        System.out.println(ret);
        //   String arr[]=ret.split("<mail>");
        this.quit();
    }

    public  String getContent(int num) throws IOException {
        //获取对应编号的邮件内容
        this.login("jbc2","333");
        System.out.println("login!!");

        BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        String ret;

//        JSONArray arr=new JSONArray();
//        try {
//            arr=this.LIST("lyz");//此处需获取用户名
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        socketOut.println("RETR "+num);
       socketOut.flush();
        ret = socketIn.readLine();
        System.out.println(ret);
        String temp[]=ret.split("<sen>");
        String content="";
        for(int i=0;i<temp.length-1;++i){
            content+=temp[i]+"\n";
        }

        this.quit();
        return content+temp[temp.length-1];
    }


}
