package WEKALogic;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class FileHandler
{
    private static int fileCount = 0;
    private String fileName;
    Remove remove;

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
        int[] indicesOfColumnsToUse = new int[] {49};
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
        data.setClassIndex(data.numAttributes() - 1);

        fileCount++;
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
}
