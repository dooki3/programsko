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
            wekaService.loadFile("D:\\Sve\\cpu.arff");

            wekaService.naiveBayes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}