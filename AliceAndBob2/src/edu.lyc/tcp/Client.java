package edu.lyc.tcp;

import GUI.AliBobGUIStartPage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {

    final static Object lock = new Object();
    boolean flag = false;
    public StringBuffer sendText = new StringBuffer();

    //定义一个Socket对象
    Socket socket = null;

    public Client(String host, int port) {
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
            System.out.println("#Successfully connected to "+socket.getInetAddress().getHostAddress());
            new listenMessThread().start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        super.run();
        //写操作
        //Scanner scanner = null;
        OutputStream os = null;
        try {
            //scanner = new Scanner(System.in);
            os = socket.getOutputStream();
            String in = "";
            //do {
                in = sendText.toString();
                os.write(("" + in).getBytes());
                os.flush();
            //} while (!in.equals("bye"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //scanner.close();
        //try {
        //    os.close();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

    //往Socket里面写数据，需要新开一个线程
    class listenMessThread extends Thread {
        @Override
        public void run() {

            //客户端一连接就可以写数据个服务器了

            super.run();
            try {
                // 读Sock里面的数据
                InputStream s = socket.getInputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = s.read(buf)) != -1) {
                    String str = new String(buf, 0, len);
                    System.out.println(str);
                    //AliBobGUIStartPage.jTextPaneWriter(str,true);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    //函数入口

    /**
     *
     * @param hostIP
     * @param port
     */
    public static void runClient(String hostIP, int port) {
        //需要服务器的正确的IP地址和端口号
        Client clientTest = new Client("192.168.31.193", 1234);
        clientTest.start();
    }

}