import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AliBobGUIStartPage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("AliBobGUIStartPage");
        frame.setContentPane(new AliBobGUIStartPage().pabel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public AliBobGUIStartPage(){
        textField2.setHorizontalAlignment(JTextField.RIGHT);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(clientRadioButton);
        buttonGroup.add(serverRadioButton);
        textField1.setText("127.0.0.1");
        textField3.setText("1000");

        /**
         * 点击Connect按钮
         */
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(serverRadioButton.isSelected()){

                }else{

                }
            }
        });


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
    private JTextPane textPane1;
    private JTextPane 请先运行Server端再运行Client端TextPane;
    private JTextField textField2;
}
