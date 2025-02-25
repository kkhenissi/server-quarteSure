package com.fdc.quartesure.quartesurebackend;

import com.google.gson.JsonArray;
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
@EnableAsync
public class QuarteSureBackendApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(QuarteSureBackendApplication.class, args);
/*
		double learningRate=0.001;
		int numbInputs=7;
		int numbHidden=10;
		int numbOutputs=5;
		int batchSize=1;
		int classIndex=7;
        int numEpochs=500;
*/






//		try {
//
//			URL url = new URL("https://online.turfinfo.api.pmu.fr/rest/client/1/programme/prochainesCourses");
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.connect();
//
//			// Check if Connect is made
//			int responseCode = conn.getResponseCode();
//
//			// 200 Ok
//			if(responseCode != 200) {
//				throw new RuntimeException("HttpResponseCode:"+responseCode);
//			} else {
//				StringBuilder informationRetrived = new StringBuilder();
//				Scanner scanner = new Scanner(url.openStream());
//				while (scanner.hasNext()){
//					informationRetrived.append(scanner.nextLine());
//				}
//			// close the scanner
//				scanner.close();
//				System.out.println(informationRetrived);
//				GsonJsonParser parse = new GsonJsonParser();
//				List<Object> dataObject = parse.parseList(informationRetrived.toString());
//				System.out.println(dataObject);
//			}
//
//		}catch (Exception ex) {
//			System.out.println(ex.getStackTrace());
//		}







	}

//	@Bean
//	public JavaMailSender getJavaMailSender() {
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		mailSender.setHost("smtp.gmail.com");
//		mailSender.setPort(587);
//
//		mailSender.setUsername("kkhenissi@gmail.com");
//		mailSender.setPassword("eyalenakhenissi");
//
//		Properties props = mailSender.getJavaMailProperties();
//		props.put("mail.transport.protocol", "smtp");
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.debug", "true");
//
//		return mailSender;
//	}





}
