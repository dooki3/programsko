package WEKALogic;


import JavaFX.FXMLDocumentController;
import javafx.scene.control.TextArea;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.GaussianProcesses;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.Random;

public class AlgorithmsWEKA
{
    private String testResult = "";
    private NaiveBayes nB = null;
    FXMLDocumentController controller;

    TextArea txt = null;

    public AlgorithmsWEKA(FXMLDocumentController controller)
    {
        this.controller = controller;
    }

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
        //trainModelResult = eval.toSummaryString("\nResults of training\n=======\n", true);
    }

    protected void naiveBayes(Instances train, Instances test) throws Exception
    {
        nB = new NaiveBayes();
        nB.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(nB, test);
        eval.predictions().toArray();
        testResult = eval.toSummaryString("\nResults of testing\n=======\n", true);
        printResults();
    }

    private void printResults()
    {
        txt = controller.getTextOutputArea();
        txt.setText(txt.getText() + "\n\n\n" + testResult);
        txt.positionCaret(txt.getLength());
    }
}
