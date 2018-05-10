package WEKALogic;
// This is the class in which the datasets will be examined and pruned regarding the requested requirements

import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class ProcessData {

    private int bugCount;
    private int runTime;
    private List<Instances> listOfFiles = new ArrayList();
    private Instances prunedDataset;

    public ProcessData()
    {

    }

    public void addFile(Instances file)
    {
        listOfFiles.add(file);
    }
    //prunedDataSet = buildPredictionModel(listOfFiles.get(0), listOfFiles.get(1));
   /*
    private Instances pruneDataSet(Instances first, Instances second)
    {

        return new Instances(null);
    }
*/
    public void buildPredictionModel(ArrayList listaFajlovaKojeZelimoObraditi)
    {
        // TODO: Build prediction model
    }
}