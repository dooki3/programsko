package WEKALogic;
// This is the class in which the datasets will be examined and pruned regarding the requested requirements

import JavaFX.FXMLDocumentController;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.util.*;

public class ProcessData extends AlgorithmsWEKA{

    private Thread t1;
    private Map<String, Double> allInstances = new LinkedHashMap<>();
    private Instances prunedDataset = null;
    private Instances set1, set2;

    public ProcessData(FXMLDocumentController controller)
    {
        super(controller);
    }

    // The purpose of this is to collect all the data that contains bugs in the dataset, which were found in both the first and the second
    // or only in the second dataset.
    // This method returns the dataset instance which contains all the Instance objects with a bug_cnt > 0 condition
    // We use this in training the NaiveBayes prediction model algorithm
    public Instances pruneDataSet(Instances first, Instances second)     //Deprecated, using different method because of dataset header incompatibilities
    {
        String key;
        double value;

        for (int i = 0; i < first.size() - 1; i++)
        {
            key = first.get(i).stringValue(first.attribute("File"));
            value = first.get(i).value(1);
            if(value != 0)
            {
                allInstances.put(key, value);
            }
        }

        for (int j = 0; j < second.size() - 1; j++)
        {
            key = second.get(j).stringValue(second.attribute("File"));
            value = second.get(j).value(1);
            if(value != 0)
            {
                allInstances.put(key, value);
            }
            if(allInstances.containsKey(key) && allInstances.get(key)!= 0 && value == 0)
            {
                allInstances.remove(key);
            }
        }

        Instances newInstance = createNewInstance();
        return newInstance;
    }
    // This method creates a new Instance file from the hashmap of the pruned dataset we've collected
    private Instances createNewInstance()
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> valuesofBug = new ArrayList<>();
        valuesofBug.add("0");
        valuesofBug.add("1");
        for (Map.Entry<String, Double> entry : allInstances.entrySet())
        {
            String fileName = entry.getKey();
            fileNames.add(fileName);
        }
        attributes.add(new Attribute("File", fileNames));
        attributes.add(new Attribute("bug_cnt", valuesofBug));
        Instances dataRaw = new Instances("PrunedFile", attributes, 0);

        for (Map.Entry<String, Double> entry : allInstances.entrySet())
        {
            String fileName = entry.getKey();
            double value = entry.getValue();

            Instance i = new DenseInstance(attributes.size());
            i.setDataset(dataRaw);
            i.setValue(attributes.get(0), fileName);
            i.setValue(attributes.get(1), value);
            //i.setValue(attributes.get(0), value);

            dataRaw.add(i);
            dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
        }
        return dataRaw;
    }

    private Instances filterDatasets(Instances dataIn) throws Exception
    {
        Instances dataOut = null;
        Remove remove = new Remove();
        int[] indicesOfColumnsToUse = new int[] {1};
        remove.setAttributeIndicesArray(indicesOfColumnsToUse);
        remove.setInvertSelection(true);
        remove.setInputFormat(dataIn);
        dataOut = Filter.useFilter(dataIn, remove);
        return dataOut;
    }

    public void buildPredictionModel(List<Instances> list)
    {
        set1 = list.get(0);
        set2 = list.get(1);
        prunedDataset = pruneDataSet(set1, set2);

        try {
            prunedDataset = filterDatasets(prunedDataset);
            set2 = filterDatasets(set2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //prunedDataset = convertValues(prunedDataset, FileHandler.convertMethods.NUMERIC_TO_BINARY, "2");
        t1 = new Thread(() ->
        {
            try
            {
                runEvaluation(prunedDataset, set2);
                //runEvaluation(set1, set2);
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

    public Instances getPrunedDataset() {
        return prunedDataset;
    }
}