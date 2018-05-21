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

import WEKALogic.FileHandler;
import WEKALogic.ProcessData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import weka.core.Instances;

/**
 *
 * @author Karlo
 * @author dkralj
 */
public class FXMLDocumentController implements Initializable {

    private Thread t1;
    private Thread t2;
    private ObservableList<String> options;
    private List<FileHandler> fileHandlers = new ArrayList<>();
    private FileChooser fc;
    private FileHandler currentlySelectedFile = null;
    private ProcessData dataPruner;
    private int currentFileIndex;
    TextArea testTextArea;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox checkBoxes;
    @FXML
    private Button RemoveBtn;
    @FXML
    private ComboBox FilesComboBox;
    @FXML
    private void removeSelectedFile(ActionEvent e1)
    {
        if(e1.getSource().equals(RemoveBtn))
        {
            if(currentlySelectedFile != null)
            {
                currentlySelectedFile.decreaseFileCount();
                currentlySelectedFile.removeFileInstance();
                fileHandlers.remove(currentFileIndex);
                checkBoxes.getChildren().remove(currentFileIndex);
            }
            options.remove(currentlySelectedFile.getFileName());
            FilesComboBox.setItems(options);
            FilesComboBox.getItems();


        }
    }
    @FXML
    private void zatvori(ActionEvent event) {
        // TODO implement exiting of threads when clicking exit, so that the threads don't keep the program on
       System.exit(0);
    }

    @FXML
    private void comboBoxChanged(ActionEvent e)
    {
        if(e.getSource().equals(FilesComboBox))
        {
            String currentFile = (String) FilesComboBox.getValue();
            currentlySelectedFile = findFileByName(currentFile, fileHandlers);
            if(currentlySelectedFile != null)
            {
                System.out.println("Currently selected file: " + currentlySelectedFile.getFileName());
            }
        }
    }

    @FXML
    private void onPruneClicked()
    {
        t2 = new Thread(() ->
        {
            //dataPruner.buildPredictionModel(getSelectedFiles(fileHandlers));
        });
        t2.start();
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
            if(!checkIfAlreadyOpened(selectedFile.getName()))
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
                testTextArea.setText(testTextArea.getText() + "\n" + "WARNING: File " + selectedFile.getName() + " is already open!");
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
        String filename = selectedFile.getName();
        FileHandler newFile = new FileHandler();
        newFile.setFileName(filename);
        fileHandlers.add(newFile);
        newFile.loadFile(selectedFile.getAbsolutePath());
        options.add(filename);
        FilesComboBox.setItems(options);
        FilesComboBox.getItems();
        currentlySelectedFile = newFile;

        addChildToPane(filename);
        // Only for debugging purposes, will delete later on
        System.out.println("Successfully loaded file " + selectedFile.getName() + "!");
        System.out.println("Currently " + fileHandlers.size() + " files are loaded in the programs memory");
        System.out.println("This is what the object holds: " + fileHandlers.get(0).getFileCount());
    }

    // Running this process in another thread so that the algorithm doesn't block the GUI
    public void runAlgorithmsClicked(MouseEvent e)
    {
        t1 = new Thread(() ->
        {
            try
            {
                dataPruner.buildPredictionModel(getSelectedFiles(fileHandlers));
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
                currentFileIndex = i;
                return list.get(i);
            }
        }
        return null;
    }

    private boolean checkIfAlreadyOpened(String fileName)
    {
        for(int i = 0; i < fileHandlers.size(); i++)
        {
            if(fileHandlers.get(i).getFileName().equals(fileName))
            {
                return true;
            }
        }
        return false;
    }

    private List<Instances> getSelectedFiles(List<FileHandler> list)
    {
        List<Integer> selectedIndices = new ArrayList<>();
        for (int i = 0; i < checkBoxes.getChildren().size(); i++)
        {
            CheckBox cb = (CheckBox) checkBoxes.getChildren().get(i);
            if(cb.isSelected())
            {
                selectedIndices.add(i);
            }
        }
        List<Instances> newListOfFiles = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            if(selectedIndices.contains(i))
            {
                newListOfFiles.add(list.get(i).getData());
            }
        }
        return newListOfFiles;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        testTextArea = new TextArea();
        testTextArea.setLayoutX(210);
        testTextArea.setLayoutY(60);
        testTextArea.setPrefHeight(399);
        testTextArea.setPrefWidth(774);
        rootPane.getChildren().add(testTextArea);
        dataPruner = new ProcessData(this);
    }
    // Constructor
    public FXMLDocumentController()
    {

        options = FXCollections.observableArrayList();
    }

    // FXML dynamic addition of checkboxes
    private void addChildToPane(String fileName)
    {
        CheckBox box = new CheckBox(fileName);
        box.setText(fileName);
        checkBoxes.getChildren().add(box);
    }

    public TextArea getTextOutputArea() {
        return testTextArea;
    }
}
