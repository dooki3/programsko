package WEKALogic;
// This is the class in which the datasets will be examined and pruned regarding the requested requirements

import JavaFX.FXMLDocumentController;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.util.*;

public class ProcessData extends AlgorithmsWEKA{

    private Thread t1;
    private Instances prunedDataset = null;
    private Instances set1, set2;

    public ProcessData(FXMLDocumentController controller)
    {
        super(controller);
    }

    private Instances pruneDataSet(Instances first, Instances second)
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        Instances smaller, bigger;
        if(first.numInstances() <= second.numInstances())
        {
            smaller = first;
            bigger = second;
        }
        else
        {
            smaller = second;
            bigger = first;
        }

        // Add all attributes to an arraylist
        for (int i = 0; i < first.numAttributes(); i++)
        {
            attributes.add(first.attribute(i));
        }

        // Create empty Instances object
        Instances newDataset = new Instances("Pruned dataset", attributes, 0);

        // Go through datasets
        for (int i = 0; i < bigger.numInstances(); i++)
        {
            Instance smallDatasetInstance, bigDatasetInstance;
            bigDatasetInstance = bigger.get(i);
            for(int j = 0; j < smaller.numInstances(); j++)
            {
                smallDatasetInstance = smaller.get(j);
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
                        for (int z = 1; z < bigger.numAttributes() - 1; z++)
                        {
                            // Pass all attributes and check if they are equal, if they are then add the instance to dataset
                            double val1, val2;
                            val1 = smaller.get(j).value(smaller.get(j).attribute(z));
                            val2 = bigger.get(i).value(bigger.get(i).attribute(z));
                            if(val1 != val2) break;
                            else if(val1 == val2 && z == bigger.numAttributes() - 2)
                            {
                                newDataset.add(bigDatasetInstance);
                                break;
                            }
                        }
                    }
                }
                else
                {
                    // If filenames are different
                    if (bugCount.equals("1"))
                    {
                        newDataset.add(bigger.get(i));
                        break;
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

    public void buildPredictionModel(List<Instances> list)
    {
        set1 = list.get(0);
        set2 = list.get(1);

        t1 = new Thread(() ->
        {
            try
            {
                prunedDataset = pruneDataSet(set1, set2);
                prunedDataset = filterDatasets(prunedDataset);
                set1 = filterDatasets(set1);
                set2 = filterDatasets(set2);
                runEvaluation(prunedDataset, set2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    public void runEvaluation(Instances train, Instances test) throws Exception
    {
        naiveBayes(train, test);
    }
}