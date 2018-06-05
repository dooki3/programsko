package WEKALogic;
// This is the class in which the datasets will be examined and pruned regarding the requested requirements

import JavaFX.FXMLDocumentController;
import javafx.scene.control.TextArea;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.util.*;

public class ProcessData extends AlgorithmsWEKA{

    private Instances prunedDataset = null;
    private Instances set1, set2;
    private TextArea txt;

    public ProcessData(FXMLDocumentController controller)
    {
        super(controller);
        txt = controller.getTextOutputArea();
    }

    private Instances pruneDataSet(Instances first, Instances second)
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        // Add all attributes to an arraylist
        for (int i = 0; i < first.numAttributes(); i++)
        {
            attributes.add(first.attribute(i));
        }

        // Create empty Instances object
        Instances newDataset = new Instances("Pruned dataset", attributes, 0);

        // Go through datasets and add data of interest to the new Instances object
        for (int i = 0; i < first.numInstances(); i++)
        {
            Instance smallDatasetInstance, bigDatasetInstance;
            bigDatasetInstance = first.get(i);
            for(int j = 0; j < second.numInstances(); j++)
            {
                smallDatasetInstance = second.get(j);
                String bugCount = bigDatasetInstance.stringValue(49);
                if(bigDatasetInstance.stringValue(bigDatasetInstance.attribute(0)).equals(smallDatasetInstance.stringValue(smallDatasetInstance.attribute(0))))
                {
                    // If filenames are the same
                    if (bugCount.equals("1"))
                    {
                        // Add to new dataset
                        newDataset.add(bigDatasetInstance);
                        break;
                    }
                    else
                    {
                        for (int z = 1; z < first.numAttributes() - 1; z++)
                        {
                            // Pass all attributes and check if they are equal, if they are then add the instance to dataset
                            double val1, val2;
                            val1 = second.get(j).value(second.get(j).attribute(z));
                            val2 = first.get(i).value(first.get(i).attribute(z));
                            if(val1 != val2) break;
                            else if(val1 == val2 && z == first.numAttributes() - 2)
                            {
                                newDataset.add(bigDatasetInstance);
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Size of pruned dataset: " + newDataset.size());
        newDataset.setClassIndex(newDataset.numAttributes() - 1);
        return newDataset;
    }

    private Instances filterDatasets(Instances dataIn) throws Exception
    {
        Instances dataOut = null;
        Remove remove = new Remove();
        int[] indicesOfColumnsToUse = new int[] {0};
        remove.setAttributeIndicesArray(indicesOfColumnsToUse);
        remove.setInputFormat(dataIn);
        dataOut = Filter.useFilter(dataIn, remove);
        return dataOut;
    }

    public void buildPredictionModel(List<Instances> list) {
        set1 = list.get(0);
        set2 = list.get(1);
        try
        {
            prunedDataset = pruneDataSet(set1, set2);
            prunedDataset = filterDatasets(prunedDataset);
            set1 = filterDatasets(set1);
            set2 = filterDatasets(set2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try
        {
            txt.clear();
            runEvaluation(prunedDataset, set2);
            runEvaluation(set1, set2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runEvaluation(Instances train, Instances test) throws Exception
    {
        naiveBayes(train, test);
    }
}