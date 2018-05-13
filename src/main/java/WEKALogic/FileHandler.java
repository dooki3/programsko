package WEKALogic;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;

import java.util.ArrayList;
import java.util.List;

public class FileHandler
{
    private static int fileCount = 0;
    private String fileName;
    Remove remove;
    private List<Instance> instanceList = new ArrayList();
    private ConverterUtils.DataSource source;
    private Instances data;
    private ArrayList<Attribute> headerAtts = new ArrayList<>();
    public FileHandler()
    {
        initializeOptionArray();
    }

    private void initializeOptionArray()
    {
        // This array is to be changed on demand, representative of the Attributes we wish to compare in our algorithm
        // In our .csv files, this specific value refers to "bug_cnt" attribute
        int[] indicesOfColumnsToUse = new int[] {0, 49};
        remove = new Remove();
        remove.setAttributeIndicesArray(indicesOfColumnsToUse);
        remove.setInvertSelection(true);
    }

    public void loadFile(String filepath) throws Exception
    {
        source = new ConverterUtils.DataSource(filepath);
        data = source.getDataSet();
        remove.setInputFormat(data);
        data = Filter.useFilter(data, remove);
        data = numericToNominal(data);
        data.setClassIndex(data.numAttributes() - 1);
        createAttributeArray();
        for (int i = 0; i < data.numInstances(); i++) {
            instanceList.add(data.get(i));
        }
        System.out.println("No. of instances: " + instanceList.size());
        //System.out.println(instanceList.get(0).attribute(0).value());
        fileCount++;
    }

    private void createAttributeArray()
    {
        //data.enumerateAttributes()
    }
    public int getFileCount()
    {
        return this.fileCount;
    }

    // Returns the loaded dataset
    public Instances getData()
    {
        return this.data;
    }

    public ConverterUtils.DataSource getDataSource()
    {
        return this.source;
    }
    // Removes the file from memory -> hopefully garbage collector understands this as a cue to delete the files from memory.
    public void removeFileInstance()
    {
        this.source = null;
        this.data = null;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    public void decreaseFileCount()
    {
        if(fileCount > 0)
        {
            fileCount--;
        }
    }
    // Converting numeric to nominal values
    protected static Instances numericToNominal(Instances instance)
    {
        Instances newInstance = null;
        NumericToNominal convert = new NumericToNominal();
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = "2";

        try
        {
            convert.setOptions(options);
            convert.setInputFormat(instance);
            newInstance = Filter.useFilter(instance, convert);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        /*
        //  Only for debugging purposes
        System.out.println("Before");
        for(int i=0; i<2; i=i+1)
        {
            //System.out.println("String? "+instance.attribute(i).isString());
            System.out.println(instance.attribute(i).type());
        }

        System.out.println("After");
        for(int i=0; i<2; i=i+1)
        {
            //System.out.println("Nominal? "+newInstance.attribute(i).isNominal());
            System.out.println(newInstance.attribute(i).type());
        }*/

        return newInstance;
    }

    protected static Instances stringToNominal(Instances instance)
    {
        Instances newInstance = null;
        StringToNominal convert = new StringToNominal();
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = "1-2";

        try
        {
            convert.setOptions(options);
            convert.setInputFormat(instance);
            newInstance = Filter.useFilter(instance, convert);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return newInstance;
    }

    public List<Instance> getInstanceList()
    {
        return instanceList;
    }

}
