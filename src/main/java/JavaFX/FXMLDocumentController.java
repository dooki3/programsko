/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX;

import WEKALogic.DataInput;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


/**
 *
 * @author Karlo
 * @author dinok
 */
public class FXMLDocumentController implements Initializable {

    private Thread t1;
    protected DataInput dataInputClass;
    private FileChooser fc;
    @FXML
    private Label label;

    public FXMLDocumentController() {
        dataInputClass = new DataInput();
    }

    @FXML
    private void zatvori(ActionEvent event) {
        // TODO implement exiting of threads when clicking exit, so that the threads don't keep the program on
       System.exit(0);
    }

    // File chooser for pressing the "Add file" image
    @FXML
    public void addFileClicked(MouseEvent event) {
        fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("CSV files", "*.csv"),
                new ExtensionFilter("ARFF files", "*.arff"));
        File selectedFile = fc.showOpenDialog(null);

        if(selectedFile != null)
        {
            try {
                dataInputClass.loadFile(selectedFile.getAbsolutePath());
                System.out.println("Successfully loaded file " + selectedFile.getName() + "!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Failed to select file");
        }
    }

    // Running this process in another thread so that the algorithm doesn't block the GUI
    public void runAlgorithmsClicked(MouseEvent e)
    {
        t1 = new Thread(() -> {
            try {
                dataInputClass.gaussianProcesses();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        t1.start();
    }




    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }


}
