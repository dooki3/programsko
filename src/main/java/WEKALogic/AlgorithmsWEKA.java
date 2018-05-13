package WEKALogic;


import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.GaussianProcesses;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.Random;

public class AlgorithmsWEKA
{
    private String trainModelResult = "";
    private String testResult = "";
    private NaiveBayes nB = null;


    protected void gaussianProcesses(Instances data) throws Exception
    {
        GaussianProcesses gp = new GaussianProcesses();
        gp.buildClassifier(data);
        System.out.println(gp.toString());
    }

    protected void naiveBayesAlternative(Instances data, ConverterUtils.DataSource source) throws Exception
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

    protected void naiveBayesCrossValidate(Instances instance) throws Exception
    {
        NaiveBayes nB = new NaiveBayes();
        nB.buildClassifier(instance);
        Evaluation eval = new Evaluation(instance);
        eval.crossValidateModel(nB, instance, 10, new Random(1));
        trainModelResult = eval.toSummaryString("\nResults of training\n=======\n", true);
    }

    protected void naiveBayes(Instances train, Instances test) throws Exception
    {
        train.setClassIndex(train.numAttributes() - 1);
        test.setClassIndex(test.numAttributes() - 1);
        nB = new NaiveBayes();
        nB.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        /*if(!train.equalHeaders(test))
        {
            System.out.println(train.equalHeadersMsg(test));
            System.out.println(train.attribute(1));
            System.out.println(test.attribute(1));
        }
        else
        {
            eval.evaluateModel(nB, test);
            testResult = eval.toSummaryString("\nResults of testing\n=======\n", true);
        }*/
        System.out.println(train.attribute(1));
        System.out.println(test.attribute(1));
        eval.evaluateModel(nB, test);
        testResult = eval.toSummaryString("\nResults of testing\n=======\n", true);
    }

    public String getTrainResult() {
        return trainModelResult;
    }

    public String getTestResult() {
        return testResult;
    }
}
