# SecureVideoFileSpring
## About

Secure Video File service is a powerful tool that enables users to keep their valuable files safe and secure. It uses advanced encryption and file signing techniques to protect zip files, which are then transformed into black and white pixel videos. This service allows users to store all their sensitive data in encrypted videos that can only be accessed by authorized users, providing a unique and innovative solution for secure data storage and sharing. With Secure Video File, users can have confidence in the safety and protection of their important files.


## Tools needed to setup application

Java 17 -> [JDK 17 download link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
> **Note:** You can also use newer version of JDK

Apache Maven 3.9.1 -> [download link](https://maven.apache.org/download.cgi)

## List of all dependencies

* **Spring Boot Starter Web** => Starter for testing Spring Boot applications
* **Spring Boot Starter Test** => Starter for building web applications using Spring MVC
* **Project Lombok** => Automatic Resource Management
* **SpringDoc OpenAPI UI** => Automating generation of API documentation
* **JavaFX Controls** => Control functionality inside a JavaFX application
* **JCodec** => Pure Java implementation of video/audio codecs and formats
* **GroupDocs.Comparison Java** => Finds differences between two revisions of a document
* **Google APIs Client Library For Java** => Provides functionality common to all Google APIs
* **Google Drive API** => Provides access to Google Drive cloud storage
* **OAuth Verification For Google** => Used for user authentication

## Google Auth Setup
![step1](https://user-images.githubusercontent.com/77991435/230670446-154b1381-1d7d-4157-8f15-cd6823be63f2.png)

![step2](https://user-images.githubusercontent.com/77991435/230670633-41318582-6b53-4b26-85ad-37c9de8b4d1e.png)

![step3](https://user-images.githubusercontent.com/77991435/230670672-27af7bf8-6b3b-49fe-84a3-8107668bd735.png)

![step4](https://user-images.githubusercontent.com/77991435/230670780-11fd8467-1f6f-4cfb-b384-195e96e10e06.png)

![step5](https://user-images.githubusercontent.com/77991435/230670819-9ee769aa-c515-40ae-9aff-c6b1321dde44.png)

![step6](https://user-images.githubusercontent.com/77991435/230670937-ed32205e-4825-42aa-afaa-01acd3e04a7f.png)

## Spring Application Setup
Configuring the Spring service is a straightforward process that can be accomplished in a few simple steps:

1. Clone the project from GitHub, and navigate to the project folder.
2. Add the downloaded client-secret.json file to the project folder.
3. Run the application.
4. You will receive a URL in terminal that will take you to sign up with the Gmail account that you used in the Google Drive API setup.
![urlGoogleDrive](https://user-images.githubusercontent.com/78024969/231885492-6c5be1ad-e68e-471a-80ee-f1bbf09ddc54.png)
5. Your credentials will be stored in the driveCredentials folder.
6. Congratulations! You are all set and ready to use the service.

## Application flows

1. **Encrypting file**

      * sending zipped file on Spring service
      * encrypting file using password
      * creating binary images from encrypted byte array
      * generating video from image list
      * uploading generated video to Google Drive

 2. **Decrypting file**
 
    * fetching video from Google Drive using video name or video url
    * converting video to binary image list
    * converting binary image list to byte array
    * decrypting file using password
    * receiving file as a byte array
    
## Example of video on Google Drive

![ezgif](https://user-images.githubusercontent.com/78024969/231885772-a745c6fc-5810-477b-bb5e-8e8b7cd3cd1a.gif)

## Upload and retrieve of video via Postman

![upload](https://user-images.githubusercontent.com/78024969/232131465-06aea36a-d397-4f43-b641-458b9dba08ad.png)

Image above shows request being sent on Spring service with following parameters:
1. password -> used for encrypting file
2. name -> used for naming video on Google Drive

Response contains name of video stored on Google Drive. Number in video name simbolizes number of useful bytes in video.

![retrieve](https://user-images.githubusercontent.com/78024969/232131504-bad4aaca-3846-427f-92a1-4a8cd636f46d.png)

Image above shows request being sent on Spring service with following parameters:
1. password -> used for decrypting file
2. name -> used for finding video on Google Drive

Response contains compressed byte array of zipped file.

## Example of complex code

```java
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
```  
The code above enables connection to Google Drive service. Successful authorization is possible if there is client_secret.json file with dedicated values and StoredCredentials file in driveCredentials. In case there is no StoredCredentials file, user receives URL where he can log and thus create StoredCredentials file. With StoredCredentials file, there is no need for repeated authorization every time user access application.

## List of Tasks

- [x] [ENG] - Conversion Service Implementation
* [ENG] - Conversion Utils Implementation @icon-check
* [ENG] - Conversion Manager Implementation @icon-check
* [ENG] - Connecting Spring To Google Disk @icon-check
* [ENG] - Conversion Flow Connection @icon-check
* [ENG] - Create and handle exceptions @icon-check
* [ENG] - Constants and Documentation @icon-check
* [ENG] - Preparing spring service for API comunication @icon-check
* [ENG] - Conversion Controller Implementation @icon-check
* [DOC] - Spring README file
* [DOC] - Spring repository wiki
* [DOC] - Adding logs and variable documentation
* [API] - File CRUD
* [API] - User CRUD
* [API] - Server Deployment
* [WEB] - Figma UI design
* [MOB] - Figma UI design

> * **[ENG]:** backend(engine)
> * **[API]:** application programming interface
> * **[WEB]:** frontend(GUI)
> * **[MOB]:** mobile development

## All Secure Video File repositories

- https://github.com/Realman78/SecureVideoFile-web
- https://github.com/Realman78/SecureVideoFile-API
- https://github.com/cvele3/SecureVideoFile
- https://github.com/cvele3/secureVideoFileSpring

## Contacts

Contact us for further inquiries about Secure Video File service
- securevideofile@gmail.com



