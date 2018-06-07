package WEKALogic;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToBinary;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;

public class FileHandler
{
    public enum convertMethods
    {
        NUMERIC_TO_NOMINAL, NUMERIC_TO_BINARY, STRING_TO_NOMINAL
    }
    private static int fileCount = 0;
    private String fileName;
    private ConverterUtils.DataSource source;
    private Instances data;
    public FileHandler(){}


    public void loadFile(String filepath) throws Exception
    {
        source = new ConverterUtils.DataSource(filepath);
        data = source.getDataSet();
        data = convertValues(data, convertMethods.NUMERIC_TO_BINARY, "50");
        data.setClassIndex(data.numAttributes() - 1);
        fileCount++;
    }
    // Removes the file from memory -> hopefully garbage collector understands this as a cue to delete the files from memory.
    public void removeFileInstance()
    {
        if(fileCount > 0) { fileCount--; }
        this.source = null;
        this.data = null;
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
}