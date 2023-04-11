package hr.projekt.secureVideoFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import hr.projekt.secureVideoFile.enums.StatusCode;
import hr.projekt.secureVideoFile.exceptions.GoogleDriveInitializationException;
import hr.projekt.secureVideoFile.exceptions.HttpTransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

@SpringBootApplication
public class SecureVideoFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureVideoFileApplication.class, args);
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers(connector -> {
			connector.setMaxPostSize(-1);
		});
		return factory;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Bean
	public void checkConnectionToGoogleDrive() {
		Drive drive = initializeDrive();
	}

	private Drive initializeDrive(){
		JsonFactory jsonFactory = new GsonFactory();

		NetHttpTransport httpTransport = null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Exception e) {
			throw new HttpTransportException(StatusCode.HTTP_TRANSPORT_ERROR, e.getMessage());
		}


		File file = new File("client_secret.json");
		GoogleClientSecrets clientSecrets = null;
		try {
			FileInputStream in = new FileInputStream(file);
			clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
		} catch (IOException e) {
			throw new GoogleDriveInitializationException(StatusCode.GOOGLE_DRIVE_INITIALIZATION_ERROR, e.getMessage());
		}

		GoogleAuthorizationCodeFlow flow = null;
		try {
			flow = new GoogleAuthorizationCodeFlow.Builder(
					httpTransport,
					jsonFactory,
					clientSecrets,
					Collections.singletonList(DriveScopes.DRIVE)
			)
					.setDataStoreFactory(new FileDataStoreFactory(new File("driveCredentials")))
					.setAccessType("offline")
					.build();
		} catch (IOException e) {
			throw new GoogleDriveInitializationException(StatusCode.GOOGLE_DRIVE_INITIALIZATION_ERROR, e.getMessage());
		}

		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		Credential credential = null;
		try {
			credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		} catch (IOException e) {
			throw new GoogleDriveInitializationException(StatusCode.GOOGLE_DRIVE_INITIALIZATION_ERROR, e.getMessage());
		}


		return new Drive.Builder(httpTransport, jsonFactory, credential)
				.setApplicationName("my-comparison-application")
				.build();

	}

}
