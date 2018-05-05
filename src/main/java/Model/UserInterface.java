package Model;

import javax.swing.*;
import java.util.*;

public class UserInterface {
/*
    private String filepath;
    private JFrame frame;
    private JLabel label;
    private JButton button;
*/



    public static void main(String [] args)
    {
        DataInput wekaService = new DataInput();
        try {
            wekaService.loadFile("C:\\Users\\dinok\\Desktop\\seipJDT\\JDT_R2_0.csv");
            wekaService.gaussianProcesses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}