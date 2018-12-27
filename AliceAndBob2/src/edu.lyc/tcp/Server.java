package edu.lyc.tcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import GUI.AliBobGUIStartPage;

public class Server extends Thread {

    final static Object lock = new Object();
    boolean flag = false;
    public StringBuffer sendText = new StringBuffer();

    ServerSocket server = null;
    Socket socket = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("#wait client connect...");
            socket = server.accept();
            new listenMessThread().start();//连接并返回socket后，再启用发送消息线程
            System.out.println('#'+socket.getInetAddress().getHostAddress() + "SUCCESS TO CONNECT...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        //Scanner scanner = null;
        //new inputMessage().start();
        OutputStream out = null;
        try {
            if (socket != null) {
                //scanner = new Scanner(System.in);
                out = socket.getOutputStream();
                String in = "";
                //do {
                    //try {
                     //   synchronized (lock) {
                            //if(flag==false) {
                            //    lock.wait();
                            //}
                            in = sendText.toString();
                    //    }
                    //}catch (InterruptedException e){
                    //    e.printStackTrace();
                    //}
                    out.write(("" + in).getBytes());
                    out.flush();//清空缓存区的内容
                    flag=false;
                //} while (!in.equals("q"));
                //scanner.close();
                //try {
                    //out.close();
                //} catch (IOException e) {
                //    e.printStackTrace();
                //}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class listenMessThread extends Thread {
        @Override
        public void run() {

            super.run();
            try {
                InputStream in = socket.getInputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) != -1) {
                    String str = new String(buf, 0, len);
                    //System.out.println("client saying: " + new String(buf, 0, len));
                    System.out.println("" + new String(buf, 0, len));
                    //todo
                }

            } catch (IOException e) {
                e.printStackTrace();
            }




        }
    }

    public class inputMessage extends Thread{
        @Override
        public void run(){
            super.run();
            Scanner sc = new Scanner(System.in);
            synchronized (lock) {
                sendText = new StringBuffer(sc.nextLine());
                lock.notify();
                flag=true;
            }
        }
    }

    //函数入口
    public static void main(String[] args) {
        Server server = new Server(1234);
        //Scanner sc = new Scanner(System.in);
        //String str = sc.nextLine();
        server.start();

    }

}