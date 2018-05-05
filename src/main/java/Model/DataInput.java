package Model;

import java.util.*;
import java.io.*;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.GaussianProcesses;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class DataInput {

    private File file;
    private int fileCount;
    private String savePath;
    private String[] removeOptionsArray;
    Remove remove;
    //private BufferedReader buffReader = null;

    private DataSource source;
    private Instances data;
    private Instances train = null;

    public DataInput()
    {
        initializeOptionArray();
    }

    private void initializeOptionArray()
    {
        int[] indicesOfColumnsToUse = new int[] {0, 49};
        remove = new Remove();
        remove.setAttributeIndicesArray(indicesOfColumnsToUse);
        remove.setInvertSelection(true);
    }

    public void loadFile(String filepath) throws Exception
    {
        source = new DataSource(filepath);

        // Note: Why does this need to be instanced twice? Is there a workaround for double the loading?
        data = source.getDataSet();
        remove.setInputFormat(data);
        data = Filter.useFilter(data, remove);
        //
        data.setClassIndex(data.numAttributes() - 1);
    }

    public void gaussianProcesses() throws Exception
    {
        GaussianProcesses gp = new GaussianProcesses();
        gp.buildClassifier(data);
        System.out.println(gp.toString());
    }

    public void naiveBayesAlternative() throws Exception        // Doesn't work with numerical datasets
    {
        NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
        nb.buildClassifier(data);
        Instance current;

        while ((current = (Instance) source.getStructure()) != null)
        {
            nb.updateClassifier(current);
        }
        System.out.println(nb);
    }

    public void naiveBayes() throws Exception                   // Doesn't work with numerical datasets
    {
        NaiveBayes nB = new NaiveBayes();
        nB.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        eval.crossValidateModel(nB, train, 10, new Random(1));
        System.out.println(eval.toSummaryString("\nResults\n=======\n", true));
        System.out.println(eval.fMeasure(1) + " " + eval.precision(1) + " " + eval.recall(1));
    }

    public void getFileCount()
    {
        // TODO implement here
    }
    public void checkFile()
    {
        // TODO implement here
    }

}