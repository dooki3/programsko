package WEKALogic;

import JavaFX.JavaFXApplication6;
import javafx.application.Application;

public class UserInterface {
/*
    private String filepath;
    private JFrame frame;
    private JLabel label;
    private JButton button;
*/

    static Thread t1 = new Thread(() -> Application.launch(JavaFXApplication6.class));


    public static void main(String [] args)
    {
        t1.start();

        /*
        WEKALogic.DataInput wekaService = new WEKALogic.DataInput();
        try {
            wekaService.loadFile("C:\\Users\\dinok\\Desktop\\seipJDT\\JDT_R2_0.csv");
            wekaService.gaussianProcesses();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

}