package GUI;

import edu.lyc.crypt.AESCrypt;
import edu.lyc.crypt.BaseCert;
import edu.lyc.crypt.MD5;
import edu.lyc.crypt.RSA;
import edu.lyc.tcp.Client;
import edu.lyc.tcp.Server;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.SecureRandom;
import java.util.HashMap;

public class AliBobGUIStartPage extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("AliBobGUIStartPage");
        frame.setLocationByPlatform(true);
        frame.setContentPane(new AliBobGUIStartPage().pabel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

    }

    //JDialog类
    class MyJDialog extends JDialog {
        MyJDialog(JFrame frame) {
            super(frame, "Cosole", false);
            textArea = new JTextArea();
            this.add(textArea);
            textArea.setLineWrap(true);
            this.add(new JScrollPane(textArea));
            this.setBounds(0, 0, 460, 650);
            this.setVisible(true);
        }
    }


    //截取控制台输出信息
    class catchMessage extends Thread {
        InputStream inputStream;

        catchMessage() throws IOException {
            final LoopedStreams ls = new LoopedStreams();
            // 重定向System.out和System.err
            PrintStream ps = new PrintStream(ls.getOutputStream());
            System.setOut(ps);
            System.setErr(ps);
            inputStream = ls.getInputStream();
            //startConsoleReaderThread(ls.getInputStream());
        }

        @Override
        public void run() {
            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            try {
                String s;
                Document doc = textPane1.getDocument();
                while ((s = br.readLine()) != null) {
                    boolean caretAtEnd = false;
                    caretAtEnd = textPane1.getCaretPosition() == doc.getLength() ?
                            true : false;
                    sb.setLength(0);
                    sb.append(s).toString();
                    jTextPaneWriter(sb.toString(), false);
                    textArea.append(sb.toString()+'\n');
                    if (caretAtEnd)
                        textPane1.setCaretPosition(doc.getLength());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "从BufferedReader读取错误：" + e);
                System.exit(1);
            }
        }
    }

    public AliBobGUIStartPage() {
        textField2.setHorizontalAlignment(JTextField.RIGHT);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(clientRadioButton);
        buttonGroup.add(serverRadioButton);
        textField1.setText("127.0.0.1");
        textField3.setText("1234");
        textPane1.setEditable(false);
        MyJDialog myJDialog = new MyJDialog(this);

        map = RSA.getKeys();//RSA加密生成公钥私钥，见末尾

        //JScrollPane jsp = new JScrollPane(textPane2);
        //JScrollPane jsp2 = new JScrollPane(textPane1);


        /*
        Style def = textPane1.getStyledDocument().addStyle(null,null);
        StyleConstants.setFontFamily(def,"verdana");
        StyleConstants.setFontSize(def,12);
        Style clientStyle = textPane1.addStyle("clientStyle", def);
        Style serverStyle = textPane1.addStyle("serverStyle", def);
        StyleConstants.setForeground(clientStyle,Color.BLUE);
        StyleConstants.setForeground(serverStyle,Color.YELLOW);
        textPane1.setParagraphAttributes(serverStyle,true);
        */


        try {
            catchMessage cm = new catchMessage();
            cm.start();
        } catch (IOException e) {
            e.printStackTrace();
            new JDialog(this, "打开监听器失败！", false);
        }

        /**
         * Server对象创立
         */
        class buildServer extends Thread {
            boolean flag = false;
            public Server server;

            @Override
            public void run() {
                String portStr = textField3.getText();
                int port = Integer.parseInt(portStr);
                server = new Server(port);
                statusLabel.setText("Connected!");

                //交换公钥
                statusLabel.setText("Sending PublicKey");
                String getPublicKey = map.get("publicKey");
                getPublicKey = "$$" + getPublicKey;
                server.sendText = new StringBuffer(getPublicKey);
                server.run();
                flag = true;

                sendButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String toSend = new String(textField2.getText());
                        statusLabel.setText("Ciphering...");
                        server.sendText = new StringBuffer(enCryptSend(toSend));
                        statusLabel.setText("Ciphered!");
                        statusLabel.setText("Sending...");
                        if (flag == false) {
                            server.start();
                            flag = true;
                        } else {
                            server.run();
                        }
                        statusLabel.setText("Send Successful!");
                        jTextPaneWriter(textField2.getText(), true);
                        textField2.setText("");
                        try {
                            Thread.currentThread().sleep(300);
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }

                    }
                });
            }
        }

        class buildClient extends Thread {
            boolean flag = false;
            public Client client;

            @Override
            public void run() {
                String ipStr = desIPTextField.getText();
                String portStr = portTextField.getText();
                int port = Integer.parseInt(portStr);
                client = new Client(ipStr, port);
                statusLabel.setText("Connected!");
                String getPublicKey = map.get("publicKey");
                getPublicKey = "$$" + getPublicKey;
                client.sendText = new StringBuffer(getPublicKey);
                client.run();
                flag = true;

                sendButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String toSend = textField2.getText();
                        statusLabel.setText("Ciphering...");
                        client.sendText = new StringBuffer(enCryptSend(toSend));
                        statusLabel.setText("Ciphered!");
                        statusLabel.setText("Sending...");
                        if (flag == false) {
                            client.start();
                            flag = true;
                        } else {
                            client.run();
                        }
                        statusLabel.setText("Send Successful!");
                        jTextPaneWriter(textField2.getText(), true);
                        textField2.setText("");
                        try {
                            Thread.currentThread().sleep(300);
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }
                    }
                });
            }
        }

        /**
         * 点击Connect按钮
         */
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Connecting");
                if (serverRadioButton.isSelected()) {
                    new buildServer().start();
                } else {
                    new buildClient().start();
                }
                //connectButton.setEnabled(false);
            }
        });
        /**
         * 点击Send
         */


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        clientRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desIPTextField.setEditable(true);
                portTextField.setEditable(true);
            }
        });
        serverRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desIPTextField.setEditable(false);
                portTextField.setEditable(false);
            }
        });


    }

    /*
    //mode == true,靠左发送，字体黄色；mode == false,靠右发送，字体蓝色
    public static void jTextPaneWriter(String str, boolean mode){
        if(mode==false) {
            textPane1.setSelectedTextColor(Color.BLUE);
            textPane1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }else{
            textPane1.setSelectedTextColor(Color.YELLOW);
            textPane1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        Document doc = textPane1.getDocument();
        try{
            doc.insertString(doc.getLength(),str,attributeSet);
        }catch (BadLocationException e){
            e.printStackTrace();
        }
    }
    */

    void jTextPaneWriter(String string, boolean mode) {
        //string = string+"\n";
        Color color = Color.BLACK;
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attrSet, color);
        StyleConstants.setFontFamily(attrSet, "verdana");
        StyleConstants.setFontSize(attrSet, 12);
        //向GUI console 界面输出 未处理的 全部 控制台语句
        //Document doc2 = textPane2.getDocument();
        //try {
        //    doc2.insertString(doc2.getLength(), string + "", attrSet);
        //} catch (BadLocationException e) {
        //    e.printStackTrace();
        //}


        //向pane1输出审查后的语句
        if (mode == false) {//Server
            color = Color.RED;
            string = manuConsoleStream(string);
        } else {//Client
            color = Color.BLUE;
        }
        StyleConstants.setForeground(attrSet, color);
        Document doc = textPane1.getDocument();
        try {
            doc.insertString(doc.getLength(), string + "\n", attrSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
/*
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            Document doc = textPane2.getDocument();
            try {
                doc.insertString(doc.getLength(), string, attributeSet);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }*/
    }

    String manuConsoleStream(String str) {
        String result = new String();
        result = str;
        if (str.length() == 0) {
            return result;
        }
        if (str.charAt(0) == '#') {
            result = "";
        } else if (str.startsWith("@#$")) {
            statusLabel.setText("Decrypting...");
            String string = new String(str.substring(3));
            result = deCryptReceive(string);//解密调用
            statusLabel.setText("Decrypt Complete");
        } else if (str.charAt(0) == '$' && str.charAt(1) == '$') {
            result = "";
            statusLabel.setText("Received PublicKey!");

            //将PublicKey储存
            gotPublicKey = str.substring(2, str.length() - 1 + 1);
        } else if (result.equals("java.net.SocketException: Connection reset")) {
            statusLabel.setText("Lost");
        } else if (result.equals("java.net.ConnectException: Connection refused: connect")) {
            statusLabel.setText("Refused");
        } else if (result.equals("Exception in thread \"Thread-4\" java.lang.NumberFormatException: For input string: \"\"")) {
            statusLabel.setText("Illegal Input");
        } else if (result.equals("java.net.ConnectException: Connection timed out: connect")) {
            statusLabel.setText("Time Out");
        }
        return result;
    }

    private JPanel pabel1;
    public JRadioButton clientRadioButton;
    public JRadioButton serverRadioButton;
    private JButton connectButton;
    private JButton exitButton;
    private JTextField textField1;
    private JTextField desIPTextField;
    private JTextField textField3;
    private JTextField portTextField;
    private JButton sendButton;
    private JLabel statusLabel;
    public JTextPane textPane1;
    public JTextPane textPane2;
    private JTextField textField2;
    public JTextArea textArea;

    HashMap<String, String> map;//TODO 生成RSA公钥私钥
    String gotPublicKey;

    /**
     * * TODO 当前密文格式：
     * * TODO
     * * TODO 明文  ->  AES加密（明文，随机数作为密码）    ->  RSAPublicKey加密随机数  -> 对以上信息取MD5 -> 对MD5消息摘要RSA证书签名
     * * TODO Apache Random ↑                      RSA生成本次连接所需公钥，私钥↑                   线下进行RSA证书交换↑
     * * TODO
     * * TODO 1. 公钥交换，明文发送，前加标识符"$$"，TCP连接后立即发送，双方每次连接仅交换一次公钥（216字节）
     * * TODO 2. 用户输入：明文
     * * TODO 3. AES密文（不定长）AND 随机数明文（64字节）
     * * TODO 4. AES密文（不定长）AND RSA对方公钥加密的随机数（172字节）
     * * TODO 5. 顺序确定：[RSA对方公钥加密的随机数（172字节）+ AES密文（不定长）]AND  MD5（32字节）
     * * TODO 6. [RSA对方公钥加密的随机数（172字节）+ AES密文（不定长）]AND RSA(MD5)（344字节）
     * * TODO 待发送，规定格式：【RSA对方公钥加密的随机数（172字节）+ RSA(MD5)（344字节）+MD5（32字节）+ AES密文（不定长）】
     * * TODO 每个Socket包留给AES密文 字节，AES明文（发送内容）320字节，UNICODE下不得多于80个汉字
     * <p>
     * 加密函数
     *
     * @param msg 待发送明文
     * @return 待发送密文
     */
    String enCryptSend(String msg) {

        //AES加密明文
        //AESCrypt aesCrypt = new AESCrypt();
        String randomMain = new String();
        randomMain = RandomStringUtils.randomAscii(64);//生成64位随机数
        String AESCipheredText = new String();
        AESCipheredText = AESCrypt.encrypt(msg, randomMain);//加密
        System.out.println("#####################  AES Crypt  #####################\n");
        System.out.println("#AES加密结果:" + AESCipheredText + '\n');

        //RSA加密随机数RandomMain

        String privateKey = map.get("privateKey");
        String publicKey = gotPublicKey;
        String RSACipheredText = new String();
        try {
            RSACipheredText = RSA.encrypt(RSA.loadPublicKey(publicKey), randomMain.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#####################  RSA 公钥加密  #####################\n");
        System.out.println("#RSA公钥加密结果：" + RSACipheredText + '\n');

        //证书签名
        String toSign = RSACipheredText + AESCipheredText;

        String password = "123456";
        String alias = "CATest";
        String certificatePath = "C:/JAVACertificate/CATest.cer";
        String keyStorePath = "C:/JAVACertificate/CATest.keystore";

        String getSign = new String();
        try {
            getSign = BaseCert.sign(toSign.getBytes(), keyStorePath, alias, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("#####################  RSA Certificate Signature  #####################\n");
        System.out.println("#RSASign:" + getSign + '\n');

        //拼接结果成规范格式 RSA加密密钥+被签名的MD5+AES密文
        String result = "@#$" + RSACipheredText + getSign + AESCipheredText;
        return result;
    }

    String deCryptReceive(String msg) {
        //将密文拆分
        String RSACipheredText = msg.substring(0, 172);//RSA加密了的用于解密AES的秘钥
        String signedMSG = msg.substring(172, 516);//签名了的消息
        String AESCiphedText = msg.substring(516);//AES加密了的密文

        //验证签名
        String password = "123456";
        String alias = "CATest";
        String certificatePath = "C:/JAVACertificate/CATest.cer";
        String keyStorePath = "C:/JAVACertificate/CATest.keystore";

        String toTest = RSACipheredText + AESCiphedText;
        boolean status = false;
        try {
            status = BaseCert.verify(toTest.getBytes(),signedMSG,certificatePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //验证消息完整性
        if(!status){
            statusLabel.setText("Message Broken");
            return "消息被篡改！";
        }else{
            System.out.println("消息完整性确认，发送方确认");
        }

        //解密RSA
        String privateKey = map.get("privateKey");
        String key = new String();
        try {
            key = RSA.deCrypt(RSA.loadPrivateKey(privateKey), Base64.decodeBase64(RSACipheredText));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //解密AES
        String plainText = new String();
        plainText = AESCrypt.decrypt(AESCiphedText, key);

        return plainText;
    }

}