package WEKALogic;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToBinary;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;

import java.util.ArrayList;
import java.util.List;

public class FileHandler
{
    public enum convertMethods
    {
        NUMERIC_TO_NOMINAL, NUMERIC_TO_BINARY, STRING_TO_NOMINAL
    }
    private static int fileCount = 0;
    private String fileName;
    Remove remove;
    private List<Instance> instanceList = new ArrayList();
    private ConverterUtils.DataSource source;
    private Instances data;
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
        data = convertValues(data, convertMethods.NUMERIC_TO_BINARY, "2");
        data.setClassIndex(data.numAttributes() - 1);
        for (int i = 0; i < data.numInstances(); i++) {
            instanceList.add(data.get(i));
        }
        System.out.println("No. of instances: " + instanceList.size());
        fileCount++;
    }
    // Removes the file from memory -> hopefully garbage collector understands this as a cue to delete the files from memory.
    public void removeFileInstance()
    {
        this.source = null;
        this.data = null;
    }

    public void decreaseFileCount()
    {
        if(fileCount > 0)
        {
            fileCount--;
        }
    }

    // ATTENTION: It is expected that the ones invoking this method know what they're doing, and what they're expecting
    // to do with it, since it can create very improper Instances objects, as well as create exceptions
    protected static Instances convertValues(Instances instance, convertMethods conv, String columns)
    {
        Filter convert;
        Instances dataOut = null;
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = columns;

        switch (conv)
        {
            case NUMERIC_TO_BINARY:
                convert = new NumericToBinary();
                break;
            case NUMERIC_TO_NOMINAL:
                convert = new NumericToNominal();
                break;
            case STRING_TO_NOMINAL:
                convert = new StringToNominal();
                break;
            default:
                System.err.println("Wrong method selected!");
                return dataOut;
        }
        try
        {
            convert.setOptions(options);
            convert.setInputFormat(instance);
            dataOut = Filter.useFilter(instance, convert);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //System.out.println("Attribute 1: isNominal - " + dataOut.attribute(0).isNominal());
        //System.out.println("Attribute 1: isString - " + dataOut.attribute(0).isString());
        return dataOut;
    }

    public List<Instance> getInstanceList()
    {
        return instanceList;
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

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public ConverterUtils.DataSource getDataSource()
    {
        return this.source;
    }

}
