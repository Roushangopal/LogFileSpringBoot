package com.dharbor.caseassignment.log.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LogController {
	
    private static final String DIRECTORY = "D:/logs";
    private static final String DATE = "27/01/2022";
    
    @Autowired
    private ServletContext servletContext;
	
    //Get the Logs
    @GetMapping("/log")
    public ResponseEntity<InputStreamResource> getLog(
            @RequestParam(defaultValue = DATE) String date) throws IOException {

    	String fileName = getFileName(date);
//    	System.out.println(fileName);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
//        System.out.println("fileName: " + fileName);
//        System.out.println("mediaType: " + mediaType);
//        getAttributes("D:/logs/log.txt");

        File file = new File(DIRECTORY + "/" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
    
    //Get the properties of files to separate it by date
    public String getAttributes(String pathStr) throws IOException {
        Path p = Paths.get(pathStr);
        BasicFileAttributes view
           = Files.getFileAttributeView(p, BasicFileAttributeView.class)
                  .readAttributes();
        
        long date = view.creationTime().toMillis();
        ZonedDateTime t = Instant.ofEpochMilli(date).atZone(ZoneId.of("UTC"));
        String dateCreated = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(t);
//        System.out.println(dateCreated);
        return dateCreated;
      }
    
    
    public String getFileName(String date) throws IOException{
    	String fileName = null;
    	Dictionary<String, String> fileSystem = new Hashtable<String, String>();
    	File[] files = new File(DIRECTORY).listFiles();
    	 for (File filename : files) {
//    		 System.out.println("File: "+ filename.getName());
    		 String fname = filename.getName();
    		 String attr = getAttributes(DIRECTORY+"/"+fname);
    		 fileSystem.put(attr, fname);
    	 }
    	 fileName = fileSystem.get(date);
    	return fileName;
    }
}
