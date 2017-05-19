package com.fliplearn.cronShedular.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fliplearn.cronShedular.DTO.ResponseDTO;
import com.fliplearn.cronShedular.Repository.FileRepository;
import com.fliplearn.cronShedular.constants.PropertyConstants;
import com.fliplearn.cronShedular.model.FileInformationS3;

@Component
@PropertySource("classpath:config.properties")
public class ConnectorHttp {

	@Autowired
	private FileRepository filerepo;

	@Value("${Base_URL}")
	private String baseUrl;

	private static final Logger logger = LoggerFactory.getLogger(ConnectorHttp.class);
	private static int count = 0;

	@Scheduled(fixedRate = 5000)
	public void sendFile() throws Exception {
		// FileInformationS3 fileinfo = filerepo.findTop1ByFileStatus(false);
		BufferedWriter bw = null;
		Map<String, Object> mapObject = downloadFileFromS3();
		/* if (fileinfo != null && fileinfo.getFileStatus() == false) */
		FileInformationS3 fileinfo = null;
		String filename = "";
		if (mapObject != null) {
			fileinfo = (FileInformationS3) mapObject.get("fileobject");
			filename = (String) mapObject.get("filename");
		}
		if (!filename.isEmpty() && filename.trim().length() > 0 && fileinfo != null) {
			// String url = fileinfo.getFileUrl();
			count++;
			logger.info("inside sendFlie() count: " + count);

			/* File file = new File(url); */
			File file = new File(filename);
			String BASE_URL = "http://localhost:8080/questions";
			BASE_URL = baseUrl;
			HttpClient client = HttpClientBuilder.create().build();

			HttpPost postRequest = new HttpPost(BASE_URL);

			try {
				MultipartEntityBuilder multiPartEntity = MultipartEntityBuilder.create();
				multiPartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				multiPartEntity.addBinaryBody("file", file, ContentType.create("application/octet-stream"),
						file.getName());

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
						String FILENAME = "logs/Responselogs.txt";
						FileWriter fw = new FileWriter(FILENAME, true);
						bw = new BufferedWriter(fw);
						bw.write(myObject.toString());
						bw.newLine();
						bw.flush();
						/*
						 * Path path = Paths.get("logs/Responselogs.txt"); try
						 * (BufferedWriter writer =
						 * Files.newBufferedWriter(path)) {
						 * writer.write("Hello World !!"); }
						 */
						filerepo.updateFileStatus(true, fileinfo.getId());
						logger.info(myObject.toString());
					} else {
						logger.info(EntityUtils.toString(entityhttp));
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
		logger.info("exiting sendFlie() count: " + count);
	}

	public Map<String, Object> downloadFileFromS3() throws FileNotFoundException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		FileInformationS3 fileinfo = filerepo.findTop1ByFileStatus(false);
		String fileName = "";
		map.put("filename", fileName);
		map.put("fileobject", fileinfo);
		if (fileinfo != null) {
			Properties properties = new Properties();
			properties.load(new FileInputStream(PropertyConstants.propertyFileLocation));
			AWSCredentials credentials = new BasicAWSCredentials(properties.getProperty("accessKey"),
					properties.getProperty("secretKey"));
			AmazonS3 s3client = new AmazonS3Client(credentials);
			fileName = new SimpleDateFormat(properties.getProperty("dateFormat")).format(new Date()) + ""
					+ fileinfo.getFileName();

			File inputFile = new File(PropertyConstants.s3DocCopyLocation + fileName);
			GetObjectRequest getObjectRequest = new GetObjectRequest(
					properties.getProperty(PropertyConstants.bucketName),
					properties.getProperty("s3DocLocation") + fileinfo.getFileName());
			s3client.getObject(getObjectRequest, inputFile);

		}
		return map;
	}

}
