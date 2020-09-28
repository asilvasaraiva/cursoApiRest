package com.alexsaraiva.cursomc.services;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alexsaraiva.cursomc.services.exception.FileException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;


@Service
public class AWSS3Service {

	private Logger LOG = LoggerFactory.getLogger(AWSS3Service.class);

	@Autowired
	private AmazonS3 s3Client;

	@Value("${s3.bucket}")
	private String bucketName;

	public URI updateFile(MultipartFile multipartFile ) {

		try {
			String fileName = multipartFile.getOriginalFilename();
			InputStream inputStream = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();
			return updateFile(inputStream,fileName,contentType);
		} catch (IOException e) {
			throw new FileException("Erro de IO: "+ e.getMessage());
		}
			
	}

	public URI updateFile(InputStream inputStream,String fileName, String contentType) {
		
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando Upload");
			s3Client.putObject(bucketName,fileName,inputStream,meta);
			LOG.info("Upload Concluido");
			return s3Client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI AWS S3");
		}
	}
	
	
	
}
