package Model;

import java.util.*;
import java.io.*;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class DataInput {

    private File file;
    private int fileCount;
    private String savePath;
    private BufferedReader buffReader = null;
    private Instances train = null;

    public DataInput(){}


    public void loadFile(String filepath) throws Exception
    {
        buffReader = new BufferedReader(new FileReader(filepath));

        train = new Instances(buffReader);
        train.setClassIndex(train.numAttributes() - 1);
        buffReader.close();
    }

    public void naiveBayes() throws Exception
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