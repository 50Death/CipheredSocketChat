import javax.swing.*;

public class AliBobGUIConnectingPage extends AliBobGUIStartPage {

    AliBobGUIConnectingPage(){
        JFrame frame = new JFrame("AliBobGUIConnectingPage");
        frame.setContentPane(new AliBobGUIConnectingPage().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        JLable1.setHorizontalAlignment(SwingConstants.CENTER);
        JLable1.setText("Connecting...Please Wait");

        if(clientRadioButton.isSelected()){
            JLable1.setText("Connecting As Client...Please Wait");
        }else{
            JLable1.setText("Connecting As Server...Please Wait");
        }
    }

    private JProgressBar progressBar1;
    private JPanel panel1;
    private JLabel JLable1;
}
