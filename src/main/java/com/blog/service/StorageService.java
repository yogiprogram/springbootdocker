package com.blog.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class StorageService {

	@Value("${aws.accesKeyId}")
	private String awsAccessKeyId;

	@Value("${aws.secretKey}")
	private String awsSecretKey;

	@Value("${aws.bucketName}")
	private String awsBucketName;

	private AWSCredentials credentials;
	private AmazonS3 s3client;;

	@PostConstruct
	public void init() {
		credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
		s3client = new AmazonS3Client(credentials);
	}
	
	public void uploadFile(MultipartFile file) throws IOException {
		File fileForUpload = transformMultipartToFile(file);
		s3client.putObject(new PutObjectRequest(awsBucketName, file.getOriginalFilename(), fileForUpload));
	}

	public InputStream downloadFile(String amazonFileKey) throws IOException {
		S3Object fetchFile = s3client.getObject(new GetObjectRequest(awsBucketName, amazonFileKey));
		InputStream objectData = fetchFile.getObjectContent();
		return objectData;
	}

	private File transformMultipartToFile(MultipartFile multipart) throws IOException {
		File convertedFile = new File(multipart.getOriginalFilename());
		convertedFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(multipart.getBytes());
		fos.close();
		return convertedFile;

	}

}
