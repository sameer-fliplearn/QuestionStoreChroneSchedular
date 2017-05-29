package com.fliplearn.cronShedular.Service;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fliplearn.cronShedular.DTO.ResponseDTO;
import com.fliplearn.cronShedular.Repository.FileRepository;
import com.fliplearn.cronShedular.constants.PropertyConstants;
import com.fliplearn.cronShedular.exception.PropertyLoadException;
import com.fliplearn.cronShedular.exception.RetryFailureException;
import com.fliplearn.cronShedular.model.FileInformationS3;

@Component
/* @PropertySource("classpath:config.properties") */
public class ConnectorHttp {

	@Autowired
	private FileRepository filerepo;

	/*
	 * @Value("${Base_URL}") private String baseUrl;
	 */

	private static final Logger logger = LoggerFactory.getLogger(ConnectorHttp.class);
	private static int count = 0;
	private static Properties properties;
	private static AmazonS3 s3client;
	static int countretry = 0;

	/*
	 * static { pathh =
	 * ClassLoader.getSystemClassLoader().getResource(".").getPath(); pathh =
	 * pathh.substring(0, pathh.length() - 1); System.out.println(pathh); int
	 * index = pathh.lastIndexOf('/'); pathh = pathh.substring(0, index);
	 * System.out.println(pathh); }
	 */
	@Scheduled(fixedRate = 5000)
	public void sendFile() {
		// String computername = InetAddress.getLocalHost().getHostName();
		// System.out.println(computername);
		// FileInformationS3 fileinfo = filerepo.findTop1ByFileStatus(false);

		BufferedWriter bw = null;

		try {
			Map<String, Object> mapObject = downloadFileFromS3();
			/* if (fileinfo != null && fileinfo.getFileStatus() == false) { */
			FileInformationS3 fileinfo = null;
			String filename = "";
			if (mapObject != null) {
				fileinfo = (FileInformationS3) mapObject.get("fileobject");
				filename = (String) mapObject.get("filename");
			}
			if (fileinfo != null && !filename.isEmpty() && filename.trim().length() > 0) {
				/* String url = fileinfo.getFileUrl(); */
				count++;
				logger.info("inside sendFlie() count: " + count);

				/* File file = new File(url); */
				File file = new File(PropertyConstants.s3DocCopyLocation + filename);
				String BASE_URL = "http://localhost:8080/questions";
				/* BASE_URL = baseUrl */;
				BASE_URL = properties.getProperty(PropertyConstants.BASEURL);
				HttpClient client = HttpClientBuilder.create().build();

				HttpPost postRequest = new HttpPost(BASE_URL);

				try {
					MultipartEntityBuilder multiPartEntity = MultipartEntityBuilder.create();
					multiPartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					multiPartEntity.addBinaryBody("file", file, ContentType.create("application/octet-stream"),
							file.getName());
					String FILENAME = "logs/Responselogs.txt";
					FileWriter fw = new FileWriter(FILENAME);
					bw = new BufferedWriter(fw);
					HttpEntity entity = multiPartEntity.build();

					postRequest.setEntity(entity);
					ObjectMapper objectMapper = new ObjectMapper();

					HttpResponse response = client.execute(postRequest);

					if (response != null) {
						HttpEntity entityhttp = response.getEntity();
						if (response.getStatusLine().getStatusCode() == 200) {

							InputStream content = entityhttp.getContent();
							// String jsonString =
							// EntityUtils.toString(response.getEntity());
							// System.out.println(jsonString);
							ResponseDTO myObject = (ResponseDTO) objectMapper.readValue(content, ResponseDTO.class);
							System.out.println(myObject);

							bw.write(myObject.toString());
							bw.newLine();
							bw.flush();
							/*
							 * Path path = Paths.get("logs/Responselogs.txt");
							 * try (BufferedWriter writer =
							 * Files.newBufferedWriter(path)) {
							 * writer.write("Hello World !!"); }
							 */
							uploadToS3("logs/Responselogs.txt", filename);
							filerepo.updateFileStatus(true, fileinfo.getId());
							logger.info(myObject.toString());
						} else {
							bw.write(response.toString());
							bw.newLine();
							bw.flush();
							uploadToS3("logs/Responselogs.txt", filename);
							logger.info(response.toString());
						}
					} else {
						logger.error("http response null");
					}
					// System.out.println(jsonString););
				} catch (Exception ex) {
					logger.error(ex.toString());
					ex.printStackTrace();
				} finally {
					if (bw != null)
						try {
							bw.close();
						} catch (IOException ioe2) {
							// just ignore it
						}

				}
			} else {
				logger.info("no active files to upload");
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		logger.info("exiting sendFlie() count: " + count);
	}
	// }

	public Map<String, Object> downloadFileFromS3() throws FileNotFoundException, IOException, PropertyLoadException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (properties == null) {
			loadPropertyObj();
		} else if (properties.isEmpty()) {
			loadPropertyObj();
		}
		FileInformationS3 fileinfo = null;

		if (properties != null && properties.getProperty("cronNum") != null
				&& StringUtils.isNumeric(properties.getProperty("cronNum"))) {

			fileinfo = filerepo.findTop1ByFileStatusAndAssignedCron(false,
					Integer.parseInt(properties.getProperty("cronNum")));
		}
		String fileName = "";

		map.put("fileobject", fileinfo);
		if (fileinfo != null) {

			AWSCredentials credentials = new BasicAWSCredentials(properties.getProperty("accessKey"),
					properties.getProperty("secretKey"));
			if (s3client == null)
				s3client = new AmazonS3Client(credentials);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);
			InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
			/*
			 * PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET,
			 * "HtmlResponse" + FOLDER_SUFFIX,
			 * 
			 * emptyContent, metadata);
			 */

			/*
			 * PutObjectRequest putObjectRequest = new
			 * PutObjectRequest(properties.getProperty(PropertyConstants.
			 * bucketName),"questionbank/"+"HtmlResponse"+"/",emptyContent,
			 * metadata); s3client.putObject(putObjectRequest);
			 */
			fileName = "sentFromCron" + new SimpleDateFormat(properties.getProperty("dateFormat")).format(new Date())
					+ "" + fileinfo.getFileName();
			map.put("filename", fileName);
			File inputFile = new File(PropertyConstants.s3DocCopyLocation + fileName);
			GetObjectRequest getObjectRequest = new GetObjectRequest(
					properties.getProperty(PropertyConstants.bucketName),
					properties.getProperty("s3DocLocation") + fileinfo.getFileName().trim());
			s3client.getObject(getObjectRequest, inputFile);
		}
		return map;
	}

	public static void uploadToS3(String filepath, String fileName) throws PropertyLoadException {
		if (properties == null) {
			loadPropertyObj();
		}
		try {

			File tempFile = new File(filepath);
			/*
			 * Properties properties = new Properties(); properties.load(new
			 * FileInputStream(PropertyConstants.propertyFileLocation));
			 */

			AWSCredentials credentials = new BasicAWSCredentials(properties.getProperty("accessKey"),
					properties.getProperty("secretKey"));
			/* if (s3client == null) */
			s3client = new AmazonS3Client(credentials);
			// String bucketName
			// =properties.getProperty(QuestionBankConstants.bucketName);

			PutObjectRequest putObjectRequest = new PutObjectRequest(
					properties.getProperty(PropertyConstants.bucketName),
					properties.getProperty("responseLocation") + fileName + tempFile.getName(), tempFile);
			PutObjectResult result = s3client.putObject(putObjectRequest);
			System.out.println(result.getVersionId());
			// create folder code
			/*
			 * ObjectMetadata metadata = new ObjectMetadata();
			 * metadata.setContentLength(0); // create empty content InputStream
			 * emptyContent = new ByteArrayInputStream(new byte[0]); // create a
			 * PutObjectRequest passing the folder name suffixed by /
			 * PutObjectRequest putObjectRequest1 = new
			 * PutObjectRequest(properties.getProperty(PropertyConstants.
			 * bucketName), "questionbank/"+"HttpResponseLogs" + "/",
			 * emptyContent, metadata);
			 */
			// send request to S3 to create folder
			// s3client.putObject(putObjectRequest);
		} catch (AmazonClientException e) {
			if (countretry <= 5) {
				countretry++;
				uploadToS3(filepath, fileName);
			} else {
				throw new RetryFailureException("count");
			}

		}
		countretry = 0;
	}

	public static void loadPropertyObj() throws PropertyLoadException {
		try {
			/*
			 * String computername = InetAddress.getLocalHost().getHostName();
			 * String[] domainname = null; String domname = ""; if
			 * (computername.contains("-")) { domainname =
			 * computername.split("-"); } if (domainname != null) { domname =
			 * domainname[0]; } properties = new Properties(); String name =
			 * "/home/" + domname.trim() +
			 * PropertyConstants.propertyFileLocation; properties.load(new
			 * FileInputStream(name));
			 */
			String path = "";
			try {
				// path =
				// ClassLoader.getSystemClassLoader().getResource(".").getPath();
				path = ConnectorHttp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				//path = ConnectorHttp.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				path = path.replace("file:", "");
			   //path = path.substring(0, path.length() - 1);
				System.out.println(path);

				if (path.contains(".jar")) {
					path = path.substring(0, path.indexOf(".jar"));
					//path = path.substring(0, path.lastIndexOf('/'));
				}
				System.out.println("**************************************" + path);
				/* .getCodeSource().getLocation().toURI())).getName() */
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("exception occured");
				e.printStackTrace();
			}

			System.out.println(path);
			path = path.substring(0, path.length() - 1);
			path = path.substring(0, path.lastIndexOf('/'));
			System.out.println("finalpath" + path);
			System.out.println(path.trim() + PropertyConstants.propertyFileLocation);
			properties = new Properties();
			if (path != null && !path.isEmpty()) {
				properties.load(new FileInputStream(path.trim() + PropertyConstants.propertyFileLocation));
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			throw new PropertyLoadException(e);
		} catch (UnknownHostException ex) {
			// ex.printStackTrace();
			throw new PropertyLoadException(ex);
		} catch (IOException e) {
			// e.printStackTrace();
			throw new PropertyLoadException(e);
		} catch (NullPointerException e) {
			// e.printStackTrace();
			throw new PropertyLoadException(e);
		}
	}

	/*
	 * public static void main(String args[]) throws PropertyLoadException {
	 * 
	 * String basePath = new File("").getAbsolutePath();
	 * System.out.println(basePath); String
	 * path=ClassLoader.getSystemClassLoader().getResource(".").getPath();
	 * path=path.substring(0,path.length()-1); System.out.println(path); int
	 * index=path.lastIndexOf('/'); path=path.substring(0, index);
	 * System.out.println(path);
	 * 
	 * for (int i = 0; i < 17; i++) { uploadToS3("logs/Responselogs.txt",
	 * "filename");
	 * 
	 * } }
	 */

}
