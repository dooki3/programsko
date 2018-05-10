package WEKALogic;
// This class is where the WEKA data mining algorithms are to be implemented

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
    private String result = "";

    public void gaussianProcesses(Instances data) throws Exception
    {
        GaussianProcesses gp = new GaussianProcesses();
        gp.buildClassifier(data);
        System.out.println(gp.toString());
    }

    public void naiveBayesAlternative(Instances data, ConverterUtils.DataSource source) throws Exception
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

    public void naiveBayes(Instances data) throws Exception
    {
        NaiveBayes nB = new NaiveBayes();
        nB.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(nB, data, 10, new Random(1));
        System.out.println(eval.toSummaryString("\nResults\n=======\n", true));
        result = eval.toSummaryString("\nResults\n=======\n", true);
    }

    public String toString()
    {
        return this.result;
    }
}
