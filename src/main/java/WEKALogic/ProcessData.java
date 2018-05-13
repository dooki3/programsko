package WEKALogic;
// This is the class in which the datasets will be examined and pruned regarding the requested requirements


import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

public class ProcessData extends AlgorithmsWEKA{

    private Thread t1;
    private Map<String, String> allInstances = new LinkedHashMap<>();
    private Instances prunedDataset = null;
    private Instances set1, set2;

    public ProcessData()
    {

    }
    // The purpose of this is to collect all the data that contains bugs in the dataset, which were found in both the first and the second
    // or only in the second dataset.
    // This method returns the dataset instance which contains all the Instance objects with a bug_cnt > 0 condition
    // We use this in training the NaiveBayes prediction model algorithm
    public Instances pruneDataSet(Instances first, Instances second)     //Deprecated, using different method because of dataset header incompatibilities
    {
        String key;
        String value;

        for (int i = 0; i < first.size(); i++)
        {
            key = first.get(i).stringValue(first.attribute("File"));
            value = first.get(i).stringValue(first.attribute("bug_cnt"));
            if(!value.equals("0"))
            {
                allInstances.put(key, value);
            }
        }

        for (int j = 0; j < second.size(); j++)
        {
            key = second.get(j).stringValue(second.attribute("File"));
            value = second.get(j).stringValue(second.attribute("bug_cnt"));
            if(!value.equals("0"))
            {
                allInstances.put(key, value);
            }
            if(allInstances.containsKey(key) && !allInstances.get(key).equals("0") && value.equals("0"))
            {
                allInstances.remove(key);
            }
        }

        Instances newInstance = createNewInstance();
        return newInstance;
    }

    private Instances combineDatasets(Instances set1, Instances set2) // This is used to avoid WEKA dataset incompatibilities because of different headers
    {
        Instances combinedDataset = new Instances(set2);
        for(int i = 0; i < set1.numInstances(); i++)
        {
            combinedDataset.add(combinedDataset.numInstances(), set1.get(i));
        }
        System.out.println(set1.size());
        System.out.println(set2.size());
        System.out.println(combinedDataset.size());

        combinedDataset = splitDataset(combinedDataset, set2.numInstances() + 1, set1.numInstances());
        System.out.println(prunedDataset.attribute("bug_cnt"));
        System.out.println(combinedDataset.attribute("bug_cnt"));
        for (int i = 0; i < combinedDataset.numInstances(); i++)
        {
            //System.out.println(i);
            String value = prunedDataset.get(i).stringValue(prunedDataset.attribute("bug_cnt"));
            combinedDataset.get(i).setValue(combinedDataset.attribute("bug_cnt"), value);
        }
        return combinedDataset;
    }

    private Instances splitDataset(Instances set, int start, int howMany)
    {
        Instances splitDataset = new Instances(set, start - 1, howMany);
        return splitDataset;
    }

    // This method creates a new Instance file from the hashmap of the pruned dataset we've collected
    private Instances createNewInstance()
    {
        ArrayList<Attribute> attributes = new ArrayList<>();
        String str = set2.attribute(1).toString();
        //String names = set2.attribute(0).toString();
        String[] s=str.split("\\D+");
        /*
        String[] n=names.split("\\s*,\\s*");

        n[0] = n[0].substring(n[0].indexOf("org"));
        n[n.length-1] = n[n.length-1].substring(0, n[n.length-1].indexOf("rdPage.java") + 11);
        String test1 = n[n.length-2];
        String test2 = n[n.length-1];
        ArrayList<String> listofNames = new ArrayList<>();*/
        ArrayList<String> listofAttr = new ArrayList<>();
        for (int i = 1; i < s.length; i++)
        {
            listofAttr.add(s[i]);
        }
        /*
        for(int i = 0; i < n.length; i++)
        {
            System.out.println(i);
            listofNames.add(n[i]);
        }*/
        attributes.add(new Attribute("File", true));
        attributes.add(new Attribute("bug_cnt", listofAttr));
        Instances dataRaw = new Instances("PrunedFile", attributes, 0);


        for (Map.Entry<String, String> entry : allInstances.entrySet())
        {
            String fileName = entry.getKey();
            String value = entry.getValue();

            Instance i = new DenseInstance(attributes.size());
            i.setDataset(dataRaw);
            i.setValue(attributes.get(0), fileName);
            i.setValue(attributes.get(1), value);

            dataRaw.add(i);
            dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
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

            //Instances toBeSplit = combineDatasets(prunedDataset, set2);
            try
            {
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

    public Instances getPrunedDataset() {
        return prunedDataset;
    }
}