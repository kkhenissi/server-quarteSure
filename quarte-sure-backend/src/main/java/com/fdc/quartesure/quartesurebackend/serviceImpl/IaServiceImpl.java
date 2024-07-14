package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.service.IaService;
import lombok.extern.slf4j.Slf4j;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class IaServiceImpl implements IaService {

    double learningRate=0.001;
    int numbInputs=7;
    int numbHidden=10;
    int numbOutputs=5;
    int batchSize=1;
    int classIndex=7;
    int numEpochs=500;


    @Override
    public ResponseEntity<String[]> getAtteletPrediction(String requestMap) throws IOException, InterruptedException {
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        System.out.println(requestMap);
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"); // Micro services End to End Testing - ADRIA

        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(1234)
                .updater(new Adam(learningRate))
                .weightInit(WeightInit.XAVIER)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(numbInputs)
                        .nOut(numbHidden)
                        .activation(Activation.SIGMOID)
                        .build() )
                .layer(1, new DenseLayer.Builder()
                        .nIn(numbHidden)
                        .nOut(numbHidden)
                        .activation(Activation.SIGMOID)
                        .build() )
                .layer(2,new OutputLayer.Builder()
                        .nIn(numbHidden)
                        .nOut(numbOutputs)
                        .activation(Activation.SOFTMAX)
                        .lossFunction(LossFunctions.LossFunction.MEAN_SQUARED_LOGARITHMIC_ERROR)
                        .build()
                )
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(configuration);
        model.init();

        System.out.println("Etrainnement du model *********************************");

        File fileTrain = new ClassPathResource("currentcource1.csv").getFile();

        RecordReader recordReaderTrain  = new CSVRecordReader();
        recordReaderTrain.initialize(new FileSplit(fileTrain));


        DataSetIterator dataSetIteratorTrain =
                new RecordReaderDataSetIterator(recordReaderTrain, batchSize,classIndex,numbOutputs);

/*        while(dataSetIteratorTrain.hasNext()) {
			DataSet dataSet = dataSetIteratorTrain.next();
			System.out.println("---------------------------------------------");
			System.out.println(dataSet.getFeatures());
			System.out.println(dataSet.getLabels());
		}*/

        /**
         Demarage du sserveur duu monitoring du processus d'apprentissage
         */
        UIServer uiServer=UIServer.getInstance();
        InMemoryStatsStorage inMemoryStatsStorage=new InMemoryStatsStorage();
        uiServer.attach(inMemoryStatsStorage);
        model.setListeners(new StatsListener(inMemoryStatsStorage));

        for(int i = 0; i < numEpochs; i++) {
            model.fit(dataSetIteratorTrain);
            System.out.println("------ ------ -----");
        }


        // Evaluation du model
        System.out.println(" ------------------ Evaluation du model ----------------------------");

        File fileTest = new ClassPathResource("currentcourceTest.csv").getFile();
        RecordReader recordReaderTest = new CSVRecordReader();
        recordReaderTest.initialize(new FileSplit(fileTest));
        DataSetIterator dataSetIteratorTest = new RecordReaderDataSetIterator(recordReaderTest,batchSize,classIndex,numbOutputs);
        Evaluation evaluation=new Evaluation();
        while (dataSetIteratorTest.hasNext()) {
            DataSet dataSetTest = dataSetIteratorTest.next();
            INDArray features = dataSetTest.getFeatures();
            INDArray targetLabels = dataSetTest.getLabels();
            INDArray predictedLabels = model.output(features);
            evaluation.eval(predictedLabels,targetLabels);
        }

        System.out.println(evaluation.stats());

        // save winnerModel
        ModelSerializer.writeModel(model, "winnerModel.zip",true);


        // prediction des example

        System.out.println("-----------  Prediction----------------");

/*		{2,3,2500,16390,1,57.5,7},
		{1,2,2500,16390,1,57.5,7},
		{4,1,2500,16390,1,57.5,7},
		{1,2,2500,16390,1,57.5,7},
		{1,2,2500,16390,1,57.5,7},
		{2,3,2500,16390,1,57.5,7},
		{1,2,2500,16390,1,57.5,7}*/

        INDArray inputData = Nd4j.create(new double[][]{

                {1,2,2500,16390,4,57.5,7},

        });

        INDArray output=model.output(inputData);
        int[] classes = output.argMax(1).toIntVector();
        System.out.println(output);
        for (int i = 0; i < classes.length; i++) {
            System.out.println("Classe :"+classes[i]);

        }
        return (ResponseEntity<String[]>) output;
    }

    @Override
    public ResponseEntity<String[]> getPlattPrediction() {
        return null;
    }

    @Override
    public ResponseEntity<String[]> getMontePrediction() {
        return null;
    }

    @Override
    public ResponseEntity<String[]> getHaiPrediction() {
        return null;
    }
}
