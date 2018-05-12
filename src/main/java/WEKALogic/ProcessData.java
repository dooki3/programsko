package WEKALogic;
// This is the class in which the datasets will be examined and pruned regarding the requested requirements

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.util.*;

public class ProcessData extends AlgorithmsWEKA{

    private Thread t1;
    private Map<String, Double> allInstances = new LinkedHashMap<String, Double>();
    private Instances prunedDataset = null;
    private Instances set1, set2;

    public ProcessData()
    {

    }

    // ATTENTION: This method must be called in a separate thread, since it iterates over a large number of instances
    // The purpose of this is to collect all the data that contains bugs in the dataset, which were found in both the first and the second
    // or only in the second dataset.
    // This method returns the dataset instance which contains all the Instance objects with a bug_cnt > 0 condition
    // We use this in training the NaiveBayes prediction model algorithm
    public Instances pruneDataSet(Instances first, Instances second)
    {
        String key;
        double value;
        for (int i = 0; i < first.size(); i++)
        {
            key = first.get(i).stringValue(first.attribute("File"));
            value = first.get(i).value(first.attribute("bug_cnt"));
            if(value != 0 )
            {
                allInstances.put(key, value);
            }
        }

        for (int j = 0; j < second.size(); j++)
        {
            key = second.get(j).stringValue(second.attribute("File"));
            value = second.get(j).value(second.attribute("bug_cnt"));
            if(value != 0 )
            {
                allInstances.put(key, value);
            }
            if(allInstances.containsKey(key) && allInstances.get(key).doubleValue() != 0 && value == 0)
            {
                allInstances.remove(key);
            }
        }
        Instances newInstance = createNewInstance();
        /* Debugging purposes
        for(int i = 0; i < 5; i++)
        {
            System.out.println(newInstance.get(i).stringValue(newInstance.attribute("bug_cnt")));
        }
        */
        return newInstance;
    }

    // This method creates a new Instance file from the hashmap of the pruned dataset we've collected
    private Instances createNewInstance()
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("File", true));
        attributes.add(new Attribute("bug_cnt", true));
        Instances dataRaw = new Instances("PrunedFile", attributes, 0);
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1);

        for (Map.Entry<String, Double> entry : allInstances.entrySet())
        {
            String fileName = entry.getKey();
            String value = String.valueOf(Double.valueOf(entry.getValue()).intValue());
            Instance i = new DenseInstance(attributes.size());
            i.setDataset(dataRaw);
            i.setValue(attributes.get(0), fileName);
            i.setValue(attributes.get(1), value);

            dataRaw.add(i);
        }
        return dataRaw;
    }

    public void buildPredictionModel(List<Instances> list)
    {
        set1 = list.get(0);
        set2 = list.get(1);
        t1 = new Thread(() ->
        {
            prunedDataset = pruneDataSet(set1, set2);
            prunedDataset = FileHandler.stringToNominal(prunedDataset);
/*
            try
            {
                runEvaluation(prunedDataset, set2);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        });
        t1.start();

        //runEvaluation(set1, set2);
    }

    public void runEvaluation(Instances train, Instances test) throws Exception
    {
        naiveBayes(train, test);
    }

    public Instances getPrunedDataset() {
        return prunedDataset;
    }

}