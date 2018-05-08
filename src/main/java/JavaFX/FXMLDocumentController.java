/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import WEKALogic.AlgorithmsWEKA;
import WEKALogic.FileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


/**
 *
 * @author Karlo
 * @author dkralj
 */
public class FXMLDocumentController implements Initializable {

    private Thread t1;
    private List<FileHandler> fileHandlers = new ArrayList<>();
    private FileChooser fc;
    private AlgorithmsWEKA WEKA = new AlgorithmsWEKA();
    private ObservableList<String> options;
    private FileHandler currentlySelectedFile = null;

    @FXML
    private ComboBox FilesComboBox;
    @FXML
    private Label label;
    @FXML
    private void removeSelectedFile(ActionEvent e)
    {
        options.remove(currentlySelectedFile.getFileName());
        FilesComboBox.setItems(options);
        if(currentlySelectedFile != null)
        {
            currentlySelectedFile.removeFileInstance();
        }
        FilesComboBox.getItems();
    }
    @FXML
    private void zatvori(ActionEvent event) {
        // TODO implement exiting of threads when clicking exit, so that the threads don't keep the program on
       System.exit(0);
    }

    @FXML
    private void comboBoxChanged(ActionEvent e)
    {
        String currentFile = (String) FilesComboBox.getValue();
        currentlySelectedFile = findFileByName(currentFile, fileHandlers);
        if(currentlySelectedFile != null)
        {
            System.out.println(currentlySelectedFile.getFileName());
        }
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
            try
            {
                updateGUIonFileAdd(selectedFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Failed to select file");
        }
    }
    private void updateGUIonFileAdd(File selectedFile) throws Exception
    {
        // Adding a FileHandler class object to the list of filehandlers, essentially keeping tabs on all the files currently loaded
        FileHandler newFile = new FileHandler();
        newFile.setFileName(selectedFile.getName());
        fileHandlers.add(newFile);
        newFile.loadFile(selectedFile.getAbsolutePath());
        options.add(selectedFile.getName());
        FilesComboBox.setItems(options);
        FilesComboBox.getItems();

        System.out.println("Successfully loaded file " + selectedFile.getName() + "!");
        System.out.println("Currently " + fileHandlers.size() + " files are loaded in the programs memory");
        System.out.println("This is what the object holds: " + fileHandlers.get(0).getFileCount());
    }

    // Running this process in another thread so that the algorithm doesn't block the GUI
    public void runAlgorithmsClicked(MouseEvent e)
    {
        t1 = new Thread(() -> {
            try {
                WEKA.gaussianProcesses(currentlySelectedFile.getData());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        t1.start();
    }

    private FileHandler findFileByName(String filename, List<FileHandler> list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getFileName().equals(filename))
            {
                return list.get(i);
            }
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }
    // Constructor
    public FXMLDocumentController()
    {
        options = FXCollections.observableArrayList();
    }
}
