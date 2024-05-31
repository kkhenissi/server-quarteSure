package com.fdc.quartesure.quartesurebackend;

import com.google.gson.JsonArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
@EnableAsync
public class QuarteSureBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuarteSureBackendApplication.class, args);

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
