package com.application.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileServiceImpl implements FileService{

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
	    // Original file name
	    String originalFileName = file.getOriginalFilename();

	    
	    // Generate a unique file name
	    String randomId = UUID.randomUUID().toString();
	    String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));

	    // Construct the file path
	    String filePath = Paths.get(path, fileName).toString();

	    // Check if the directory exists, create if not
	    File folder = new File(path);
	    if (!folder.exists()) {
	        folder.mkdir(); // Creates the directory
	    }

	    // Upload to the server
	    Files.copy(file.getInputStream(), Paths.get(filePath));
	    System.out.println("Image saved at: " + filePath);

	    // Return the file name
	    return fileName;
}
}